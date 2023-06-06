const moduleManager = require("../../ModuleManager");

module.exports = function(app, io) {

	app.get("/api/", (req, res) => {
		res.render("api", {
			api: app.api,
			moduleManager: moduleManager
		});
	});

	app.get("/api/:moduleType", (req, res) => {
		if(app.api.hasOwnProperty(req.params.moduleType)) {
			res.render("api", {
				selectedModule: req.params.moduleType,
				api: app.api,
				moduleManager: moduleManager
			});
		} else {
			res.redirect("/api/");
		}
	});

}