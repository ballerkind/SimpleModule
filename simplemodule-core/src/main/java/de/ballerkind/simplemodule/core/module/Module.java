package de.ballerkind.simplemodule.core.module;

import de.ballerkind.simplemodule.core.Main;
import org.bson.Document;

import java.util.List;

public abstract class Module {

	protected transient Main main;
	private String userId;
	private String type;
	private String id;
	private String name;
	private String secret;
	private List<String> shared;
	private boolean enabled;

	public Module(Main main, String userId, String type, String id, String name, String secret, List<String> shared) {
		this.main = main;
		this.userId = userId;
		this.type = type;
		this.id = id;
		this.name = name;
		this.secret = secret;
		this.shared = shared;
		this.enabled = false;
	}

	public void onCreate(Document toStore) {}
	public void onLoad(Document fromDatabase) {}

	public String getUserId() {
		return userId;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public List<String> getShared() {
		return shared;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
