package de.ballerkind.simplemodule.minecraft.spigot.console;

import de.ballerkind.simplemodule.network.NetClient;
import de.ballerkind.simplemodule.minecraft.spigot.network.PacketModuleMinecraftConsole;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

public class ConsoleAppender extends AbstractAppender {

	private NetClient netClient;

	public ConsoleAppender(NetClient netClient) {
		super("MC", null, null, false);
		this.netClient = netClient;

		start();
	}

	@Override
	public void append(LogEvent event) {
		String log = event.getMessage().getFormattedMessage();
		netClient.getChannel().writeAndFlush(new PacketModuleMinecraftConsole(log));
	}

}