package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.Out;

import com.google.gson.Gson;

import bbdp.db.model.DBConnection;


public class HealthTrackingServer {
	// 一進來取得所有項目//已關資料庫
	public HashMap allItemDefault(DBConnection conn, String doctorID) {
		HashMap allItem = new HashMap();
		try {
			// ResultSet rs = conn.runSql("select itemID, name, type, detail,
			// cycle, doctorID from healthtrackingitem");
			ResultSet rs = conn.runSql("select distinct type, doctorID from healthtrackingitem");
			System.out.println("Listener runSql 成功");

			// String doctor, itemID, name, type, detail, cycle;
			String doctor, type;
			int i = 0; // 共有幾項項目
			ArrayList typeList = new ArrayList(); // item type List

			while (rs.next()) {
				doctor = rs.getString("doctorID");
				if (doctorID.equals(doctor) == true) { // true 找到此使用者ID
					type = rs.getString("type"); // 取得使用者的項目type
					typeList.add(type);
					i++;
				}
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}//關閉resultSet
			String name, itemID;
			ArrayList nameList = new ArrayList(); // item name List
			ArrayList itemIDList = new ArrayList(); // item itemID List

			// 取得所有項目的name
			ResultSet rs2 = conn.runSql("select itemID, name, doctorID from healthtrackingitem ORDER BY itemID DESC");
			System.out.println("Listener runSql 成功");

			while (rs2.next()) {
				doctor = rs2.getString("doctorID");
				if (doctorID.equals(doctor) == true) { // true 找到此使用者ID
					itemID = rs2.getString("itemID"); // 取得使用者的項目itemID
					name = rs2.getString("name"); // 取得使用者的項目name
					nameList.add(name);
					itemIDList.add(itemID);
				}
			}
			if (rs2 != null){ try {rs2.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}
			
			allItem.put("typeList", typeList);
			allItem.put("itemIDList", itemIDList);
			allItem.put("nameList", nameList);
			System.out.println("Server allItemDefault : " + allItem + " item數量:0~" + i);

		} catch (SQLException e) {
			System.out.println("HealthTrackingServer allItemDefault Exception :" + e.toString());
		}
		return allItem;
	}

	// 新增紀錄:細項
	class ItemDetail {
		List<String> detailID;

		ItemDetail(List<String> detailID) {
			this.detailID = detailID;
		}
	}

	// 選取分類後的項目//已關資料庫
	public HashMap typeSelect(DBConnection conn, String doctorID, String select) {
		HashMap typeSelect = new HashMap();
		try {
			// ResultSet rs = conn.runSql("select itemID, name, type, detail,
			// cycle, doctorID from healthtrackingitem");
			ResultSet rs = conn.runSql("select name, itemID from healthtrackingitem where doctorID='" + doctorID
					+ "' and type='" + select + "' ORDER BY itemID DESC");

			System.out.println("Listener runSql 成功");

			// String doctor, itemID, name, type, detail, cycle;\
			String name, itemID;
			ArrayList nameList = new ArrayList(); // item name List
			ArrayList itemIDList = new ArrayList(); // item itemID List

			while (rs.next()) {
				name = rs.getString("name"); // 取得項目名稱
				nameList.add(name);
				itemID = rs.getString("itemID"); // 取得項目id
				itemIDList.add(itemID);
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}//關閉resultSet
			
			typeSelect.put("itemIDList", itemIDList);
			typeSelect.put("nameList", nameList);
			System.out.println("Server typeSelect : " + typeSelect);

		} catch (SQLException e) {
			System.out.println("HealthTrackingServer typeSelect Exception :" + e.toString());
		}
		return typeSelect;
	}

	// 新增儲存//已關資料庫
	public void addItemStorage(DBConnection conn, String doctorID, String modelName, String typeName, String[] nameList,
			String[] unitList, String[] range_1_List, String[] range_2_List, String[] upperLimitList,
			String[] lowerLimitList, String cycle) {
		Gson gson = new Gson();

		for (int i = 0; i < nameList.length; i++) {
			if (unitList[i].equals("")) {
				unitList[i] = "NULL";
			}
			if (range_1_List[i].equals("")) {
				range_1_List[i] = "NULL";
			}
			if (range_2_List[i].equals("")) {
				range_2_List[i] = "NULL";
			}
			if (upperLimitList[i].equals("")) {
				upperLimitList[i] = "NULL";
			}
			if (lowerLimitList[i].equals("")) {
				lowerLimitList[i] = "NULL";
			}
		}

		System.out.println("modelName : " + modelName);
		System.out.println("typeName : " + typeName);
		System.out.println("nameList : " + gson.toJson(nameList));
		System.out.println("unitList : " + gson.toJson(unitList));
		System.out.println("range_1_List : " + gson.toJson(range_1_List));
		System.out.println("range_2_List : " + gson.toJson(range_2_List));
		System.out.println("upperLimitList : " + gson.toJson(upperLimitList));
		System.out.println("lowerLimitList : " + gson.toJson(lowerLimitList));
		//System.out.println("cycleList : " + gson.toJson(cycleList));

		ResultSet rs;
		String insertdetailSQL, insertItemSQL;
		int insert;

		String detail = "{\"detailID\":["; // 新增進去healthtrackingitem資料庫的detail
		ArrayList detailList = new ArrayList(); // item detail List	//確認用//並無實際使用

		try {

			/////////////////////////// 新增detail ///////////////////////////
			for (int i = 0; i < nameList.length; i++) {
				insertdetailSQL = "insert into healthtrackingdetail(detailID,name,unit,range_1,range_2,upperLimit,lowerLimit) "
						+ "select ifNULL(max(detailID+0),0)+1,'" + nameList[i] + "','" + unitList[i] + "','"
						+ range_1_List[i] + "','" + range_2_List[i] + "','" + upperLimitList[i] + "','"
						+ lowerLimitList[i] + "' FROM healthtrackingdetail";

				insert = conn.updateSql(insertdetailSQL);
				System.out.println("Listener insertDetail : " + insert);

				// 取得所有新增的detailID(除了最後一個)
				if (i == nameList.length - 1) {
					break;
				} else {
					rs = conn.runSql("select max(detailID+0) as maxID from healthtrackingdetail");
					while (rs.next()) {
						detailList.add(rs.getString("maxID")); // 取得detailID
						detail += rs.getString("maxID") + ",";
					}
				}
			}
			
			// 取得最後新增的detailID
			rs = conn.runSql("select max(detailID+0) as maxID from healthtrackingdetail");
			while (rs.next()) {
				detailList.add(rs.getString("maxID")); // 取得detailID
				detail += rs.getString("maxID") + "]}";
			}
			System.out.println("detailList : " + detailList);
			System.out.println("detail : " + detail);
			
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}//關閉resultSet
			/////////////////////////// 新增detail 結束 ///////////////////////////
			/////////////////////////// 新增item ///////////////////////////
			insertItemSQL = "insert into healthtrackingitem(itemID,name,type,detail,cycle,doctorID, uesd) "
					+ "select ifNULL(max(itemID+0),0)+1,'" + modelName + "','" + typeName + "','"
					+ detail + "','" + cycle + "','" + doctorID + "' ,0 FROM healthtrackingitem";
			insert = conn.updateSql(insertItemSQL);
			System.out.println("Listener insertItem : " + insert);
			/////////////////////////// 新增item 結束 ///////////////////////////

		} catch (SQLException e) {
			System.out.println("HealthTrackingServer addItemStorage Exception :" + e.toString());
		}
		// return "新增失敗";
	}

