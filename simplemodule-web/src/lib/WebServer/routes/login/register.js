const userManager = require.main.require("./lib/UserManager");
const netClient = require.main.require("./lib/NetClient");
const mailManager = require.main.require("./lib/MailManager");
const utils = require.main.require("./lib/Utils");

const bcrypt = require("bcryptjs");

module.exports = function(app, io) {

	app.route("/Register/")
		.get((req, res) => {

			if(!req.session.userId) {
				res.render("login/register");
			} else {
				res.redirect("/Dashboard/");
			}

		})
		.post((req, res) => {

			if(typeof req.body.name != "string" || typeof req.body.password != "string" || typeof req.body.password_repeat != "string" || typeof req.body.email != "string") {
				// BEIM POST FEHLT EIN PARAMETER -> MODIFIZIERTES HTML DOKUMENT
				return;
			}

			if(req.body.name.length == 0 || req.body.password.length == 0 || req.body.password_repeat.length == 0 || req.body.email.length == 0) {
				res.send("empty");
				return;
			}

			if(req.body.name.length > 16) {
				// GEHT NUR MIT MODIFIZIERTEM HTML
				return;
			}

			if(!/^[A-Za-z0-9_]+$/.test(req.body.name)) {
				res.send("name_invalid");
				return;
			}

			if(req.body.password.length < 8) {
				res.send("password_length");
				return;
			}

			if(!/^[A-Za-z0-9]+$/.test(req.body.password)) {
				res.send("password_invalid");
				return;
			}

			if(req.body.password != req.body.password_repeat) {
				res.send("password_equal");
				return;
			}

			if(!/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(req.body.email)) {
				res.send("email_invalid");
				return;
			}

			for(let user of userManager.users) {
				if(user.name.toLowerCase() == req.body.name.toLowerCase()) {
					res.send("name_exists");
					return;
				}

				if(user.email.toLowerCase() == req.body.email.toLowerCase()) {
					res.send("email_exists");
					return;
				}
			}

			let id = "";
			while(userManager.getUser(id = utils.randomString(16)) != null);

			bcrypt.hash(req.body.password, 10, (err, hash) => {
				let user = {
					id: id,
					email: req.body.email,
					name: req.body.name,
					password: hash,
					verified: false,
					modules: {},
					sharedModules: {}
				};

				userManager.addUser(user);

				netClient.sendPacket("PacketUserCreate", {
					user: user
				});

				mailManager.sendVerifyMail(user);
			});

			res.send("success");

		});

}