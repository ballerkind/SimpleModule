module.exports = function(app, io) {

	app.get("/Support/", app.isLogged, (req, res) => {
		res.render("interface/support");
	});

};