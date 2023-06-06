package de.ballerkind.simplemodule.core.network.net.handler;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.packets.JsonPacketModuleStart;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.module.types.ExternalModule;
import de.ballerkind.simplemodule.core.network.net.packet.packets.PacketAuth;
import de.ballerkind.simplemodule.core.network.net.packet.packets.PacketAuthStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

public class AuthHandler extends SimpleChannelInboundHandler<Packet> {

	private Main main;

	public AuthHandler(Main main) {
		this.main = main;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {

		if(packet instanceof PacketAuth) {
			PacketAuth auth = (PacketAuth) packet;

			Module module = main.getModuleManager().getModuleById(auth.getModuleType(), auth.getModuleId());
			if(module != null) {
				if(module.getSecret().equals(auth.getModuleSecret())) {

					if(module instanceof ExternalModule) {
						ExternalModule external = (ExternalModule) module;
						if(external.getClient() == null) {
							external.setEnabled(true);
							external.setClient(ctx.channel());
							external.onConnect();
						} else {
							main.getLogger().warning("Client hat versucht sich doppelt anzumelden! (" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + ", " + module.getType() + ", " + module.getId() + ")");
							ctx.writeAndFlush(new PacketAuthStatus(false));
							ctx.close();
							return;
						}
					} else {
						main.getLogger().warning("Client hat versucht sich mit einem falschen Modul anzumelden! (" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + ", " + module.getType() + ")");
						ctx.writeAndFlush(new PacketAuthStatus(false));
						ctx.close();
						return;
					}

					ChannelPipeline pipeline = ctx.pipeline();

					pipeline.remove(this);
					pipeline.addLast(new PacketHandler(main, module));

					main.getLogger().info("Client hat sich erfolgreich angemeldet. (" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + ", " + module.getType() + ", " + module.getId() + ")");
					ctx.writeAndFlush(new PacketAuthStatus(true));

					main.getNetJsonServer().getClient().writeAndFlush(new JsonPacketModuleStart(module.getType(), module.getId()));

				} else {
					main.getLogger().warning("Client hat falschen Secret angegeben! (" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + ", " + module.getType() + ", " + module.getId() + ")");
					ctx.writeAndFlush(new PacketAuthStatus(false));
					ctx.close();
				}
			} else {
				main.getLogger().warning("Client hat falschen ModulTypen oder ModulID angegeben! (" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + ", " + auth.getModuleType() + ", " + auth.getModuleId() + ")");
				ctx.writeAndFlush(new PacketAuthStatus(false));
				ctx.close();
			}
		} else {
			main.getLogger().warning("Irgendwer versucht Packets zu senden, ohne sich zu authentifizieren. Uff");
		}

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		main.getLogger().info("Client hat sich verbunden. (" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + ")");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		main.getLogger().info("Ein Random hat die Verbindung getrennt. (" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + ")");
	}

}