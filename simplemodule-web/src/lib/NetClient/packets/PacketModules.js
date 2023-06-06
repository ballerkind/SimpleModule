const moduleManager = require.main.require("./lib/ModuleManager");

module.exports = {
	handle: function(data) {
		let moduleTypes = data.moduleTypes;

		for(let type in moduleTypes) {
			let moduleType = moduleTypes[type];
			try {
				moduleType.moduleClass = require.main.require("./lib/ModuleManager/modules/" + type);
			} catch(e) {
				console.log("alarm.. " + type + " hat keine class");
				continue;
			}

			for(let id in moduleType.modules) {
				let module = new moduleType.moduleClass();

				let fromServer = moduleType.modules[id];
				for(let key in fromServer) {
					module[key] = fromServer[key];
				}

				moduleType.modules[id] = module;
			}
		}

		console.log(moduleTypes["Minecraft"].modules);
		moduleManager.moduleTypes = moduleTypes;
	}
};