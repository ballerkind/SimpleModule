package de.ballerkind.simplemodule.core.network.json;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.network.json.codec.JsonPacketDecoder;
import de.ballerkind.simplemodule.core.network.json.codec.JsonPacketEncoder;
import de.ballerkind.simplemodule.core.network.json.handler.JsonAuthHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.network.json.packet.packets.*;
import de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft.*;
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
import io.netty.handler.codec.LineBasedFrameDecoder;

public class NetJsonServer {

	private Main main;
	private int port;

	public PacketRegistry<JsonPacket> packetRegistry;
	private Channel channel;
	private Channel client;

	public NetJsonServer(Main main, int port) {

		this.main = main;
		this.port = port;
		this.packetRegistry = new PacketRegistry<>();

		this.packetRegistry
			.register(JsonPacketAuth.class)
			.register(JsonPacketAuthStatus.class)
			.register(JsonPacketLanguage.class)
			.register(JsonPacketModuleAdd.class)
			.register(JsonPacketModuleDelete.class)
			.register(JsonPacketModuleRedeem.class)
			.register(JsonPacketModuleRemove.class)
			.register(JsonPacketModules.class)
			.register(JsonPacketModuleStart.class)
			.register(JsonPacketModuleStop.class)
			.register(JsonPacketModuleUpdate.class)
			.register(JsonPacketUserChangeEmail.class)
			.register(JsonPacketUserChangePassword.class)
			.register(JsonPacketUserCreate.class)
			.register(JsonPacketUserDelete.class)
			.register(JsonPacketUsers.class)
			.register(JsonPacketUserVerify.class)
			.next() // Minecraft
			.register(JsonPacketModuleMinecraftConsole.class)
			.register(JsonPacketModuleMinecraftInfo.class)
			.register(JsonPacketModuleMinecraftPlayer.class)
			.register(JsonPacketModuleMinecraftPluginStatus.class)
			.register(JsonPacketModuleMinecraftStatistic.class)
			.register(JsonPacketModuleMinecraftStatisticErase.class)
			.register(JsonPacketModuleMinecraftTabComplete.class)
			.register(JsonPacketModuleMinecraftTabCompleteResponse.class);
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
						pipeline.addLast(new LineBasedFrameDecoder(8192));
						pipeline.addLast("PacketDecoder", new JsonPacketDecoder(main));
						pipeline.addLast("PacketEncoder", new JsonPacketEncoder(main));
						pipeline.addLast("AuthHandler", new JsonAuthHandler(main));

					}
				});

			channel = b.bind(port).sync().channel();
			channel.closeFuture().addListener(future -> {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			});

			main.getLogger().info("NetJsonServer auf Port " + port + " erfolgreich gestartet.");

		} catch(InterruptedException e) {
			e.printStackTrace();
		}

	}

	public PacketRegistry<JsonPacket> getPacketRegistry() {
		return packetRegistry;
	}

	public Channel getChannel() {
		return channel;
	}

	public Channel getClient() {
		return client;
	}

	public void setClient(Channel client) {
		this.client = client;
	}

}