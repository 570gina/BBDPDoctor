package bbdp.db.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
public class DBConnection {
 
	private Connection conn = null;
 
	public DBConnection(String url, String user_name, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
 
			this.conn = DriverManager.getConnection(url+"?user="+user_name+"&password="+password+"&useUnicode=true&characterEncoding=UTF-8");
 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
	public Connection getConnection() {
		return this.conn;
	}
 
	public ResultSet runSql(String sql) throws SQLException {	//SELECT
		Statement sta = conn.createStatement();
		return sta.executeQuery(sql);
	}
	public int updateSql(String sql) throws SQLException {	//INSERT、UPDATE、DELETE
		Statement sta = conn.createStatement();
		return sta.executeUpdate(sql);	//代表所影響到的資料筆數，
	}
}