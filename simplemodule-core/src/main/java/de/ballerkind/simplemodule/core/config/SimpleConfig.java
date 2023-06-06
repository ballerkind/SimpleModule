package de.ballerkind.simplemodule.core.config;

import de.ballerkind.simplemodule.core.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleConfig {

	private static final String DELIMITER = "=";

	private Main main;
	private File configFile;
	private Map<String, String> config;

	public SimpleConfig(Main main) {
		this.main = main;

		this.configFile = new File("config.cfg");
		this.config = new LinkedHashMap<>();
	}

	public String get(String key) {
		return config.get(key);
	}

	public int getAsInt(String key) {
		return Integer.parseInt(get(key));
	}

	public void put(String key, String value) {
		config.put(key, value);
	}

	public void load() {
		try {
			// defaults
			put("web_port", "2201");
			put("web_authkey", UUID.randomUUID().toString().replaceAll("-", ""));
			put("server_port", "2202");
			put("db_host", "localhost");
			put("db_port", "27017");
			put("db_name", "SimpleModule");
			// defaults

			if(configFile.createNewFile()) {
				save();
				main.getLogger().info("Config file was successfully created!");
			} else {
				for(String line : Files.readAllLines(Paths.get(configFile.getPath()))) {
					if(line.contains(DELIMITER)) {
						String[] split = line.split(DELIMITER);
						config.put(split[0], split[1]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		StringBuilder builder = new StringBuilder();
		config.forEach((key, value) -> {
			builder.append(key).append(DELIMITER).append(value).append("\n");
		});

		try {
			Files.write(Paths.get(configFile.getPath()), builder.substring(0, builder.length() - 1).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}