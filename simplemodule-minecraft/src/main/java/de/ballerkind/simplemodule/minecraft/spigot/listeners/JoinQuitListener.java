package de.ballerkind.simplemodule.minecraft.spigot.listeners;

import de.ballerkind.simplemodule.minecraft.spigot.Main;
import de.ballerkind.simplemodule.minecraft.spigot.network.PacketModuleMinecraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

	private Main main;

	public JoinQuitListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		main.getNetClient().getChannel().writeAndFlush(new PacketModuleMinecraftPlayer(e.getPlayer().getName(), true));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		main.getNetClient().getChannel().writeAndFlush(new PacketModuleMinecraftPlayer(e.getPlayer().getName(), false));
	}

}