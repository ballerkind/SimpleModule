package de.ballerkind.simplemodule.minecraft.spigot.network;

import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketModuleMinecraftPlayer extends Packet {

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

}