package de.ballerkind.simplemodule.core.command.commands;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.command.Command;

public class HelpCommand extends Command {

	public HelpCommand(Main main, String name, String description, String... usage) {
		super(main, name, description, usage);
	}

	@Override
	public boolean execute(String[] args) {

		if(args.length == 0) {
			main.getCommandManager().getCommands().forEach(command -> main.getLogger().info("Befehl: " + command.getName()));

			return true;
		}

		if(args.length == 1) {
			for(Command command : main.getCommandManager().getCommands()) {
				if(command.getName().equalsIgnoreCase(args[0])) {

					main.getLogger().info("");
					main.getLogger().info("Name: " + command.getName());
					main.getLogger().info("Verwendung: ");
					command.getUsage().forEach(usage -> main.getLogger().info(" + " + usage));
					main.getLogger().info("Beschreibung: " + command.getDescription());
					main.getLogger().info("");

					return true;
				}
			}

			main.getLogger().info("Der Befehl '" + args[0] + "' konnte nicht gefunden werden!");
			return true;
		}

		return false;

	}

}