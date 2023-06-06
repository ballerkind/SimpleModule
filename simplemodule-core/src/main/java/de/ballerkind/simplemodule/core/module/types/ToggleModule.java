package de.ballerkind.simplemodule.core.module.types;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;

import java.util.List;

public abstract class ToggleModule extends Module {

	public ToggleModule(Main main, String userId, String type, String id, String name, String secret, List<String> shared) {
		super(main, userId, type, id, name, secret, shared);
	}

	public void onEnable() {}
	public void onDisable() {}

}