package bbdp.doctor.model;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.HealthTrackingServer.ItemDetail;
import bbdp.push.fcm.PushToFCM;

public class PatientHealthTrackingServer {

	//一進來取得所有項目//PatientHealthTracking.html//已關資料庫
	public HashMap allItemDefault(DBConnection conn, String doctorID, String patientID) {
		Gson gson = new Gson();
		HashMap allItem = new HashMap();
		try {
			ResultSet rs = conn.runSql("SELECT healthTrackingID, itemID, name, detail FROM healthtracking NATURAL JOIN healthtrackingitem WHERE patientID = '"
							+ patientID + "' and doctorID = '" + doctorID + "' ORDER BY itemID ASC");
			System.out.println("Listener runSql 成功");

			//String healthTrackingID, itemID, name, detail;
			String itemID, time = null, value, name;

			ArrayList itemIDList = new ArrayList(); 				// itemID List
			ArrayList itemNameList = new ArrayList(); 				// item name List
			ArrayList itemRecordList = new ArrayList(); 			// item record number List
			int record = 0;
			ArrayList itemLastTimeList = new ArrayList(); 			// item last time record List
			ArrayList itemLastValueList = new ArrayList(); 			// item last value record List
			
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}	//關閉ResultSet
			//////////////////////////取得item id、name、lastValue、lastTime(開始)////////////////////////////////////////////////////
			rs = conn.runSql("SELECT itemID, time, value, name FROM healthtracking NATURAL JOIN healthtrackingitem NATURAL JOIN history WHERE patientID = '"
							+ patientID + "' and doctorID = '" + doctorID + "'ORDER BY healthTrackingID ASC");
			while (rs.next()) {
				itemID = rs.getString("itemID");
				time = rs.getString("time");
				value = rs.getString("value");
				name = rs.getString("name");
				time = time.substring(0, 16);
				
				//近一個月的history紀錄
				if (isRecent(time.substring(0, 10))) {
					//System.out.println("\n///////////////////////itemID : " + itemID);
					//System.out.println("///////////////////////time : " + time);
					//System.out.println("//////////////////////value : " + value);
					//System.out.println("//////////////////////name : " + name);

					if (itemIDList.size() == 0) { // 都還沒有項目
						itemRecordList.add(1);
						itemIDList.add(itemID);
						itemLastTimeList.add(time);
						itemLastValueList.add(value);
						itemNameList.add(name);
						//System.out.println("都還沒有項目 itemRecordList : " + itemRecordList);
						//System.out.println("itemIDList : " + itemIDList);
						//System.out.println("itemLastTimeList : " + itemLastTimeList);
						//System.out.println("itemLastValueList : " + itemLastValueList);
						//System.out.println("itemNameList : " + itemNameList);
					} else { // 有項目了
						for (int i = 0; i < itemIDList.size(); i++) {
							if (itemIDList.get(i).equals(itemID)) { // 判斷是不是已有這個項目在陣列裡//是的話項目number加加
								record = (int) itemRecordList.get(i);
								record += 1;
								//System.out.println("record : " + record);
								itemRecordList.set(i, record);	//修改紀錄的資料總數+1
								
								if(isDateBigger(time, (String)itemLastTimeList.get(i))){	//如果新的時間比較晚，就紀錄較晚的時間
									itemLastTimeList.set(i, time);
									itemLastValueList.set(i, value);
								}
								//System.out.println("判斷是不是已有這個項目在陣列裡//是的話項目number加加 itemRecordList : " + itemRecordList);
								//System.out.println("itemIDList : " + itemIDList);
								//System.out.println("itemLastTimeList : " + itemLastTimeList);
								//System.out.println("itemLastValueList : " + itemLastValueList);
								break;
							}
							if (i == itemIDList.size() - 1) { // 判斷是不是已有這個項目在陣列裡//不是的話新增項目進陣列
								itemRecordList.add(1);
								itemIDList.add(itemID);
								itemLastTimeList.add(time);
								itemLastValueList.add(value);
								itemNameList.add(name);
								//System.out.println("判斷是不是已有這個項目在陣列裡//不是的話新增項目進陣列 itemRecordList : " + itemRecordList);
								//System.out.println("itemIDList : " + itemIDList);
								//System.out.println("itemLastTimeList : " + itemLastTimeList);
								//System.out.println("itemLastValueList : " + itemLastValueList);
								//System.out.println("itemNameList : " + itemNameList);
								break;
							}
						}
					}
				}
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}	//關閉ResultSet
			//////////////////////////取得item id、name、lastValue、lastTime(結束)////////////////////////////////////////////////////
			///////////////////////////解析history last value json(開始)////////////////////////////////////////////////////
			ArrayList detailIDList = new ArrayList();
			ArrayList detailValueList = new ArrayList();
			
			for(int i = 0; i < itemLastValueList.size(); i++){
				ArrayList tempDetailIDList = new ArrayList();		//必須再回圈內重新宣告，不然存進去地值會被刷掉
				ArrayList tempDetailValueList = new ArrayList();	//必須再回圈內重新宣告，不然存進去地值會被刷掉
				
				HistoryValue historyValue = gson.fromJson((String) itemLastValueList.get(i), HistoryValue.class);
				//System.out.println(i + "historyValue itemID : " + historyValue.itemID);
				List<Detail> tempList = historyValue.detail;
				for(int k = 0; k < tempList.size(); k++){
					tempDetailIDList.add(tempList.get(k).detailID);			//取得解析後的detailID
					tempDetailValueList.add(tempList.get(k).detailValue);	//取得解析後的detailValue
				}
				//System.out.println("historyValue detailID detailIDList before: " + detailIDList);
				//System.out.println("historyValue detailValue detailValueList before: " + detailValueList);
				detailIDList.add(tempDetailIDList);			//放到陣列裡
				detailValueList.add(tempDetailValueList);	//放到陣列裡
				//System.out.println("historyValue detailID tempDetailIDList : " + tempDetailIDList);
				//System.out.println("historyValue detailValue tempDetailValueList: " + tempDetailValueList);
				//System.out.println("historyValue detailID detailIDList: " + detailIDList);
				//System.out.println("historyValue detailValue detailValueList: " + detailValueList);
				//tempDetailIDList.clear();		//清空暫存
				//tempDetailValueList.clear();	//清空暫存
			}
			//System.out.println("historyValue detailID detailIDList: " + detailIDList);
			//System.out.println("historyValue detailValue detailValueList: " + detailValueList);
			///////////////////////////解析history last value json(結束)////////////////////////////////////////////////////
			///////////////////////////透過detail id取得detail name 跟 unit(開始)////////////////////////////////////////////////////
			String detailID;
			ArrayList tempList1 = new ArrayList();
			ArrayList detailNameList = new ArrayList();
			ArrayList detailUnitList = new ArrayList();
			
			for(int i = 0; i<detailIDList.size(); i++){
				tempList1 = (ArrayList) detailIDList.get(i);// 取得dtail id
				ArrayList tempList2 = new ArrayList();		// detail name的暫存區//必須再回圈內重新宣告，不然存進去地值會被刷掉
				ArrayList tempList3 = new ArrayList();		// detail unit的暫存區//必須再回圈內重新宣告，不然存進去地值會被刷掉
				
				for(int k = 0; k < tempList1.size(); k++){
					detailID = tempList1.get(k).toString();		
					//System.out.println("detailID : " + detailID);
					rs = conn.runSql("SELECT detailID, name, unit FROM healthtrackingdetail WHERE detailID = '" + detailID + "' ");
					while (rs.next()) {
						tempList2.add(rs.getString("name"));
						tempList3.add(rs.getString("unit"));
					}
				}
				detailNameList.add(tempList2);
				detailUnitList.add(tempList3);
			}
			//System.out.println("detailIDList: " + detailIDList);
			//System.out.println("detailNameList : " + detailNameList);
			//System.out.println("detailUnitList : " + detailUnitList);
			///////////////////////////透過detail id取得detail name 跟 unit(結束)////////////////////////////////////////////////////
			///////////////////////////如果病患沒有紀錄的話 放空值進去(開始)////////////////////////////////////////////////////
			boolean judge;
			rs = conn.runSql("SELECT itemID, name FROM healthtracking NATURAL JOIN healthtrackingitem WHERE patientID = '"
					+ patientID + "' and doctorID = '" + doctorID + "'ORDER BY healthTrackingID ASC");
			while (rs.next()) {
				judge = false;
				itemID = rs.getString("itemID");
				name = rs.getString("name");
				//System.out.println("itemID : "+itemID);
				for(int i = 0; i < itemIDList.size(); i++){	//從有紀錄的項目陣列尋找
					if(itemID.equals(itemIDList.get(i))){	//醫生有給病患此項目，病患有新增過紀錄
						//System.out.println("itemID "+itemIDList.get(i)+" 有");
						judge = true;
						break;
					}
				}
				if(judge == false){							//醫生有給病患此項目，但病患並沒有新增任何紀錄
					//System.out.println("itemID : "+itemID + " 沒有喔喔喔喔");	
					
					itemIDList.add(itemID);
					itemNameList.add(name);
					itemRecordList.add(0);
					itemLastTimeList.add("...");
					detailValueList.add("");
					detailIDList.add("");
					detailNameList.add("");
					detailUnitList.add("");
				}
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}	//關閉ResultSet
			///////////////////////////如果病患沒有紀錄的話 放空值進去(結束)////////////////////////////////////////////////////
			
			allItem.put("itemIDList", itemIDList);
			allItem.put("itemNameList", itemNameList);
			allItem.put("itemRecordList", itemRecordList);
			allItem.put("itemLastTimeList", itemLastTimeList);
			allItem.put("detailValueList", detailValueList);
			allItem.put("detailIDList", detailIDList);
			allItem.put("detailNameList", detailNameList);
			allItem.put("detailUnitList", detailUnitList);

			System.out.println("Server allItemDefault : " + allItem);

		} catch (SQLException e) {
			System.out.println("PatientHealthTrackingServer allItemDefault Exception :" + e.toString());
		}
		return allItem;
	}

	// 判斷日期是否是最近一個月
	private static boolean isRecent(String inputDate) {
		int year = Integer.valueOf(inputDate.substring(0, 4));
		int month = Integer.valueOf(inputDate.substring(5, 7));
		int date = Integer.valueOf(inputDate.substring(8));
		LocalDate birthday = LocalDate.of(year, month, date);
		LocalDate today = LocalDate.now();
		Period period = Period.between(birthday, today);
		if (period.getMonths() == 0)
			return true;
		return false;
	}

	// 比較日期大小
	private static boolean isDateBigger(String Date1, String Date2) {
		if (Date1.compareTo(Date2) > 0) { // Date1比較大
			return true;
		}
		return false; // Date1比較小
	}

	// 取得紀錄:細項
	class ItemDetail {
		List<String> detailID;

		ItemDetail(List<String> detailID) {
			this.detailID = detailID;
		}
	}

	// 取得紀錄:項目id與細項
	class HistoryValue {
		String itemID;
		List<Detail> detail;

		HistoryValue(String itemID, List<Detail> detail) {
			this.itemID = itemID;
			this.detail = detail;
		}
	}

	// 取得紀錄:細項id跟value
	public class Detail {
		String detailID;
		String detailValue;

		Detail(String detailID, String detailValue) {
			this.detailID = detailID;
			this.detailValue = detailValue;
		}
	}

	//取得分類下拉選單的值//NewPatientHealthTracking.html//已關資料庫
	public HashMap allTypeDefault(DBConnection conn, String doctorID, String patientID){
		HashMap allType = new HashMap();
		try {
			ResultSet rs = conn.runSql("select distinct type, doctorID from healthtrackingitem where doctorID = '"+doctorID+"' ORDER BY itemID DESC");
			System.out.println("Listener runSql 成功");

			ArrayList typeList = new ArrayList(); // item type List

			while (rs.next()) {
				typeList.add(rs.getString("type")); // 取得使用者的項目type
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}	//關閉ResultSet
			// 取得所有項目的name
			rs = conn.runSql("select itemID, name from healthtrackingitem where doctorID = '"+doctorID+"' ORDER BY itemID DESC");
			System.out.println("Listener runSql 成功");
			
			ArrayList itemIDList = new ArrayList(); // item itemID List
			ArrayList nameList = new ArrayList(); // item name List
			
			while (rs.next()) {
					nameList.add(rs.getString("name")); // 取得使用者的項目name
					itemIDList.add(rs.getString("itemID"));	// 取得使用者的項目itemID	
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}	//關閉ResultSet
			
			allType.put("typeList", typeList);
			allType.put("itemIDList", itemIDList);
			allType.put("nameList", nameList);

			System.out.println("Server allTypeDefault : " + allType);

		} catch (SQLException e) {
			System.out.println("PatientHealthTrackingServer allTypeDefault Exception :" + e.toString());
		}
		return allType;
		
	}
	
	//選什麼分類，得到該分類的項目//NewPatientHealthTracking.html//已關資料庫
	public HashMap typeSelectItem(DBConnection conn, String doctorID, String patientID, String select){
		HashMap typeSelectItem = new HashMap();
		try {
			ResultSet rs = conn.runSql("select name, itemID from healthtrackingitem where doctorID='" + doctorID
					+ "' and type='" + select + "' ORDER BY itemID DESC");
			System.out.println("Listener runSql 成功");

			ArrayList nameList = new ArrayList(); // item name List
			ArrayList itemIDList = new ArrayList(); // item itemID List

			while (rs.next()) {
				nameList.add(rs.getString("name")); // 取得項目名稱
				itemIDList.add(rs.getString("itemID")); // 取得項目id
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}	//關閉ResultSet

			typeSelectItem.put("itemIDList", itemIDList);
			typeSelectItem.put("nameList", nameList);
			System.out.println("Server typeSelectItem : " + typeSelectItem);

		} catch (SQLException e) {
			System.out.println("PatientHealthTrackingServer typeSelectItem Exception :" + e.toString());
		}
		return typeSelectItem;
	}
	
	//按下後新增項目給病患//NewPatientHealthTracking.html//已關資料庫
	public HashMap addItemToPatient(DBConnection conn, String doctorID, String patientID, String itemSelect){
		HashMap addItemToPatient = new HashMap();
		String result = "請重新嘗試";
		try {
			itemSelect = (String) itemSelect.subSequence(4, itemSelect.length());	//取得選擇的項目id
			//////////////////////////////確認並沒有新增給病患過(開始)///////////////////////////////////////
			ResultSet rs = conn.runSql("select patientID, itemID from healthtracking Where patientID='"+patientID+"' ORDER BY healthTrackingID DESC");
			System.out.println("Listener runSql 成功");
			
			boolean isUsed = false;	//判斷是否新增過
			LocalDate today = LocalDate.now();
			System.out.println("現在時間 : " + today);
			
			while (rs.next()) {
				if(rs.getString("itemID").equals(itemSelect)){
					isUsed = true;
					result = "已新增過此模板給病患!";
				}
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}	//關閉ResultSet
			//////////////////////////////確認並沒有新增給病患過(結束)///////////////////////////////////////
			//////////////////////////////確認並未新增給病患過後開始新增，並去item那裏把used改成1，表示已經使用過(開始)///////////////////////////////////////
			if(!isUsed){	
				//新增給病患
				String insertSQL = "insert into healthtracking(healthTrackingID, patientID, itemID, next)"
						+ "select ifNULL(max(healthTrackingID+0),0)+1, '"+patientID+"', '"+itemSelect+"', '"+today+"' From healthtracking";
				int insert = conn.updateSql(insertSQL);
				System.out.println("Listener insertHealthtracking : " + insert);		
				result = "新增成功!";
				//把項目used改為1
				String updateSQL = "update healthtrackingitem SET uesd = 1 WHERE itemID = '"+itemSelect+"' ";
				int update = conn.updateSql(updateSQL);
				System.out.println("Listener updateUsed : " + update);	
				
				//推播給病患
				PushToFCM.sendNotification("健康追蹤填寫提醒", "請記得按時填寫", patientID);
			}
			//////////////////////////////確認並未新增給病患過後開始新增，並去item那裏把used改成1，表示已經使用過(結束)///////////////////////////////////////
		} catch (SQLException e) {
			System.out.println("PatientHealthTrackingServer addItemToPatient Exception :" + e.toString());
		}
		addItemToPatient.put("result", result);
		return addItemToPatient;
	}
	
	//取得現在的時間
	public String getNowDate(){
		//取得現在時間
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = simpleDateFormat.format(timestamp);
		//System.out.println("currentTime : " + currentTime);
		return currentTime;
	}
	
	//取得該項目一些基本資料//EditPatientHealthTracking.html//已關資料庫
	public HashMap itemAllDetail(DBConnection conn, String doctorID, String patientID, String itemID){
		Gson gson = new Gson();
		HashMap itemAllDetail = new HashMap();
		try {
			ResultSet rs;

			String time, value, name = null;

			ArrayList itemTimeList = new ArrayList(); 			// item time record List
			ArrayList itemValueList = new ArrayList(); 			// item value record List
			//////////////////////////取得item name(開始)////////////////////////////////////////////////////
			rs = conn.runSql("SELECT name FROM healthtrackingitem WHERE doctorID = '" + doctorID + "' and itemID = '"+itemID+"' ");
			System.out.println("Listener runSql 成功");
			while (rs.next()) {
				name = rs.getString("name");
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}
			//////////////////////////取得item Value、Time(開始)////////////////////////////////////////////////////
			rs = conn.runSql("SELECT time, value FROM healthtracking NATURAL JOIN healthtrackingitem NATURAL JOIN history WHERE patientID = '"
							+ patientID + "' and doctorID = '" + doctorID + "' and itemID = '"+itemID+"' ORDER BY time ASC");
			System.out.println("Listener runSql 成功");
			while (rs.next()) {
				time = rs.getString("time");
				value = rs.getString("value");
				time = time.substring(0, 16);
				
				itemTimeList.add(time);
				itemValueList.add(value);
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}
			//////////////////////////取得item Value、Time(結束)////////////////////////////////////////////////////
			//////////////////////////解析history value json(開始)////////////////////////////////////////////////////
			//ArrayList detailIDList = new ArrayList();
			ArrayList detailValueList = new ArrayList();
			
			for(int i = 0; i < itemValueList.size(); i++){
				//ArrayList tempDetailIDList = new ArrayList();		//必須再回圈內重新宣告，不然存進去地值會被刷掉
				ArrayList tempDetailValueList = new ArrayList();	//必須再回圈內重新宣告，不然存進去地值會被刷掉
				
				HistoryValue historyValue = gson.fromJson((String) itemValueList.get(i), HistoryValue.class);
				List<Detail> tempList = historyValue.detail;
				for(int k = 0; k < tempList.size(); k++){
					//tempDetailIDList.add(tempList.get(k).detailID);			//取得解析後的detailID
					tempDetailValueList.add(tempList.get(k).detailValue);	//取得解析後的detailValue
				}
				//detailIDList.add(tempDetailIDList);			//放到陣列裡
				detailValueList.add(tempDetailValueList);	//放到陣列裡
			}
			///////////////////////////解析history value json(結束)////////////////////////////////////////////////////
			///////////////////////////透過item id取得detail name 跟 id(開始)////////////////////////////////////////////////////
			//取得item
			rs = conn.runSql("select detail from healthtrackingitem where itemID = '"+itemID+"' and doctorID = '" + doctorID + "' ");
			System.out.println("Listener runSql 成功");
			
			String detail = null;
			
			while (rs.next()) {
				detail = rs.getString("detail"); 	// 取得項目detail	
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}
			//取得detail
			ItemDetail detailClass = gson.fromJson(detail, ItemDetail.class);
			String detailID;
			ArrayList detailIDList = new ArrayList();
			ArrayList detailNameList = new ArrayList();
			
			for(int i = 0; i< detailClass.detailID.size(); i++){
				detailID = detailClass.detailID.get(i);
				rs = conn.runSql("select detailID, name from healthtrackingdetail where detailID = '"+detailID+"'");
				while (rs.next()) {
					detailIDList.add(rs.getString("detailID"));				// 取得細項detailID
					detailNameList.add(rs.getString("name")); 				// 取得細項name
		
				}
			}
			///////////////////////////透過detail id取得detail name 跟 id(結束)////////////////////////////////////////////////////
			
			itemAllDetail.put("itemName", name);
			itemAllDetail.put("itemTimeList", itemTimeList);
			//itemAllDetail.put("itemValueList", itemValueList);
			itemAllDetail.put("detailIDList", detailIDList);
			itemAllDetail.put("detailValueList", detailValueList);
			itemAllDetail.put("detailNameList", detailNameList);

			System.out.println("Server itemAllDetail : " + itemAllDetail);

		} catch (SQLException e) {
			System.out.println("PatientHealthTrackingServer itemAllDetail Exception :" + e.toString());
		}
		return itemAllDetail;
	}
	
	//改變圖表//EditPatientHealthTracking.html//已關資料庫
	public HashMap changeChart(DBConnection conn, String doctorID, String patientID, String itemID, String dateStart, String dateEnd, String[] checkArray){
		HashMap changeChart = new HashMap();
		Gson gson = new Gson();

		System.out.println("checkArray: "+ gson.toJson(checkArray));
		
		try {
			ResultSet rs;

			String time, value;
			boolean dateIsBetween;		//判斷時間是否介於開始日期及結束日期之間

			ArrayList itemTimeList = new ArrayList(); 			// item time record List
			ArrayList itemValueList = new ArrayList(); 			// item value record List

			//////////////////////////取得item Value、Time(開始)////////////////////////////////////////////////////
			rs = conn.runSql("SELECT time, value FROM healthtracking NATURAL JOIN healthtrackingitem NATURAL JOIN history WHERE patientID = '"
							+ patientID + "' and doctorID = '" + doctorID + "' and itemID = '"+itemID+"' ORDER BY time ASC");
			System.out.println("Listener runSql 成功");
			while (rs.next()) {
				time = rs.getString("time");
				value = rs.getString("value");
				time = time.substring(0, 16);
				
				if(betweenDate(dateStart, dateEnd, time.substring(0, 10))){			//如果介於開始與結束日期之間
					itemTimeList.add(time);
					itemValueList.add(value);
					//System.out.println("betweenDate : " + time.substring(0, 10));
				}
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}
			//////////////////////////取得item Value、Time(結束)////////////////////////////////////////////////////
			//////////////////////////解析history value json(開始)////////////////////////////////////////////////////
			//ArrayList detailIDList = new ArrayList();
			ArrayList detailValueList = new ArrayList();
			
			for(int i = 0; i < itemValueList.size(); i++){
				ArrayList tempDetailValueList = new ArrayList();	//必須再回圈內重新宣告，不然存進去地值會被刷掉
				
				HistoryValue historyValue = gson.fromJson((String) itemValueList.get(i), HistoryValue.class);
				List<Detail> tempList = historyValue.detail;
				for(int k = 0; k < tempList.size(); k++){
					tempDetailValueList.add(tempList.get(k).detailValue);	//取得解析後的detailValue
				}
				detailValueList.add(tempDetailValueList);	//放到陣列裡
			}
			///////////////////////////解析history value json(結束)////////////////////////////////////////////////////
			///////////////////////////透過item id取得detail name 跟 id(開始)////////////////////////////////////////////////////
			//取得item
			rs = conn.runSql("select detail from healthtrackingitem where itemID = '"+itemID+"' and doctorID = '" + doctorID + "' ");
			System.out.println("Listener runSql 成功");
			
			String detail = null;
			
			while (rs.next()) {
				detail = rs.getString("detail"); 	// 取得項目detail	
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}
			//取得detail
			ItemDetail detailClass = gson.fromJson(detail, ItemDetail.class);
			String detailID;
			ArrayList detailIDList = new ArrayList();
			ArrayList detailNameList = new ArrayList();
			
			for(int i = 0; i< detailClass.detailID.size(); i++){
				detailID = detailClass.detailID.get(i);
				rs = conn.runSql("select detailID, name from healthtrackingdetail where detailID = '"+detailID+"'");
				while (rs.next()) {
					detailIDList.add(rs.getString("detailID"));				// 取得細項detailID
					detailNameList.add(rs.getString("name")); 				// 取得細項name
		
				}
			}
			///////////////////////////透過detail id取得detail name 跟 id(結束)////////////////////////////////////////////////////
			
			changeChart.put("itemTimeList", itemTimeList);
			changeChart.put("detailIDList", detailIDList);
			changeChart.put("detailValueList", detailValueList);
			changeChart.put("detailNameList", detailNameList);

			System.out.println("Server changeChart : " + changeChart);

		} catch (SQLException e) {
			System.out.println("PatientHealthTrackingServer changeChart Exception :" + e.toString());
		}
		return changeChart;
	}
	
	//比較日期
	public static boolean betweenDate(String date1, String date2, String date3){
		//date1 開始日期//date2結束日期//比較日期
		boolean flag;
		if (date1.compareTo(date3) > 0) { 		//date1(開始日期)比較大//false
			flag = false;
		}
		else{
			flag = true;						//date1(開始日期)比較小//true
			if(date2.compareTo(date3) >=0){		//date2(結束日期)比較大//true//加上等於才會比到結束日期是對的
				flag=true;
			}
			else{
				flag = false;					//date2(結束日期)比較小//false
			}
		}
		return flag;
	}
	
	//刪除該追蹤項目//EditPatientHealthTracking.html//已關資料庫
	public HashMap deleteHealthTracking(DBConnection conn, String doctorID, String patientID, String itemID){
		HashMap deleteHealthTracking = new HashMap();
		String result = "請重新嘗試";
		try {
			/////////////////////////// 判斷history裡面病患是否有填過健康追蹤項目 開始 ///////////////////////////
			ResultSet rs = conn.runSql("SELECT healthTrackingID, time, value FROM healthtracking NATURAL JOIN healthtrackingitem NATURAL JOIN history WHERE patientID = '"
					+ patientID + "' and doctorID = '" + doctorID + "' and itemID = '"+itemID+"' ORDER BY time ASC");
			
			boolean patientUsed = false;	//病患尚未紀錄過健康追蹤項目//false
			while (rs.next()) {				
				System.out.println("追蹤項目id : " + rs.getString("healthTrackingID"));				// 取得healthTrackingID
				patientUsed = true;			//病患已紀錄過健康追蹤項目//true
				break;
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}
			/////////////////////////// 判斷history裡面病患是否有填過健康追蹤項目 結束 ///////////////////////////
			/////////////////////////// 刪除HealthTracking 開始 ///////////////////////////
			if(patientUsed){
				result = "病患已記錄過，無法刪除該追蹤項目";
			}
			else{
				//刪除健康追蹤項目
				String deleteHealthTrackingSQL = "Delete FROM healthtracking"+
						" WHERE itemID = '"+itemID+"' and patientID = '"+patientID+"' "; 
			
				int delete = conn.updateSql(deleteHealthTrackingSQL);
				System.out.println("Listener deleteHealthTracking : " + delete);
				if(delete > 0){
					//更改項目used變為0，也就是未使用過
					String updateSQL = "update healthtrackingitem SET uesd = 0 WHERE itemID = '"+itemID+"' ";
					int update = conn.updateSql(updateSQL);
					System.out.println("Listener updateUsed : " + update);	
					if(update>0){
						result="健康追蹤項目刪除成功";
					}
				}
			}
			/////////////////////////// 刪除HealthTracking 結束 ///////////////////////////
		
			deleteHealthTracking.put("result", result);
			System.out.println("deleteHealthTracking : " + deleteHealthTracking);

		} catch (SQLException e) {
			System.out.println("PatientHealthTrackingServer deleteHealthTracking Exception :" + e.toString());
		}
		return deleteHealthTracking;
	}
	
	public static void main(String[] args) {
		/*
		String temp = "item123";
		String test = (String) temp.subSequence(4, temp.length());
		System.out.println(test);

		String temp2 = "{detailID:[1,2,3]}";
		Gson gson = new Gson();
		ItemDetail temp3 = gson.fromJson(temp2, ItemDetail.class);
		System.out.println(temp3.detailID.size());
		System.out.println(temp3.detailID.get(0));
	
		
		String temp6 = "{\"itemID\": \"1\", \"detail\": [{\"detailID\": \"1\", \"detailValue\": 110}, {\"detailID\": \"5\", \"detailValue\": 75}]}";
		Gson gson = new Gson();
		
		HistoryValue historyValue = gson.fromJson(temp6, HistoryValue.class);
		System.out.println("historyValue : " + historyValue.itemID);
		List<Detail> tempList = historyValue.detail;
		System.out.println(tempList.get(0).detailID);
		System.out.println(tempList.get(0).detailValue);
		System.out.println(tempList.get(1).detailID);
		System.out.println(tempList.get(1).detailValue);
		
		System.out.println(isDateBigger("2017-01-07 01:14:57.0", "2017-04-07 01:14:56.0"));
		
		ArrayList tempList1 = new ArrayList();
		ArrayList tempList2 = new ArrayList();
		ArrayList tempList3 = new ArrayList();
		
		tempList1.add("123");
		tempList1.add("234");
		tempList1.add("345");
		
		tempList2.add(tempList1);
		tempList2.add(tempList1);
		
		System.out.println("tempList1 : " + tempList1);
		System.out.println("tempList2 : " + tempList2);
		
		for(int i = 0; i<tempList2.size(); i++){
			tempList3 = (ArrayList) tempList2.get(i);
			System.out.println("tempList2.get("+i+") : " + tempList3);
			
			for(int k = 0; k<tempList3.size(); k++){
				tempList3.get(k);	
				System.out.println("tempList1.get("+i+") : " + tempList3.get(k));
			}
			
		}
		*/
		
		String temp8 = "2017-03-01";
		String temp9 = "2017-03-02";
		String temp10 = "2017-03-01";
		//如果跟開始日期一樣的話，會是true，如果跟結束日期一樣的話，會是false
		System.out.println(temp10 + " : " + betweenDate(temp8, temp9, temp10));
				
	}
}
