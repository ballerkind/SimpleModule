package de.ballerkind.simplemodule.core;

import de.ballerkind.simplemodule.core.command.CommandManager;
import de.ballerkind.simplemodule.core.database.DatabaseManager;
import de.ballerkind.simplemodule.core.language.LanguageManager;
import de.ballerkind.simplemodule.core.logging.LogManager;
import de.ballerkind.simplemodule.core.network.json.NetJsonServer;
import de.ballerkind.simplemodule.core.config.SimpleConfig;
import de.ballerkind.simplemodule.core.module.ModuleManager;
import de.ballerkind.simplemodule.core.network.net.NetServer;
import de.ballerkind.simplemodule.core.user.UserManager;

import java.util.logging.Logger;

public class Main {

	private final Logger logger;
	private final SimpleConfig config;
	private final UserManager userManager;
	private final ModuleManager moduleManager;
	private final DatabaseManager databaseManager;
	private final LanguageManager languageManager;
	private final CommandManager commandManager;

	private final NetJsonServer netJsonServer;
	private final NetServer netServer;

	public Main() {

		this.logger = new LogManager().getLogger();
		this.config = new SimpleConfig(this);
		this.config.load();
		this.userManager = new UserManager();
		this.moduleManager = new ModuleManager();
		this.databaseManager = new DatabaseManager(this, config.get("db_host"), config.getAsInt("db_port"), config.get("db_name"));
		this.databaseManager.connect();
		this.languageManager = new LanguageManager(this);
		this.languageManager.reload();
		this.commandManager = new CommandManager(this);

		this.netJsonServer = new NetJsonServer(this, config.getAsInt("web_port"));
		this.netJsonServer.start();
		this.netServer = new NetServer(this, config.getAsInt("server_port"));
		this.netServer.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("System wird heruntergefahren...");

			netServer.getChannel().close().awaitUninterruptibly();
			logger.info("NetServer wurde gestoppt.");

			netJsonServer.getChannel().close().awaitUninterruptibly();
			logger.info("NetJsonServer wurde gestoppt.");

			config.save();
			logger.info("Config was saved.");

			logger.info("System heruntergefahren!");
		}));

	}

	public Logger getLogger() {
		return logger;
	}

	public SimpleConfig getConfig() {
		return config;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public LanguageManager getLanguageManager() {
		return languageManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public NetJsonServer getNetJsonServer() {
		return netJsonServer;
	}

	public NetServer getNetServer() {
		return netServer;
	}


	public static void main(String[] args) {
		new Main();
	}

}