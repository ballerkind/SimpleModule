const moduleManager = require.main.require("./lib/ModuleManager");
const utils = require.main.require("./lib/Utils");

module.exports = {
	handle: function(data, io) {
		let module = moduleManager.getModule("Minecraft", data.moduleId);

		if(data.joined) {
			let index = utils.binarySearch(module.players, data.name,
				(first, second) => first.toLowerCase().localeCompare(second.toLowerCase()));
			module.players.splice(index < 0 ? -(index + 1) : index, 0, data.name);
		} else {
			let index = module.players.indexOf(data.name);
			if(index !== -1) module.players.splice(index, 1);
		}

		io.toModule(module).emit("minecraft_player", {
			name: data.name,
			joined: data.joined
		});
	}
};