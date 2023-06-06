package de.ballerkind.simplemodule.core.network.json.codec;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.Main;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonPacketDecoder extends ByteToMessageDecoder {

	private Main main;
	private Gson gson;

	public JsonPacketDecoder(Main main) {
		this.main = main;
		this.gson = new Gson();
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) {

		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		String json = new String(bytes, StandardCharsets.UTF_8);
		JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

		Class<? extends JsonPacket> clazz = main.getNetJsonServer().getPacketRegistry().getPacket(obj.get("_id").getAsInt());
		if(clazz != null) {
			JsonPacket packet = gson.fromJson(obj, clazz);
			list.add(packet);
		} else {
			main.getLogger().severe("Packet with ID '" + obj.get("_id").getAsInt() + "' not found!");
		}


	}

}