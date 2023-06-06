const moduleManager = require.main.require("./lib/ModuleManager");

module.exports = function(app, io) {

	app.get("/UrlShortener/:id", app.isLogged, (req, res) => {

		let module = res.locals.user.getModule("UrlShortener", req.params.id);

		if(module != null) {
			res.render("interface/modules/UrlShortener/index", {
				module: module
			});
		} else {
			res.redirect("/Dashboard/");
		}

	});

	app.get("/UrlShortener/target/:url", (req, res) => {

		for(let module of moduleManager.moduleTypes["UrlShortener"].modules) {
			if(module.urls.hasOwnProperty(req.params.url)) {
				res.redirect(module.urls[req.params.url]);
				return;
			}
		}

		res.redirect("/");

	});

}