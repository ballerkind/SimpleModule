package de.ballerkind.simplemodule.network;

import de.ballerkind.simplemodule.network.codec.PacketDecoder;
import de.ballerkind.simplemodule.network.codec.PacketEncoder;
import de.ballerkind.simplemodule.network.handler.RSAHandshakeHandler;
import de.ballerkind.simplemodule.network.packet.packets.PacketAuth;
import de.ballerkind.simplemodule.network.packet.packets.PacketAuthStatus;
import de.ballerkind.simplemodule.network.packet.packets.PacketHandshake;
import de.ballerkind.simplemodule.network.packet.packets.PacketHandshakeResponse;
import de.ballerkind.simplemodule.network.registry.PacketRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.function.Consumer;

public class NetClient {

	private String moduleType;
	private String moduleId;
	private String moduleSecret;

	private PacketRegistry packetRegistry;
	private Channel channel;

	public NetClient(String moduleType, String moduleId, String moduleSecret) {
		this.moduleType = moduleType;
		this.moduleId = moduleId;
		this.moduleSecret = moduleSecret;

		this.packetRegistry = new PacketRegistry();
		this.packetRegistry
			.register(PacketHandshake.class)
			.register(PacketHandshakeResponse.class)
			.register(PacketAuth.class)
			.register(PacketAuthStatus.class);
	}

	public void connect(Consumer<Boolean> onAuth) {

		try {

			EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

			Bootstrap b = new Bootstrap()
				.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
				.group(workerGroup)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) {

						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
						pipeline.addLast(new LengthFieldPrepender(4));
						pipeline.addLast("PacketDecoder", new PacketDecoder(packetRegistry));
						pipeline.addLast("PacketEncoder", new PacketEncoder(packetRegistry));
						pipeline.addLast("RSAHandshakeHandler", new RSAHandshakeHandler(onAuth, new PacketAuth(moduleType, moduleId, moduleSecret)));
//						pipeline.addLast("AuthHandler", new AuthHandler(onAuth));

					}
				});

			channel = b.connect("localhost", 2202).sync().channel();
			channel.closeFuture().addListener(future -> workerGroup.shutdownGracefully());

		} catch(InterruptedException e) {
			e.printStackTrace();
		}

	}

	public PacketRegistry getPacketRegistry() {
		return packetRegistry;
	}

	public Channel getChannel() {
		return channel;
	}

}