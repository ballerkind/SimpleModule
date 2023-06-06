package de.ballerkind.simplemodule.minecraft.fun;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;

public class MapListener implements Listener {

	@EventHandler
	public void onMapCreate(MapInitializeEvent event) {
		Bukkit.broadcastMessage("§dinit §asheesh");
		event.getMap().getRenderers().forEach(r -> {
			event.getMap().removeRenderer(r);
		});
		event.getMap().addRenderer(new SMapRender());
	}

}