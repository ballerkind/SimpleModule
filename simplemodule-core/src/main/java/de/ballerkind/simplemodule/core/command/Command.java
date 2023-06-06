package de.ballerkind.simplemodule.core.command;

import de.ballerkind.simplemodule.core.Main;

import java.util.Arrays;
import java.util.List;

public abstract class Command {

	public Main main;
	private String name;
	private String description;
	private List<String> usage;

	public Command(Main main, String name, String description, String... usage) {
		this.main = main;
		this.name = name;
		this.description = description;
		this.usage = Arrays.asList(usage);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getUsage() {
		return usage;
	}

	public abstract boolean execute(String[] args);

}