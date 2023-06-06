function getModule(moduleType, moduleId) {

	if(module.exports.moduleTypes.hasOwnProperty(moduleType)) {
		let modules = module.exports.moduleTypes[moduleType].modules;
		if(modules.hasOwnProperty(moduleId)) {
			return modules[moduleId];
		}
	}

	return null;

}

module.exports = {
	moduleTypes: {},
	getModule: getModule
};