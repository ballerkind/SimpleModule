package de.ballerkind.simplemodule.core.language;

import de.ballerkind.simplemodule.core.Main;
import org.bson.Document;

import java.util.HashMap;

public class LanguageManager {

	private Main main;
	private HashMap<String, String> languages;
	private HashMap<String, HashMap<String, String>> messages;

	public LanguageManager(Main main) {
		this.main = main;
		this.languages = new HashMap<>();
		this.messages = new HashMap<>();
	}

	public HashMap<String, String> getLanguages() {
		return languages;
	}

	public HashMap<String, HashMap<String, String>> getMessages() {
		return messages;
	}

	public void reload() {

		this.languages.clear();
		this.messages.clear();

		Document document = main.getDatabaseManager().getDatabase().getCollection("Language").find().first();
		document.get("languages", Document.class).forEach((key, value) -> languages.put(key, value.toString()));
		document.get("messages", Document.class).forEach((key, value) -> {
			messages.put(key, new HashMap<>());
			((Document) value).forEach((k, v) -> messages.get(key).put(k, v.toString()));
		});

	}

}