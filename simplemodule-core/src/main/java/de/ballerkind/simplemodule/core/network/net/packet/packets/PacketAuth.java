package de.ballerkind.simplemodule.core.network.net.packet.packets;

import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketAuth extends Packet {

	private String moduleType;
	private String moduleId;
	private String moduleSecret;

	public String getModuleType() {
		return moduleType;
	}

	public String getModuleId() {
		return moduleId;
	}

	public String getModuleSecret() {
		return moduleSecret;
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