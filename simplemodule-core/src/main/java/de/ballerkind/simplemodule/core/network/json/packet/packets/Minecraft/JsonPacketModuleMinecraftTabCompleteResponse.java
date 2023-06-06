package de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;

import java.util.List;

public class JsonPacketModuleMinecraftTabCompleteResponse extends JsonPacket {

	private String moduleId;
	private String socketId;
	private List<String> response;

	public JsonPacketModuleMinecraftTabCompleteResponse(String moduleId, String socketId, List<String> response) {
		this.moduleId = moduleId;
		this.socketId = socketId;
		this.response = response;
	}

}