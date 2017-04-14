$.ajax({
	type : "POST",
	//url : "http://localhost:8080/BBDPDoctor/NotificationServlet",
	url : "http://140.121.197.130:8000/BBDPDoctor/NotificationServlet",
	data : {
		option : "getData", 
		doctorID : localStorage.getItem("login")
	},
	dataType : "json",
	success : function(response) {
		$("#notificationUl").empty();
		for(var i=0; i<response.length; i++) {
			$("#notificationUl").append("<li class='divider'></li><li><a href='javascript: notificationOnclick(" + response[i].patientID + ");'><span class='chat-img pull-left' style='margin-right: 10px;'><img src='picture/patientBasicInformation/photo.png' /></span><div><strong>診間推播</strong><span class='pull-right text-muted'><small>" + response[i].time.substring(0, 16) + "</small></span></div><div>" + response[i].content + "</div></a></li>");
		}
		$("#notificationUl").prepend("<li><a href='javascript: clearAllNotification();' align='right'><big><i class='fa fa-trash-o'></i></big>&nbsp;&nbsp;清除所有通知</a></li>");
	},
	error : function() {
		console.log("notification.js error");
	}
});

//清除所有通知
function clearAllNotification() {
	$.ajax({
		type : "POST",
		//url : "http://localhost:8080/BBDPDoctor/NotificationServlet",
		url : "http://140.121.197.130:8000/BBDPDoctor/NotificationServlet",
		data : {
			option : "clearAllNotification", 
			doctorID: localStorage.getItem("login")
		},
		dataType : "text",
		success : function(response) {
			$("#notificationUl").empty();
			$("#notificationUl").prepend("<li><a href='javascript: clearAllNotification();' align='right'><big><i class='fa fa-trash-o'></i></big>&nbsp;&nbsp;清除所有通知</a></li>");
			},
		error : function() {
			console.log("ClearAllNotification error");
		}
	});
}

function notificationOnclick(patientID) {
	$.ajax({
		type : "POST",
		//url : "http://localhost:8080/BBDPDoctor/NotificationServlet",
		url : "http://140.121.197.130:8000/BBDPDoctor/NotificationServlet",
		data : {
			option : "click", 
			doctorID: patientID
		},
		dataType : "text",
		success : function(response) {
			window.location.href = "PatientBasicInformation.html";
		},
		error : function() {
			console.log("notificationOnclick error");
		}
	});
}