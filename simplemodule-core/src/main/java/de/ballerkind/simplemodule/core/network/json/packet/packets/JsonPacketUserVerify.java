package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import org.bson.Document;

public class JsonPacketUserVerify extends JsonPacket implements IPacketHandler {

	private String userId;

	public JsonPacketUserVerify(String userId) {
		this.userId = userId;
	}

	@Override
	public void handle(Main main) {

		main.getUserManager().getUser(userId).setVerified(true);

		main.getDatabaseManager().getExecutorService().execute(() -> {
			Document find = new Document("_id", userId);
			Document update = new Document("$set", new Document("verified", true));
			main.getDatabaseManager().getDatabase().getCollection("Users").updateOne(find, update);
		});

	}

}