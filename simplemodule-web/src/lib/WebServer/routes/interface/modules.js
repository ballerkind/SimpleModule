const moduleManager = require.main.require("./lib/ModuleManager");
const userManager = require.main.require("./lib/UserManager");
const netClient = require.main.require("./lib/NetClient");
const utils = require.main.require("./lib/Utils");

module.exports = function(app, io) {

	app.get("/Modules/", app.isLogged, (req, res) => {
		res.render("interface/modules");
	});

	io.on("connection", socket => {
		socket.on("module_add", (data, callback) => {
			if(data != null && typeof data.moduleType == "string" && typeof data.name == "string" && typeof callback == "function") {
				if(socket.request.session.userId) {
					if(/^[A-Za-z0-9_-]+$/.test(data.name)) {
						let moduleType = moduleManager.moduleTypes[data.moduleType];

						if(moduleType != null) {
							let user = userManager.getUser(socket.request.session.userId);

							let id = "";
							while(moduleType.modules.hasOwnProperty(id = utils.randomString(6)));

							let module = new moduleType.moduleClass();
							module.userId = user.id;
							module.type = moduleType.name;
							module.id = id;
							module.name = data.name;
							module.secret = utils.randomString(10);
							module.shared = [];
							module.enabled = false;

							module.onCreate();

							// --------------------- SORT + ADD ---------------------
							if(!user.modules.hasOwnProperty(module.type)) user.modules[module.type] = [];
							let index = utils.binarySearch(user.modules[module.type], module.name,
											(first, second) => moduleManager.getModule(module.type, first).name.toLowerCase().localeCompare(second.toLowerCase()));
							user.modules[module.type].splice(index < 0 ? -(index + 1) : index, 0, module.id);

							moduleType.modules[module.id] = module;
							// --------------------- SORT + ADD ---------------------

							netClient.sendPacket("PacketModuleAdd", {
								userId: module.userId,
								type: module.type,
								id: module.id,
								name: module.name,
								secret: module.secret
							});

							callback("/" + module.type + "/" + module.id);
						}
					} else {
						callback("name_invalid");
					}
				}
			}
		});

		socket.on("module_update", (data, callback) => {
			if(data != null && typeof data.moduleType == "string" && typeof data.moduleId == "string" && typeof data.name == "string" && typeof data.secret == "string" && Array.isArray(data.shared) && typeof data.delete == "boolean" && typeof callback == "function") {
				if(socket.request.session.userId) {
					if(!data.delete) {
						if(/^[A-Za-z0-9_-]+$/.test(data.name)) {
							if(/^[A-Za-z0-9_-]+$/.test(data.secret)) {
								let module = moduleManager.getModule(data.moduleType, data.moduleId);

								if(module != null) {
									let user = userManager.getUser(socket.request.session.userId);

									if(module.userId === user.id) {
										module.name = data.name;
										module.secret = data.secret;
										for(let i = module.shared.length - 1; i >= 0; i--) {
											let user = userManager.getUser(module.shared[i]);

											if(!data.shared.includes(user.name)) {
												let modules = user.sharedModules[module.type];
												modules.splice(modules.indexOf(module.id), 1);
												if(modules.length === 0) delete user.sharedModules[module.type];

												module.shared.splice(i, 1);
											}
										}

										// SORT
										let array = [];
										array.push(user.modules[module.type]);
										module.shared.forEach(userId => array.push(userManager.getUser(userId).sharedModules[module.type]));

										array.forEach(modules => {
											modules.splice(modules.indexOf(module.id), 1);

											let index = utils.binarySearch(modules, module.name,
												(first, second) => moduleManager.getModule(module.type, first).name.toLowerCase().localeCompare(second.toLowerCase()));

											modules.splice(index < 0 ? -(index + 1) : index, 0, module.id);
										});
										// SORT

										netClient.sendPacket("PacketModuleUpdate", {
											moduleType: module.type,
											moduleId: module.id,
											name: module.name,
											secret: module.secret,
											shared: module.shared
										});

										callback("success");
									}
								}
							} else {
								callback("secret_invalid");
							}
						} else {
							callback("name_invalid");
						}

					// DELETE
					} else {
						let module = moduleManager.getModule(data.moduleType, data.moduleId);
						if(module != null) {
							let user = userManager.getUser(socket.request.session.userId);

							if(module.userId === user.id) {
								let array = [];
								array.push(user.modules);
								module.shared.forEach(userId => array.push(userManager.getUser(userId).sharedModules));

								array.forEach(moduleTypes => {
									let modules = moduleTypes[module.type];
									modules.splice(modules.indexOf(module.id), 1);
									if(modules.length === 0) delete moduleTypes[module.type];
								});

								delete moduleManager.moduleTypes[module.type].modules[module.id];

								netClient.sendPacket("PacketModuleDelete", {
									moduleType: module.type,
									moduleId: module.id
								});

								callback("success");
							}
						}
					}
				}
			}
		});

		socket.on("module_share", (data, callback) => {
			if(data != null && typeof data.moduleType == "string" && typeof data.moduleId == "string" && typeof callback == "function") {
				if(socket.request.session.userId) {
					let module = moduleManager.getModule(data.moduleType, data.moduleId);

					if(module != null && module.userId === socket.request.session.userId) {
						let shareToken;

						loop:
						while(true) {
							shareToken = utils.randomString(5);

							for(let type in moduleManager.moduleTypes) {
								let moduleType = moduleManager.moduleTypes[type];
								for(let moduleName in moduleType.modules) {
									let module = moduleType.modules[moduleName];
									if(module.share != null && module.share.token === shareToken && module.share.time >= new Date().getTime()) {
										continue loop;
									}
								}
							}

							break;
						}

						module.share = {
							token: shareToken,
							time: new Date().getTime() + 60 * 1000
						};

						callback(shareToken);
					}
				}
			}
		});

		socket.on("module_redeem", (shareToken, callback) => {
			if(shareToken != null && typeof shareToken == "string" && typeof callback == "function") {
				if(socket.request.session.userId) {
					for(let type in moduleManager.moduleTypes) {
						for(let moduleName in moduleManager.moduleTypes[type].modules) {
							let module = moduleManager.moduleTypes[type].modules[moduleName];
							if(module.share != null && module.share.token === shareToken && module.share.time >= new Date().getTime()) {
								if(module.userId !== socket.request.session.userId) {
									if(!module.shared.includes(socket.request.session.userId)) {
										let user = userManager.getUser(socket.request.session.userId);

										if(!user.sharedModules.hasOwnProperty(module.type)) user.sharedModules[module.type] = [];
										let index = utils.binarySearch(user.sharedModules[module.type], module.name,
											(first, second) => moduleManager.getModule(module.type, first).name.toLowerCase().localeCompare(second.toLowerCase()));
										user.sharedModules[module.type].splice(index < 0 ? -(index + 1) : index, 0, module.id);

										module.shared.push(user.id);
										module.share = null;

										netClient.sendPacket("PacketModuleRedeem", {
											userId: user.id,
											moduleType: module.type,
											moduleId: module.id
										});

										callback("success");
									} else {
										callback("user_shared");
									}
								} else {
									callback("user_owner");
								}

								return;
							}
						}
					}

					callback("code_invalid");
				}
			}
		});

		socket.on("module_remove", data => {
			if(data != null && typeof data.moduleType == "string" && typeof data.moduleId == "string") {
				if(socket.request.session.userId) {
					let module = moduleManager.getModule(data.moduleType, data.moduleId);

					if(module != null && module.shared.includes(socket.request.session.userId)) {
						let user = userManager.getUser(socket.request.session.userId);

						let modules = user.sharedModules[module.type];
						modules.splice(modules.indexOf(module.id), 1);
						if(modules.length === 0) delete user.sharedModules[module.type];

						module.shared.splice(module.shared.indexOf(user.id), 1);

						netClient.sendPacket("PacketModuleRemove", {
							userId: user.id,
							moduleType: module.type,
							moduleId: module.id
						});
					}
				}
			}
		});
	});

};