package de.ballerkind.simplemodule.minecraft.spigot.network;

import de.ballerkind.simplemodule.minecraft.spigot.Main;
import de.ballerkind.simplemodule.network.packet.IPacketHandler;
import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.bukkit.Bukkit;

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
		Bukkit.getScheduler().runTask(Main.getInstance(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command));
	}

}