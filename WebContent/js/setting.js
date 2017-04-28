//取得設定資料
$(document).ready(function() {
	$("#account").empty();
	$("#name").empty();
	$("#hospital").empty();
	$("#password").empty();
	$("#passwordCheck").empty();
	$("#department").empty();
	//一進來先得到存在local storage的doctorID值並顯示出來
	var doctorID = window.localStorage.getItem('login');
	
	//$("#account").append(window.localStorage.getItem('login'));
	//把local storage的account值設定給那個account欄位
	//$("#account").val(window.localStorage.getItem('login'));
	
	$.ajax({
		//url : "http://localhost:8080/BBDPDoctor/AccountSettingServlet",
		url : "http://140.121.197.130:8000/BBDPDoctor/AccountSettingServlet",
		data : {
			state : "Default",
			doctorID : doctorID
		},
		dataType : "json",

		success : function(response) {
			$("#account").val(response.account);
			$("#name").val(response.name);
			$("#hospital").val(response.hospital);
			$("#password").val(response.password);
			$("#passwordCheck").val(response.passwordCheck);
			$("#department").val(response.department);
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
});

//修改設定資料
$(document).ready(function() {
	$("#change").click(function() {
		//一進來先得到存在local storage的doctorID值並顯示出來
		var doctorID = window.localStorage.getItem('login');
		
		$.ajax({
			//url : "http://localhost:8080/BBDPDoctor/AccountSettingServlet",
			url : "http://140.121.197.130:8000/BBDPDoctor/AccountSettingServlet",
			data : {
				state : "Change",
				doctorID : doctorID,
				account : $("#account").val(),
				name : $("#name").val(),
				hospital : $("#hospital").val(),
				password : $("#password").val(),
				passwordCheck : $("#passwordCheck").val(),
				department : $("#department").val()
			},
			dataType : "json",

			success : function(response) {
				//$("#display").empty();
				//$("#display").append("<h3>" + response.show + "</h3>");
				modalGenerator("帳戶設定修改", response.show);	//模組產生器
				
				$("#name").empty();
				$("#hospital").empty();
				$("#password").empty();
				$("#passwordCheck").empty();
				$("#department").empty();
				
				$("#name").val(response.name);
				$("#hospital").val(response.hospital);
				$("#password").val(response.password);
				$("#passwordCheck").val(response.passwordCheck);
				$("#department").val(response.department);
			},
			error : function() {
				console.log("錯誤訊息");
			}
		});
	});
});

