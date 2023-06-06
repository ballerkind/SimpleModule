package de.ballerkind.simplemodule.core.network.net.packet.packets;

import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketAuthStatus extends Packet {

	private boolean success;

	public PacketAuthStatus() {}
	public PacketAuthStatus(boolean success) {
		this.success = success;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeBoolean(success);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.success = buf.readBoolean();
	}

}