package de.ballerkind.simplemodule.core.network.json.packet.packets;

import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;

import java.util.HashMap;

public class JsonPacketLanguage extends JsonPacket {

	private HashMap<String, String> languages;
	private HashMap<String, HashMap<String, String>> messages;

	public JsonPacketLanguage(HashMap<String, String> languages, HashMap<String, HashMap<String, String>> messages) {
		this.languages = languages;
		this.messages = messages;
	}

}