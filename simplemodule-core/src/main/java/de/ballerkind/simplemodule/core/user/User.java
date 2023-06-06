package de.ballerkind.simplemodule.core.user;

import java.util.LinkedHashMap;
import java.util.List;

public class User {

	private String id;
	private String email;
	private String name;
	private String password;
	private boolean verified;
	private LinkedHashMap<String, List<String>> modules;
	private LinkedHashMap<String, List<String>> sharedModules;

	public User(String id, String email, String name, String password, boolean verified) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.verified = verified;
		this.modules = new LinkedHashMap<>();
		this.sharedModules = new LinkedHashMap<>();
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public LinkedHashMap<String, List<String>> getModules() {
		return modules;
	}

	public LinkedHashMap<String, List<String>> getSharedModules() {
		return sharedModules;
	}

}