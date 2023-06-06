const netClient = require.main.require("./lib/NetClient");

module.exports = function(app, io) {

	app.get("/Minecraft/:id", app.isLogged, (req, res) => {
		let module = res.locals.user.getModule("Minecraft", req.params.id);

		if(module != null) {
			res.render("interface/modules/Minecraft/index", {
				module: module
			});
		} else {
			res.redirect("/Dashboard/");
		}
	});

	io.on("connection", socket => {
		socket.on("minecraft_statistic_erase", () => {
			if(socket.module && socket.module.type === "Minecraft") {
				let time = new Date();
				time.setMinutes(time.getMinutes() - (time.getMinutes() % 5));
				time.setSeconds(0);
				time.setMilliseconds(0);

				socket.module.statistic.start = time.getTime();
				socket.module.statistic.data = [null];

				netClient.sendPacket("PacketModuleMinecraftStatisticErase", {
					moduleId: socket.module.id
				});
			}
		})

		socket.on("minecraft_console", command => {
			if(typeof command == "string") {
				if(socket.module && socket.module.type === "Minecraft") {
					netClient.sendPacket("PacketModuleMinecraftConsole", {
						moduleId: socket.module.id,
						command: command
					});
				}
			}
		});

		socket.on("minecraft_tabcomplete", message => {
			if(typeof message == "string") {
				if(socket.module && socket.module.type === "Minecraft") {
					netClient.sendPacket("PacketModuleMinecraftTabComplete", {
						moduleId: socket.module.id,
						socketId: socket.id,
						message: message
					});
				}
			}
		});

		socket.on("minecraft_plugin", pluginName => {
			if(typeof pluginName == "string") {
				if(socket.module && socket.module.type === "Minecraft") {
					let plugin = null;
					for(let p of socket.module.plugins) {
						if(p.name === pluginName) {
							plugin = p;
							break;
						}
					}

					if(plugin != null) {
						netClient.sendPacket("PacketModuleMinecraftPluginStatus", {
							moduleId: socket.module.id,
							name: plugin.name,
							enabled: !plugin.enabled
						});
					} else {
						console.log("Plugin abuse detected: " + socket.module.userId + " - " + socket.module.id + " - " + pluginName);
					}
				}
			}
		});
	});

	app.registerAPI("Minecraft", "console", (module, params, res) => {
		if(typeof params.command == "string") {
			netClient.sendPacket("PacketModuleMinecraftConsole", {
				moduleId: module.id,
				command: params.command
			});

			res.send({
				success: "Command was executed."
			});
		} else {
			res.send({
				error: "No command found!"
			});
		}
	}, {
		info: "With this method you can send a command to the minecraft server.",
		parameters: [
			"command = yourCommand"
		],
		result: {
			success: "Command was executed."
		}
	});

	app.registerAPI("Minecraft", "players", (module, res) => {
		res.send({
			players: module.players,
			maxPlayers: module.maxPlayers
		});
	}, {
		info: "With this method you can access which players are online and how many slots the minecraft server has.",
		result: {
			players: [
				"ballerkind", "Notch", "..."
			],
			maxPlayers: "100"
		}
	});

}