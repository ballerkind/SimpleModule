package de.ballerkind.simplemodule.minecraft.spigot.network;

import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketModuleMinecraftInfo extends Packet {

	private String version;
	private int port;
	private int maxPlayers;

	public PacketModuleMinecraftInfo() {}
	public PacketModuleMinecraftInfo(String version, int port, int maxPlayers) {
		this.version = version;
		this.port = port;
		this.maxPlayers = maxPlayers;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeUTF(version);
		buf.writeInt(port);
		buf.writeInt(maxPlayers);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.version = buf.readUTF();
		this.port = buf.readInt();
		this.maxPlayers = buf.readInt();
	}

}