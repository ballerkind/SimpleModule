const moduleManager = require.main.require("./lib/ModuleManager");

module.exports = {
	handle: function(data, io) {
		let modules = moduleManager.moduleTypes["Minecraft"].modules;

		for(let id in modules) {
			let module = modules[id];
			module.statistic.data.push(module.enabled ? module.players.length : null);
		}
	}
}