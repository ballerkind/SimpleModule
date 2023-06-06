package de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.ModuleMinecraft;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft.PacketModuleMinecraftConsole;

public class JsonPacketModuleMinecraftConsole extends JsonPacket implements IPacketHandler {

	private String moduleId;
	private String command;

	public JsonPacketModuleMinecraftConsole(String moduleId, String command) {
		this.moduleId = moduleId;
		this.command = command;
	}

	@Override
	public void handle(Main main) {

		ModuleMinecraft minecraft = (ModuleMinecraft) main.getModuleManager().getModuleById("Minecraft", moduleId);
		if(minecraft != null) {
			if(minecraft.getClient() != null) {
				minecraft.getClient().writeAndFlush(new PacketModuleMinecraftConsole(command));
			}
		}

	}

}