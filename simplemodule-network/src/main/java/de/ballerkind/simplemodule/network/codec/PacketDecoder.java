package de.ballerkind.simplemodule.network.codec;

import de.ballerkind.simplemodule.network.packet.Packet;
import de.ballerkind.simplemodule.network.registry.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

	private PacketRegistry registry;

	public PacketDecoder(PacketRegistry registry) {
		this.registry = registry;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {

		if(byteBuf instanceof EmptyByteBuf) return;

		int id = byteBuf.readInt();
		Class<? extends Packet> clazz = registry.getPacket(id);
		if(clazz != null) {
			Packet packet = clazz.getConstructor().newInstance();
			packet.read(new ByteBufInputStream(byteBuf));
			list.add(packet);
		} else {
			System.out.println("Packet with id '" + id + "' not found!");
		}

	}

}