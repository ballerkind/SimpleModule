package de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketModuleMinecraftTabComplete extends Packet {

	private String socketId;
	private String message;

	public PacketModuleMinecraftTabComplete() {}
	public PacketModuleMinecraftTabComplete(String socketId, String message) {
		this.socketId = socketId;
		this.message = message;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeUTF(socketId);
		buf.writeUTF(message);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.socketId = buf.readUTF();
		this.message = buf.readUTF();
	}

}