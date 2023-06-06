const moduleManager = require.main.require("./lib/ModuleManager");

module.exports = {
	handle: function(data, io) {
		let module = moduleManager.getModule("Minecraft", data.moduleId);

		module.version = data.version;
		module.ip = data.ip;
		module.port = data.port;
		module.maxPlayers = data.maxPlayers;

		io.toModule(module).emit("minecraft_info", {
			version: data.version,
			ip: data.ip,
			port: data.port,
			maxPlayers: data.maxPlayers
		});
	}
}