function getLanguage(code) {
	return {
		getMessage: function(name) {
			if(module.exports.messages.hasOwnProperty(name)) {
				if(module.exports.messages[name].hasOwnProperty(code)) {
					return module.exports.messages[name][code];
				} else {
					return "Message '" + name + "' has no language '" + code + "'!";
				}
			} else {
				return "Message '" + name + "' doesn't exist!";
			}
		},
		getCurrentLanguage: function() {
			return code;
		},
		getLanguages: function() {
			return module.exports.languages;
		}
	}
}

module.exports = {
	languages: {},
	messages: {},
	getLanguage: getLanguage
};