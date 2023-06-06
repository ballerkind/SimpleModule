let queue = [];
function notify(message, type) {

	queue.push({
		message: message,
		type: type
	});

	if(queue.length === 1) {
		draw(message, type);
	}

	function draw(message, type) {

		let div = $("<div class='alert alert-" + type + "'> " + message + "</div>");

		$("body").append(div);
		
		div.animate({
			right: "30px"
		}, 400);

		let timeout = setTimeout(remove, 2000);

		div.on("click",() => {
			clearTimeout(timeout);
			remove();
		});

		function remove() {
			div.animate({
				bottom: "100%"
			}, 500, () => {
				div.remove();
				queue.shift();

				if(queue.length > 0) {
					let json = queue[0];
					draw(json.message, json.type);
				}
			});

			div.off("click");
		}

	}

}

$("#nav > ul > li > a").on("click", function() {
	let a = $("#nav > ul > li > a").not(this);
	a.next().slideUp();
	a.children(".fas.fa-angle-down").removeClass("rotate");

	let current = $(this);
	current.next().slideToggle();
	current.children(".fas.fa-angle-down").toggleClass("rotate");
});

function toggleDarkMode() {
	let css = $("link[href*='/css/style_']");
	let appearance = css.attr("href").includes("dark") ? "light" : "dark";

	css.attr("href", "/css/style_" + appearance + ".css");
	$(event.target).toggleClass("fas").toggleClass("far");

	let date = new Date();
	date.setTime(date.getTime() + 365 * 24 * 60 * 60 * 1000);
	document.cookie = "appearance=" + appearance + "; expires=" + date.toUTCString() + "; path=/";
}