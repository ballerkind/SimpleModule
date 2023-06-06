package de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft.JsonPacketModuleMinecraftInfo;
import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.ModuleMinecraft;
import de.ballerkind.simplemodule.core.network.net.packet.IModulePacketHandler;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.net.InetSocketAddress;

public class PacketModuleMinecraftInfo extends Packet implements IModulePacketHandler<ModuleMinecraft> {

	private String version;
	private int port;
	private int maxPlayers;

	public PacketModuleMinecraftInfo() {}
	public PacketModuleMinecraftInfo(String version, int port, int maxPlayers) {
		this.version = version;
		this.port = port;
		this.maxPlayers = maxPlayers;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeUTF(version);
		buf.writeInt(port);
		buf.writeInt(maxPlayers);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.version = buf.readUTF();
		this.port = buf.readInt();
		this.maxPlayers = buf.readInt();
	}

	@Override
	public void handle(Main main, ModuleMinecraft module) {

		module.setVersion(version);
		module.setIP( ((InetSocketAddress) module.getClient().remoteAddress()).getAddress().getHostAddress() );
		module.setPort(port);
		module.setMaxPlayers(maxPlayers);

		main.getNetJsonServer().getClient().writeAndFlush(new JsonPacketModuleMinecraftInfo(module.getId(), version, module.getIP(), port, maxPlayers));

	}

}