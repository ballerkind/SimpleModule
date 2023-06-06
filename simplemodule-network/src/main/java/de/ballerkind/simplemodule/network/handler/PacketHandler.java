package de.ballerkind.simplemodule.network.handler;

import de.ballerkind.simplemodule.network.packet.IPacketHandler;
import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {

		if(packet instanceof IPacketHandler) {
			IPacketHandler handler = (IPacketHandler) packet;
			handler.handle();
		}

	}

}