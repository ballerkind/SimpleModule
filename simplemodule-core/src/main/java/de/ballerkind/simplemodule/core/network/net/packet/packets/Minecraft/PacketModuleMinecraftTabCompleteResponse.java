package de.ballerkind.simplemodule.core.network.net.packet.packets.Minecraft;

import de.ballerkind.simplemodule.core.network.json.packet.packets.Minecraft.JsonPacketModuleMinecraftTabCompleteResponse;
import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.Minecraft.ModuleMinecraft;
import de.ballerkind.simplemodule.core.network.net.packet.IModulePacketHandler;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketModuleMinecraftTabCompleteResponse extends Packet implements IModulePacketHandler<ModuleMinecraft> {

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

	@Override
	public void handle(Main main, ModuleMinecraft module) {

		main.getNetJsonServer().getClient().writeAndFlush(new JsonPacketModuleMinecraftTabCompleteResponse(module.getId(), socketId, response));

	}

}