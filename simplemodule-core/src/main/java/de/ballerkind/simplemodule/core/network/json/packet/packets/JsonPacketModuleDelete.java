package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import org.bson.Document;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class JsonPacketModuleDelete extends JsonPacket implements IPacketHandler {

	private String moduleType;
	private String moduleId;

	public JsonPacketModuleDelete(String moduleType, String moduleId) {
		this.moduleType = moduleType;
		this.moduleId = moduleId;
	}

	@Override
	public void handle(Main main) {

		Module module = main.getModuleManager().getModuleById(moduleType, moduleId);

		List<LinkedHashMap<String, List<String>>> list = new ArrayList<>();
		list.add(main.getUserManager().getUser(module.getUserId()).getModules());
		module.getShared().forEach(userId -> list.add(main.getUserManager().getUser(userId).getSharedModules()));

		list.forEach(moduleTypes -> {
			List<String> modules = moduleTypes.get(module.getType());
			modules.remove(module.getId());
			if(modules.size() == 0) moduleTypes.remove(module.getType());
		});

		main.getModuleManager().getModuleTypes().get(module.getType()).getModules().remove(module.getId());

		main.getDatabaseManager().getExecutorService().execute(() -> {
			Document find = new Document("name", module.getType());
			Document update = new Document("$pull", new Document("modules", new Document("id", module.getId())));
			main.getDatabaseManager().getDatabase().getCollection("Modules").updateOne(find, update);
		});

	}

}