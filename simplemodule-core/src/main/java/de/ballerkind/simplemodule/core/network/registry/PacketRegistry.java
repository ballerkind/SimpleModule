package de.ballerkind.simplemodule.core.network.registry;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry<T> {

	private HashMap<Integer, Class<? extends T>> packets;
	private int currentId;

	public PacketRegistry() {
		this.packets = new HashMap<>();
		this.currentId = 1;
	}

	public PacketRegistry<T> register(Class<? extends T> packetClass) {
		packets.put(currentId++, packetClass);
		return this;
	}

	public PacketRegistry<T> next() {
		currentId = ((currentId + 100) / 100) * 100;
		return this;
	}

	public int getId(Class<? extends T> packetClass) {
		for(Map.Entry<Integer, Class<? extends T>> entry : packets.entrySet()) {
			if(entry.getValue().equals(packetClass)) {
				return entry.getKey();
			}
		}

		return -1;
	}

	public Class<? extends T> getPacket(int id) {
		return packets.get(id);
	}

}