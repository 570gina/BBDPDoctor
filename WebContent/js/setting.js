$("#page-wrapper").empty();
$("#page-wrapper").append("<br> " +
		"	<form class='form-horizontal' role='form' style='font-size: large; text-align: center;'> " +
		"		<div class='form-group' style='width: 90vw; height: 10vh;'> " +
		"			<label for='account' class='col-sm-3 control-label' style='float: left;'>帳號</label>  " +
		"			<input type='text' class='form-control' id='account' placeholder='test' disabled style='font-size: large; width: 50%; text-align: center;'>  " +
		"		</div>" +  
					
		"		<div class='form-group' style='width: 90vw; height: 10vh;' >  " +
		"			<label for='hospital' class='col-sm-3 control-label' style='float: left;' >Hospital</label>   " +
		"			<input type='text' class='form-control' id='hospital' placeholder='test' disabled style='font-size: large; width: 50%; text-align: center;'>" +
		"		</div>  " +
		"		<div class='form-group' style='width: 90vw; height: 10vh;' >  " +
		"			<label for='name' class='col-sm-3 control-label' style='float: left;'>Name</label>   " +
		"			<input type='text' class='form-control' id='name' style='font-size: large; width: 50%; text-align: center;'>  " +
		"		</div>  " +
						
		"		<div class='form-group' style='width: 90vw; height: 10vh;'>  " +
		"			<label for='password' class='col-sm-3 control-label' style='float: left;'>密碼</label>   " +
		"			<input type='password' class='form-control' id='password' style='font-size: large; width: 50%; text-align: center;' placeholder='介於6~15字元間'>  " +
		"		</div>  " +
		"		<div class='form-group' style='width: 90vw; height: 10vh;'>  " +
		"			<label for='passwordCheck' class='col-sm-3 control-label' style='float: left;'>確認密碼</label>   " +
		"			<input type='password' class='form-control' id='passwordCheck' style='font-size: large; width: 50%; text-align: center;' placeholder='再一次輸入密碼'>  " +
		"		</div>  " +
		"		<div class='form-group' style='width: 90vw; height: 10vh;'>  " +
		"			<label for='department' class='col-sm-3 control-label' style='float: left;'>科別</label>   " +
		"			<input type='text' class='form-control' id='department' style='font-size: large; width: 50%; text-align: center;'>  " +
		"		</div>  " +
				
		"		<div class='form-group' style='width: 90vw; height: 10vh;'>  " +
		"			<!-- 修改 modal start-->  " +
		"			<a id='modal-395003' href='#modal-container-395003' role='button' class='btn' data-toggle='modal' >  " +
		"					<input type='submit' class='form-control btn btn-default' style='color: white; background-color: #56494e; font-size:large; float:right;'id='change'>  " +
		"			</a>  " +
		"			<div class='modal fade' id='modal-container-395003' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>  " +
		"				<div class='modal-dialog'>  " +
		"					<div class='modal-content'>" +  
		"						<div class='modal-header'>" +  
		"							<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×</button>  " +
		"							<h4 class='modal-title' id='myModalLabel'>修改</h4>  " +
		"						</div>  " +
		"						<div class='modal-body'>  " +
		"							<div id='display'></div> " + 
		"						</div>  " +
		"						<div class='modal-footer'>  " +
		"							<button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>  " +
		"						</div>  " +
		"					</div>  " +
		"				</div>  " +
		"			</div>  " +
		"			<!-- 修改 modal end-->  " +
					
		"		</div>  " +
		"	</form>" );


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
				$("#display").empty();
				$("#display").append("<h3>" + response.show + "</h3>");
				
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

