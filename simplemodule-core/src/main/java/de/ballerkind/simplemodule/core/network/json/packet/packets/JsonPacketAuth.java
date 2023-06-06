package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;

public class JsonPacketAuth extends JsonPacket {

	private String key;

	public JsonPacketAuth(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}