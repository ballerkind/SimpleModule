package de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft.JsonPacketModuleMinecraftConsole;
import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.ModuleMinecraft;
import de.ballerkind.simplemodule.core.network.net.packet.IModulePacketHandler;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.HashMap;

public class PacketModuleMinecraftConsole extends Packet implements IModulePacketHandler<ModuleMinecraft> {

	private final static HashMap<String, String> spigotColors = new HashMap<>();
	private final static HashMap<String, String> bungeeColors = new HashMap<>();
	static {
		spigotColors.put("30;22", "#010101");
		spigotColors.put("31;22", "#DE382B");
		spigotColors.put("32;22", "#39B54A");
		spigotColors.put("33;22", "#FFC706");
		spigotColors.put("34;22", "#006FB8");
		spigotColors.put("35;22", "#762671");
		spigotColors.put("36;22", "#2CB5E9");
		spigotColors.put("37;22", "#CCCCCC");

		spigotColors.put("30;1", "#808080");
		spigotColors.put("31;1", "#FF0000");
		spigotColors.put("32;1", "#00FF00");
		spigotColors.put("33;1", "#FFFF00");
		spigotColors.put("34;1", "#0000FF");
		spigotColors.put("35;1", "#FF00FF");
		spigotColors.put("36;1", "#00FFFF");
		spigotColors.put("37;1", "#FFFFFF");


		bungeeColors.put("0", "#000000");
		bungeeColors.put("1", "#0000AA");
		bungeeColors.put("2", "#00AA00");
		bungeeColors.put("3", "#00AAAA");
		bungeeColors.put("4", "#AA0000");
		bungeeColors.put("5", "#AA00AA");
		bungeeColors.put("6", "#FFAA00");
		bungeeColors.put("7", "#AAAAAA");
		bungeeColors.put("8", "#555555");
		bungeeColors.put("9", "#5555FF");
		bungeeColors.put("a", "#55FF55");
		bungeeColors.put("b", "#55FFFF");
		bungeeColors.put("c", "#FF5555");
		bungeeColors.put("d", "#FF55FF");
		bungeeColors.put("e", "#FFFF55");
		bungeeColors.put("f", "#FFFFFF");
		bungeeColors.put("r", "#FFFFFF");
	}

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
	public void handle(Main main, ModuleMinecraft module) {

		this.command = command.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		this.command = "<font>" + command + "</font>";

		if(module.getVersion().contains("BungeeCord")) {
			bungeeColors.forEach((code, color) -> this.command = command.replaceAll("§" + code, "</font><font color='" + color + "'>"));
		} else {
			this.command = command.replaceAll("\\u001b\\[m", "");
			spigotColors.forEach((code, color) -> this.command = command.replaceAll("\\u001b\\[0;" + code + "m", "</font><font color='" + color + "'>"));
		}

		module.getConsole().add(command);
		main.getNetJsonServer().getClient().writeAndFlush(new JsonPacketModuleMinecraftConsole(module.getId(), command));

	}

}