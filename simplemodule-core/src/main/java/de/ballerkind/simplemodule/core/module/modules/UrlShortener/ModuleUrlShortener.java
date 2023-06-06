package de.ballerkind.simplemodule.core.module.modules.UrlShortener;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;

import java.util.HashMap;
import java.util.List;

public class ModuleUrlShortener extends Module {

	private HashMap<String, String> urls;

	public ModuleUrlShortener(Main main, String userId, String type, String id, String name, String secret, List<String> shared) {
		super(main, userId, type, id, name, secret, shared);
	}

	public HashMap<String, String> getUrls() {
		return urls;
	}

}