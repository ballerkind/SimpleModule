package de.ballerkind.simplemodule.network.handler;

import de.ballerkind.simplemodule.network.packet.Packet;
import de.ballerkind.simplemodule.network.packet.packets.PacketAuthStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.function.Consumer;

public class AuthHandler extends SimpleChannelInboundHandler<Packet> {

	private Consumer<Boolean> onAuth;

	public AuthHandler(Consumer<Boolean> onAuth) {
		this.onAuth = onAuth;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {

		if(packet instanceof PacketAuthStatus) {
			PacketAuthStatus auth = (PacketAuthStatus) packet;

			if(auth.isSuccess()) {
				ChannelPipeline pipeline = ctx.pipeline();
				pipeline.remove(this);
				pipeline.addLast(new PacketHandler());
			}

			this.onAuth.accept(auth.isSuccess());
		}

	}

}