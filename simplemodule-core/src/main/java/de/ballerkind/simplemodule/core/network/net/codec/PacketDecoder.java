package de.ballerkind.simplemodule.core.network.net.codec;

import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import de.ballerkind.simplemodule.core.Main;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

	private Main main;

	public PacketDecoder(Main main) {
		this.main = main;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {

		if(byteBuf instanceof EmptyByteBuf) {
			System.out.println("debug: emptybytebuf. ok nice2know");
			return;
		}

		int id = byteBuf.readInt();
		Class<? extends Packet> clazz = main.getNetServer().getPacketRegistry().getPacket(id);
		if(clazz != null) {
			Packet packet = clazz.getConstructor().newInstance();
			packet.read(new ByteBufInputStream(byteBuf));
			list.add(packet);
		} else {
			main.getLogger().severe("Packet with ID '" + id + "' not found!");
		}

	}

}