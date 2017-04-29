$("body").append(
		"	<a id='modal-1' href='#modal-container-1' role='button' class='btn' data-toggle='modal' >"+
		"	</a>");
$(document).ready(function() {
	localStorage.clear();//清除存在web local stroge的login資料，達到登出的效果
	modalGenerator("登出結果", "成功");
	console.log("登出成功");

	// 登出
	$.ajax({
		//url : "http://localhost:8080/BBDPDoctor/LogoutServlet",
		url : "http://140.121.197.130:8000/BBDPDoctor/LogoutServlet",
		data : {},
		dataType : "json",

		success : function(response) {
			if (response == true) {
				setTimeout(function(){
					window.location.href = 'Login.html';
				},1500);
			}
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
});
