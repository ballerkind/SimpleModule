package de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.ModuleMinecraft;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.statistic.MinecraftStatistic;
import de.ballerkind.simplemodule.core.network.json.packet.IPacketHandler;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import org.bson.Document;

import java.util.ArrayList;

public class JsonPacketModuleMinecraftStatisticErase extends JsonPacket implements IPacketHandler {

	private String moduleId;

	@Override
	public void handle(Main main) {

		ModuleMinecraft module = (ModuleMinecraft) main.getModuleManager().getModuleById("Minecraft", moduleId);

		Document doc = new Document();
		module.onCreate(doc);
		doc = doc.get("statistic", Document.class);

		MinecraftStatistic stat = new MinecraftStatistic(doc.getLong("start"), doc.get("data", new ArrayList<>()));

		module.setStatistic(stat);

		main.getDatabaseManager().getExecutorService().execute(() -> {
			Document find = new Document("name", module.getType()).append("modules.id", module.getId());
			Document update = new Document("$set", new Document("modules.$.statistic", new Document("start", stat.getStart()).append("data", stat.getData())));
			main.getDatabaseManager().getDatabase().getCollection("Modules").updateOne(find, update);
		});

	}

}