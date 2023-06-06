package de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.ModuleMinecraft;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft.PacketModuleMinecraftTabComplete;

public class JsonPacketModuleMinecraftTabComplete extends JsonPacket implements IPacketHandler {

	private String moduleId;
	private String socketId;
	private String message;

	public JsonPacketModuleMinecraftTabComplete(String moduleId, String message) {
		this.moduleId = moduleId;
		this.message = message;
	}

	@Override
	public void handle(Main main) {

		ModuleMinecraft module = (ModuleMinecraft) main.getModuleManager().getModuleById("Minecraft", moduleId);
		if(module.getClient() != null) {
			module.getClient().writeAndFlush(new PacketModuleMinecraftTabComplete(socketId, message));
		}

	}

}