package de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft.JsonPacketModuleMinecraftPluginStatus;
import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.ModuleMinecraft;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.plugin.MinecraftPlugin;
import de.ballerkind.simplemodule.core.network.net.packet.IModulePacketHandler;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.Collections;

public class PacketModuleMinecraftPluginStatus extends Packet implements IModulePacketHandler<ModuleMinecraft> {

	private String name;
	private boolean enabled;

	public PacketModuleMinecraftPluginStatus() {}
	public PacketModuleMinecraftPluginStatus(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeUTF(name);
		buf.writeBoolean(enabled);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.name = buf.readUTF();
		this.enabled = buf.readBoolean();
	}

	@Override
	public void handle(Main main, ModuleMinecraft module) {

		boolean exists = false;
		for(MinecraftPlugin plugin : module.getPlugins()) {
			if(plugin.getName().equals(name)) {
				plugin.setEnabled(enabled);
				exists = true;
				break;
			}
		}

		if(!exists) {
			MinecraftPlugin plugin = new MinecraftPlugin(name, enabled);
			int index = Collections.binarySearch(module.getPlugins(), plugin, (first, second) -> first.getName().compareToIgnoreCase(second.getName()));
			module.getPlugins().add(index < 0 ? -(index + 1) : index, plugin);
		}

		main.getNetJsonServer().getClient().writeAndFlush(new JsonPacketModuleMinecraftPluginStatus(module.getId(), name, enabled));

	}

}