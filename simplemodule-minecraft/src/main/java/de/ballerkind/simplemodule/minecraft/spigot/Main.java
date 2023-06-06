package de.ballerkind.simplemodule.minecraft.spigot;

import de.ballerkind.simplemodule.minecraft.spigot.console.ConsoleAppender;
import de.ballerkind.simplemodule.minecraft.spigot.listeners.JoinQuitListener;
import de.ballerkind.simplemodule.minecraft.spigot.listeners.PluginListener;
import de.ballerkind.simplemodule.minecraft.fun.MapListener;
import de.ballerkind.simplemodule.minecraft.spigot.network.*;
import de.ballerkind.simplemodule.network.NetClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin {

	public static final String PREFIX = "[SimpleMinecraft] ";

	private static Main instance;
	private NetClient netClient;

	private CommandMap commandMap;

	@Override
	public void onEnable() {

		instance = this;

		getServer().getPluginManager().registerEvents(new MapListener(), this);

		if(!getConfig().contains("moduleId") || !getConfig().contains("moduleSecret")) {
			getConfig().set("moduleId", "id123");
			getConfig().set("moduleSecret", "secret123");
			saveConfig();

			System.out.println(PREFIX + "---------------------------------------------");
			System.out.println(PREFIX + "Config successfully created.");
			System.out.println(PREFIX + "Please enter your credentials into the file.");
			System.out.println(PREFIX + "Shutting down the server...");
			System.out.println(PREFIX + "---------------------------------------------");

			Bukkit.shutdown();
			return;
		}

		this.netClient = new NetClient("Minecraft", getConfig().getString("moduleId"), getConfig().getString("moduleSecret"));
		this.netClient.getPacketRegistry()
			.next(100) // Minecraft-ID
			.register(PacketModuleMinecraftConsole.class)
			.register(PacketModuleMinecraftInfo.class)
			.register(PacketModuleMinecraftPlayer.class)
			.register(PacketModuleMinecraftPluginStatus.class)
			.register(PacketModuleMinecraftTabComplete.class)
			.register(PacketModuleMinecraftTabCompleteResponse.class);
		this.netClient.connect((success) -> {
			if(success) {
				System.out.println(PREFIX + "Successfully connected!");

				((Logger) LogManager.getRootLogger()).addAppender(new ConsoleAppender(this.netClient));

				getServer().getPluginManager().registerEvents(new JoinQuitListener(this), this);
				getServer().getPluginManager().registerEvents(new PluginListener(this), this);

				this.netClient.getChannel().writeAndFlush(new PacketModuleMinecraftInfo(Bukkit.getBukkitVersion(), Bukkit.getPort(), Bukkit.getMaxPlayers()));
				for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
					this.netClient.getChannel().writeAndFlush(new PacketModuleMinecraftPluginStatus(plugin.getName(), plugin.isEnabled()));
				}
				Bukkit.getOnlinePlayers().forEach(player -> this.netClient.getChannel().writeAndFlush(new PacketModuleMinecraftPlayer(player.getName(), true)));

				// TODO: test & remove
				new Thread(() -> {
					Random rdm = new Random();
					int player = 0;

					try {
						List<String> user = new ArrayList<>();

						for(int i = 0; i < 100; i++) {
							int wait = rdm.nextInt(2) * 60 * 1000;
							System.out.println("waiting for " + wait + " minutes");
							Thread.sleep(wait);

							if(user.size() > 0)
								for(int t = 0; t < rdm.nextInt(user.size()); t++) {
									String s = user.get(rdm.nextInt(user.size()));
									user.remove(s);
									this.netClient.getChannel().writeAndFlush(new PacketModuleMinecraftPlayer(s, false));
									System.out.println("- " + s);
								}

							for(int t = 0; t < rdm.nextInt(20); t++) {
								String s = "Player" + player++;
								user.add(s);
								this.netClient.getChannel().writeAndFlush(new PacketModuleMinecraftPlayer(s, true));
								System.out.println("+ " + s);
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}).start();
			} else {
				System.out.println(PREFIX + "Authentication failed!");
			}
		});

		// GetCommandMap
		try {
			Field f = getServer().getClass().getDeclaredField("commandMap");
			f.setAccessible(true);
			this.commandMap = (CommandMap) f.get(this.getServer());
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDisable() {

		instance = null;

		Bukkit.getScheduler().cancelTasks(this);

		if(this.netClient != null && this.netClient.getChannel() != null && this.netClient.getChannel().isActive()) {
			this.netClient.getChannel().close().awaitUninterruptibly();
		}

	}

	public CommandMap getCommandMap() {
		return commandMap;
	}

	public NetClient getNetClient() {
		return netClient;
	}

	public static Main getInstance() {
		return instance;
	}

}