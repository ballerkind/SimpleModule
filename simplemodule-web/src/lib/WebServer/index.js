const log = require("../LogManager");
const netClient = require("../NetClient");
const moduleManager = require("../ModuleManager");
const userManager = require("../UserManager");
const languageManager = require("../LanguageManager");

const express = require("express");
const cookieParser = require("cookie-parser");
const bodyParser = require("body-parser");
const session = require("express-session");

const url = require("url");
const fs = require('fs')

const app = express();
const server = require("http").Server(app);
const io = require("socket.io")(server);

const sessionMiddleware = session({
	name: "session",
	resave: true,
	saveUninitialized: true,
	secret: "U910r4nGoASngg3417q35aSfA1"
});

/* --------------------- APP --------------------- */

app.disable("x-powered-by");

app.set("views", __dirname + "/views");
app.set("view engine", "ejs");

app.use(express.static(__dirname + "/public"));
app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser());
app.use(sessionMiddleware);

/* --------------------- SocketIO --------------------- */

netClient.io = io;

io.use((socket, next) => {
	if(socket.request.res) {
		sessionMiddleware(socket.request, socket.request.res, next);
	} else {
		setTimeout(() => {
			socket.disconnect();
		}, 10);
	}
});

io.use((socket, next) => {

	if(socket.request.session.userId) {
		let user = userManager.getUser(socket.request.session.userId);

		let path = url.parse(socket.request.headers.referer).pathname;
		let split = path.split("/");

		if(split.length === 3) {
			let module = user.getModule(split[1], split[2]);

			if(module != null) {
				socket.module = module;
				socket.join(module.type + "_" + module.id);
			}
		}
	}

	next();

});

io.toModule = module => io.to(module.type + "_" + module.id);

/* --------------------- LANGUAGE --------------------- */

app.use((req, res, next) => {

	if(req.cookies.language == null || !languageManager.languages.hasOwnProperty(req.cookies.language)) {
		req.cookies.language = "en";
		res.cookie("language", "en", {maxAge: 365 * 24 * 60 * 60 * 1000});
	}

	res.locals.language = languageManager.getLanguage(req.cookies.language);
	next();

});

/* --------------------- DarkMode --------------------- */

app.use((req, res, next) => {

	if(req.cookies.appearance == null || (req.cookies.appearance !== "light" && req.cookies.appearance !== "dark")) {
		req.cookies.appearance = "light";
		res.cookie("appearance", "light", {maxAge: 365 * 24 * 60 * 60 * 1000});
	}

	res.locals.appearance = req.cookies.appearance;
	next();

});

/* --------------------- API --------------------- */
app.api = {};
app.registerAPI = (moduleType, method, callback, description) => {
	if(!app.api.hasOwnProperty(moduleType)) app.api[moduleType] = {};

	app.api[moduleType][method.toLowerCase()] = {
		callback: callback,
		description: description
	}
};

app.all("/api/:moduleType/:moduleId/:secret/:method", (req, res) => {

	if(app.api.hasOwnProperty(req.params.moduleType)) {
		let type = app.api[req.params.moduleType];

		if(type.hasOwnProperty(req.params.method)) {
			let method = type[req.params.method];

			if((method.callback.length === 2 && req.method === "GET") || (method.callback.length === 3 && req.method === "POST")) {
				let module = moduleManager.getModule(req.params.moduleType, req.params.moduleId);

				if(module != null) {
					if(module.secret === req.params.secret) {
						if(method.callback.length === 3) {
							method.callback(module, req.body, res);
						} else {
							method.callback(module, res);
						}
					} else {
						res.send({
							error: "Wrong secret!"
						});
					}
				} else {
					res.send({
						error: "Module not found!"
					});
				}
			} else {
				res.send({
					error: "Wrong request method!"
				});
			}
		} else {
			res.send({
				error: "Method doesn't exist!"
			});
		}
	} else {
		res.send({
			error: "Module doesn't exist or has no methods!"
		});
	}

});

/* --------------------- ROUTES --------------------- */

app.isLogged = (req, res, next) => {
	if(req.session.userId) {
		res.locals.currentUrl = req.originalUrl;
		res.locals.user = userManager.getUser(req.session.userId);
		res.locals.userManager = userManager;
		res.locals.moduleManager = moduleManager;

		next();
	} else {
		res.redirect("/Login/");
	}
};

let routes = [
	"welcome",
	"login/login", "login/register", "login/verify", "login/resetpassword",
	"interface/dashboard", "interface/modules"
];
for(let type in moduleManager.moduleTypes) {
	routes.push("interface/modules/" + type);
}
routes.push(
	"interface/settings", "interface/support", "login/logout",
	"api"
);

routes.forEach(route => {
	let path = "./routes/" + route;

	try {
		require(path)(app, io);
	} catch(ex) {
		log.warn("The file '" + path + "' does not exist!");
	}
});

app.use((req, res, next) => {
	if(req.session.userId) {
		res.redirect("/Dashboard/");
	} else {
		res.redirect("/");
	}

	next();
});

/* --------------------- START --------------------- */

server.listen(80, () => {
	log.info("WebServer auf Port " + server.address().port + " gestartet");
});