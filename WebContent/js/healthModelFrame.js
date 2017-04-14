		//一進來取得所有項目//取得下拉選單的值//取得div的值
		$(document).ready(function() {
			//console.log("hello1" + $("#healthType1").val());
			//console.log("hello1" + $("#healthType1 :selected").text());
			//取得存在local storage的doctoeID值並顯示出來
			var doctorID = window.localStorage.getItem('login');
			$("#itemDiv").empty();
			
			$.ajax({
				//url : "http://localhost:8080/BBDPDoctor/HealthTrackingServlet",
				url : "http://140.121.197.130:8000/BBDPDoctor/HealthTrackingServlet",
				data : {
					state : "allItem",
					doctorID : doctorID
				},
				dataType : "json",
	
				success : function(response) {
					//取得下拉選單的值
					for(var number = 0; number < response.typeList.length; number++){
						var itemType = response.typeList[number];	//項目類別名稱
						$("#healthType1").append("<option value='op1'>"+itemType+"</option>");
					}
					//取得div的值
					for(number = 0; number < response.itemIDList.length; number++){
						var itemNumber = "item" + response.itemIDList[number];	//項目數字
						var itemName = response.nameList[number];				//項目名稱
						$("#itemDiv").append("<div style='text-align:left;width:100%;height:70px;line-height:70px;background-color:#F7E56E;vertical-align:middle;font-size:15px;border:none;text-overflow: ellipsis; margin-bottom:10px;'"+
							" id='"+itemNumber+"' onclick=itemSelect('"+itemNumber+"')>&nbsp;&nbsp;"+itemName+"</div>");
					}
					
				},
				error : function() {
					console.log("錯誤訊息");
				}
			});
		});

		//選取分類後的項目
		function change1(index){
			//取得存在local storage的doctoeID值並顯示出來
			var doctorID = window.localStorage.getItem('login');
			
			$("#itemDiv").empty();
			var select = $("#healthType1 :selected").text();
			console.log("我選了 : " + select);
			
			//如果選回健康狀況追蹤分類的話，顯示全部的
			if(select=="健康狀況追蹤分類"){
				$.ajax({
					//url : "http://localhost:8080/BBDPDoctor/HealthTrackingServlet",
					url : "http://140.121.197.130:8000/BBDPDoctor/HealthTrackingServlet",
					data : {
						state : "allItem",
						doctorID : doctorID
					},
					dataType : "json",
		
					success : function(response) {
						//取得div的值
						for(number = 0; number < response.itemIDList.length; number++){
							var itemNumber = "item" + response.itemIDList[number];	//項目數字
							var itemName = response.nameList[number];				//項目名稱
							$("#itemDiv").append("<div style='text-align:left;width:100%;height:70px;line-height:70px;background-color:#F7E56E;vertical-align:middle;font-size:15px;border:none;text-overflow: ellipsis; margin-bottom:10px;'"+
									" id='"+itemNumber+"' onclick=itemSelect('"+itemNumber+"')>&nbsp;&nbsp;"+itemName+"</div>");
						}
						
					},
					error : function() {
						console.log("錯誤訊息");
					}
				});
			}
			//選什麼分類，顯示該分類的項目
			else{
				$.ajax({
					//url : "http://localhost:8080/BBDPDoctor/HealthTrackingServlet",
					url : "http://140.121.197.130:8000/BBDPDoctor/HealthTrackingServlet",
					data : {
						state : "typeSelect",
						doctorID : doctorID,
						select: select
					},
					dataType : "json",
		
					success : function(response) {
						//選取分類後取得div的值
						for(number = 0; number < response.itemIDList.length; number++){
							var itemNumber = "item" + response.itemIDList[number];	//項目數字
							var itemName = response.nameList[number];				//項目名稱
							$("#itemDiv").append("<div style='text-align:left;width:100%;height:70px;line-height:70px;background-color:#F7E56E;vertical-align:middle;font-size:15px;border:none;text-overflow: ellipsis; margin-bottom:10px;'"+
									" id='"+itemNumber+"' onclick=itemSelect('"+itemNumber+"')>&nbsp;&nbsp;"+itemName+"</div>");
						}
					},
					error : function() {
						console.log("錯誤訊息");
					}
				});
			}	
		}
		
		//select後跳掉修改頁面
		function itemSelect(itemNumber){
			//console.log("有近來測試的function李 : " + itemNumber);
			window.location.href = 'EditHealthTracking.html?itemID='+ itemNumber;
		}