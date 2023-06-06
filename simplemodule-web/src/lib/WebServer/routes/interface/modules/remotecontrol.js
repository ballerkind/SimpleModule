const moduleManager = require.main.require("./lib/ModuleManager");

module.exports = function(app, io) {

	app.get("/RemoteControl/:id", app.isLogged, (req, res) => {

		let module = res.locals.user.getModule("RemoteControl", req.params.id);

		if(module != null) {
			res.render("interface/modules/RemoteControl/index", {
				module: module
			});
		} else {
			res.redirect("/Dashboard/");
		}

	});

}