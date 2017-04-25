package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import bbdp.db.model.DBConnection;

public class PushServer {
	public static String getPatientName(DBConnection conn, String inputID) {
		String result;
		try {
			ResultSet resultSet = conn.runSql("SELECT * FROM patient WHERE patientID = '" + inputID + "'");
			if (resultSet.next()) {
				result = resultSet.getString("name");
				if (resultSet != null) resultSet.close();
				return result;
			} else {
				System.out.println("fail in ClinicPushServer getPatientName");
				result = "fail";
				if (resultSet != null) resultSet.close();
				return result;
			}
		} catch (SQLException e) {
			result = "SQLException";
			System.out.println("SQLException in ClinicPushServer getPatientName");
			return result;
		}
	}
	public static void newClinicPush(DBConnection conn, String doctorID, String patientID) {
		//取得現在時間
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = simpleDateFormat.format(timestamp);
		//String hyperlink = "http://localhost:8080/BBDPDoctor/PatientBasicInformation.html";
		String hyperlink = "http://140.121.197.130:8000/BBDPDoctor/PatientBasicInformation.html";
		String title = "診間推播";
		String body = getPatientName(conn, patientID) + "的診間推播";
		
		try {
			conn.updateSql("INSERT INTO push(doctorID, patientID, time, hyperlink, title, body) VALUES ('" + doctorID + "', '" + patientID + "', '" + currentTime + "', '" + hyperlink + "', '" + title + "', '" + body + "')");
		} catch (SQLException e) {
			System.out.println("SQLException in ClinicPushServer newClinicPush");
		}
	}
	public static void newRemindPush(DBConnection conn, String doctorID, String patientID, String title, String body, String hyperlink) {
		//取得現在時間
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = simpleDateFormat.format(timestamp);
		
		try {
			conn.updateSql("INSERT INTO push(doctorID, patientID, time, hyperlink, title, body) VALUES ('" + doctorID + "', '" + patientID + "', '" + currentTime + "', '" + hyperlink + "', '" + title + "', '" + body + "')");
		} catch (SQLException e) {
			System.out.println("SQLException in ClinicPushServer newRemindPush");
		}
	}
}