package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import org.bson.Document;

public class JsonPacketUserChangePassword extends JsonPacket implements IPacketHandler {

	private String userId;
	private String password;

	public JsonPacketUserChangePassword(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	@Override
	public void handle(Main main) {

		main.getUserManager().getUser(userId).setPassword(password);

		main.getDatabaseManager().getExecutorService().execute(() -> {
			Document find = new Document("_id", userId);
			Document update = new Document("$set", new Document("password", password));
			main.getDatabaseManager().getDatabase().getCollection("Users").updateOne(find, update);
		});

	}

}