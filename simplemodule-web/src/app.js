const log = require("./lib/LogManager");
const netClient = require("./lib/NetClient");

netClient.registerAllPackets();
netClient.connect(success => {
	if(success) {
		log.info("Erfolgreich zum MasterServer verbunden.");
		log.info("Starte WebServer...");

		require("./lib/WebServer");
	} else {
		log.error("Das Passwort ist falsch!");
	}
});

process.on("SIGINT", () => {
	netClient.connection.end();
});