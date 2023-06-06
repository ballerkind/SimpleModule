package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import org.bson.Document;

public class JsonPacketUserChangeEmail extends JsonPacket implements IPacketHandler {

	private String userId;
	private String email;

	public JsonPacketUserChangeEmail(String userId, String email) {
		this.userId = userId;
		this.email = email;
	}

	@Override
	public void handle(Main main) {

		main.getUserManager().getUser(userId).setEmail(email);

		main.getDatabaseManager().getExecutorService().execute(() -> {
			Document find = new Document("_id", userId);
			Document update = new Document("$set", new Document("email", email));
			main.getDatabaseManager().getDatabase().getCollection("Users").updateOne(find, update);
		});

	}

}