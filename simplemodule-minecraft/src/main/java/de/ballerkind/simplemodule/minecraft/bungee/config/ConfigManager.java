package de.ballerkind.simplemodule.minecraft.bungee.config;

import de.ballerkind.simplemodule.minecraft.bungee.Main;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigManager {

	public static Map<String, String> getConfig(Plugin main) {

		main.getDataFolder().mkdir();

		File cfg = new File(main.getDataFolder(), "config.cfg");
		if(!cfg.exists()) {
			try {
				cfg.createNewFile();

				String toSave = "moduleId=id123\nmoduleSecret=secret123";
				Files.write(Paths.get(cfg.getPath()), toSave.getBytes());

				System.out.println(Main.PREFIX + "---------------------------------------------");
				System.out.println(Main.PREFIX + "Config successfully created.");
				System.out.println(Main.PREFIX + "Please enter your credentials into the file.");
				System.out.println(Main.PREFIX + "Shutting down the server...");
				System.out.println(Main.PREFIX + "---------------------------------------------");

				main.getProxy().stop();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			Map<String, String> map = new HashMap<>();

			for(String line : Files.lines(Paths.get(cfg.getPath())).collect(Collectors.toList())) {
				if(line.contains("=")) {
					String[] split = line.split("=");
					map.put(split[0], split[1]);
				} else {
					System.out.println(Main.PREFIX + "Config file contains illegal content: " + line);
					return null;
				}
			}

			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

}