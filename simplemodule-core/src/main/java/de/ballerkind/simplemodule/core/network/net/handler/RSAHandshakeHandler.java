package de.ballerkind.simplemodule.core.network.net.handler;

import de.ballerkind.simplemodule.core.network.encryption.AESUtils;
import de.ballerkind.simplemodule.core.network.encryption.RSAUtils;
import de.ballerkind.simplemodule.core.network.net.codec.AESDecrypter;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import de.ballerkind.simplemodule.core.network.net.packet.packets.PacketHandshake;
import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.net.codec.AESEncrypter;
import de.ballerkind.simplemodule.core.network.net.packet.packets.PacketHandshakeResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.crypto.SecretKey;
import java.security.KeyPair;

public class RSAHandshakeHandler extends SimpleChannelInboundHandler<Packet> {

	private Main main;
	private KeyPair keyPair;

	public RSAHandshakeHandler(Main main, KeyPair keyPair) {
		this.main = main;
		this.keyPair = keyPair;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {

		if(packet instanceof PacketHandshakeResponse) {
			PacketHandshakeResponse handshake = (PacketHandshakeResponse) packet;
			byte[] decryptedKey = RSAUtils.decrypt(handshake.getEncryptedKey(), keyPair.getPrivate());
			SecretKey key = AESUtils.getKeyFromByteArray(decryptedKey);

			ChannelPipeline pipeline = ctx.pipeline();

			pipeline.remove(this);
			pipeline.addBefore("PacketDecoder", "AESDecrypter", new AESDecrypter(key));
			pipeline.addBefore("PacketEncoder", "AESEncrypter", new AESEncrypter(key));
			pipeline.addLast("AuthHandler", new AuthHandler(main));
		} else {
			System.out.println("Client hält Protokol nicht ein! >:|");
			ctx.close();
		}

		ctx.fireChannelRead(packet);

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("Channel hat sich verbunden, ok, dann send ich ihm jz public key:");
		ctx.writeAndFlush(new PacketHandshake(keyPair.getPublic()));
	}

}