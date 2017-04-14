package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import bbdp.db.model.DBConnection;

public class NotificationServer {
	public static String getNotification(DBConnection conn, String doctorID) {
		String result = "";
		try {
			ResultSet resultSet = conn.runSql("SELECT * FROM push WHERE doctorID = '" + doctorID + "' ORDER BY time DESC");
			ArrayList<Notification> notifications = new ArrayList<Notification>();
			while (resultSet.next()) {
				notifications.add(new Notification(resultSet.getString("doctorID"), resultSet.getString("patientID"), resultSet.getString("time"),
						resultSet.getString("hyperlink"), resultSet.getString("content")));
			}
			Gson gson = new Gson();
			result = gson.toJson(notifications);
			return result;
		} catch (SQLException e) {
			result = "SQLException";
			return result;
		}
	}

	public static String clearAllNotification(DBConnection conn, String doctorID) {
		String result = "";
		try {
			conn.updateSql("DELETE FROM push WHERE doctorID = '" + doctorID + "'");
			return result;
		} catch (SQLException e) {
			result = "SQLException";
			return result;
		}
	}
}