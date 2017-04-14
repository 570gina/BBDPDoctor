$.ajax({
	type : "POST",
	//url : "http://localhost:8080/BBDPDoctor/PatientBasicInformationServlet",
	url : "http://140.121.197.130:8000/BBDPDoctor/PatientBasicInformationServlet",
	data : {
		doctorID : localStorage.getItem("login")
	},
	dataType : "json",
	success : function(response) {
		//基本資料
		$("#patientName").html("&nbsp;&nbsp;&nbsp;" + response.patientName);
		$("#patientBirth").html(response.birth);
		$("#patientAge").html(response.age + "歲");
		$("#patientIdentityNumber").html(response.account);
		
		//上次看診病歷摘要
		$("#patientBasicMedicalRecordDate").html("上次看診病歷摘要 (" + response.MRDate + ")");
		$("#patientBasicMedicalRecordContent").html(response.MRContent);
		
		//最近一個月的問卷
		$("#recentQuestionnaireNum").html(response.QNum);
		
		//最近一個月的檔案夾
		$("#recentFolderNum").html(response.FNum);
		
		//最近一個月的健康狀況追蹤
		if(response.healthTracking == "資料空白" || response.healthTracking == "取得資料失敗") $("#healthTrackingBody").html(response.healthTracking);
		else {
			var selectCode = "";
			for(var i=0; i<response.healthTracking.length; i++) {
				selectCode += "<option value='optionValue" + i + "'>" + response.healthTracking[i].HTItemName + "</option>";
			}
			//init c3.js chart data
			var index = 0;
			var column = [];
			var time = ['x'];
			for(var i=0; i<response.healthTracking[index].HTTime.length; i++) {
				time.push(response.healthTracking[index].HTTime[i].substring(0, 16));
			}
			column.push(time);
			for(var i=0; i<response.healthTracking[index].HTDetail.length; i++) {
				var value = [];
				value.push(response.healthTracking[index].HTDetail[i].HTDetailName);
				for(var j=0; j<response.healthTracking[index].HTDetail[i].HTDetailValue.length; j++) {
					value.push(response.healthTracking[index].HTDetail[i].HTDetailValue[j]);
				}
				column.push(value);
			}
			//c3.js chart
			var chart = c3.generate({
				bindto: '#chart',
				data: {
					x: 'x',
					xFormat: '%Y-%m-%d %H:%M',
					columns: column
				},
				axis: {
					x: {
						label: '日期',
						type: 'timeseries',
						tick: {
							format: '%Y-%m-%d %H:%M'
						}
					}
				}
			});
		}
		
		
		$("#recentHealthTrackingName").html(selectCode);
		$("#recentHealthTrackingName").change(function() {
			console.log($('#recentHealthTrackingName').val().substring(11));
			var selectIndex = $('#recentHealthTrackingName').val().substring(11);
			//init c3.js chart data
			var index = selectIndex;
			var column = [];
			var time = ['x'];
			for(var i=0; i<response.healthTracking[index].HTTime.length; i++) {
				time.push(response.healthTracking[index].HTTime[i].substring(0, 16));
			}
			column.push(time);
			for(var i=0; i<response.healthTracking[index].HTDetail.length; i++) {
				var value = [];
				value.push(response.healthTracking[index].HTDetail[i].HTDetailName);
				for(var j=0; j<response.healthTracking[index].HTDetail[i].HTDetailValue.length; j++) {
					value.push(response.healthTracking[index].HTDetail[i].HTDetailValue[j]);
				}
				column.push(value);
			}
			//c3.js chart
			var chart = c3.generate({
				bindto: '#chart',
				data: {
					x: 'x',
					xFormat: '%Y-%m-%d %H:%M',
					columns: column
				},
				axis: {
					x: {
						label: '日期',
						type: 'timeseries',
						tick: {
							format: '%Y-%m-%d %H:%M'
						}
					}
				}
			});
		});
	},
	error : function(xhr, ajaxOptions, thrownError){
		console.log("patientBasicInformation.js error");
		console.log(xhr.status + "\n" + thrownError);
	}
});