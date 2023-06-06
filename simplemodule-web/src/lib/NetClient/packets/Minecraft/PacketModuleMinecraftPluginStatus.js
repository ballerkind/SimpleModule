const moduleManager = require.main.require("./lib/ModuleManager");
const utils = require.main.require("./lib/Utils");

module.exports = {
	handle: function(data, io) {
		let module = moduleManager.getModule("Minecraft", data.moduleId);

		let exists = false;
		for(let plugin of module.plugins) {
			if(plugin.name === data.name) {
				plugin.enabled = data.enabled;
				exists = true;

				break;
			}
		}

		if(!exists) {
			let index = utils.binarySearch(module.plugins, data.name,
				(first, second) => first.name.toLowerCase().localeCompare(second.toLowerCase()));

			module.plugins.splice(index < 0 ? -(index + 1) : index, 0, {
				name: data.name,
				enabled: data.enabled
			});
		}

		io.toModule(module).emit("minecraft_plugin", {
			name: data.name,
			enabled: data.enabled
		})
	}
};