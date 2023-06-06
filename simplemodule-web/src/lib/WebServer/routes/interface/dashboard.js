module.exports = function(app, io) {

	app.get("/Dashboard/", app.isLogged, (req, res) => {
	    res.render("interface/dashboard");
	});
	
};