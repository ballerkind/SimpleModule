package de.ballerkind.simplemodule.core.network.net.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public abstract class Packet {

	public abstract void write(ByteBufOutputStream buf) throws IOException;

	public abstract void read(ByteBufInputStream buf) throws IOException;

}