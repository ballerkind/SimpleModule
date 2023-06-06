package de.ballerkind.simplemodule.core.module.modules.Minecraft;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.plugin.MinecraftPlugin;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.statistic.MinecraftStatistic;
import de.ballerkind.simplemodule.core.module.types.ExternalModule;
import de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft.JsonPacketModuleMinecraftStatistic;
import org.bson.Document;

import java.util.*;

public class ModuleMinecraft extends ExternalModule {

	public static Thread checkPlayerThread;
	private static final long STATS_PERIOD = 300_000L;

	private String version;
	private String ip;
	private int port;
	private MinecraftStatistic statistic;
	private List<MinecraftPlugin> plugins;
	private List<String> console;
	private List<String> players;
	private int maxPlayers;

	public ModuleMinecraft(Main main, String userId, String type, String id, String name, String secret, List<String> shared) {
		super(main, userId, type, id, name, secret, shared);

		if(checkPlayerThread == null) {

			checkPlayerThread = new Thread(() -> {
				try {

					main.getLogger().info("Minecraft statistics-updater started.");

					Calendar c = Calendar.getInstance();
					c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - (c.get(Calendar.MINUTE) % 5));
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);

					// Adjust statistics because of downtime
					main.getModuleManager().getModuleTypes().get("Minecraft").getModules().values().forEach(module -> {
						MinecraftStatistic stat = ((ModuleMinecraft) module).getStatistic();

						List<Integer> toAdd = new ArrayList<>();
						while(stat.getStart() + stat.getData().size() * STATS_PERIOD <= c.getTimeInMillis()) {
							stat.getData().add(null);
							toAdd.add(null);
						}

						if(toAdd.size() == 0) {
							main.getLogger().fine("[Debug] Bug at ModuleMinecraft: Nothing to add after downtime!");
							return;
						}

						main.getDatabaseManager().getExecutorService().execute(() -> {
							Document find = new Document("name", super.getType()).append("modules.id", super.getId());
							Document update = new Document("$push", new Document("modules.$.statistic.data", new Document("$each", toAdd)));
							main.getDatabaseManager().getDatabase().getCollection("Modules").updateOne(find, update);
						});
					});
					main.getLogger().info("Adjusted all Minecraft statistics.");

					c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + (5 - c.get(Calendar.MINUTE) % 5));
					main.getLogger().info("Time until first update: " + (c.getTimeInMillis() - System.currentTimeMillis()) / (1000 * 60f) + " minutes");
					Thread.sleep(c.getTimeInMillis() - System.currentTimeMillis());

					while(true) {
						main.getModuleManager().getModuleTypes().get("Minecraft").getModules().values().forEach(module -> {
							ModuleMinecraft mc = (ModuleMinecraft) module;

							Integer players = super.isEnabled() ? mc.getPlayers().size() : null;
							mc.getStatistic().getData().add(players);

							main.getDatabaseManager().getExecutorService().execute(() -> {
								Document find = new Document("name", super.getType()).append("modules.id", super.getId());
								Document update = new Document("$push", new Document("modules.$.statistic.data", players));
								main.getDatabaseManager().getDatabase().getCollection("Modules").updateOne(find, update);
							});
						});

						main.getNetJsonServer().getClient().writeAndFlush(new JsonPacketModuleMinecraftStatistic());
						main.getLogger().info("Updated all Minecraft statistics.");

						Thread.sleep(STATS_PERIOD);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});

		}
	}

	@Override
	public void onCreate(Document toStore) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - (c.get(Calendar.MINUTE) % 5));
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		toStore.append("statistic", new Document("start", c.getTimeInMillis()).append("data", Collections.singletonList(null)));
	}

	@Override
	public void onLoad(Document fromDatabase) {
		this.version = "";
		this.ip = "";
		this.port = 0;

		Document stats = fromDatabase.get("statistic", Document.class);
		this.statistic = new MinecraftStatistic(stats.getLong("start"), stats.get("data", new ArrayList<>()));

		this.plugins = new ArrayList<>();
		this.console = new ArrayList<>();
		this.players = new ArrayList<>();
		this.maxPlayers = 0;
	}

	@Override
	public void onConnect() {
		//updateStatistic(null);
	}

	@Override
	public void onDisconnect() {
		this.version = "";
		this.ip = "";
		this.port = 0;
		//updateStatistic(this.players.size());
		this.plugins.clear();
		this.console.clear();
		this.players.clear();
		this.maxPlayers = 0;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public MinecraftStatistic getStatistic() {
		return statistic;
	}

	public void setStatistic(MinecraftStatistic statistic) {
		this.statistic = statistic;
	}

	public List<MinecraftPlugin> getPlugins() {
		return plugins;
	}

	public List<String> getConsole() {
		return console;
	}

	public List<String> getPlayers() {
		return players;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

}