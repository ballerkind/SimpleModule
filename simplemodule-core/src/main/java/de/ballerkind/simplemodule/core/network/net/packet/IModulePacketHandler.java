package de.ballerkind.simplemodule.core.network.net.packet;

import de.ballerkind.simplemodule.core.Main;
import de.ballerkind.simplemodule.core.module.Module;

public interface IModulePacketHandler<T extends Module> {

	void handle(Main main, T module);

}