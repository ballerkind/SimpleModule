package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.user.User;
import org.bson.Document;

public class JsonPacketUserCreate extends JsonPacket implements IPacketHandler {

	private User user;

	public JsonPacketUserCreate(User user) {
		this.user = user;
	}

	@Override
	public void handle(Main main) {

		main.getLogger().info("Neuer Benutzer '" + user.getName() + "' hat sich registriert.");
		main.getUserManager().getUsers().add(user);

		main.getDatabaseManager().getExecutorService().execute(() -> {
			Document insert = new Document("_id", user.getId()).append("email", user.getEmail()).append("name", user.getName()).append("password", user.getPassword()).append("verified", user.isVerified());
			main.getDatabaseManager().getDatabase().getCollection("Users").insertOne(insert);
		});

	}

}