const moduleManager = require("../ModuleManager");
const users = [];

function addUser(user) {

	user.getModule = function(moduleType, moduleId) {
		let module = moduleManager.getModule(moduleType, moduleId);

		if(module != null) {
			if(module.userId === user.id || module.shared.includes(user.id)) {
				return module;
			}
		}

		return null;
	};

	users.push(user);

}

function getUser(id) {

	for(let user of users) {
		if(user.id === id) {
			return user;
		}
	}

	return null;

}

module.exports = {
	users: users,
	addUser: addUser,
	getUser: getUser
};