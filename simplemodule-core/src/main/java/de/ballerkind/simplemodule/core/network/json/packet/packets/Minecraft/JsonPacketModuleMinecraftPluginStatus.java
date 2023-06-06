package de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.ModuleMinecraft;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft.PacketModuleMinecraftPluginStatus;

public class JsonPacketModuleMinecraftPluginStatus extends JsonPacket implements IPacketHandler {

	private String moduleId;
	private String name;
	private boolean enabled;

	public JsonPacketModuleMinecraftPluginStatus(String moduleId, String name, boolean enabled) {
		this.moduleId = moduleId;
		this.name = name;
		this.enabled = enabled;
	}

	@Override
	public void handle(Main main) {

		ModuleMinecraft minecraft = (ModuleMinecraft) main.getModuleManager().getModuleById("Minecraft", moduleId);
		if(minecraft != null) {
			if(minecraft.getClient() != null) {
				minecraft.getClient().writeAndFlush(new PacketModuleMinecraftPluginStatus(name, enabled));
			}
		}

	}

}