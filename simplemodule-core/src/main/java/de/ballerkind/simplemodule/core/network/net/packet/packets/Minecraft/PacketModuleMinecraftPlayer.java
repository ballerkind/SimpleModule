package de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft.JsonPacketModuleMinecraftPlayer;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.ModuleMinecraft;
import de.ballerkind.simplemodule.core.network.net.packet.IModulePacketHandler;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.Collections;

public class PacketModuleMinecraftPlayer extends Packet implements IModulePacketHandler<ModuleMinecraft> {

	private String name;
	private boolean joined;

	public PacketModuleMinecraftPlayer() {}
	public PacketModuleMinecraftPlayer(String name, boolean joined) {
		this.name = name;
		this.joined = joined;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeUTF(name);
		buf.writeBoolean(joined);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.name = buf.readUTF();
		this.joined = buf.readBoolean();
	}

	@Override
	public void handle(Main main, ModuleMinecraft module) {

		if(joined) {
			int index = Collections.binarySearch(module.getPlayers(), name, String::compareToIgnoreCase);
			module.getPlayers().add(index < 0 ? -(index + 1) : index, name);
		} else {
			module.getPlayers().remove(name);
		}

		main.getNetJsonServer().getClient().writeAndFlush(new JsonPacketModuleMinecraftPlayer(module.getId(), name, joined));

	}

}