package de.ballerkind.simplemodule.core.command;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.command.commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CommandManager {

	private List<Command> commands;

	public CommandManager(Main main) {

		this.commands = new ArrayList<>();
		this.commands.add(new HelpCommand(main, "help", "Zeigt den Hilfebereich an", "help [Befehl]"));
		this.commands.add(new LanguageCommand(main, "language", "Verwaltet das Sprachsystem", "language reload"));
		this.commands.add(new StopCommand(main, "stop", "Stoppt das System", "stop"));
		this.commands.add(new UserCommand(main, "user", "Zeigt dir einen bestimmten Nutzer an", "user <name>"));
		this.commands.add(new UserListCommand(main, "userlist", "Zeigt dir die Liste aller Nutzer", "userlist"));

		new Thread(() -> {

			Scanner scanner = new Scanner(System.in);

			loop:
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] split = line.split(" ");

				String cmd = split[0];
				String[] args = Arrays.copyOfRange(split, 1, split.length);

				for(Command command : commands) {
					if(command.getName().equalsIgnoreCase(cmd)) {
						if(!command.execute(args)) {
							main.getLogger().info("Verwendung:");
							command.getUsage().forEach(usage -> main.getLogger().info(" + " + usage));
						}

						continue loop;
					}
				}

				main.getLogger().info("Der Befehl '" + cmd + "' konnte nicht gefunden werden!");
			}

			scanner.close();

		}).start();

	}

	public List<Command> getCommands() {
		return commands;
	}

}