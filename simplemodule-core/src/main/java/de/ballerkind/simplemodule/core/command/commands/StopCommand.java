package de.ballerkind.simplemodule.core.command.commands;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.command.Command;

public class StopCommand extends Command {

	public StopCommand(Main main, String name, String description, String... usage) {
		super(main, name, description, usage);
	}

	@Override
	public boolean execute(String[] args) {
		System.exit(0);
		return true;
	}

}