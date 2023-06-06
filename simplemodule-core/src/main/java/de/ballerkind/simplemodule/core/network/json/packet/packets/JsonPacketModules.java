package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.module.ModuleType;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;

import java.util.HashMap;

public class JsonPacketModules extends JsonPacket {

	private HashMap<String, ModuleType> moduleTypes;

	public JsonPacketModules(HashMap<String, ModuleType> moduleTypes) {
		this.moduleTypes = moduleTypes;
	}

}