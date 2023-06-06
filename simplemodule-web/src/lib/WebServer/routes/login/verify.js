const userManager = require.main.require("./lib/UserManager");
const netClient = require.main.require("./lib/NetClient");

module.exports = function(app, io) {

	app.get("/Verify/:id", (req, res) => {
		let user = userManager.getUser(req.params.id);

		if(user != null) {
			if(!user.verified) {
				user.verified = true;

				netClient.sendPacket("PacketUserVerify", {
					userId: user.id
				});

				res.render("login/verify");
				return;
			}
		}

		res.redirect("/");
	});

}