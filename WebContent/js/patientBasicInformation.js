$.ajax({
	type : "POST",
	//url : "http://localhost:8080/BBDPDoctor/PatientBasicInformationServlet",
	url : "http://140.121.197.130:8000/BBDPDoctor/PatientBasicInformationServlet",
	data : {
		doctorID : localStorage.getItem("login")
	},
	dataType : "json",
	success : function(response) {
		/*$.getScript("js/photoTemp.js");
		//大頭貼!!
		$("#patientImg").attr("src", "picture/patientPhoto/" + setTempPhotoByName(response.patientName) + ".png");*/
		var result;
		switch(response.patientName) {
			case "王小明": {
				result = "A123456789";
				break;
			}
			case "王小弟": {
				result = "B112345678";
				break;
			}
			case "李志偉": {
				result = "C123456789";
				break;
			}
			case "陳志明": {
				result = "D112345678";
				break;
			}
			case "沈建宏": {
				result = "E101234567";
				break;
			}
			case "楊美玲": {
				result = "F223456789";
				break;
			}
			case "莊雅婷": {
				result = "G212345678";
				break;
			}
			case "黃淑娟": {
				result = "H223456789";
				break;
			}
			case "謝淑芬": {
				result = "I212345678";
				break;
			}
			case "陳雅惠": {
				result = "J201234567";
				break;
			}
			default: {
				result = "default";
				break;
			}
		}
		$("#patientImg").attr("src", "picture/patientPhoto/" + result + ".png");
		$("#patientImg").attr("style", "width: 50px; height: 50px; display:inline;");
		
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