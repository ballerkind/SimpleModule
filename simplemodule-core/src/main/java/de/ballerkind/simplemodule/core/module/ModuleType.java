package de.ballerkind.simplemodule.core.module;

import java.util.HashMap;

public class ModuleType {

	private String name;
	private transient Class<? extends Module> moduleClass;
	private String icon;
	private HashMap<String, Module> modules;

	public ModuleType(String name, Class<? extends Module> moduleClass, String icon) {
		this.name = name;
		this.moduleClass = moduleClass;
		this.icon = icon;
		this.modules = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public Class<? extends Module> getModuleClass() {
		return moduleClass;
	}

	public String getIcon() {
		return icon;
	}

	public HashMap<String, Module> getModules() {
		return modules;
	}

}