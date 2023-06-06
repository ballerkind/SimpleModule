package de.ballerkind.simplemodule.network.codec;

import de.ballerkind.simplemodule.network.encryption.AESUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.SecretKey;

public class AESEncrypter extends MessageToByteEncoder<ByteBuf> {

	private SecretKey key;

	public AESEncrypter(SecretKey key) {
		this.key = key;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
		byte[] bytes = new byte[in.readableBytes()];
		in.readBytes(bytes);
		out.writeBytes(AESUtils.encrypt(bytes, key));
	}

}