const moduleManager = require.main.require("./lib/ModuleManager");
const userManager = require.main.require("./lib/UserManager");
const netClient = require.main.require("./lib/NetClient");

const bcrypt = require("bcryptjs");

module.exports = function(app, io) {

	app.get("/Settings/", app.isLogged, (req, res) => {
		res.render("interface/settings");
	});

	io.on("connection", socket => {
		socket.on("settings_changePassword", (data, callback) => {
			if(data != null && typeof data.oldPassword == "string" && typeof data.newPassword == "string" && typeof data.newPassword_repeat == "string" && typeof callback == "function") {
				if(socket.request.session.userId) {

					if(data.oldPassword.length === 0 || data.newPassword.length === 0 || data.newPassword_repeat === 0) {
						callback("empty");
						return;
					}

					if(data.newPassword.length < 8) {
						callback("password_length");
						return;
					}

					if(!/^[A-Za-z0-9]+$/.test(data.newPassword)) {
						callback("password_invalid");
						return;
					}

					if(data.newPassword !== data.newPassword_repeat) {
						callback("password_equal");
						return;
					}

					let user = userManager.getUser(socket.request.session.userId);
					bcrypt.compare(data.oldPassword, user.password, (err, valid) => {
						if(valid) {
							bcrypt.hash(data.newPassword, 10, (err, hash) => {
								user.password = hash;

								netClient.sendPacket("PacketUserChangePassword", {
									userId: user.id,
									password: hash
								});

								callback("success");
							});
						} else {
							callback("oldPassword_wrong");
						}
					});

				}
			}
		});

		socket.on("settings_changeEmail", (email, callback) => {
			if(typeof email == "string" && typeof callback == "function") {
				if(socket.request.session.userId) {
					if(!/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(email)) {
						callback("email_invalid");
						return;
					}

					let user = userManager.getUser(socket.request.session.userId);
					user.email = email;

					netClient.sendPacket("PacketUserChangeEmail", {
						userId: user.id,
						email: email
					});

					callback("success");
				}
			}
		});

		socket.on("settings_deleteAccount", (password, callback) => {
			if(typeof password == "string" && typeof callback == "function") {
				if(socket.request.session.userId) {

					let user = userManager.getUser(socket.request.session.userId);
					console.log(password);
					bcrypt.compare(password, user.password, (err, valid) => {
						if(valid) {
							for(let type in user.modules) {
								user.modules[type].forEach(moduleId => {
									let module = moduleManager.getModule(type, moduleId);

									module.shared.forEach(sharedWith => {
										let modules = userManager.getUser(sharedWithId).sharedModules[type];
										modules.splice(modules.indexOf(module.id), 1);
									});
								});
							}
							userManager.users.splice(userManager.users.indexOf(user), 1);

							netClient.sendPacket("PacketUserDelete", {
								userId: user.id
							});

							socket.request.session.destroy();
							callback("success");
						} else {
							callback("password_wrong");
						}
					});

				}
			}
		});
	});

};