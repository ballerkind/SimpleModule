package de.ballerkind.simplemodule.network.handler;

import de.ballerkind.simplemodule.network.codec.AESDecrypter;
import de.ballerkind.simplemodule.network.codec.AESEncrypter;
import de.ballerkind.simplemodule.network.encryption.AESUtils;
import de.ballerkind.simplemodule.network.encryption.RSAUtils;
import de.ballerkind.simplemodule.network.packet.Packet;
import de.ballerkind.simplemodule.network.packet.packets.PacketAuth;
import de.ballerkind.simplemodule.network.packet.packets.PacketHandshake;
import de.ballerkind.simplemodule.network.packet.packets.PacketHandshakeResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Objects;
import java.util.function.Consumer;

public class RSAHandshakeHandler extends SimpleChannelInboundHandler<Packet> {

	private Consumer<Boolean> onAuth;
	private PacketAuth packetAuth;

	public RSAHandshakeHandler(Consumer<Boolean> onAuth, PacketAuth packetAuth) {
		this.onAuth = onAuth;
		this.packetAuth = packetAuth;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {

		if(packet instanceof PacketHandshake) {
			PacketHandshake handshake = (PacketHandshake) packet;
			PublicKey publicKey = handshake.getPublicKey();

			SecretKey keyToUse = AESUtils.generate();
			byte[] encryptedKey = RSAUtils.encrypt(Objects.requireNonNull(keyToUse).getEncoded(), publicKey);

			ctx.writeAndFlush(new PacketHandshakeResponse(encryptedKey));

			ChannelPipeline pipeline = ctx.pipeline();
			pipeline.remove(this);
			pipeline.addBefore("PacketDecoder", "AESDecrypter", new AESDecrypter(keyToUse));
			pipeline.addBefore("PacketEncoder", "AESEncrypter", new AESEncrypter(keyToUse));
			pipeline.addLast("AuthHandler", new AuthHandler(onAuth));

			ctx.writeAndFlush(packetAuth);
		}

	}

}