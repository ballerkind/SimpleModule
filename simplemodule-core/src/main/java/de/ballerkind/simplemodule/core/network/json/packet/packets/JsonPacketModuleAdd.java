package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.user.User;
import org.bson.Document;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonPacketModuleAdd extends JsonPacket implements IPacketHandler {

	private String userId;
	private String type;
	private String id;
	private String name;
	private String secret;

	public JsonPacketModuleAdd(String userId, String type, String id, String name, String secret) {
		this.userId = userId;
		this.type = type;
		this.id = id;
		this.name = name;
		this.secret = secret;
	}

	@Override
	public void handle(Main main) {
		try {
			Module module = main.getModuleManager().getModuleTypes().get(this.type).getModuleClass().getConstructor(Main.class, String.class, String.class, String.class, String.class, String.class, List.class)
								.newInstance(main, userId, type, id, name, secret, new ArrayList<>());

			//Document toStore = new Document("modules", new Document("userId", module.getUserId()).append("id", module.getId()).append("secret", module.getSecret()).append("name", module.getName()).append("shared", module.getShared()));
			Document toStore = new Document("userId", module.getUserId()).append("id", module.getId()).append("secret", module.getSecret()).append("name", module.getName()).append("shared", module.getShared());
			module.onCreate(toStore); // Could overwrite default keys but ok

			User user = main.getUserManager().getUser(module.getUserId());

			// --------------------- SORT + ADD ---------------------
			if(!user.getModules().containsKey(module.getType())) user.getModules().put(module.getType(), new ArrayList<>());
			int index = Collections.binarySearch(user.getModules().get(module.getType()), module.getName(),
							(first, second) -> main.getModuleManager().getModuleById(module.getType(), first).getName().compareToIgnoreCase(second));
			user.getModules().get(module.getType()).add(index < 0 ? -(index + 1) : index, module.getId());

			main.getModuleManager().getModuleTypes().get(module.getType()).getModules().put(module.getId(), module);
			// --------------------- SORT + ADD ---------------------

			module.onLoad(toStore);

			main.getDatabaseManager().getExecutorService().execute(() -> {
				Document find = new Document("name", module.getType());
				Document update = new Document("$push", new Document("modules", toStore));
				main.getDatabaseManager().getDatabase().getCollection("Modules").updateOne(find, update);
			});

		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
	}

}