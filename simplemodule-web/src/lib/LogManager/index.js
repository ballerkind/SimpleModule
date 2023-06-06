function info(message) {
	console.log('\x1b[32m[INFO ' + getTime() + '] \x1b[37m' + message);
}

function warn(message) {
	console.log('\x1b[33m[WARNING ' + getTime() + '] \x1b[37m' + message);
}

function error(message) {
	console.log('\x1b[31m[ERROR ' + getTime() + '] \x1b[37m' + message);
}

function debug(message) {
	console.log('\x1b[36m[DEBUG ' + getTime() + '] \x1b[37m' + message);
}

function getTime() {

	let date = new Date();

	let hour = date.getHours();
	hour = (hour < 10 ? "0" : "") + hour;

	let min = date.getMinutes();
	min = (min < 10 ? "0" : "") + min;

	let sec = date.getSeconds();
	sec = (sec < 10 ? "0" : "") + sec;

	return hour + ":" + min + ":" + sec + " " + date.getDate() + "." + (date.getMonth() + 1) + "." + date.getFullYear();

}

module.exports = {
	info: info,
	warn: warn,
	error: error,
	debug: debug
};