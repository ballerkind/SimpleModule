package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;

public class JsonPacketModuleStop extends JsonPacket {

	private String moduleType;
	private String moduleId;

	public JsonPacketModuleStop(String moduleType, String moduleId) {
		this.moduleType = moduleType;
		this.moduleId = moduleId;
	}

}