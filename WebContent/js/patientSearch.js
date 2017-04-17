$(document).ready(function(){
$("body").append(
	"<div class='modal fade' id='searchModal' tabindex=''-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>" +
		"<div class='modal-dialog modal-sm'>" +
			"<div class='modal-content'>" +
				"<div class='modal-header'>" +
					"<button type='button' class='close' data-dismiss='modal'><span aria-hidden='true'>&times;</span><span class='sr-only'>Close</span>" +
					  "</button>" +
					  "<h4 class='modal-title' id='myModalLabel'>搜尋結果</h4>" +
				 "</div>" +
					"<div class='modal-body'>" +
					"</div>" +
			 "</div>" +
		"</div>" +
	"</div>");
});

var selecrPatientID = [];

$(document).ready(function() {
	//modal出現
	$("#searchModal").on("show.bs.modal", function() {
		$(".modal-body").html("");

		if ($('#patientID').val().length != 5){
			$(".modal-body").html("請輸入病患身分證字號後5碼");
		}
		else{
			$.ajax({
				type: "POST",	//指定http參數傳輸格式
				url: "http://localhost:8080/BBDPDoctor/PatientSearchServlet",
				//url: "http://140.121.197.130:8000/BBDPDoctor/PatientSearchServlet",
				data: {option : "search", account: $('#patientID').val()}, //要傳給目標的data
				dataType: "json",              //目標url處理完後回傳的值之type
						
				success : function(response){
					if(response.length > 0){
						for(var i = 0; i<response.length; i++){	
							selecrPatientID[i] = response[i]["patientID"]
							item = "<a class='list-group-item' onclick='searchConfirm("+i+")'>" + 
			        				"<h4 class='list-group-item-heading'>" + 
			        					response[i]["name"] + 
			        				"</h4>" + 
			        				"<p class='list-group-item-text'>" +
			        					response[i]["account"] + 
			        				"</p>" + 
			        				"</a>";
							$(".modal-body").append(item);
						}
					}
					
					
					$(".modal-footer").html("");

				},

				error : function(){
					//alert("Server沒有回應");
					$(".modal-body").html("查無病患資料");
				}
			}); 
		}
	  	  
	  });
	
});

function searchConfirm(i) {
	//alert(selecrPatientID[i]);
	
	$.ajax({
		type: "POST",	
		url: "http://localhost:8080/BBDPDoctor/PatientSearchServlet",
		//url: "http://140.121.197.130:8000/BBDPDoctor/PatientSearchServlet",
		data: {option : "select", selectPatient : selecrPatientID[i]},
		success : function(response){
			window.location.href='PatientBasicInformation.html';
		},

		error : function(){
			alert("server沒有回應");
		}
	}); 
	
}
