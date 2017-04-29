package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bbdp.db.model.DBConnection;

public class PatientBasicInformationServer {
	
	public static String getPatientName(DBConnection conn, String patientID) {
		String result = "";
		try {
			ResultSet resultSet = conn.runSql("SELECT name FROM patient WHERE patientID = '" + patientID + "'");
			//ResultSet resultSet = conn.runSql("SELECT name FROM patient WHERE patientID = 1000");
			while (resultSet.next()) {
				result = resultSet.getString("name");
			}
			
			if(result.equals("")) {
				System.out.println("getPatientName empty");
				result = "病患姓名";
			}
			if (resultSet != null) resultSet.close();
			return result;
		} catch (SQLException e) {
			System.out.println("getPatientName SQLException: " + e);
			result = "病患姓名";
			return result;
		}
	}
	
	public static String getPatientBasicInformation(DBConnection conn, String patientID, String doctorID) {		//取得病患所有基本資料
		return "{" + getPatientBasicInfo(conn, patientID) + ", " + getLatestMedicalRecord(conn, patientID, doctorID) + ", " + getRecentQuestionnaire(conn, patientID, doctorID) + ", " + getRecentFolder(conn, patientID, doctorID) + ", " + getRecentHealthTracking(conn, patientID, doctorID) + "}";
	}

	private static String getPatientBasicInfo(DBConnection conn, String patientID) {		//病患姓名, 生日, 年齡(計算), 身分證字號
		String result = "";
		try {
			ResultSet resultSet = conn.runSql("SELECT * FROM patient WHERE patientID = '" + patientID + "'");
			while (resultSet.next()) {
				result = "\"patientName\": \"" + resultSet.getString("name") + "\", \"birth\": \"" + resultSet.getString("birthday") + "\", \"age\": " + calculateAge(resultSet.getString("birthday")) + ", \"account\": \"" + resultSet.getString("account") + "\"";
			}
			if(result.equals("")) {
				result = "\"patientName\": " + "\"資料空白\"" + ", \"birth\": " + "\"資料空白\"" + ", \"age\": " + "\"資料空白\"" + ", \"account\": " + "\"資料空白\"";
			}
			if (resultSet != null) resultSet.close();
			return result;
		} catch (SQLException e) {
			System.out.println("getPatientBasicInformation SQLException");
			result = "\"patientName\": " + "\"取得資料失敗\"" + ", \"birth\": " + "\"取得資料失敗\"" + ", \"age\": " + "\"取得資料失敗\"" + ", \"account\": " + "\"取得資料失敗\"";
			return result;
		}
	}

	private static String getRecentQuestionnaire(DBConnection conn, String patientID, String doctorID) {		//近一個月的問卷數量
		String result = "";
		try {
			ResultSet resultSet = conn.runSql("SELECT date FROM questionnaire, answer WHERE patientID = '" + patientID + "' AND questionnaire.questionnaireID = answer.questionnaireID AND questionnaire.doctorID = '" + doctorID + "' AND answer.doctorID is NULL");
			int countRecentQuestionnireNum= 0;
			while (resultSet.next()) {
				if(isRecent(resultSet.getString("date"))) countRecentQuestionnireNum++;
			}
			result = "\"QNum\": " + countRecentQuestionnireNum;
			if (resultSet != null) resultSet.close();
			return result;
		} catch (SQLException e) {
			System.out.println("getRecentQuestionnaire SQLException");
			result = "\"QNum\": " + "0";
			return result;
		}
	}

	private static String getRecentFolder(DBConnection conn, String patientID, String doctorID) {		//近一個月的檔案數量
		String result = "";
		try {
			ResultSet resultSet = conn.runSql("SELECT time FROM file WHERE patientID = '" + patientID + "' and doctorID = '" + doctorID + "'");
			int countRecentFolderNum= 0;
			while (resultSet.next()) {
				if(isRecent(resultSet.getString("time").substring(0, 10))) countRecentFolderNum++;
			}
			result = "\"FNum\": " + countRecentFolderNum;
			if (resultSet != null) resultSet.close();
			return result;
		} catch (SQLException e) {
			System.out.println("getRecentFolder SQLException");
			result = "\"FNum\": " + "0";
			return result;
		}
	}