	// 編輯前的原本值//已關資料庫
	public HashMap editDefault(DBConnection conn, String doctorID, String itemID) {
		Gson gson = new Gson();
		HashMap editDefault = new HashMap();
		
		itemID = (String) itemID.subSequence(4, itemID.length());	//取出item id
		System.out.println("editDefault : docoteID : " + doctorID + " itemID : " + itemID);
		
		ResultSet rs;
		
		try {
			/////////////////////////////////取得item///////////////////////////////////////////////
			rs = conn.runSql("select itemID, name, type, detail, cycle, doctorID from healthtrackingitem where itemID = '"+itemID+"'");
			System.out.println("Listener runSql 成功");

			String doctor, item, modelName, type, detail = null, cycle;

			while (rs.next()) {
				doctor = rs.getString("doctorID");		//取得項目的使用者醫生ID
				if(doctorID.equals(doctor) == true){
					modelName = rs.getString("name"); 	// 取得項目name
					type = rs.getString("type"); 		// 取得項目type
					detail = rs.getString("detail"); 	// 取得項目detail
					cycle = rs.getString("cycle"); 		// 取得項目cycle	
					
					editDefault.put("modelName", modelName);
					editDefault.put("type", type);
					editDefault.put("detail", detail);
					editDefault.put("cycle", cycle);
				}
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}//關閉resultSet
			/////////////////////////////////取得detail///////////////////////////////////////////////
			ItemDetail detailClass = gson.fromJson(detail, ItemDetail.class);
			String detailID;
			ArrayList detailIDList = new ArrayList();
			ArrayList nameList = new ArrayList();
			ArrayList unitList = new ArrayList();
			ArrayList range_1_List = new ArrayList();
			ArrayList range_2_List = new ArrayList();
			ArrayList upperLimitList = new ArrayList();
			ArrayList lowerLimitList = new ArrayList();
			
			for(int i = 0; i< detailClass.detailID.size(); i++){
				detailID = detailClass.detailID.get(i);
				rs = conn.runSql("select detailID, name, unit, range_1, range_2, upperLimit, lowerLimit from healthtrackingdetail where detailID = '"+detailID+"'");
				while (rs.next()) {
					detailIDList.add(rs.getString("detailID"));			// 取得細項detailID
					nameList.add(rs.getString("name")); 				// 取得細項name
					unitList.add(rs.getString("unit")); 				// 取得細項unitList
					range_1_List.add(rs.getString("range_1")); 			// 取得細項range_1_List
					range_2_List.add(rs.getString("range_2")); 			// 取得細項range_2_List
					upperLimitList.add(rs.getString("upperLimit")); 	// 取得細項upperLimitList
					lowerLimitList.add(rs.getString("lowerLimit")); 	// 取得細項lowerLimitList
				}
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}//關閉resultSet

			editDefault.put("detailIDList", detailIDList);	
			editDefault.put("nameList", nameList);	
			editDefault.put("unitList", unitList);	
			editDefault.put("range_1_List", range_1_List);	
			editDefault.put("range_2_List", range_2_List);	
			editDefault.put("upperLimitList", upperLimitList);	
			editDefault.put("lowerLimitList", lowerLimitList);	
							
			System.out.println("Server editDefault : " + editDefault);

		} catch (SQLException e) {
			System.out.println("HealthTrackingServer editDefault Exception :" + e.toString());
		}
		return editDefault;
	}

