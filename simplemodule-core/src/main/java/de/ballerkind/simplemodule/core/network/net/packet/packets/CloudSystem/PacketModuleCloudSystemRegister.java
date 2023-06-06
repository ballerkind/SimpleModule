package de.ballerkind.simplemodule.core.network.net.packet.packets.CloudSystem;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.modules.CloudSystem.ModuleCloudSystem;
import de.ballerkind.simplemodule.core.network.net.packet.IModulePacketHandler;
import de.ballerkind.simplemodule.core.network.net.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketModuleCloudSystemRegister extends Packet implements IModulePacketHandler<ModuleCloudSystem> {

	@Override
	public void write(ByteBufOutputStream buf) throws IOException {

	}

	@Override
	public void read(ByteBufInputStream buf) throws IOException {

	}

	@Override
	public void handle(Main main, ModuleCloudSystem module) {


	}

}