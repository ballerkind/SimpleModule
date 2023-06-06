package de.ballerkind.simplemodule.core.network.net;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.encryption.RSAUtils;
import de.ballerkind.simplemodule.core.network.net.codec.PacketDecoder;
import de.ballerkind.simplemodule.core.network.net.codec.PacketEncoder;
import de.ballerkind.simplemodule.core.network.net.handler.RSAHandshakeHandler;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft.*;
import de.ballerkind.simplemodule.core.network.net.packet.packets.PacketAuth;
import de.ballerkind.simplemodule.core.network.net.packet.packets.PacketAuthStatus;
import de.ballerkind.simplemodule.core.network.net.packet.packets.PacketHandshake;
import de.ballerkind.simplemodule.core.network.net.packet.packets.PacketHandshakeResponse;
import de.ballerkind.simplemodule.core.network.registry.PacketRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.security.KeyPair;

public class NetServer {

	private Main main;
	private int port;

	public PacketRegistry<Packet> packetRegistry;
	private Channel channel;
	private KeyPair keyPair;

	public NetServer(Main main, int port) {

		this.main = main;
		this.port = port;
		this.packetRegistry = new PacketRegistry<>();

		this.packetRegistry
			.register(PacketHandshake.class)
			.register(PacketHandshakeResponse.class)
			.register(PacketAuth.class)
			.register(PacketAuthStatus.class)
			.next()	// Minecraft
			.register(PacketModuleMinecraftConsole.class)
			.register(PacketModuleMinecraftInfo.class)
			.register(PacketModuleMinecraftPlayer.class)
			.register(PacketModuleMinecraftPluginStatus.class)
			.register(PacketModuleMinecraftTabComplete.class)
			.register(PacketModuleMinecraftTabCompleteResponse.class);

		this.keyPair = RSAUtils.generate();
	}

	public void start() {

		try {

			EventLoopGroup bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
			EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

			ServerBootstrap b = new ServerBootstrap()
				.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
				.group(bossGroup, workerGroup)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) {

						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
						pipeline.addLast(new LengthFieldPrepender(4));
						pipeline.addLast("PacketDecoder", new PacketDecoder(main));
						pipeline.addLast("PacketEncoder", new PacketEncoder(main));
						pipeline.addLast("RSAHandshakeHandler", new RSAHandshakeHandler(main, keyPair));
//						pipeline.addLast("AuthHandler", new AuthHandler(main));

					}
				});

			channel = b.bind(port).sync().channel();
			channel.closeFuture().addListener(future -> {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			});

			main.getLogger().info("NetServer auf Port " + port + " erfolgreich gestartet.");

		} catch(InterruptedException e) {
			e.printStackTrace();
		}

	}

	public PacketRegistry<Packet> getPacketRegistry() {
		return packetRegistry;
	}

	public Channel getChannel() {
		return channel;
	}

}