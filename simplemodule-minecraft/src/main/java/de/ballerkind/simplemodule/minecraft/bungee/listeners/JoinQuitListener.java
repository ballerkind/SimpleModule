package de.ballerkind.simplemodule.minecraft.bungee.listeners;

import de.ballerkind.simplemodule.minecraft.bungee.Main;
import de.ballerkind.simplemodule.minecraft.spigot.network.PacketModuleMinecraftPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinQuitListener implements Listener {

	private Main main;

	public JoinQuitListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onLogin(PostLoginEvent event) {
		main.getNetClient().getChannel().writeAndFlush(new PacketModuleMinecraftPlayer(event.getPlayer().getName(), true));
	}

	@EventHandler
	public void onQuit(PlayerDisconnectEvent event) {
		main.getNetClient().getChannel().writeAndFlush(new PacketModuleMinecraftPlayer(event.getPlayer().getName(), false));
	}

}