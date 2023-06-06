package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.user.User;
import org.bson.Document;

import java.util.*;

public class JsonPacketModuleUpdate extends JsonPacket implements IPacketHandler {

	private String moduleType;
	private String moduleId;
	private String name;
	private String secret;
	private List<String> shared;

	public JsonPacketModuleUpdate(String moduleType, String moduleId, String name, String secret, List<String> shared) {
		this.moduleType = moduleType;
		this.moduleId = moduleId;
		this.name = name;
		this.secret = secret;
		this.shared = shared;
	}

	@Override
	public void handle(Main main) {

		Module module = main.getModuleManager().getModuleById(moduleType, moduleId);

		module.setName(name);
		module.setSecret(secret);

		Iterator<String> it = module.getShared().iterator();
		while(it.hasNext()) {
			String userId = it.next();
			if(!shared.contains(userId)) {
				User u = main.getUserManager().getUser(userId);

				List<String> modules = u.getSharedModules().get(module.getType());
				modules.remove(module.getId());
				if(modules.size() == 0) u.getSharedModules().remove(module.getType());

				it.remove();
			}
		}

		// SORT
		List<List<String>> list = new ArrayList<>();
		list.add(main.getUserManager().getUser(module.getUserId()).getModules().get(module.getType()));
		module.getShared().forEach(userId -> list.add(main.getUserManager().getUser(userId).getSharedModules().get(module.getType())));

		list.forEach(modules -> {
			modules.remove(module.getId());

			int index = Collections.binarySearch(modules, module.getName(),
							(first, second) -> main.getModuleManager().getModuleById(module.getType(), first).getName().compareToIgnoreCase(second));

			modules.add(index < 0 ? -(index + 1) : index, module.getId());
		});
		// SORT

		main.getDatabaseManager().getExecutorService().execute(() -> {
			Document find = new Document("name", module.getType()).append("modules.id", module.getId());
			Document update = new Document("$set", new Document("modules.$.name", module.getName()).append("modules.$.secret", module.getSecret()).append("modules.$.shared", module.getShared()));
			main.getDatabaseManager().getDatabase().getCollection("Modules").updateOne(find, update);
		});

	}

}