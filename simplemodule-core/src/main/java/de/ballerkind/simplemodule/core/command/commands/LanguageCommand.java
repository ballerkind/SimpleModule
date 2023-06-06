package de.ballerkind.simplemodule.core.command.commands;

import de.ballerkind.simplemodule.core.network.json.packet.packets.JsonPacketLanguage;
import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.command.Command;

public class LanguageCommand extends Command {

	public LanguageCommand(Main main, String name, String description, String... usage) {
		super(main, name, description, usage);
	}

	@Override
	public boolean execute(String[] args) {
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			main.getLanguageManager().reload();
			main.getNetJsonServer().getClient().writeAndFlush(new JsonPacketLanguage(main.getLanguageManager().getLanguages(), main.getLanguageManager().getMessages()));
			main.getLogger().info("Sprachen wurden aktualisiert.");
			return true;
		}

		return false;
	}

}