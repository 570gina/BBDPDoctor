var user = window.localStorage.getItem('login');
$(document).ready(function() {
	
	$.ajax({
		url : "http://140.121.197.130:8000/BBDPDoctor/PatientMedicalRecordServlet",
		data : {
			state : "searchPatientMedicalRecord",
			doctorID : user
		},
		dataType : "json",
		
		success : function(response) {
			$("#MedicalRecordList").empty();
			var temp="";
			for(var i=0;i<response.length;i++){
				var tmp = response[i].split(".");
				temp+="<a href='#' onclick = 'clickMRbtn(\""+response[i]+"\")'><div id='item' style='background-color:#A3E7FC'><div class='col-sm-12'><p style='font-weight:bold;font-size:20px;margin-top:10px;margin-bottom:5px;'>"+tmp[0]+"</p></div></div></a>";
			}
			$("#MedicalRecordList").append(temp);
		},
		error : function() {

			console.log("錯誤訊息");
		}
	});
	
	
	$('#MRdateRange').change(function(){
		$.ajax({
			url : "http://140.121.197.130:8000/BBDPDoctor/PatientMedicalRecordServlet",
			data : {
				state : "searchPatientMRdateRange",
				edit : $('#MRdateRange option:selected').val(),
				doctorID : user
			},
			dataType : "json",
			
			success : function(response) {
				$("#MRdate").empty();
				var temp="<option value='all'>所有時間</option>";
				for(var i=0;i<response.length;i++){
					temp+="<option value='"+response[i]+"'>"+response[i]+"</option>";
				}	
				temp+="</select>";
				$("#MRdate").append(temp);
			},
			error : function() {
				console.log("錯誤訊息");
			}
		});
	});
	
});

function changeMR(){
	$.ajax({
		url : "http://140.121.197.130:8000/BBDPDoctor/PatientMedicalRecordServlet",
		data : {
			state : "changeMedicalRecord",
			dateType : $('#MRdateRange option:selected').val(),
			date : $('#MRdate option:selected').val(),
			doctorID : user
		},
		dataType : "json",
		
		success : function(response) {
			$("#MedicalRecordList").empty();
			var temp="";
			for(var i=0;i<response.length;i++){
				var tmp = response[i].split(".");
				temp+="<a href='#' onclick = 'clickMRbtn(\""+response[i]+"\")'><div id='item' style='background-color:#A3E7FC'><div class='col-sm-12'><p style='font-weight:bold;font-size:20px;margin-top:10px;margin-bottom:5px;'>"+tmp[0]+"</p></div></div></a>";
			}
			$("#MedicalRecordList").append(temp);
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
}


function clickMRbtn(num){
	window.location.href = 'EditPatientMedicalRecord.html?MR='+num;
	
}
	
