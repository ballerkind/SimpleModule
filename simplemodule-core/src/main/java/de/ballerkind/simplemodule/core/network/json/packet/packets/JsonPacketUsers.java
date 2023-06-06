package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.user.User;

import java.util.List;

public class JsonPacketUsers extends JsonPacket {

	private List<User> users;

	public JsonPacketUsers(List<User> users) {
		this.users = users;
	}

}