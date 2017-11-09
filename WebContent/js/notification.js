//取得該醫生所有通知
function getAllNotification() {
	$.ajax({
		type: "POST",
		url: "NotificationServlet",
		data: {
			option: "getAllNotification"
		},
		dataType: "json",
		success: function(response) {
			$(".notification-list").empty();
			$(".notification-list").append(
"							<li style='cursor: pointer;' onclick='clearAllNotification()'>" + "\n" + 
"								<a>" + "\n" + 
"									<div class='row'>" + "\n" + 
"										<div class='col-md-12 col-xs-12'>" + "\n" + 
"											<span class='pull-right'>" + "\n" + 
"												<i class='fa fa-trash-o'></i>" + "\n" + 
"												清除所有通知" + "\n" + 
"											</span>" + "\n" + 
"										</div>" + "\n" + 
"									</div>" + "\n" + 
"								</a>" + "\n" + 
"							</li>" + "\n");
			if(response.length == 0) {
				$(".notification-list").append(
"							<li class='divider'></li>" + "\n" + 
"							<li style='cursor: pointer;'>" + "\n" + 
"								<a>" + "\n" + 
"									<div class='row'>" + "\n" + 
"										<div class='col-md-12 col-xs-12'>" + "\n" + 
"											<h5>沒有通知</h5>" + "\n" + 
"										</div>" + "\n" + 
"									</div>" + "\n" + 
"								</a>" + "\n" + 
"							</li>" + "\n");
			}
			else {
				for(var i=0; i<response.length; i++) {
					$(".notification-list").append(
"							<li class='divider'></li>" + "\n" + 
"							<li style='cursor: pointer;' onclick='clickNotification(\"" + response[i].hyperlink + "\", \"" + response[i].patientID + "\");'>" + "\n" + 
"								<a>" + "\n" + 
"									<div class='row'>" + "\n" + 
"										<div class='col-md-3 col-xs-3'>" + "\n" + 
"											<div id='notificationPatientPicture" + i + "'>" + "\n" + 
"												<img src='http://140.121.197.130:8004/BBDPPatient/ProfilePictureServlet?option=getProfilePicture&patientID=" + response[i].patientID + "' onerror='failToLoadNotificationPatientPicture(\"" + i + "\");' class='img-responsive' />" + "\n" + 
"											</div>" + "\n" + 
"										</div>" + "\n" + 
"										<div class='col-md-9 col-xs-9'>" + "\n" + 
"											<span class='pull-left'><b>" + response[i].title + "</b></span>" + "\n" + 
"											<span class='pull-right text-muted'><small>" + response[i].time.substring(0, 16) + "</small></span><br>" + "\n" + 
"											<p style='margin-bottom: 0;'>" + response[i].body + "</p>" + "\n" + 
"										</div>" + "\n" + 
"									</div>" + "\n" + 
"								</a>" + "\n" + 
"							</li>" + "\n");
				}
			}
		},
		error: function() {
			console.log("notification.js getAllNotification error");
		}
	});
}

function failToLoadNotificationPatientPicture(i) {
	console.log("in failToLoadNotificationPatientPicture!");
	$("#notificationPatientPicture" + i).empty();
	$("#notificationPatientPicture" + i).append("<img src='img/frame/user.png'  class='img-responsive' />");
}

//刪除該醫生所有通知
function clearAllNotification() {
	$.ajax({
		type: "POST",
		url: "NotificationServlet",
		data: {
			option: "clearAllNotification"
		},
		dataType: "text",
		success: function(response) {
			$(".notification-list").empty();
			$(".notification-list").append(
"							<li style='cursor: pointer;' onclick='clearAllNotification()'>" + "\n" + 
"								<a>" + "\n" + 
"									<div class='row'>" + "\n" + 
"										<div class='col-md-12 col-xs-12'>" + "\n" + 
"											<span class='pull-right'>" + "\n" + 
"												<i class='fa fa-trash-o'></i>" + "\n" + 
"												清除所有通知" + "\n" + 
"											</span>" + "\n" + 
"										</div>" + "\n" + 
"									</div>" + "\n" + 
"								</a>" + "\n" + 
"							</li>" + "\n" + 
"							<li class='divider'></li>" + "\n" + 
"							<li>" + "\n" + 
"								<a>" + "\n" + 
"									<div class='row'>" + "\n" + 
"										<div class='col-md-12 col-xs-12'>" + "\n" + 
"											<h5>沒有通知</h5>" + "\n" + 
"										</div>" + "\n" + 
"									</div>" + "\n" + 
"								</a>" + "\n" + 
"							</li>" + "\n");
		},
		error: function() {
			console.log("notification.js clearAllNotification error");
		}
	});
}

/*======================================================================================================================================================================*/
//websocket
var websocket = new WebSocket("ws://140.121.197.130:8004/BBDPDoctor/PushServerEndpoint");

