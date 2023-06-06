package de.ballerkind.simplemodule.network.packet.packets;

import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketHandshakeResponse extends Packet {

	private byte[] encryptedKey;

	public PacketHandshakeResponse() {}
	public PacketHandshakeResponse(byte[] encryptedKey) {
		this.encryptedKey = encryptedKey;
	}

	public byte[] getEncryptedKey() {
		return encryptedKey;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.write(encryptedKey);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		byte[] bytes = new byte[buf.available()];
		buf.readFully(bytes);

		this.encryptedKey = bytes;
	}

}