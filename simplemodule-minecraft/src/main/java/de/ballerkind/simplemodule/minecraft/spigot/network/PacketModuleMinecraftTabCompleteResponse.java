package de.ballerkind.simplemodule.minecraft.spigot.network;

import de.ballerkind.simplemodule.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketModuleMinecraftTabCompleteResponse extends Packet {

	private String socketId;
	private List<String> response;

	public PacketModuleMinecraftTabCompleteResponse() {}
	public PacketModuleMinecraftTabCompleteResponse(String socketId, List<String> response) {
		this.socketId = socketId;
		this.response = response;
	}

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {
		buf.writeUTF(socketId);
		buf.writeInt(response.size());
		for(String s : response) {
			buf.writeUTF(s);
		}
	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {
		this.socketId = buf.readUTF();
		this.response = new ArrayList<>();

		int size = buf.readInt();
		for(int i = 0; i < size; i++) {
			response.add(buf.readUTF());
		}
	}

}