	// 修改儲存//不須關資料庫
	public void updateItemStorage(DBConnection conn, String doctorID, String itemID, String[] detailArray, String modelName, String typeName, String[] nameList,
			String[] unitList, String[] range_1_List, String[] range_2_List, String[] upperLimitList,
			String[] lowerLimitList, String cycle) {
		itemID = (String) itemID.subSequence(4, itemID.length());	//取出item id
		
		Gson gson = new Gson();

		for (int i = 0; i < nameList.length; i++) {
			if (unitList[i].equals("")) {
				unitList[i] = "NULL";
			}
			if (range_1_List[i].equals("")) {
				range_1_List[i] = "NULL";
			}
			if (range_2_List[i].equals("")) {
				range_2_List[i] = "NULL";
			}
			if (upperLimitList[i].equals("")) {
				upperLimitList[i] = "NULL";
			}
			if (lowerLimitList[i].equals("")) {
				lowerLimitList[i] = "NULL";
			}
		}
		System.out.println("modelName : " + modelName);
		System.out.println("typeName : " + typeName);
		System.out.println("itemID : " + itemID);
		System.out.println("detailArray : " + gson.toJson(detailArray));
		System.out.println("nameList : " + gson.toJson(nameList));
		System.out.println("unitList : " + gson.toJson(unitList));
		System.out.println("range_1_List : " + gson.toJson(range_1_List));
		System.out.println("range_2_List : " + gson.toJson(range_2_List));
		System.out.println("upperLimitList : " + gson.toJson(upperLimitList));
		System.out.println("lowerLimitList : " + gson.toJson(lowerLimitList));
		System.out.println("cycle : " + cycle);

		String updatedetailSQL, updateItemSQL;
		int update;

		try {
			/////////////////////////// 修改detail ///////////////////////////
			for (int i = 0; i < detailArray.length; i++) {
				updatedetailSQL = "UPDATE healthtrackingdetail"+
						" SET name='"+nameList[i]+"' ,unit='"+unitList[i]+"' ,range_1='"+range_1_List[i]+"' ,range_2='"+range_2_List[i]+"' ,upperLimit='"+upperLimitList[i]+"' ,lowerLimit='"+lowerLimitList[i]+"' "+
						" WHERE detailID = '"+detailArray[i]+"'";				
				System.out.println("updatedetailSQL : " + updatedetailSQL);
		
				update = conn.updateSql(updatedetailSQL);
				System.out.println("Listener updateDetail : " + update);		
			}
			/////////////////////////// 修改detail 結束 ///////////////////////////
			/////////////////////////// 修改item ///////////////////////////
			updateItemSQL = "UPDATE healthtrackingitem"+
					" SET name='"+modelName+"' ,type='"+typeName+"' ,cycle='"+cycle+"' " +
					" WHERE itemID = '"+itemID+"' "; 
					
			update = conn.updateSql(updateItemSQL);
			System.out.println("Listener updateItem : " + update);
			/////////////////////////// 修改item 結束 ///////////////////////////

		} catch (SQLException e) {
			System.out.println("HealthTrackingServer updateItemStorage Exception :" + e.toString());
		}
	}

