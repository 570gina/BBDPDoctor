var user = window.localStorage.getItem('login');
var number;
$(document).ready(function() {
	$.ajax({
		url : "http://140.121.197.130:8000/BBDPDoctor/QuestionnaireModuleServlet",
		data : {
			state : "selectQMType",
			selectType : $('#QMType option:selected').val(),
			doctorID : user
		},
		dataType : "json",
		success : function(response) {
			$("#QuestionnaireList").empty();
			var temp="";
			var questionnaireNumber=0;
			for(var i=0;i<response.length;i++){
				questionnaireNumber++;
				temp+="<div><input class='btn btn-default' value='"+response[i]+"' id='Qbtn"+questionnaireNumber+"' type= 'button'style='text-align:left;width:95%;height:70px;background-color:#D2F898;vertical-align:middle;font-size:15px;border:none;text-overflow: ellipsis;overflow : hidden;'onclick='clickQustionnaire("+questionnaireNumber+")'></div><div style='height:7px'></div>";
			}
			$("#QuestionnaireList").append(temp);
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
	$('#QMType').change(function(){
		$.ajax({
			url : "http://140.121.197.130:8000/BBDPDoctor/QuestionnaireModuleServlet",
			data : {
				state : "selectQMType",
				selectType : $('#QMType option:selected').val(),
				doctorID : user
			},
			dataType : "json",
			success : function(response) {
				$("#QuestionnaireList").empty();
				var temp="";
				var questionnaireNumber=0;
				for(var i=0;i<response.length;i++){
					questionnaireNumber++;
					temp+="<div><input class='btn btn-default' value='"+response[i]+"' id='Qbtn"+questionnaireNumber+"' type= 'button'style='text-align:left;width:95%;height:70px;background-color:#D2F898;font-size:15px;border:none;text-overflow: ellipsis;overflow : hidden;'onclick='clickQustionnaire("+questionnaireNumber+")'></div><div style='height:7px'></div>";
				}
				
				$("#QuestionnaireList").append(temp);
			},
			error : function() {
				console.log("錯誤訊息");
			}
		});	
	});
	
	
});
function clickQustionnaire(number){
	var QBtnID = $('#Qbtn'+number).val();
	$.ajax({
		url : "http://140.121.197.130:8000/BBDPDoctor/QuestionnaireModuleServlet",
		data : {
			state : "selectQID",
			selectName : QBtnID,
			doctorID : user
		},

		success : function(response) {
			window.location.href = 'EditQuestionnaireModule.html?Qid='+response;
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});		
}



