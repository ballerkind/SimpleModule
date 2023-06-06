package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.user.User;
import org.bson.Document;

import java.util.List;

public class JsonPacketModuleRemove extends JsonPacket implements IPacketHandler {

	private String userId;
	private String moduleType;
	private String moduleId;

	public JsonPacketModuleRemove(String userId, String moduleType, String moduleId) {
		this.userId = userId;
		this.moduleType = moduleType;
		this.moduleId = moduleId;
	}

	@Override
	public void handle(Main main) {

		User user = main.getUserManager().getUser(userId);
		Module module = main.getModuleManager().getModuleById(moduleType, moduleId);

		List<String> modules = user.getSharedModules().get(module.getType());
		modules.remove(module.getId());
		if(modules.size() == 0) user.getSharedModules().remove(module.getType());

		module.getShared().remove(user.getId());

		main.getDatabaseManager().getExecutorService().execute(() -> {
			Document find = new Document("name", module.getType()).append("modules.id", module.getId());
			Document update = new Document("$pull", new Document("modules.$.shared", user.getId()));
			main.getDatabaseManager().getDatabase().getCollection("Modules").updateOne(find, update);
		});

	}

}