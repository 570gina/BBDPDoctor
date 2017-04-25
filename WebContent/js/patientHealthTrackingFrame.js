//目前無法使用

//一進來取得所有項目
$(document).ready(function() {
	$("#displayItem").empty();
	var doctorID = window.localStorage.getItem('login');
	
	$.ajax({
		url : "http://localhost:8080/BBDPDoctor/PatientHealthTrackingServlet",
		//url : "http://140.121.197.130:8000/BBDPDoctor/PatientHealthTrackingServlet",
		data : {
			state : "allItem",
			doctorID : doctorID
		},
		dataType : "json",

		success : function(response) {
			for(var number=0; number<response.itemIDList.length; number++){
				var itemName = response.itemNameList[number];
				var itemRecord = response.itemRecordList[number];
				var itemLastTime = response.itemLastTimeList[number];
				var itemID = response.itemIDList[number];
				var url = "EditPatientHealthTracking.html?itemID=" + response.itemIDList[number] ;
				
				var detailName = response.detailNameList[number];
				var detailValue = response.detailValueList[number];
				var detailUnit = response.detailUnitList[number];
				//console.log("value : " + detailValue);
				//console.log("value[0] : " + detailValue[0]);
				//console.log("value.length : " + detailValue.length);
				
				var appendString = 
					"<li><div class='panel panel-default' style='width:24%;float:left;margin-right:10px;'>"+
					"	<div class='panel-body' id='item1' style='background-color:#F7E56E;min-height:110px;max-height:110px;text-overflow:ellipsis;overflow:hidden;font-weight:bold;'>"+
					"		最近一個月的"+itemName+"紀錄 : "+itemRecord+"筆<br>"+
					"		最後一次"+itemName+"紀錄 : "+itemLastTime+"<br>";					
				for(var i=0; i<detailName.length; i++){
					appendString+="		"+detailName[i]+" : "+detailValue[i]+detailUnit[i]+"<br>";
				}	
				appendString+=
					"	</div>"+
					"	<a href='EditPatientHealthTracking.html?itemID="+itemID+"'>"+
					"		<div class='panel-footer' style='background-color: white;border-style:solid;border-color:#F7E56E;'>"+
					"			<span class='pull-left' style='color: black;'>詳細資料</span>"+
					"			<span class='pull-right' style='color: black;'><i class='fa fa-arrow-circle-right'></i></span>"+
					"			<div class='clearfix'></div>"+
					"		</div>"+
					"	</a>"+
					"</div></li>";
				
				$("#displayItem").append(appendString);
				
				console.log("displayItem : "+$("#displayItem").html());
			}	
		},
		error : function() {
			console.log("錯誤訊息");
		}
	});
});		

$(document).ready(function(){
	var w = $("#divWidth").width();
	//console.log("width 寬度 : " + w);
	
	$('.bxslider').bxSlider({
		slideWidth: w,
		minSlides: 3,
		maxSlides: 3,
		slideMargin: 10
	});
});