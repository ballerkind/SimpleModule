const userManager = require.main.require("./lib/UserManager");

module.exports = {
	handle: function(data) {
		data.users.forEach(user => {
			userManager.addUser(user);
		});
	}
};