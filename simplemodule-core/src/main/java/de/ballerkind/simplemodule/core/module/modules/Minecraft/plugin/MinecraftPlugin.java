package de.ballerkind.simplemodule.core.module.modules.Minecraft.plugin;

public class MinecraftPlugin {

	private String name;
	private boolean enabled;

	public MinecraftPlugin(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}