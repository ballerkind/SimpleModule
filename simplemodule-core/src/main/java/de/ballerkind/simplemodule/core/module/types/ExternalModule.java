package de.ballerkind.simplemodule.core.module.types;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;
import io.netty.channel.Channel;

import java.util.List;

public abstract class ExternalModule extends Module {

	private transient Channel client;

	public ExternalModule(Main main, String userId, String type, String id, String name, String secret, List<String> shared) {
		super(main, userId, type, id, name, secret, shared);
	}

	public final Channel getClient() {
		return client;
	}

	public final void setClient(Channel client) {
		this.client = client;
	}

	public void onConnect() {}
	public void onDisconnect() {}

}