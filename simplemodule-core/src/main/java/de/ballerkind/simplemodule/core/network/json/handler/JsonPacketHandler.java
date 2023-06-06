package de.ballerkind.simplemodule.core.network.json.handler;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class JsonPacketHandler extends SimpleChannelInboundHandler<JsonPacket> {

	private Main main;

	public JsonPacketHandler(Main main) {
		this.main = main;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, JsonPacket packet) {

		if(IPacketHandler.class.isAssignableFrom(packet.getClass())) {
			IPacketHandler handler = (IPacketHandler) packet;
			handler.handle(main);
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		main.getLogger().info("NodeJS Client hat die Verbindung beendet!");

		main.getNetJsonServer().setClient(null);
	}

}