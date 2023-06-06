const languageManager = require.main.require("./lib/LanguageManager");

module.exports = {
	handle: function(data) {
		languageManager.languages = data.languages;
		languageManager.messages = data.messages;
	}
}