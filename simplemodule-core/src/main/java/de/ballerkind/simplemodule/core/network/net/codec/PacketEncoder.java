package de.ballerkind.simplemodule.core.network.net.codec;

import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import de.ballerkind.simplemodule.core.Main;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	private Main main;

	public PacketEncoder(Main main) {
		this.main = main;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf byteBuf) throws Exception {

		int id = main.getNetServer().getPacketRegistry().getId(packet.getClass());
		if(id != -1) {
			byteBuf.writeInt(id);
			packet.write(new ByteBufOutputStream(byteBuf));
		} else {
			main.getLogger().severe("Packet " + packet.getClass().getSimpleName() + " has no ID!");
		}

	}

}