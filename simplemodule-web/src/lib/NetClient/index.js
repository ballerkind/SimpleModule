const log = require("../LogManager");

const net = require("net");
const packets = {};
let connection = null;

function registerAllPackets() {
	getPacketRegistry()
		.register("PacketAuth")
		.register("PacketAuthStatus")
		.register("PacketLanguage")
		.register("PacketModuleAdd")
		.register("PacketModuleDelete")
		.register("PacketModuleRedeem")
		.register("PacketModuleRemove")
		.register("PacketModules")
		.register("PacketModuleStart")
		.register("PacketModuleStop")
		.register("PacketModuleUpdate")
		.register("PacketUserChangeEmail")
		.register("PacketUserChangePassword")
		.register("PacketUserCreate")
		.register("PacketUserDelete")
		.register("PacketUsers")
		.register("PacketUserVerify")
		.next("/Minecraft/")
		.register("PacketModuleMinecraftConsole")
		.register("PacketModuleMinecraftInfo")
		.register("PacketModuleMinecraftPlayer")
		.register("PacketModuleMinecraftPluginStatus")
		.register("PacketModuleMinecraftStatistic")
		.register("PacketModuleMinecraftStatisticErase")
		.register("PacketModuleMinecraftTabComplete")
		.register("PacketModuleMinecraftTabCompleteResponse");

	function getPacketRegistry() {
		return {
			currentId: 1,
			currentPath: "/",
			register: function(file) {
				let packet = require(__dirname + "/packets" + this.currentPath + file);
				packet.id = this.currentId++;
				packets[file] = packet;

				return this;
			},
			next: function(path) {
				this.currentPath = path;
				this.currentId = Math.ceil(this.currentId / 100) * 100;

				return this;
			}
		}
	}
}

function connect(onAuth) {
	packets["PacketAuthStatus"].onAuth = onAuth;

	log.info("Starte TCP Server...");
	connection = net.connect(2201, "localhost", () => {
		log.info("Authentifiziere Client...");
		sendPacket("PacketAuth", {
			key: "372db471de744d768a0dea1088e7bad7" // TODO: config
		});
	});

	let chunk = "";
	connection.on("data", data => {

		chunk += data;
		let d_index = chunk.indexOf("\n");

		while(d_index > -1) {
			let string = chunk.substring(0, d_index);
			let json = JSON.parse(string);

			let exists = false;
			for(let packetName in packets) {
				let packet = packets[packetName];

				if(packet.id === json._id) {
					if(typeof packet.handle == "function") {
						packet.handle(json, module.exports.io);
					} else {
						log.error("Packet mit der ID " + packet.id + " hat keine HandleMethode!");
					}

					exists = true;
					break;
				}
			}
			if(!exists) log.error("Unbekanntes Packet empfangen!");

			chunk = chunk.substring(d_index + 1);
			d_index = chunk.indexOf("\n");
		}

	});

	connection.on("error", error => {
		if(error.code === "ECONNREFUSED") {
			log.error("Verbindung zum MasterServer ist fehlgeschlagen!");
		} else {
			log.error("Es ist ein Fehler aufgetreten! " + error.code);
		}
	});

	connection.on("close", () => {
		log.info("Verbindung wurde geschlossen. System wird beendet...");
		process.exit(0);
	});

	module.exports.connection = connection;
}

function sendPacket(name, json) {

	if(packets[name] !== undefined) {
		json._id = packets[name].id;
		connection.write(JSON.stringify(json) + "\n");
	} else {
		log.error("Packet mit dem Namen '" + name + "' wurde nicht gefunden!");
	}

}

module.exports = {
	connection: null,
	io: null,
	registerAllPackets: registerAllPackets,
	connect: connect,
	sendPacket: sendPacket
};