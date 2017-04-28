// websocket
//var websocket = new WebSocket("ws://localhost:8080/BBDPDoctor/PushServerEndpoint");
var websocket = new WebSocket("ws://140.121.197.130:8000/BBDPDoctor/PushServerEndpoint");

websocket.onopen = function(evt) {
	sendMessage();
	console.log("onopen!");
};
websocket.onmessage = function(evt) {
	console.log("onmessage!");
	var json = JSON.parse(evt.data);
	var patientID = json.patientID;
	if(localStorage.getItem("login") == json.doctorID && json.patientID != "doctor" && json.option == "clinicPush") {		//是病患傳來的診間推播
		$.ajax({
			type: "POST",
			//url: "http://localhost:8080/BBDPDoctor/PushServlet",
			url: "http://140.121.197.130:8000/BBDPDoctor/PushServlet",
			data: {
				option: "clinicPush",
				json: evt.data
			},
			dataType: "text",
			success : function(response){
				console.log(response);
				generateClinicNotification(json.patientID, response);
			},
			error : function(){
				console.log("PushServlet clinicPush 沒有回應");
			}
		});
	}
	else if(json.patientID = "doctor" && json.option == "open") {		//是醫生open所回傳的
		console.log("is doctor");
	}
	else if(localStorage.getItem("login") == json.doctorID && json.patientID != "doctor" && json.option == "remindPush") {		//是病患傳來的提醒推播
		$.ajax({
			type: "POST",
			//url: "http://localhost:8080/BBDPDoctor/PushServlet",
			url: "http://140.121.197.130:8000/BBDPDoctor/PushServlet",
			data: {
				option: "getPatientName",
				json: evt.data
			},
			dataType: "text",
			success : function(response){
				console.log(response);
				generateRemindNotification(json, patientID, response);
			},
			error : function(){
				console.log("PushServlet remindPush 沒有回應");
			}
		});
	}
	else {
		console.log("you are so weird!");
	}
	console.log("websocket onmessage: " + evt.data);
};
websocket.onerror = function(evt) {
	console.log("websocket error: " + evt.data);
	websocket.close();
};
function sendMessage() {
	var message = "{\"doctorID\":\"" + localStorage.getItem("login") + "\",\"patientID\":\"doctor\", \"option\":\"open\"}";
	websocket.send(message);
}

function generateClinicNotification(patientID, patientName) {		//產生診間推播modal視窗
	$("body").append("<div class='modal fade' id='clinicPushModal' role='dialog'>"
		 + "<div class='modal-dialog modal-sm'>"
		 + "<div class='modal-content'>"
		 + "<div class='modal-header'>"
		 + "<button type='button' class='close' data-dismiss='modal'>&times;</button>"
		 + "<h4 class='modal-title'>診間推播</h4>"
		 + "</div>"
		 + "<div class='modal-body'>"
		 + "<p>前往&nbsp" + patientName + "&nbsp的病患資訊</p>"
		 + "</div>"
		 + "<div class='modal-footer'>"
		 + "<button type='button' class='btn btn-default' onclick='putSession(" + patientID + ", \"http://140.121.197.130:8000/BBDPDoctor/PatientBasicInformation.html\")'>前往</button>"
		 + "<button type='button' class='btn btn-default' data-dismiss='modal'>取消</button>"
		 + "</div></div></div></div>");
	$('#clinicPushModal').modal('show');
}

function generateRemindNotification(json, patientID, patientName) {		//產生提醒推播右下角notification視窗
	var options = {
		body: json.body,
		icon: "picture/patientPhoto/" + setTempPhoto(patientID) + ".png"		//大頭貼!!
	}
	var notification = new Notification(json.title, options);
	notification.onclick = function() {
		putSession(patientID, json.hyperlink);
		notification.close();
	};
}

function putSession(patientID, hyperlink) {
	$.ajax({
		type: "POST",
		//url: "http://localhost:8080/BBDPDoctor/PushServlet",
		url: "http://140.121.197.130:8000/BBDPDoctor/PushServlet",
		data: {
			option: "putSession",
			patient: patientID
		},
		dataType: "text",
		success : function(response){
			window.location.href = hyperlink;
		},
		error : function(){
			console.log("PushServlet putSession 沒有回應");
		}
	});
}