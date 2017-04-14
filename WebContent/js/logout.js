
$(document).ready(function() {
	localStorage.clear();//清除存在web local stroge的login資料，達到登出的效果
	alert("登出成功");

	// 登出
	$.ajax({
		//url : "http://localhost:8080/BBDPDoctor/LogoutServlet",
		url : "http://140.121.197.130:8000/BBDPDoctor/LogoutServlet",
		data : {},
		dataType : "json",

		success : function(response) {
			if (response == true) {
				window.location.href = 'Login.html';
			}
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
});