	private static String getLatestMedicalRecord(DBConnection conn, String patientID, String doctorID) {
		String result = "";
		String content = "";
		String date = "";
		try {
			ResultSet resultSet = conn.runSql("SELECT * FROM medicalrecord WHERE date = (SELECT MAX(date) FROM medicalrecord WHERE patientID = '" + patientID + "' and doctorID = '" + doctorID + "')");
			while (resultSet.next()) {
				content = resultSet.getString("content").replaceAll("\n", "<br />");
				date = resultSet.getString("date");
			}

			result = "\"MRDate\": \"" + date + "\", \"MRContent\": \"" + content + "\"";
			if(result.equals("")) {
				result = "\"MRDate\": " + "\"資料空白\"" + ", \"MRContent\": " + "\"資料空白\"";
			}
			if (resultSet != null) resultSet.close();
			return result;
		} catch (SQLException e) {
			System.out.println("getLatestMedicalRecord SQLException: " + e);
			result = "\"MRDate\": " + "\"取得資料失敗\"" + ", \"MRContent\": " + "\"取得資料失敗\"";
			return result;
		}
	}

	private static String getRecentHealthTracking(DBConnection conn, String patientID, String doctorID) {
		String result = "";
		try {
			JSONArray HT = new JSONArray();
			ResultSet resultSet = conn.runSql("SELECT healthTrackingID, itemID, name, detail FROM healthtracking NATURAL JOIN healthtrackingitem WHERE patientID = '" + patientID + "' and doctorID = '" + doctorID + "' ORDER BY itemID ASC");
			ResultSet detailNameResultSet;
			while (resultSet.next()) {
				JSONObject HTItem = new JSONObject();
				HTItem.put("HTItemName", resultSet.getString("name"));		//放項目名稱
				HTItem.put("HTTime", new JSONArray());
				HTItem.put("HTDetail", new JSONArray());
				//放detail name
				JSONObject detail = new JSONObject(resultSet.getString("detail"));
				for (int i = 0; i < detail.getJSONArray("detailID").length(); i++) {
					String detailID = detail.getJSONArray("detailID").get(i).toString();
					detailNameResultSet = conn.runSql("SELECT * FROM healthtrackingdetail WHERE detailID = '" + detailID + "'");
					JSONObject HTDetail = new JSONObject();
					while (detailNameResultSet.next()) {
						HTDetail.put("HTDetailName", detailNameResultSet.getString("name"));
					}
					HTDetail.put("HTDetailValue", new JSONArray());
					HTItem.getJSONArray("HTDetail").put(HTDetail);
				}
				HT.put(HTItem);
			}
			
			resultSet = conn.runSql("SELECT healthTrackingID, itemID, name, time, value FROM healthtracking NATURAL JOIN healthtrackingitem NATURAL JOIN history WHERE patientID = '" + patientID + "' and doctorID = '" + doctorID + "'ORDER BY time ASC");
			while (resultSet.next()) {
				if (isRecent(resultSet.getString("time").substring(0, 10))) {		//最近一個月
					for (int i = 0; i < HT.length(); i++) {
						if (resultSet.getString("name").equals(HT.getJSONObject(i).get("HTItemName"))) {
							HT.getJSONObject(i).getJSONArray("HTTime").put(resultSet.getString("time"));		//放時間
							//放detail value
							JSONObject value = new JSONObject(resultSet.getString("value"));
							for (int j = 0; j < value.getJSONArray("detail").length(); j++) {
								HT.getJSONObject(i).getJSONArray("HTDetail").getJSONObject(j).getJSONArray("HTDetailValue").put(value.getJSONArray("detail").getJSONObject(j).get("detailValue"));
							}
						}
					}
				}
			}
			//System.out.println(HT);
			result = "\"healthTracking\": " + HT.toString();
			
			if(result.equals("") || result.equals("\"healthTracking\": []")) {
				result = "\"healthTracking\": " + "\"資料空白\"";
			}
			if (resultSet != null) resultSet.close();
			return result;
		} catch (SQLException e) {
			System.out.println("getRecentHealthTracking SQLException: " + e);
			result = "\"healthTracking\": " + "\"取得資料失敗\"";
			return result;
		} catch (JSONException e) {
			System.out.println("getRecentHealthTracking JSONException: " + e);
			result = "\"healthTracking\": " + "\"取得資料失敗\"";
			return result;
		}
	}
	
	private static int calculateAge(String birth) {		//計算年齡
		int year = Integer.valueOf(birth.substring(0, 4));
		int month = Integer.valueOf(birth.substring(5, 7));
		int date = Integer.valueOf(birth.substring(8));
		LocalDate birthday = LocalDate.of(year, month, date);
		LocalDate today = LocalDate.now();
		Period period = Period.between(birthday, today);
		return period.getYears();
	}
	
	private static boolean isRecent(String inputDate) {		//判斷日期是否是最近一個月
		int year = Integer.valueOf(inputDate.substring(0, 4));
		int month = Integer.valueOf(inputDate.substring(5, 7));
		int date = Integer.valueOf(inputDate.substring(8));
		LocalDate birthday = LocalDate.of(year, month, date);
		LocalDate today = LocalDate.now();
		Period period = Period.between(birthday, today);
		if(period.getMonths() == 0) return true;
		return false;
	}
}