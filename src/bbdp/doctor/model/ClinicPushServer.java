package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import bbdp.db.model.DBConnection;

public class ClinicPushServer {
	public static String getPatientName(DBConnection conn, String inputID) {
		String result;
		try {
			ResultSet resultSet = conn.runSql("SELECT * FROM patient WHERE patientID = '" + inputID + "'");
			if (resultSet.next()) {
				result = resultSet.getString("name");
				return result;
			} else {
				result = "fail";
				return result;
			}
		} catch (SQLException e) {
			result = "SQLException";
			return result;
		}
	}
	public static void newPush(DBConnection conn, String doctorID, String patientID) {
		//取得現在時間
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = simpleDateFormat.format(timestamp);
		//String hyperlink = "http://localhost:8080/BBDPDoctor/PatientBasicInformation.html";
		String hyperlink = "http://140.121.197.130:8000/BBDPDoctor/PatientBasicInformation.html";
		String content = getPatientName(conn, patientID) + "的診間推播";
		
		try {
			conn.updateSql("INSERT INTO push(doctorID, patientID, time, hyperlink, content) VALUES ('" + doctorID + "', '" + patientID + "', '" + currentTime + "', '" + hyperlink + "', '" + content + "')");
		} catch (SQLException e) {
			System.out.println("SQLException in ClinicPushServer");
		}
	}
}