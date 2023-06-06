package de.ballerkind.simplemodule.core.command.commands;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.command.Command;

import java.util.List;

public class UserListCommand extends Command {

	public UserListCommand(Main main, String name, String description, String... usage) {
		super(main, name, description, usage);
	}

	@Override
	public boolean execute(String[] args) {

		main.getUserManager().getUsers().forEach(user -> {
			int moduleAmount = 0;
			for(List<String> modules : user.getModules().values()) {
				moduleAmount += modules.size();
			}

			int sharedAmount = 0;
			for(List<String> modules : user.getSharedModules().values()) {
				sharedAmount += modules.size();
			}

			main.getLogger().info(user.getId() + " | " + user.getEmail() + " | " + user.getName() + " | " + moduleAmount + " Module | " + sharedAmount + " Geteilte Module");
		});

		return true;

	}

}