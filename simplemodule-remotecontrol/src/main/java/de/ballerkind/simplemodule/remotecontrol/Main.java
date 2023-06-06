package de.ballerkind.simplemodule.remotecontrol;

import de.ballerkind.simplemodule.network.NetClient;
import de.ballerkind.remotecontrol.network.packet.packets.PacketModuleRemoteControlConsole;

public class Main {

	public static Main instance;
	private NetClient netClient;

	public Main(String moduleId, String moduleSecret) {

		this.netClient = new NetClient("RemoteControl", moduleId, moduleSecret);
		this.netClient.getPacketRegistry()
			.next(200)
			.register(PacketModuleRemoteControlConsole.class);
		this.netClient.setOnAuth(success -> {
			if(success) {
				System.out.println("pogU");
			} else {
				System.out.println("MAAAAAN WIESO?!?!?!?!111elf");
			}
		});
		this.netClient.connect();




		this.netClient = new NetClient(null, "", "");
		this.netClient.connect();

	}


	public static void main(String[] args) {
		if(args.length < 2) return;

		instance = new Main(args[0], args[1]);
	}

}