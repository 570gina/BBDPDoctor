package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import bbdp.db.model.DBConnection;

public class AccountSettingServer {
	public HashMap settingDefault(DBConnection conn, String doctorID) {
		HashMap accountInfo = new HashMap();
		try {
			ResultSet rs = conn.runSql("select  doctorID, account, password, name, hospital, department from doctor");
			System.out.println("Listener runSql 成功");

			String doctor, account = null, password = null, passwordCheck = null, name = null, hospital = null, department = null;

			while (rs.next()) {
				doctor = rs.getString("doctorID");
				if (doctorID.equals(doctor) == true) {// true 找到此使用者
					account = rs.getString("account");
					password = rs.getString("password");
					passwordCheck = password;
					name = rs.getString("name");
					hospital = rs.getString("hospital");
					department = rs.getString("department");
					break;
				}
			}
			accountInfo.put("name", name);
			accountInfo.put("account", account);
			accountInfo.put("hospital", hospital);
			accountInfo.put("password", password);
			accountInfo.put("passwordCheck", passwordCheck);
			accountInfo.put("department", department);

		} catch (SQLException e) {
			System.out.println("settingDefaultDB Exception :" + e.toString());
		}
		return accountInfo;
	}

	public String settingChange(DBConnection conn, String account, String password, String passwordCheck, String name,
			String hospital, String department) {
		try {
			String updatedbSQL = "UPDATE doctor " + "SET password='" + password + "',name='" + name + "',hospital='"
					+ hospital + "',department='" + department + "' WHERE account='" + account + "'";

			System.out.println("in settingChange : " + password + passwordCheck + name + hospital + department);
			// 判斷錯誤
			String errors = new String();
			if (password == null || passwordCheck == null || name == null || department == null) {
				errors = "失敗!請確認所有欄位已填寫!";
				return errors;
			} else if (isInvalidPassword(password, passwordCheck)) {
				errors = "失敗!請確認密碼符合格式並再度確認密碼!";
				return errors;
			}

			int update = conn.updateSql(updatedbSQL);
			System.out.println("Listener update : " + update);

		} catch (SQLException e) {
			System.out.println("settingChangeDB Exception :" + e.toString());
		}
		return "成功";
	}

	// 失敗!請確認密碼符合格式並再度確認密碼!
	private boolean isInvalidPassword(String password, String confirmedPasswd) {
		return password == null || password.length() < 6 || password.length() > 15 || !password.equals(confirmedPasswd);
	}
}
