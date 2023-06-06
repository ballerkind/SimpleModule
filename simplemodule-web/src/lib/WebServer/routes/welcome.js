module.exports = function(app, io) {

	app.get("/", (req, res) => {
	    res.render("welcome");
	});

}