package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import bbdp.db.model.DBConnection;

public class HealthTrackingServer {
	// 一進來取得所有項目
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

			String name, itemID;
			ArrayList nameList = new ArrayList(); // item name List
			ArrayList itemIDList = new ArrayList(); // item itemID List

			// 取得所有項目的name
			ResultSet rs2 = conn.runSql("select itemID, name, doctorID from healthtrackingitem");
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

	// 選取分類後的項目
	public HashMap typeSelect(DBConnection conn, String doctorID, String select) {
		HashMap typeSelect = new HashMap();
		try {
			// ResultSet rs = conn.runSql("select itemID, name, type, detail,
			// cycle, doctorID from healthtrackingitem");
			ResultSet rs = conn.runSql("select name, itemID from healthtrackingitem where doctorID='" + doctorID
					+ "' and type='" + select + "' ");

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

			typeSelect.put("itemIDList", itemIDList);
			typeSelect.put("nameList", nameList);
			System.out.println("Server typeSelect : " + typeSelect);

		} catch (SQLException e) {
			System.out.println("HealthTrackingServer typeSelect Exception :" + e.toString());
		}
		return typeSelect;
	}

	// 新增儲存
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
					rs = conn.runSql("select max(detailID) as maxID from healthtrackingdetail");
					while (rs.next()) {
						detailList.add(rs.getString("maxID")); // 取得detailID
						detail += rs.getString("maxID") + ",";
					}
				}
			}
			// 取得最後新增的detailID
			rs = conn.runSql("select max(detailID) as maxID from healthtrackingdetail");
			while (rs.next()) {
				detailList.add(rs.getString("maxID")); // 取得detailID
				detail += rs.getString("maxID") + "]}";
			}
			System.out.println("detailList : " + detailList);
			System.out.println("detail : " + detail);
			/////////////////////////// 新增detail 結束 ///////////////////////////
			/////////////////////////// 新增item ///////////////////////////
			insertItemSQL = "insert into healthtrackingitem(itemID,name,type,detail,cycle,doctorID) "
					+ "select ifNULL(max(itemID+0),0)+1,'" + modelName + "','" + typeName + "','"
					+ detail + "','" + cycle + "','" + doctorID + "' FROM healthtrackingitem";
			insert = conn.updateSql(insertItemSQL);
			System.out.println("Listener insertItem : " + insert);
			/////////////////////////// 新增item 結束 ///////////////////////////

		} catch (SQLException e) {
			System.out.println("HealthTrackingServer addItemStorage Exception :" + e.toString());
		}
		// return "新增失敗";
	}

	public static void main(String[] args) {

		String temp = "item123";
		String test = (String) temp.subSequence(4, temp.length());
		System.out.println(test);

		String temp2 = "{detailID:[1,2,3]}";
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
