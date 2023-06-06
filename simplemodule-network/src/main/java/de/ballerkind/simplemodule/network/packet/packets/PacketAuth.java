package de.ballerkind.simplemodule.network.packet.packets;

import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketAuth extends Packet {

	private String moduleType;
	private String moduleId;
	private String moduleSecret;

	public PacketAuth() {}
	public PacketAuth(String moduleType, String moduleId, String moduleSecret) {
		this.moduleType = moduleType;
		this.moduleId = moduleId;
		this.moduleSecret = moduleSecret;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeUTF(moduleType);
		buf.writeUTF(moduleId);
		buf.writeUTF(moduleSecret);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.moduleType = buf.readUTF();
		this.moduleId = buf.readUTF();
		this.moduleSecret = buf.readUTF();
	}

}