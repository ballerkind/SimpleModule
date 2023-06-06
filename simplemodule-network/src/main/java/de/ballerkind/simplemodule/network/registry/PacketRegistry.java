package de.ballerkind.simplemodule.network.registry;

import de.ballerkind.simplemodule.network.packet.Packet;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

	private HashMap<Integer, Class<? extends Packet>> packets;
	private int currentId;

	public PacketRegistry() {
		this.packets = new HashMap<>();
		this.currentId = 1;
	}

	public PacketRegistry register(Class<? extends Packet> packetClass) {
		packets.put(currentId++, packetClass);
		return this;
	}

	public PacketRegistry next(int id) {
		currentId = id;
		return this;
	}

	public int getId(Class<? extends Packet> packetClass) {
		for(Map.Entry<Integer, Class<? extends Packet>> entry : packets.entrySet()) {
			if(entry.getValue().equals(packetClass)) {
				return entry.getKey();
			}
		}

		return -1;
	}

	public Class<? extends Packet> getPacket(int id) {
		return packets.get(id);
	}

}