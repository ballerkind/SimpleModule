package de.ballerkind.simplemodule.minecraft.bungee;

import de.ballerkind.simplemodule.minecraft.bungee.config.ConfigManager;
import de.ballerkind.simplemodule.minecraft.bungee.network.PacketModuleMinecraftConsole;
import de.ballerkind.simplemodule.minecraft.bungee.network.PacketModuleMinecraftPluginStatus;
import de.ballerkind.simplemodule.minecraft.bungee.network.PacketModuleMinecraftTabComplete;
import de.ballerkind.simplemodule.network.NetClient;
import de.ballerkind.simplemodule.minecraft.spigot.network.PacketModuleMinecraftInfo;
import de.ballerkind.simplemodule.minecraft.spigot.network.PacketModuleMinecraftPlayer;
import de.ballerkind.simplemodule.minecraft.spigot.network.PacketModuleMinecraftTabCompleteResponse;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Main extends Plugin {

	public static final String PREFIX = "[SimpleBungee] ";

	private NetClient netClient;

	@Override
	public void onEnable() {

		Map<String, String> config = ConfigManager.getConfig(this);
		if(config == null || !config.containsKey("moduleId") || !config.containsKey("moduleSecret")) return;

		this.netClient = new NetClient("Minecraft", config.get("moduleId"), config.get("moduleSecret"));
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

				Formatter bungeeFormatter = ProxyServer.getInstance().getLogger().getHandlers()[1].getFormatter();
				Handler customHandler = new Handler() {
					@Override
					public void publish(LogRecord record) {
						String line = bungeeFormatter.format(record);
						netClient.getChannel().writeAndFlush(new PacketModuleMinecraftConsole(line));
					}

					@Override
					public void flush() {
						System.out.println("toilet");
					}

					@Override
					public void close() throws SecurityException {
						System.out.println("bbbbbbbbb");
					}
				};
				ProxyServer.getInstance().getLogger().addHandler(customHandler);

				this.netClient.getChannel().writeAndFlush(new PacketModuleMinecraftInfo(super.getProxy().getVersion(), ((InetSocketAddress) ProxyServer.getInstance().getConfigurationAdapter().getListeners().iterator().next().getSocketAddress()).getPort(), getProxy().getConfig().getPlayerLimit()));
				for(Plugin plugin : getProxy().getPluginManager().getPlugins()) {
					this.netClient.getChannel().writeAndFlush(new PacketModuleMinecraftPluginStatus(plugin.getDescription().getName(), true));
				}

				getProxy().getPlayers().forEach(player -> this.netClient.getChannel().writeAndFlush(new PacketModuleMinecraftPlayer(player.getName(), true)));

			} else {
				System.out.println(PREFIX + "Authentication failed!");
			}
		});

	}

	@Override
	public void onDisable() {

		if(this.netClient != null && this.netClient.getChannel() != null && this.netClient.getChannel().isActive()) {
			this.netClient.getChannel().close().awaitUninterruptibly();
		}

	}

	public NetClient getNetClient() {
		return netClient;
	}

}