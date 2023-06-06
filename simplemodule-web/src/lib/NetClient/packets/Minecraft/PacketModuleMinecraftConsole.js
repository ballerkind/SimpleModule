const moduleManager = require.main.require("./lib/ModuleManager");

module.exports = {
	handle: function(data, io) {
		let module = moduleManager.getModule("Minecraft", data.moduleId);

		module.console.push(data.command);

		io.toModule(module).emit("minecraft_console", data.command);
	}
};