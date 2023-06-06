package de.ballerkind.simplemodule.core.database;

import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.module.ModuleType;
import de.ballerkind.simplemodule.core.user.User;
import org.bson.Document;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {

	private Main main;

	private String host;
	private int port;
	private String db;

	private MongoDatabase database;
	private ExecutorService executorService;

	public DatabaseManager(Main main, String host, int port, String db) {
		this.main = main;
		this.host = host;
		this.port = port;
		this.db = db;

		this.executorService = Executors.newCachedThreadPool();

		Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
	}

	public void connect() {

		MongoClient client = MongoClients.create("mongodb://" + host + ":" + port);
		this.database = client.getDatabase(db);

		main.getLogger().info("Versuche Verbindung zu MongoDB aufzubauen...");

		try {
			database.runCommand(new Document("ping", 1));
		} catch(MongoTimeoutException e) {
			main.getLogger().severe("Verbindung konnte nicht hergestellt werden! System wird zwangsweise heruntergefahren.");
			System.exit(-1);
		}

		main.getLogger().info("Verbindung zu MongoDB wurde erfolgreich aufgebaut.");
		main.getLogger().info("Lade alle Daten aus der MongoDB Datenbank...");

		// Load Modules
		database.getCollection("Modules").find().sort(new Document("name", 1)).forEach((Consumer<? super Document>) document -> {

			String typeName = document.getString("name");
			String icon = document.getString("icon");

			try {

				ModuleType moduleType = new ModuleType(typeName, Class.forName("de.ballerkind.simplemodule.core.module.modules." + typeName + ".Module" + typeName).asSubclass(Module.class), icon);

				List<Document> modules = document.getList("modules", Document.class);
				for(Document moduleDocument : modules) {

					String userId = moduleDocument.getString("userId");
					String type = moduleType.getName();
					String id = moduleDocument.getString("id");
					String secret = moduleDocument.getString("secret");
					String name = moduleDocument.getString("name");
					List<String> shared = moduleDocument.getList("shared", String.class);

					Module module = moduleType.getModuleClass().getConstructor(Main.class, String.class, String.class, String.class, String.class, String.class, List.class)
										.newInstance(main, userId, type, id, name, secret, shared);

					module.onLoad(moduleDocument);

					moduleType.getModules().put(module.getId(), module);

				}

				main.getModuleManager().getModuleTypes().put(typeName, moduleType);

			} catch(ClassNotFoundException e) {
				main.getLogger().warning("Die Klasse von '" + typeName + "' konnte nicht gefunden werden!");
			} catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}

		});

		// Load Users
		database.getCollection("Users").find().forEach((Consumer<? super Document>) document -> {
			User user = new User(document.getString("_id"), document.getString("email"), document.getString("name"), document.getString("password"), document.getBoolean("verified"));
			main.getUserManager().getUsers().add(user);
		});

		// Assign Modules to Users
		main.getModuleManager().getModuleTypes().forEach((type, moduleType) -> moduleType.getModules().forEach((moduleId, module) -> main.getUserManager().getUsers().forEach(user -> {
			if(module.getUserId().equals(user.getId())) {
				if(!user.getModules().containsKey(type)) user.getModules().put(type, new ArrayList<>());
				user.getModules().get(type).add(moduleId);
			}

			else if(module.getShared().contains(user.getId())) {
				if(!user.getSharedModules().containsKey(type)) user.getSharedModules().put(type, new ArrayList<>());
				user.getSharedModules().get(type).add(moduleId);
			}
		})));

		// Sort Modules
		main.getUserManager().getUsers().forEach(user -> {
			user.getModules().forEach((type, ids) -> ids.sort((first, second) -> main.getModuleManager().getModuleById(type, first).getName().compareToIgnoreCase(main.getModuleManager().getModuleById(type, second).getName())));
			user.getSharedModules().forEach((type, ids) -> ids.sort((first, second) -> main.getModuleManager().getModuleById(type, first).getName().compareToIgnoreCase(main.getModuleManager().getModuleById(type, second).getName())));
		});

		main.getLogger().info("Alle Daten erfolgreich geladen!");

		// Minecraft Statistic
		// TODO: beheben
		// ModuleMinecraft.checkPlayerThread.start();

	}

	public MongoDatabase getDatabase() {
		return database;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

}