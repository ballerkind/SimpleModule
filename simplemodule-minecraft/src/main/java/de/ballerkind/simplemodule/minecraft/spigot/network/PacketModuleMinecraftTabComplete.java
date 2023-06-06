package de.ballerkind.simplemodule.minecraft.spigot.network;

import de.ballerkind.simplemodule.minecraft.spigot.Main;
import de.ballerkind.simplemodule.network.packet.IPacketHandler;
import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.bukkit.Bukkit;

import java.io.IOException;
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

		List<String> response = Main.getInstance().getCommandMap().tabComplete(Bukkit.getConsoleSender(), message);
		Main.getInstance().getNetClient().getChannel().writeAndFlush(new PacketModuleMinecraftTabCompleteResponse(socketId, response));

	}

}