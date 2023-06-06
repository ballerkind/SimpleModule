package de.ballerkind.simplemodule.core.command.commands;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.command.Command;
import de.ballerkind.simplemodule.core.module.Module;
import de.ballerkind.simplemodule.core.user.User;

public class UserCommand extends Command {

	public UserCommand(Main main, String name, String description, String... usage) {
		super(main, name, description, usage);
	}

	@Override
	public boolean execute(String[] args) {
		if(args.length == 1) {
			User user = null;
			for(User u : main.getUserManager().getUsers()) {
				if(u.getName().equalsIgnoreCase(args[0])) {
					user = u;
					break;
				}
			}

			if(user != null) {
				main.getLogger().info("Benutzerinfo: " + user.getName() + " (" + user.getId() + ")");
				for(String type : main.getModuleManager().getModuleTypes().keySet()) {
					if(user.getModules().get(type) != null || user.getSharedModules().get(type) != null) {
						main.getLogger().info("	+ " + type + ":");

						if(user.getModules().get(type) != null) {
							user.getModules().get(type).forEach(moduleId -> {
								Module module = main.getModuleManager().getModuleById(type, moduleId);
								main.getLogger().info("		" + module.getId() + ": " + module.getName() + ", " + (module.isEnabled() ? "enabled" : "disabled"));
							});
						}

						if(user.getSharedModules().get(type) != null) {
							user.getSharedModules().get(type).forEach(moduleId -> {
								Module module = main.getModuleManager().getModuleById(type, moduleId);
								main.getLogger().info("		" + module.getId() + ": " + module.getName() + "(Geteilt von " + main.getUserManager().getUser(module.getUserId()).getName() + ")");
							});
						}
					}
				}
			} else {
				main.getLogger().info("Der Nutzer '" + args[0] + "' existiert nicht!");
			}

			return true;
		}

		return false;
	}

}