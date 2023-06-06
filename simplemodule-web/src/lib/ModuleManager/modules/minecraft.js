const Module = require("../module");

class Minecraft extends Module {

	onCreate() {
		this.version = "";
		this.ip = "";
		this.port = 0;
		this.statistic = [];
		this.console = [];
		this.plugins = [];
		this.players = [];
		this.maxPlayers = 0;
	}

	onStart() {

	}

	onStop() {
		this.version = "";
		this.ip = "";
		this.port = 0;
		this.console = [];
		this.plugins = [];
		this.players = [];
		this.maxPlayers = 0;
	}

}

module.exports = Minecraft;