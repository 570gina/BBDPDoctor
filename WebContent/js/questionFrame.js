var user = window.localStorage.getItem('login');
var number;
$(document).ready(function() {
	
	$.ajax({
		url : "http://140.121.197.130:8000/BBDPDoctor/QuestionnairePoolServlet",
		data : {
			state : "selectQPType",
			selectType : $('#QPType option:selected').val(),
			doctorID : user
		},
		dataType : "json",
		success : function(response) {
			$("#QuestionList").empty();
			var temp="";
			var questionNumber=0;
			for(var i=0;i<response.length;i++){
				questionNumber++;
				temp+="<div><input type='checkbox' value='"+response[i]+"' name='Qcheckbox' style='zoom: 1.5;vertical-align:middle;'><input class='btn btn-default' value='"+response[i]+"' id='QPbtn"+questionNumber+"' type= 'button'style='text-align:left;width:85%;height:70px;background-color:#D2F898;vertical-align:middle;font-size:15px;border:none;text-overflow: ellipsis;overflow : hidden;'onclick='clickQustion("+questionNumber+")'></div><div style='height:7px'></div>";
			}	
			$("#QuestionList").append(temp);
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
	$('#QPType').change(function(){
		$.ajax({
			url : "http://140.121.197.130:8000/BBDPDoctor/QuestionnairePoolServlet",
			data : {
				state : "selectQPType",
				selectType : $('#QPType option:selected').val(),
				doctorID : user
			},
			dataType : "json",
			success : function(response) {
				$("#QuestionList").empty();
				var temp="";
				var questionNumber=0;
				for(var i=0;i<response.length;i++){
					questionNumber++;
					temp+="<div><input type='checkbox' value='"+response[i]+"' name='Qcheckbox' style='zoom: 1.5;vertical-align:middle;'><input class='btn btn-default' value='"+response[i]+"' id='QPbtn"+questionNumber+"' type= 'button'style='text-align:left;width:85%;height:70px;background-color:#D2F898;vertical-align:middle;font-size:15px;border:none;text-overflow: ellipsis;overflow : hidden;'onclick='clickQustion("+questionNumber+")'></div><div style='height:7px'></div>";
				}	
				$("#QuestionList").append(temp);
			},
			error : function() {
				console.log("錯誤訊息");
			}
		});	
	});
	
	
});
function clickQustion(number){
	var QBtnID = $('#QPbtn'+number).val();
	$.ajax({
		url : "http://140.121.197.130:8000/BBDPDoctor/QuestionnairePoolServlet",
		data : {
			state : "selectQPID",
			selectName : QBtnID,
			doctorID : user
		},

		success : function(response) {
			window.location.href = 'EditQuestionnairePool.html?Qid='+response;
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});		
}

function addTempStorage(){
	var num = 0;
	var questionArray = "";
	$('input:checkbox:checked[name="Qcheckbox"]').each(function(i) { num+=1; questionArray += (this.value+",");});
	if(questionArray=="")
		modalGenerator("提示", "請勾選題目");
	else{
		$.ajax({
			url : "http://140.121.197.130:8000/BBDPDoctor/QuestionnairePoolServlet",
			data : {
				state : "addTempStorage",
				doctorID : user,
				questionArray : questionArray
			},
			traditional: true,
			success : function(response) {
				if(num == response)
					modalGenerator("提示", "成功加入題目暫存區");
				else
					modalGenerator("提示", "勾選的題目已有"+(num-response)+"道在暫存區內，成功再加入"+parseInt(response)+"道題目");
				$('input[name^="Qcheckbox"]').prop('checked', false); 
			},
			error : function() {
				console.log("錯誤訊息");
			}
		});
	}
}



