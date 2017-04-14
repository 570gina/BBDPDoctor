$("nav").append("<!-- navbar-header -->"
	+ "<div class='navbar-header'>"
	+ "<button type='button' class='navbar-toggle' data-toggle='collapse' data-target='.navbar-collapse'>"
	+ "<span class='sr-only'>Toggle navigation</span>"
	+ "<span class='icon-bar'></span>"
	+ "<span class='icon-bar'>"
	+ "<span class='icon-bar'></span>"
	+ "</button>"
	+ "<a class='navbar-brand' href='Homepage.html'>Bridge Between Doctor and Patient</a>"
	+ "</div>"

	+ "<!-- navbar-top-links -->"
	+ "<ul class='nav navbar-top-links navbar-right'>"
	+ "<!-- dropdown-home -->"
	+ "<li class='dropdown'><a href='Homepage.html'><i class='fa fa-home fa-fw' style='color:white; font-size: 17px'></i></a></li>"
	+ "<!-- dropdown-notification -->"
	+ "<li class='dropdown'>"
	+ "<a class='dropdown-toggle' data-toggle='dropdown' id='notification'><i class='fa fa-bell fa-fw' style='color:white'></i><i class='fa fa-caret-down' style='color:white'></i></a>"
	+ "<ul class='dropdown-menu dropdown-alerts' id='notificationUl' style='min-height: 350px; max-height: 350px; overflow-y: scroll;'></ul>"
	+ "</li>"
	+ "<!-- dropdown-account -->"
	+ "<li class='dropdown'>"
	+ "<a class='dropdown-toggle' data-toggle='dropdown' href='#'>"
	+ "<i class='fa fa-user fa-fw' style='color:white'></i>"
	+ "<i class='fa fa-caret-down' style='color:white'></i>"
	+ "</a>"
	+ "<ul class='dropdown-menu dropdown-user'>"
	+ "<li><a id='setting'><i class='fa fa-gear fa-fw'></i>帳戶設定</a></li>"
	+ "<li class='divider'></li>"
	+ "<li><a id='logout'><i class='fa fa-sign-out fa-fw'></i>登出</a></li>"
	+ "</ul>"
	+ "</li>"
	+ "</ul>"
	+ "<!-- navbar-static-side -->"
	+ "<div class='navbar-default2 sidebar' role='navigation'>"
	+ "<div class='sidebar-nav navbar-collapse'>"
	+ "<ul class='nav' id='side-menu'>"
	+ "<!-- patient search -->"
	+ "<li class='sidebar-search'>"
	+ "<div style='margin-bottom:10px'><b>病患資訊</b></div>"
	+ "<div class='input-group custom-search-form'>"
	+ "<input type='text' class='form-control' placeholder='病患身分證字號後五碼' id='patientID'>"
	+ "<span class='input-group-btn'><button class='btn btn-default' type='button'data-toggle='modal' data-target='#searchModal' id='pateintSearch'><i class='fa fa-search'></i></button></span>"
	+ "</div>"
	+ "</li>"
	+ "<li>"
	+ "<a href='#' style='color:#2e2d4d'><b>資訊平台<span class='fa arrow' style='color:#2e2d4d'></span></b></a>"
	+ "<ul class='nav nav-second-level'>"
	+ "<li><a href='PatientInstruction.html' style='color:#2e2d4d'><b>衛教資訊</b></a></li>"
	+ "<li><a href='ClinicHours.html' style='color:#2e2d4d'><b>門診時間</b></a></li>"
	+ "</ul>"
	+ "</li>"
	+ "<li>"
	+ "<a href='#' style='color:#2e2d4d'><b>問卷區</b><span class='fa arrow' style='color:#2e2d4d'></span></a>"
	+ "<ul class='nav nav-second-level'>"
	+ "<li><a href='QuestionnaireModule.html' style='color:#2e2d4d'><b>問卷模板</b></a></li>"
	+ "<li><a href='QuestionnairePool.html' style='color:#2e2d4d'><b>問卷題庫</b></a></li>"
	+ "<li><a href='QuestionnaireTempStorage.html' style='color:#2e2d4d'><b>題目暫存區</b></a></li>"
	+ "</ul>"
	+ "</li>"
	+ "<li><a href='HealthTracking.html' style='color:#2e2d4d'><b>健康狀況追蹤模板</b></a></li>"
	+ "<li><a href='Notice.html' style='color:#2e2d4d'><b>注意事項</b></a></li>"
	+ "</ul>" + "</div>" + "</div>");

$(document).ready(function() {
	// 通知欄
	$("#notification").click(function() {
		$.getScript("js/notification.js");
	});
	// Websocket
	$.getScript("js/NotificationPushHandler.js");
	// 設定
	$("#setting").click(function() {
		$.getScript("js/setting.js");
	});
	// 登出
	$("#logout").click(function() {
		$.getScript("js/logout.js");
	});
	// 病患資訊搜尋按鈕
	$.getScript("js/patientSearch.js");
});