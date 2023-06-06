package de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;

public class JsonPacketModuleMinecraftInfo extends JsonPacket {

	private String moduleId;
	private String version;
	private String ip;
	private int port;
	private int maxPlayers;

	public JsonPacketModuleMinecraftInfo(String moduleId, String version, String ip, int port, int maxPlayers) {
		this.moduleId = moduleId;
		this.version = version;
		this.ip = ip;
		this.port = port;
		this.maxPlayers = maxPlayers;
	}

}