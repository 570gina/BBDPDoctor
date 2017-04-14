package bbdp.doctor.model;

import java.sql.*;
import java.util.HashMap;

import bbdp.db.model.DBConnection;

public class LoginVerification {

	public HashMap verification(DBConnection conn, String account, String password) {
		String result = "loginDefault";
		String s, p, doctorID = null;
		
		try {
			ResultSet rs = conn.runSql("select doctorID, account, password from doctor");
			System.out.println("Listener runSql 成功");

			while (rs.next()) {
				s = rs.getString("account");
				p = rs.getString("password");
				if (account.equals(s) == true) {// true 代表已有此使用者
					if (password.equals(p) == true) {
						result = "登入成功";// true 代表此使用者密碼正確
						doctorID = rs.getString("doctorID");
						System.out.println("帳密正確");
						break;
					} else {
						result = "請輸入密碼或密碼錯誤"; // false 代表此使用者密碼不正確
						System.out.println("密碼錯誤");
						break;
					}
				} else {
					result = "沒有此使用者";// false 代表沒有此使用者
					System.out.println("搜尋使用者...");
				}
			}
		} catch (SQLException e) {
			System.out.println("LogintDB Exception :" + e.toString());
		}
		HashMap loginResult = new HashMap();
		loginResult.put("result", result);
		loginResult.put("doctorID", doctorID);
		return loginResult;
	}
}
