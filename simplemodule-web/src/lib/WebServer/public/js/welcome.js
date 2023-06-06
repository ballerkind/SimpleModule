$(window).scroll(function() {
	if($(this).scrollTop() > 0) {
		document.documentElement.style.setProperty("--current-nav-font-color", "var(--nav-font-color)");
		$("#nav").css("background", "var(--nav-bg-color)")
						.css("box-shadow", "0px 1px 10px 1px rgba(0, 0, 0, 0.3)");
		$("#nav > *").css("margin-top", "18px");
	} else {
		document.documentElement.style.setProperty("--current-nav-font-color", "#fff");
		$("#nav").css("background", "transparent")
				 		.css("box-shadow", "");
		$("#nav > *").css("margin-top", "30px");
	}
});

$("#nav > ul > li > select").change(function() {
	let date = new Date();
	date.setTime(date.getTime() + 365 * 24 * 60 * 60 * 1000);
	document.cookie = "language=" + $(this).val() + "; expires=" + date.toUTCString() + "; path=/";
	location.reload();
});

$("#nav > ul > li > a").click(function() {
	let a = $(this);
	if(a.attr("target") != null) {
		$("html").stop().animate({ scrollTop: $("section#" + a.attr("target")).offset().top - 67 }, 900);
	}
});

function toggleDarkMode() {
	let css = $("link[href*='/css/welcome_']");
	let appearance = css.attr("href").includes("dark") ? "light" : "dark";

	css.attr("href", "/css/welcome_" + appearance + ".css");
	$(event.target).toggleClass("fas").toggleClass("far");

	let date = new Date();
	date.setTime(date.getTime() + 365 * 24 * 60 * 60 * 1000);
	document.cookie = "appearance=" + appearance + "; expires=" + date.toUTCString() + "; path=/";
}

$(".fa-stream").on("click", () => {
	$("ul").slideToggle();
});