package de.ballerkind.simplemodule.minecraft.spigot.listeners;

import de.ballerkind.simplemodule.minecraft.spigot.Main;
import de.ballerkind.simplemodule.minecraft.spigot.network.PacketModuleMinecraftPluginStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginListener implements Listener {

	private Main main;

	public PluginListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent e) {
		main.getNetClient().getChannel().writeAndFlush(new PacketModuleMinecraftPluginStatus(e.getPlugin().getName(), true));
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		main.getNetClient().getChannel().writeAndFlush(new PacketModuleMinecraftPluginStatus(e.getPlugin().getName(), false));
	}

}