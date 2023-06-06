const userManager = require.main.require("./lib/UserManager");
const netClient = require.main.require("./lib/NetClient");
const mailManager = require.main.require("./lib/MailManager");
const utils = require.main.require("./lib/Utils");

const bcrypt = require("bcryptjs");

module.exports = function(app, io) {

	app.route("/ResetPassword/")
		.get((req, res) => {
			res.render("login/resetpassword");
		})
		.post((req, res) => {
			if(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(req.body.email)) {
				for(let user of userManager.users) {
					if(user.email == req.body.email) {
						let token;

						loop:
						while(true) {
							token = utils.randomString(16);

							for(let user of userManager.users) {
								if(user.resetPassword != null && user.resetPassword.token == token && user.resetPassword.time >= new Date().getTime()) {
									continue loop;
								}
							}

							break;
						}

						user.resetPassword = {
							token: token,
							time: new Date().getTime() + 10 * 60 * 60 * 1000
						}

						mailManager.sendResetPasswordMail(user);

						break;
					}
				}

				res.send("success");
			} else {
				res.send("error");
			}
		});

	app.route("/ResetPassword/:token")
		.get((req, res) => {
			for(let user of userManager.users) {
				if(user.resetPassword != null && user.resetPassword.token == req.params.token && user.resetPassword.time >= new Date().getTime()) {
					res.render("login/resetpassword_token", {
						name: user.name
					});

					return;
				}
			}

			res.redirect("/ResetPassword/");
		})
		.post((req, res) => {
			if(typeof req.body.password != "string") {
				// MODIFIZIERTER POST REQUEST
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

			for(let user of userManager.users) {
				if(user.resetPassword != null && user.resetPassword.token == req.params.token && user.resetPassword.time >= new Date().getTime()) {
					user.resetPassword = null;

					bcrypt.hash(req.body.password, 10, (err, hash) => {
						user.password = hash;

						netClient.sendPacket("PacketUserChangePassword", {
							userId: user.id,
							password: hash
						});

						res.send("success");
					});

					break;
				}
			}
		});

}