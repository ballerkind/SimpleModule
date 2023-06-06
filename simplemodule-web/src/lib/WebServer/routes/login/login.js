const userManager = require.main.require("./lib/UserManager");
const bcrypt = require("bcryptjs");

module.exports = function(app, io) {

	app.route("/Login/")
		.get((req, res) => {
			if(!req.session.userId) {
				res.render("login/login");
			} else {
				res.redirect("/Dashboard/");
			}
		})
		.post((req, res) => {
			if(typeof req.body.name == "string") {
				for(let user of userManager.users) {
					if(user.name == req.body.name) {
						bcrypt.compare(req.body.password, user.password, (err, valid) => {
							if(valid) {
								if(user.verified) {
									req.session.userId = user.id;

									if(req.body.remember == "true") {
										req.session.cookie.maxAge = 365 * 24 * 60 * 60 * 1000;
									}

									res.send("success");
								} else {
									res.send("not_verified");
								}
							} else {
								res.send("error");
							}
						});

						return;
					}
				}

				res.send("error");
			}
		});

}