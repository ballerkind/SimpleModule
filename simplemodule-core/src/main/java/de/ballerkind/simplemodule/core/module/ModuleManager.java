package de.ballerkind.simplemodule.core.module;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ModuleManager {

	private LinkedHashMap<String, ModuleType> moduleTypes;

	public ModuleManager() {
		this.moduleTypes = new LinkedHashMap<>();
	}

	public LinkedHashMap<String, ModuleType> getModuleTypes() {
		return moduleTypes;
	}

	public Module getModuleById(String moduleType, String moduleId) {

		if(moduleTypes.containsKey(moduleType)) {
			HashMap<String, Module> modules = moduleTypes.get(moduleType).getModules();
			if(modules.containsKey(moduleId)) {
				return modules.get(moduleId);
			}
		}

		return this.getModuleTypes().get(moduleType).getModules().get(moduleId);

	}

}