websocket.onopen = function(evt) {
	sendMessage();
	console.log("onopen!");
};
websocket.onmessage = function(evt) {
	console.log("onmessage!");
	console.log("websocket onmessage: " + evt.data);
	
	var notificationSetting;
	$.ajax({
		type: "POST",
		url: "NotificationServlet",
		async: false,
		data: {
			option: "getNotificationSetting"
		},
		dataType: "json",
		success: function(response) {
			notificationSetting = response;
		},
		error: function() {
			console.log("notification.js getNotificationSetting error");
		}
	});

	console.log(JSON.stringify(notificationSetting));
	var message = JSON.parse(evt.data);
	var doctorID = getDoctorID();
	if(message.doctorID == doctorID && message.patientID != "doctor" && message.option == "clinicPush" && notificationSetting.notification.clinicPush == "yes") {
		clinicPush(evt.data);
	}
	else if(message.doctorID == doctorID && message.patientID != "doctor" && message.option == "remindPush" && message.type == "questionnaire" && notificationSetting.notification.questionnaire == "yes") {
		remindPush(evt.data);
	}
	else if(message.doctorID == doctorID && message.patientID != "doctor" && message.option == "remindPush" && message.type == "folder" && notificationSetting.notification.folder == "yes") {
		remindPush(evt.data);
	}
	else if(message.doctorID == doctorID && message.patientID != "doctor" && message.option == "remindPush" && message.type == "patientInstruction" && notificationSetting.notification.patientInstruction == "yes") {
		remindPush(evt.data);
	}
	else if(message.doctorID == doctorID && message.patientID == "doctor") {
		console.log("is from this doctor!");
	}
	else {
		console.log("not this doctor!");
	}
};
websocket.onerror = function(evt) {
	console.log("websocket error: " + evt.data);
	websocket.close();
};
websocket.onclose = function(evt) {
	console.log("websocket close");
	websocket.close();
};
function sendMessage() {
	var message = "{\"doctorID\":\"" + getDoctorID() + "\",\"patientID\":\"doctor\", \"option\":\"open\"}";
	websocket.send(message);
}

//get doctorID
function getDoctorID() {
	var doctorID;
	$.ajax({
		type: "POST",
		url: "NotificationServlet",
		async: false,
		data: {
			option: "getDoctorID"
		},
		dataType: "text",
		success: function(response) {
			doctorID = response;
		},
		error: function() {
			console.log("notification.js getDoctorID error");
		}
	});
	return doctorID;
}

//取得病患姓名
function getPatientName(patientID) {
	var patientName;
	$.ajax({
		type: "POST",
		url: "NotificationServlet",
		async: false,
		data: {
			option: "getPatientName", 
			patientID: patientID
		},
		dataType: "text",
		success: function(response) {
			patientName = response;
		},
		error: function() {
			console.log("notification.js getPatientName error");
		}
	});
	return patientName;
}

//診間推播
function clinicPush(messageString) {
	var message = JSON.parse(messageString);
	//取得病患姓名
	var patientName = getPatientName(message.patientID);
	//診間推播modal
	$("body").append(
"		<div id='clinicPushModal' class='modal fade' role='dialog'>" + "\n" + 
"			<div class='modal-dialog modal-sm'>" + "\n" + 
"				<div class='modal-content'>" + "\n" + 
"					<div class='modal-header'>" + "\n" + 
"						<button type='button' class='close' data-dismiss='modal' onclick='removeClinicPushModal()'>&times;</button>" + "\n" + 
"						<h4  class='modal-title'>診間推播</h4>" + "\n" + 
"					</div>" + "\n" + 
"					<div class='modal-body'>" + "\n" + 
"						<p>前往&nbsp" + patientName + "&nbsp的病患資訊</p>" + "\n" + 
"					</div>" + "\n" + 
"					<div class='modal-footer'>" + "\n" + 
"						<button type='button' class='btn btn-default' data-dismiss='modal' onclick='goToPatientBasicInformation(\"" + message.patientID + "\")'>確定</button>" + "\n" + 
"						<button type='button' class='btn btn-default' data-dismiss='modal' onclick='removeClinicPushModal()'>取消</button>" + "\n" + 
"					</div>" + "\n" + 
"				</div" + "\n" + 
"			</div>" + "\n" + 
"		</div>" + "\n");
	$("#clinicPushModal").modal("show");
	//存診間推播通知
	$.ajax({
		type: "POST",
		url: "NotificationServlet",
		data: {
			option: "newClinicPush",
			message: messageString
		},
		dataType: "text",
		success: function(response) {
			
		},
		error: function() {
			console.log("notification.js clinicPush error");
		}
	});
}

//移除診間推播modal
function removeClinicPushModal() {
	$("#clinicPushModal").hide();
	setTimeout(function(){ $("#clinicPushModal").remove(); }, 2000);
}

//在診間推播modal點擊確定
function goToPatientBasicInformation(patientID) {
	removeClinicPushModal();
	clickNotification("PatientBasicInformation.html", patientID);
}

//提醒推播
function remindPush(messageString) {
	//存提醒推播通知
	/*$.ajax({
		type: "POST",
		url: "NotificationServlet",
		data: {
			option: "newRemindPush",
			message: messageString
		},
		dataType: "text",
		success: function(response) {
			
		},
		error: function() {
			console.log("notification.js remindPush error");
		}
	});*/
	var message = JSON.parse(messageString);
	//取得病患姓名
	var patientName = getPatientName(message.patientID);
	var options = {
		body: message.body,
		icon: "http://140.121.197.130:8004/BBDPPatient/ProfilePictureServlet?option=getProfilePicture&patientID=" + message.patientID
	}
	var notification = new Notification(message.title, options);
	notification.onclick = function() {
		clickNotification(message.hyperlink, message.patientID);
		notification.close();
	};
}

//點擊通知
function clickNotification(hyperlink, patientID) {
	$.ajax({
		type: "POST",
		url: "NotificationServlet",
		async: false,
		data: {
			option: "clickNotification", 
			patientID: patientID
		},
		dataType: "text",
		success: function(response) {
			location.href = hyperlink;
		},
		error: function() {
			console.log("notification.js clickNotification error");
		}
	});
}

//系統設定裡通知設定
function getNotificationSetting() {
	$.ajax({
		type: "POST",
		url: "NotificationServlet",
		async: false,
		data: {
			option: "getNotificationSetting"
		},
		dataType: "json",
		success: function(response) {
			
		},
		error: function() {
			console.log("notificationSetting.js getNotificationSetting error");
		}
	});
}