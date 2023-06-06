package de.ballerkind.simplemodule.minecraft.bungee.network;

import de.ballerkind.simplemodule.network.packet.IPacketHandler;
import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.md_5.bungee.api.ProxyServer;

import java.io.IOException;

public class PacketModuleMinecraftConsole extends Packet implements IPacketHandler {

	private String command;

	public PacketModuleMinecraftConsole() {}
	public PacketModuleMinecraftConsole(String command) {
		this.command = command;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeUTF(command);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.command = buf.readUTF();
	}

	@Override
	public void handle() {
		ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), command);
	}

}