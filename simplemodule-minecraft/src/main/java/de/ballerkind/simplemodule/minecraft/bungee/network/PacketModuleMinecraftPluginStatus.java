package de.ballerkind.simplemodule.minecraft.bungee.network;

import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketModuleMinecraftPluginStatus extends Packet {

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

}