package de.ballerkind.simplemodule.core.network.net.codec;

import de.ballerkind.simplemodule.core.network.encryption.AESUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import javax.crypto.SecretKey;
import java.util.List;

public class AESDecrypter extends ByteToMessageDecoder {

	private SecretKey key;

	public AESDecrypter(SecretKey key) {
		this.key = key;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		byte[] bytes = new byte[in.readableBytes()];
		in.readBytes(bytes);

		byte[] decrypted = AESUtils.decrypt(bytes, key);
		if (decrypted != null) {
			out.add(Unpooled.wrappedBuffer(decrypted));
		} else {
			System.out.println("Alarm, pls check mal AESDecrypter bzw. AESUtils#decrypt :)");
		}
	}

}