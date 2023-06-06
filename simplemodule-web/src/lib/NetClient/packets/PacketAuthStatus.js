const log = require.main.require("./lib/LogManager");
const a = require.main.require("./lib/NetClient");


module.exports = {
	onAuth: () => {},
	handle: function(data) {
		let success = data.success;
		this.onAuth(success);
	}
}