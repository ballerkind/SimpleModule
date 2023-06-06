module.exports = function(app, io) {

	app.get("/Logout/", app.isLogged, (req, res) => {
		req.session.destroy();
		res.redirect("/");
	});

}