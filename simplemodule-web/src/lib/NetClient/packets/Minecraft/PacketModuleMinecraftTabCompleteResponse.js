var moduleManager = require.main.require("./lib/ModuleManager");

module.exports = {
	handle: function(data, io) {
		let module = moduleManager.getModule("Minecraft", data.moduleId);

		io.to(data.socketId).emit("minecraft_tabcomplete", data.response);
	}
};