var user = window.localStorage.getItem('login');
var number;
$(document).ready(function() {
	$.ajax({
		url : "http://140.121.197.130:8000/BBDPDoctor/PatientQuestionnaireServlet",
		data : {
			state : "searchPatientQMType",
			doctorID : user
		},
		dataType : "json",
		
		success : function(response) {
			$("#QType").empty();
			var temp="<option value='all'>問卷分類</option>";
			for(var i=0;i<response.length;i++){
				temp+="<option value='"+response[i]+"'>"+response[i]+"</option>";
			}	
			temp+="</select>";
			$("#QType").append(temp);
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
	
	
	$('#QdateRange').change(function(){
		$.ajax({
			url : "http://140.121.197.130:8000/BBDPDoctor/PatientQuestionnaireServlet",
			data : {
				state : "searchPatientQdateRange",
				edit : $('#QdateRange option:selected').val(),
				doctorID : user
			},
			dataType : "json",
			
			success : function(response) {
				$("#Qdate").empty();
				var temp="<option value='all'>所有時間</option>";
				for(var i=0;i<response.length;i++){
					temp+="<option value='"+response[i]+"'>"+response[i]+"</option>";
				}	
				temp+="</select>";
				$("#Qdate").append(temp);
			},
			error : function() {
				console.log("錯誤訊息");
			}
		});
	});
	
	
	$.ajax({
		url : "http://140.121.197.130:8000/BBDPDoctor/PatientQuestionnaireServlet",
		data : {
			state : "selectPatientQMType",
			doctorID : user
		},
		dataType : "json",
		
		success : function(response) {
			$("#QuestionnaireList").empty();
			var temp="";
			var color;
			for(var i=0;i<response.length;i+=4){
				if(response[i] =="#F8B1B0") color="自評";
				else color="後評";
				temp+="<a href='#' onclick = 'clickQBtn(\""+response[i+3]+"\")'><div id='item' style='background-color:"+response[i]+"'><div class='col-sm-12'><p style='font-weight:bold;font-size:18px;margin-top:10px;margin-bottom:5px;'>"+response[i+1]+"</p><p style='font-size:15px;margin-bottom:5px;'>"+response[i+2]+"<span style='float:right;'>"+color+"</span></p></div></div></a>";
			}
			$("#QuestionnaireList").append(temp);
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
	
});

function changeQuestionnaire(){
	$.ajax({
		url : "http://140.121.197.130:8000/BBDPDoctor/PatientQuestionnaireServlet",
		data : {
			state : "changeQuestionnaire",
			type : $('#QType option:selected').val(),
			dateType : $('#QdateRange option:selected').val(),
			date : $('#Qdate option:selected').val(),
			doctorID : user
		},
		dataType : "json",
		
		success : function(response) {
			$("#QuestionnaireList").empty();
			var temp="";
			var color;
			for(var i=0;i<response.length;i+=4){
				if(response[i] =="#F8B1B0") color="自評";
				else color="後評";
				temp+="<a href='#' onclick = 'clickQBtn(\""+response[i+3]+"\")'><div id='item' style='background-color:"+response[i]+"'><div class='col-sm-12'><p style='font-weight:bold;font-size:18px;margin-top:10px;margin-bottom:5px;'>"+response[i+1]+"</p><p style='font-size:15px;margin-bottom:5px;'>"+response[i+2]+"<span style='float:right;'>"+color+"</span></p></div></div></a>";
			}
			$("#QuestionnaireList").append(temp);
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
}


function clickQBtn(num){
	window.location.href = 'EditPatientQuestionnaire.html?Q='+num;
	
	
}
	
