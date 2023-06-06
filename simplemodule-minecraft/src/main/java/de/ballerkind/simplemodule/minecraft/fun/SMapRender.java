package de.ballerkind.simplemodule.minecraft.fun;

import de.ballerkind.simplemodule.minecraft.spigot.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SMapRender extends MapRenderer {

	public static List<String> cmds;
	public static boolean go = false;
	public static Image img;

	private Color[] colors;
	private long lastCheck;

	public static void doNice() {
		Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(Main.class), () -> {
			try {

				Thread.sleep(5000);

				cmds.add("Loading server data...");
				Thread.sleep(1000);
				for(int i = 0; i < 16; i++) {
					StringBuilder s = new StringBuilder("Starting Server");
					for(int p = 0; p < i % 4; p++) {
						s.append(".");
					}
					cmds.add(s.toString());
					Thread.sleep(250);
					cmds.remove(cmds.size() - 1);
				}
				cmds.add("Server started!");
				Thread.sleep(1000);
				cmds.add("Stats loaded.");
				Thread.sleep(2000);
				cmds.add("System booted! Hf");
				Thread.sleep(2000);
				cmds.add("Switching to GUI...");
				Thread.sleep(1000);
				go = true;

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	public SMapRender() {

		try {
			Field f = MapPalette.class.getDeclaredField("colors");
			f.setAccessible(true);
			this.colors = (Color[]) f.get(null);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		lastCheck = 0;

		Bukkit.broadcastMessage("size: " + colors.length);

		if(cmds == null) {
			cmds = new ArrayList<>();
			img = new ImageIcon("D:/MinecraftServer/Spigot/plugins/terminal.png").getImage();
			doNice();
		}

	}

	@Override
	public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
		if(mapView.getId() == 7) {
			int i = 0;
			for(int x = 0; x < 128; x++) {
				for(int y = 0; y < 64; y++) {
					mapCanvas.setPixel(x,y, MapPalette.matchColor(colors[i]));
				}
				i = (i+1)%colors.length;
			}
			for(int x = 0; x < 128; x++) {
				for(int y = 65; y < 128; y++) {
					mapCanvas.setPixel(x,y, MapPalette.matchColor(colors[i]));
				}
				i = (i+1)%colors.length;
			}
			return;
		}

		if(System.currentTimeMillis() - lastCheck < 100) return;
		lastCheck = System.currentTimeMillis();


		if(go) {
			for(int x = 0; x < 128; x++) {
				for(int y = 0; y < 128; y++) {
					mapCanvas.setPixel(x,y, (byte) 0);
				}
			}

			String s = "-----------------";
			mapCanvas.drawText(63 - MinecraftFont.Font.getWidth(s) / 2, 30, MinecraftFont.Font, "§0;" + s);
			s = "BedWars-12";
			mapCanvas.drawText(63 - MinecraftFont.Font.getWidth(s) / 2, 44, MinecraftFont.Font, "§12;" + s);
			s = Math.round(Math.random() * 10) + " / 12";
			mapCanvas.drawText(63 - MinecraftFont.Font.getWidth(s) / 2, 44 + 5 + MinecraftFont.Font.getHeight(), MinecraftFont.Font, "§4;" + s);
			s = "Waiting";
			mapCanvas.drawText(63 - MinecraftFont.Font.getWidth(s) / 2, 44 + 5 + MinecraftFont.Font.getHeight() + 5 + MinecraftFont.Font.getHeight(), MinecraftFont.Font, "§30;" + s);
			s = "-----------------";
			mapCanvas.drawText(63 - MinecraftFont.Font.getWidth(s) / 2, 87, MinecraftFont.Font, "§0;" + s);
		} else {
			mapCanvas.drawImage(0,0, img);
			if(cmds != null && cmds.size() > 0) {
				int h = 0;
				for (int i = Math.max(0, cmds.size() - 3); i < cmds.size(); i++) {
					String l = cmds.get(i);
					mapCanvas.drawText(5, 42 + ((MinecraftFont.Font.getHeight() + 5) * h++), MinecraftFont.Font, l);
				}
			}
		}
	}

}