	// 刪除//已關資料庫
	public HashMap deleteItemStorage(DBConnection conn, String doctorID, String itemID, String[] detailArray, String modelName, String typeName, String[] nameList,
			String[] unitList, String[] range_1_List, String[] range_2_List, String[] upperLimitList,
			String[] lowerLimitList, String cycle) {
		HashMap deleteItemStorage = new HashMap();
		String result="無法刪除";
		itemID = (String) itemID.subSequence(4, itemID.length());	//取出item id
		
		Gson gson = new Gson();
		System.out.println("modelName : " + modelName);
		System.out.println("typeName : " + typeName);
		System.out.println("itemID : " + itemID);
		System.out.println("detailArray : " + gson.toJson(detailArray));
		System.out.println("nameList : " + gson.toJson(nameList));
		System.out.println("unitList : " + gson.toJson(unitList));
		System.out.println("range_1_List : " + gson.toJson(range_1_List));
		System.out.println("range_2_List : " + gson.toJson(range_2_List));
		System.out.println("upperLimitList : " + gson.toJson(upperLimitList));
		System.out.println("lowerLimitList : " + gson.toJson(lowerLimitList));
		System.out.println("cycle : " + cycle);

		ResultSet rs;
		String deletedetailSQL, deleteItemSQL;
		int delete;
		boolean used = false;


		try {
			///////////////////////////判斷可不可以刪除，是否有用過 //////////////////////////////
			rs = conn.runSql("select uesd from healthtrackingitem"+
					" WHERE itemID = '"+itemID+"' ");
			System.out.println("Listener runSql 成功");


			while (rs.next()) {
				if(rs.getString("uesd").equals("1")){
					used = true;
					System.out.println("醫生有用過喔喔喔喔!!!used==" + rs.getString("uesd"));
				}
			}
			if (rs != null){ try {rs.close(); System.out.println("關閉ResultSet");} catch (SQLException ignore) {}}//關閉resultSet
			
			//如果這個項目醫生未使用過才可刪掉
			if(!used){
				result = "刪除成功";
				/////////////////////////// 刪除item ///////////////////////////
				deleteItemSQL = "Delete FROM healthtrackingitem"+
						" WHERE itemID = '"+itemID+"' and uesd = '0' "; 
			
				delete = conn.updateSql(deleteItemSQL);
				System.out.println("Listener deleteItem : " + delete);
				/////////////////////////// 刪除item 結束 ///////////////////////////
				/////////////////////////// 刪除detail ///////////////////////////
				for (int i = 0; i < detailArray.length; i++) {
					deletedetailSQL = "Delete FROM healthtrackingdetail"+
							" WHERE detailID = '"+detailArray[i]+"'";			
		
					delete = conn.updateSql(deletedetailSQL);
					System.out.println("Listener deleteDetail : " + delete);	
				}
				/////////////////////////// 刪除detail 結束 ///////////////////////////
			}
			else{
				result = "已使用過，無法刪除";
			}
			
			deleteItemStorage.put("result", result);
			System.out.println("deleteItemStorage : " + deleteItemStorage);

		} catch (SQLException e) {
			System.out.println("HealthTrackingServer deleteItemStorage Exception :" + e.toString());
		}
		return deleteItemStorage;

	}
	
	public static void main(String[] args) {

		String temp = "item123";
		String test = (String) temp.subSequence(4, temp.length());
		System.out.println(test);

		String temp2 = "{\"detailID\":[1,2,3]}";
		Gson gson = new Gson();
		ItemDetail temp3 = gson.fromJson(temp2, ItemDetail.class);
		System.out.println(temp3.detailID.size());
		System.out.println(temp3.detailID.get(0));

		/*
		 * String temp4=
		 * "{item:item1,detailIdArray:[\"detail1\",\"detail2\",\"detail4\"],detailValueArray:[\"2\",\"3\",\"6\"]}";
		 * historyValue temp5 = gson.fromJson(temp4, historyValue.class);
		 * System.out.println(temp5.item);
		 * System.out.println(temp5.detailIdArray.size());
		 * System.out.println(temp5.detailIdArray.get(0));
		 * System.out.println(temp5.detailValueArray.size());
		 * System.out.println(temp5.detailValueArray.get(0));
		 */
	}

}
