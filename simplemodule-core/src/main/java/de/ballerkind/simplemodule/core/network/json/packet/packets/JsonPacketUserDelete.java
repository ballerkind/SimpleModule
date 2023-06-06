package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.user.User;
import org.bson.Document;

public class JsonPacketUserDelete extends JsonPacket implements IPacketHandler {

	private String userId;

	public JsonPacketUserDelete(String userId) {
		this.userId = userId;
	}

	@Override
	public void handle(Main main) {

		User user = main.getUserManager().getUser(userId);

		user.getModules().forEach((type, modules) -> {
			modules.forEach(moduleId -> {
				Module module = main.getModuleManager().getModuleById(type, moduleId);

				module.getShared().forEach(sharedWithId -> {
					User sharedWith = main.getUserManager().getUser(sharedWithId);
					sharedWith.getSharedModules().get(type).remove(module.getId());
				});
			});
		});
		main.getUserManager().getUsers().remove(user);

		main.getDatabaseManager().getExecutorService().execute(() -> {
			main.getDatabaseManager().getDatabase().getCollection("Users").deleteOne(new Document("_id", user.getId()));

			Document update = new Document("$pull", new Document("modules", new Document("userId", user.getId())));
			main.getDatabaseManager().getDatabase().getCollection("Modules").updateMany(new Document(), update);
		});

		main.getLogger().info("Der Nutzer '" + user.getName() + "' hat seinen Account gelöscht :(");

	}

}