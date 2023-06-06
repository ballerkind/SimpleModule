package de.ballerkind.simplemodule.network.codec;

import de.ballerkind.simplemodule.network.packet.Packet;
import de.ballerkind.simplemodule.network.registry.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	private PacketRegistry registry;

	public PacketEncoder(PacketRegistry registry) {
		this.registry = registry;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf byteBuf) throws Exception {

		int id = registry.getId(packet.getClass());
		if(id != -1) {
			byteBuf.writeInt(id);
			packet.write(new ByteBufOutputStream(byteBuf));
		} else {
			System.out.println("Packet " + packet.getClass().getSimpleName() + " has no Id!");
		}

	}

}