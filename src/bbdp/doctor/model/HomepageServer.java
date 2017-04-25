package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bbdp.db.model.DBConnection;

public class HomepageServer {
	public static String getHomepageData(DBConnection conn, String doctorID) {
		return "{" + getQuestionnaireList(conn, doctorID) + ", " + getFolderList(conn, doctorID) + "}";
	}
	public static String getQuestionnaireList(DBConnection conn, String doctorID) {
		//SELECT answer.patientID, patient.name as patientName, answer.answerID, questionnaire.questionnaireID, questionnaire.name as questionnaireName, answer.date FROM questionnaire, answer, patient WHERE questionnaire.questionnaireID = answer.questionnaireID AND questionnaire.doctorID = 1 AND answer.doctorID is NULL AND answer.patientID = patient.patientID ORDER BY date DESC
		String result = "";
		JSONArray QList = new JSONArray();
		try {
			ResultSet resultSet = conn.runSql("SELECT answer.patientID, patient.name as patientName, answer.answerID, questionnaire.questionnaireID, questionnaire.name as questionnaireName, answer.date FROM questionnaire, answer, patient WHERE questionnaire.questionnaireID = answer.questionnaireID AND questionnaire.doctorID = '" + doctorID + "' AND answer.doctorID is NULL AND answer.patientID = patient.patientID ORDER BY date DESC");
			while (resultSet.next()) {
				if(isToday(resultSet.getString("date"))) {
					JSONObject QItem = new JSONObject();
					QItem.put("patientID", resultSet.getString("patientID"));
					QItem.put("patientName", resultSet.getString("patientName"));
					QItem.put("answerID", resultSet.getString("answerID"));
					QItem.put("questionnaireID", resultSet.getString("questionnaireID"));
					QItem.put("questionnaireName", resultSet.getString("questionnaireName"));
					QItem.put("date", resultSet.getString("date"));
					QList.put(QItem);
				} else {
					break;
				}
			}
			result = "\"QList\": " + QList.toString();
			if(result.equals("") || result.equals("\"QList\": []")) {
				System.out.println("HomepageServer getQuestionnaireList empty");
				result = "\"QList\": \"今日目前尚未有問卷資料\"";
			}
			if (resultSet != null) resultSet.close();
			return result;
		} catch (SQLException e) {
			System.out.println("HomepageServer getQuestionnaireList SQLException: " + e);
			result = "\"QList\": \"今日目前尚未有問卷資料\"";
			return result;
		} catch (JSONException e) {
			System.out.println("HomepageServer getQuestionnaireList JSONException: " + e);
			result = "\"QList\": \"今日目前尚未有問卷資料\"";
			return result;
		}
	}
	
	public static String getFolderList(DBConnection conn, String doctorID) {
		//SELECT patientID, name, time, picture, video, description FROM file NATURAL JOIN patient where doctorID = 1 ORDER BY time DESC
		String result = "";
		JSONArray FList = new JSONArray();
		try {
			ResultSet resultSet = conn.runSql("SELECT patientID, name, time, video, description FROM file NATURAL JOIN patient where doctorID = '" + doctorID + "' ORDER BY time DESC");
			while (resultSet.next()) {
				if(isToday(resultSet.getString("time").substring(0, 10))) {
					JSONObject FItem = new JSONObject();
					FItem.put("patientID", resultSet.getString("patientID"));
					FItem.put("patientName", resultSet.getString("name"));
					FItem.put("time", resultSet.getString("time"));
					if(resultSet.getString("video").equals(null) || resultSet.getString("video").equals("")) {
						FItem.put("pictureOrVideo", "picture");
					} else {
						FItem.put("pictureOrVideo", "video");
					}
					FItem.put("description", resultSet.getString("description"));
					FList.put(FItem);
				} else {
					break;
				}
			}
			result = "\"FList\": " + FList.toString();
			if(result.equals("") || result.equals("\"FList\": []")) {
				System.out.println("HomepageServer getFolderList empty");
				result = "\"FList\": \"今日目前尚未有檔案資料\"";
			}
			if (resultSet != null) resultSet.close();
			return result;
		} catch (SQLException e) {
			System.out.println("HomepageServer getFolderList SQLException: " + e);
			result = "\"FList\": \"今日目前尚未有檔案資料\"";
			return result;
		} catch (JSONException e) {
			System.out.println("HomepageServer getFolderList JSONException: " + e);
			result = "\"FList\": \"今日目前尚未有檔案資料\"";
			return result;
		}
	}
	
	public static boolean isToday(String inputDate) {		//判斷日期是否為今日
		int year = Integer.valueOf(inputDate.substring(0, 4));
		int month = Integer.valueOf(inputDate.substring(5, 7));
		int date = Integer.valueOf(inputDate.substring(8));
		LocalDate birthday = LocalDate.of(year, month, date);
		LocalDate today = LocalDate.now();
		Period period = Period.between(birthday, today);
		if(period.getDays() == 0) return true;
		return false;
	}
}