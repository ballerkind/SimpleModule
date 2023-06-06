const nodemailer = require("nodemailer");
const fs = require("fs");

let transporter = nodemailer.createTransport({
	service: "gmail",
	auth: {
		user: "mail@googlemail.com",
		pass: "hierkommtdaspasswordrein"
	}
});

function sendVerifyMail(user) {

	fs.readFile(__dirname + "/templates/verify.html", "UTF8", (err, data) => {
		transporter.sendMail({
			from: "'SimpleModule' noreply@simplemodule.com",
			to: user.email,
			subject: "Verify your account - SimpleModule",
			html: data.replace("%name%", user.name).replace("%id%", user.id)
		});
	});

}

function sendResetPasswordMail(user) {

	fs.readFile(__dirname + "/templates/resetpassword.html", "UTF8", (err, data) => {
		transporter.sendMail({
			from: "'SimpleModule' noreply@simplemodule.com",
			to: user.email,
			subject: "Reset your password - SimpleModule",
			html: data.replace("%name%", user.name).replace("%token%", user.resetPassword.token)
		});
	});

}

module.exports = {
	sendVerifyMail: sendVerifyMail,
	sendResetPasswordMail: sendResetPasswordMail
};