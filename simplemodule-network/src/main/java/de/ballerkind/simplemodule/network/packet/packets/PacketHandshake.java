package de.ballerkind.simplemodule.network.packet.packets;

import de.ballerkind.simplemodule.network.encryption.RSAUtils;
import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.security.PublicKey;

public class PacketHandshake extends Packet {

	private PublicKey publicKey;

	public PacketHandshake() {}
	public PacketHandshake(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.write(publicKey.getEncoded());
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		byte[] bytes = new byte[buf.available()];
		buf.readFully(bytes);

		this.publicKey = RSAUtils.getPublicKeyFromByteArray(bytes);
	}

}