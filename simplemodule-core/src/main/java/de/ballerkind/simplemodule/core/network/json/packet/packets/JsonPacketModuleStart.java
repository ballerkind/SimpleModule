package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;

public class JsonPacketModuleStart extends JsonPacket implements IPacketHandler {

	private String moduleType;
	private String moduleId;

	public JsonPacketModuleStart(String moduleType, String moduleId) {
		this.moduleType = moduleType;
		this.moduleId = moduleId;
	}

	@Override
	public void handle(Main main) {
		// TODO: if ToggleModule
	}

}