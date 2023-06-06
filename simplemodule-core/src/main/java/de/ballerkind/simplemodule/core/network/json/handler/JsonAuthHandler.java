package de.ballerkind.simplemodule.core.network.json.handler;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.network.json.packet.packets.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class JsonAuthHandler extends SimpleChannelInboundHandler<JsonPacket> {

	private Main main;

	public JsonAuthHandler(Main main) {
		this.main = main;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, JsonPacket packet) {

		if(packet instanceof JsonPacketAuth) {
			JsonPacketAuth auth = (JsonPacketAuth) packet;

			if(main.getConfig().get("web_authkey").equals(auth.getKey())) {
				main.getLogger().info("NodeJS Client hat sich erfolgreich angemeldet.");

				ctx.pipeline().remove(this);
				ctx.pipeline().addLast(new JsonPacketHandler(main));

				ctx.writeAndFlush(new JsonPacketUsers(main.getUserManager().getUsers()));
				ctx.writeAndFlush(new JsonPacketModules(main.getModuleManager().getModuleTypes()));
				ctx.writeAndFlush(new JsonPacketLanguage(main.getLanguageManager().getLanguages(), main.getLanguageManager().getMessages()));
				ctx.writeAndFlush(new JsonPacketAuthStatus(true));

				main.getNetJsonServer().setClient(ctx.channel());
			} else {
				main.getLogger().warning("NodeJS Client hat versucht sich anzumelden! (PW: " + auth.getKey() + ")");

				ctx.writeAndFlush(new JsonPacketAuthStatus(false));
				ctx.close();
			}
		}

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		if(main.getNetJsonServer().getClient() == null) {
			main.getLogger().info("NodeJS Client hat sich verbunden.");
		} else {
			main.getLogger().warning("NodeJS Client wollte sich mehrmals verbinden!");
			ctx.close();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		main.getLogger().info("NodeJS Client hat die Verbindung beendet.");
	}

}