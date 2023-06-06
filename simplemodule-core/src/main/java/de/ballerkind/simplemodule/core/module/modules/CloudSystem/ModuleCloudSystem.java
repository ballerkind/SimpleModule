package de.ballerkind.simplemodule.core.module.modules.CloudSystem;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.types.ExternalModule;

import java.util.List;

public class ModuleCloudSystem extends ExternalModule {

	public ModuleCloudSystem(Main main, String userId, String type, String id, String name, String secret, List<String> shared) {
		super(main, userId, type, id, name, secret, shared);
	}

}