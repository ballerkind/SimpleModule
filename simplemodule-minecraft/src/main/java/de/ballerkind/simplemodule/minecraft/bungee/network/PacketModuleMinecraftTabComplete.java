package de.ballerkind.simplemodule.minecraft.bungee.network;

import de.ballerkind.simplemodule.minecraft.spigot.Main;
import de.ballerkind.simplemodule.minecraft.spigot.network.PacketModuleMinecraftTabCompleteResponse;
import de.ballerkind.simplemodule.network.packet.IPacketHandler;
import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.md_5.bungee.api.ProxyServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketModuleMinecraftTabComplete extends Packet implements IPacketHandler {

	private String socketId;
	private String message;

	public PacketModuleMinecraftTabComplete() {}
	public PacketModuleMinecraftTabComplete(String socketId, String message) {
		this.socketId = socketId;
		this.message = message;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeUTF(socketId);
		buf.writeUTF(message);
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.socketId = buf.readUTF();
		this.message = buf.readUTF();
	}

	@Override
	public void handle() {
		List<String> response = new ArrayList<>();
		ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), message, response);

		Main.getInstance().getNetClient().getChannel().writeAndFlush(new PacketModuleMinecraftTabCompleteResponse(socketId, response));
	}

}