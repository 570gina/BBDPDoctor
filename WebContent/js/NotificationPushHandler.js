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
	if(localStorage.getItem("login") == json.doctorID && json.patientID != "doctor") {		//是病患傳來的
		$.ajax({
			type: "POST",
			//url: "http://localhost:8080/BBDPDoctor/ClinicPushServlet",
			url: "http://140.121.197.130:8000/BBDPDoctor/ClinicPushServlet",
			data: {
				json: evt.data
			},
			dataType: "text",
			success : function(response){
				console.log(response);
				generateNotification(response);
			},
			error : function(){
				console.log("ClinicPushServlet沒有回應");
			}
		}); 
	}
	else {		//是醫生open所回傳的
		console.log("is doctor");
	}
	console.log("websocket onmessage: " + evt.data);
};
websocket.onerror = function(evt) {
	console.log("websocket error: " + evt.data);
	websocket.close();
};
function sendMessage() {
	var message = "{\"doctorID\":\"" + localStorage.getItem("login") + "\",\"patientID\":\"doctor\"}";
	websocket.send(message);
}

function generateNotification(patientName) {
	$("body").append("<div class='modal fade' id='clinicPushModal' role='dialog'>"
		 + "<div class='modal-dialog modal-sm'>"
		 + "<div class='modal-content'>"
		 + "<div class='modal-header'>"
		 + "<button type='button' class='close' data-dismiss='modal'>&times;</button>"
		 + "<h4 class='modal-title'>診間推播</h4>"
		 + "</div>"
		 + "<div class='modal-body'>"
		 + "<p>前往" + patientName + "的病患資訊</p>"
		 + "</div>"
		 + "<div class='modal-footer'>"
		 //+ "<a href='http://localhost:8080/BBDPDoctor/PatientBasicInformation.html' class='btn btn-default'>前往</a>"
		 + "<a href='http://140.121.197.130:8000/BBDPDoctor/PatientBasicInformation.html' class='btn btn-default'>前往</a>"
		 + "<button type='button' class='btn btn-default' data-dismiss='modal'>取消</button>"
		 + "</div></div></div></div>");
	$('#clinicPushModal').modal('show');
}