//init
$(document).ready(function() {
	$("#todayQuestionnairePanel>a").attr("href", "javascript: todayQuestionnaireList('show');");
	$("#todayQuestionnairePanel>a>.panel-body").html("<span class='pull-left' style='color: black;'>查看列表</span><span class='pull-right' style='color: black;'><i class='fa fa-arrow-circle-down'></i></span>");
	$("#todayQuestionnairePanel").append("<div class='panel-footer' style='display: none; background-color: white; min-height: 53vh; max-height: 53vh; overflow-y: auto;'><div class='list-group'></div></div>");	
	$("#todayFolderPanel").append("<div class='panel-footer' style='display: none; background-color: white; min-height: 53vh; max-height: 53vh; overflow-y: auto;'><div class='list-group'></div></div>");
	getQData();
	getFData();
});

//顯示或隱藏問卷列表
function todayQuestionnaireList(state) {
	if(state == "show") {
		$("#todayQuestionnairePanel>a").attr("href", "javascript: todayQuestionnaireList('hide');");
		$("#todayQuestionnairePanel>a>.panel-body").html("<span class='pull-left' style='color: black;'>收回列表</span><span class='pull-right' style='color: black;'><i class='fa fa-arrow-circle-up'></i></span>");
		$("#todayQuestionnairePanel>.panel-footer").fadeIn("slow");
	}
	else if(state == "hide") {
		$("#todayQuestionnairePanel>a").attr("href", "javascript: todayQuestionnaireList('show');");
		$("#todayQuestionnairePanel>a>.panel-body").html("<span class='pull-left' style='color: black;'>查看列表</span><span class='pull-right' style='color: black;'><i class='fa fa-arrow-circle-down'></i></span>");
		$("#todayQuestionnairePanel>.panel-footer").fadeOut("slow");
	}
}

//顯示或隱藏檔案列表
function todayFolderList(state) {
	if(state == "show") {
		$("#todayFolderPanel>a").attr("href", "javascript: todayFolderList('hide');");
		$("#todayFolderPanel>a>.panel-body").html("<span class='pull-left' style='color: black;'>收回列表</span><span class='pull-right' style='color: black;'><i class='fa fa-arrow-circle-up'></i></span>");
		$("#todayFolderPanel>.panel-footer").fadeIn("slow");
	}
	else if(state == "hide") {
		$("#todayFolderPanel>a").attr("href", "javascript: todayFolderList('show');");
		$("#todayFolderPanel>a>.panel-body").html("<span class='pull-left' style='color: black;'>查看列表</span><span class='pull-right' style='color: black;'><i class='fa fa-arrow-circle-down'></i></span>");
		$("#todayFolderPanel>.panel-footer").fadeOut("slow");
	}
}

function getQData() {
	$.ajax({
		type : "POST",
		//url : "http://localhost:8080/BBDPDoctor/HomepageServlet",
		url : "http://140.121.197.130:8000/BBDPDoctor/HomepageServlet",
		data : {
			doctorID : localStorage.getItem("login")
		},
		dataType : "json",
		success : function(response) {
			if(response.QList == "今日目前尚未有問卷資料" || response.QList == "" || response.QList == null) {
				$("#todayQuestionnaireNum").html("0");
			}
			else {
				$("#todayQuestionnaireNum").html(response.QList.length);
				for(var i=0;  i<response.QList.length; i++) {
					var questionnaireName;
					if(response.QList[i].questionnaireName.length > 14) description = response.QList[i].questionnaireName.substring(0, 14) + "...";
					else questionnaireName = response.QList[i].questionnaireName;
					$("#todayQuestionnairePanel>.panel-footer>.list-group").append("<a href='javascript: clickQItem(\"" + response.QList[i].patientID + "\");' class='list-group-item' style='background-color: #F8B1B0; border: 2px solid white;'><div class='media'><div class='media-left'><img src='picture/homepage/user.png' class='media-object' /></div><div class='media-body'><h4 class='list-group-item-heading media-heading'><b>" + response.QList[i].patientName + "</b></h4><p class='list-group-item-text'>" + questionnaireName + "</p><p class='list-group-item-text pull-right'>" + response.QList[i].date + "</p></div></div></a>");
				}
			}
		},
		error : function() {
			console.log("homepage.js getData error");
		}
	});
}

function getFData() {
	$.ajax({
		type : "POST",
		//url : "http://localhost:8080/BBDPDoctor/HomepageServlet",
		url : "http://140.121.197.130:8000/BBDPDoctor/HomepageServlet",
		data : {
			doctorID : localStorage.getItem("login")
		},
		dataType : "json",
		success : function(response) {
			if(response.FList == "今日目前尚未有檔案資料" || response.FList == "" || response.FList == null) {
				$("#todayFolderNum").html("0");
			}
			else {
				$("#todayFolderNum").html(response.FList.length);
				for(var i=0;  i<response.FList.length; i++) {
					var description;
					if(response.FList[i].description.length > 14) description = response.FList[i].description.substring(0, 14) + "...";
					else if(response.FList[i].description.length == 0) description = "無文字說明";
					else description = response.FList[i].description;
					var image = "";
					if(response.FList[i].pictureOrVideo == "video") image = "<div class='media-left'><img src='picture/homepage/video-player.png' class='media-object' /></div>";
					else if(response.FList[i].pictureOrVideo == "picture") image = "<div class='media-left'><img src='picture/homepage/picture.png' class='media-object' /></div>";
					$("#todayFolderPanel>.panel-footer>.list-group").append("<a href='javascript: clickFItem(\"" + response.FList[i].patientID + "\");' class='list-group-item' style='background-color: #dabfff; border: 2px solid white;'><div class='media'>" + image + "<div class='media-body'><h4 class='list-group-item-heading media-heading'><b>" + response.FList[i].patientName + "</b></h4><p class='list-group-item-text'>" + description + "</p><p class='list-group-item-text pull-right'>" + response.FList[i].time.substring(0, 16) + "</p></div></div></a>");
				}
			}
		},
		error : function() {
			console.log("homepage.js getData error");
		}
	});
}

function clickQItem(patientID) {
	//放session
	putSession(patientID, "PatientQuestionnaire.html");
}

function clickFItem(patientID) {
	//放session
	putSession(patientID, "PatientFolder.html");
}

function refreshQPanel() {
	$("#todayQuestionnairePanel>.panel-footer>.list-group").empty();
	getQData();
}

function refreshFPanel() {
	$("#todayFolderPanel>.panel-footer>.list-group").empty();
	getFData();
}