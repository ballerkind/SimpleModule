package de.ballerkind.simplemodule.core.user;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

	private List<User> users;

	public UserManager() {
		this.users = new ArrayList<>();
	}

	public List<User> getUsers() {
		return users;
	}

	public User getUser(String userId) {

		for(User user : users) {
			if(user.getId().equals(userId)) {
				return user;
			}
		}

		return null;

	}

}