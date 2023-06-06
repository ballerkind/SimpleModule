const moduleManager = require.main.require("./lib/ModuleManager");

module.exports = {
	handle: function(data, io) {
		let module = moduleManager.getModule(data.moduleType, data.moduleId);

		module.enabled = false;
		module.onStop();

		io.toModule(module).emit("module_stop");
	}
};