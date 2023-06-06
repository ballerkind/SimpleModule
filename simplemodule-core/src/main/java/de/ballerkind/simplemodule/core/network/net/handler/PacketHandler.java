package de.ballerkind.simplemodule.core.network.net.handler;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.packet.packets.JsonPacketModuleStop;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.module.types.ExternalModule;
import de.ballerkind.simplemodule.core.network.net.packet.IModulePacketHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;

public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

	private Main main;
	private Module module;

	public PacketHandler(Main main, Module module) {
		this.main = main;
		this.module = module;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {

		if(packet instanceof IModulePacketHandler) {
			IModulePacketHandler<Module> handler = (IModulePacketHandler<Module>) packet;

			// Checks if packet is assigned to this module
			Type type = ((ParameterizedType) handler.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
			if(module.getClass().equals(type)) {
				handler.handle(main, module);
			} else {
				main.getLogger().severe("Error: ModuleType '" + module.getClass().getSimpleName() + "' send packet of type '" + type.getTypeName() + "'. Tzzz");
			}
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {

		main.getLogger().info("Client hat die Verbindung getrennt. (" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + ", " + module.getType() + ", " + module.getId() + ")");

		if(module instanceof ExternalModule) {
			ExternalModule external = (ExternalModule) module;
			external.setEnabled(false);
			external.setClient(null);
			external.onDisconnect();
		}

		main.getNetJsonServer().getClient().writeAndFlush(new JsonPacketModuleStop(module.getType(), module.getId()));

	}

}