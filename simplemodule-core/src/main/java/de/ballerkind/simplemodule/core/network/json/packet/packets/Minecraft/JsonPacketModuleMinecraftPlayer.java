package de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;

public class JsonPacketModuleMinecraftPlayer extends JsonPacket {

	private String moduleId;
	private String name;
	private boolean joined;

	public JsonPacketModuleMinecraftPlayer(String moduleId, String name, boolean joined) {
		this.moduleId = moduleId;
		this.name = name;
		this.joined = joined;
	}

}