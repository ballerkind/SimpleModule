package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;

public class JsonPacketAuthStatus extends JsonPacket {

	private boolean success;

	public JsonPacketAuthStatus(boolean success) {
		this.success = success;
	}

}