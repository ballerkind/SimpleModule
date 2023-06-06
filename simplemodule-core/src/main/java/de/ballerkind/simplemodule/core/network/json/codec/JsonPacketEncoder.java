package de.ballerkind.simplemodule.core.network.json.codec;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.ballerkind.simplemodule.core.network.json.packet.JsonPacket;
import de.ballerkind.simplemodule.core.Main;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class JsonPacketEncoder extends MessageToByteEncoder<JsonPacket> {

	private Main main;
	private Gson gson;
	private JsonParser jsonParser;

	public JsonPacketEncoder(Main main) {
		this.main = main;
		this.gson = new Gson();
		this.jsonParser = new JsonParser();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, JsonPacket packet, ByteBuf byteBuf) {

		int id = main.getNetJsonServer().getPacketRegistry().getId(packet.getClass());
		if(id != -1) {
			String json = gson.toJson(packet);
			JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
			jsonObject.addProperty("_id", id);
			byteBuf.writeBytes((jsonObject.toString() + "\n").getBytes());
		} else {
			main.getLogger().severe("Packet " + packet.getClass().getSimpleName() + " has no ID!");
		}

	}

}