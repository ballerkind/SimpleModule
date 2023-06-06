package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.user.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;

public class JsonPacketModuleRedeem extends JsonPacket implements IPacketHandler {

	private String userId;
	private String moduleType;
	private String moduleId;

	public JsonPacketModuleRedeem(String userId, String moduleType, String moduleId) {
		this.userId = userId;
		this.moduleType = moduleType;
		this.moduleId = moduleId;
	}

	@Override
	public void handle(Main main) {

		User user = main.getUserManager().getUser(userId);
		Module module = main.getModuleManager().getModuleById(moduleType, moduleId);

		if(!user.getSharedModules().containsKey(module.getType())) user.getSharedModules().put(module.getType(), new ArrayList<>());
		int index = Collections.binarySearch(user.getSharedModules().get(module.getType()), module.getName(),
						(first, second) -> main.getModuleManager().getModuleById(module.getType(), first).getName().compareToIgnoreCase(second));
		user.getSharedModules().get(module.getType()).add(index < 0 ? -(index + 1) : index, module.getId());

		module.getShared().add(user.getId());

		main.getDatabaseManager().getExecutorService().execute(() -> {
			Document find = new Document("name", module.getType()).append("modules.id", module.getId());
			Document update = new Document("$push", new Document("modules.$.shared", user.getId()));
			main.getDatabaseManager().getDatabase().getCollection("Modules").updateOne(find, update);
		});

	}

}