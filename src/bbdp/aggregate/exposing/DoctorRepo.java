package bbdp.aggregate.exposing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.tomcat.jdbc.pool.DataSource;

public class DoctorRepo {
	
	public static HashMap getDoctorInfoById(DataSource datasource, String doctorID) {
		HashMap doctorInfo = new HashMap();
		Connection con = null;
		
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select doctorID, name, hospital, department from doctor where doctorID='"+ doctorID+"' ");

			while (rs.next()) {
				doctorInfo.put("name", rs.getString("name"));
				doctorInfo.put("hospital", rs.getString("hospital"));
				doctorInfo.put("department", rs.getString("department"));
			}
			rs.close();
		    st.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("'getDoctorInfoById' Exception :" + e.toString());
		} finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return doctorInfo;
	}
	
	public static ArrayList getAllHospitals(DataSource datasource) {
		ArrayList hospitalList = new ArrayList();
		Connection con = null;
		
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select distinct hospital from doctor");
			while (rs.next()) {
				hospitalList.add(rs.getString("hospital"));
			}
			rs.close();
		    st.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("'getAllHospitals' Exception :" + e.toString());
		} finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return hospitalList;
	}

	public static ArrayList getDepartmentsByHospital(DataSource datasource, String hospital) {
		ArrayList departmentList = new ArrayList();
		Connection con = null;
		
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select distinct department from doctor where hospital = '"+hospital+"'");
			while (rs.next()) {
				departmentList.add(rs.getString("department"));
			}
			rs.close();
		    st.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("'getDepartmentsByHospital' Exception :" + e.toString());
		} finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return departmentList;
	}
	public static ArrayList getDoctorsByHospitalAndDepartment(DataSource datasource, String hospital, String department) {
		ArrayList doctorList = new ArrayList();
		HashMap doctorInfo = null;
		Connection con = null;
		
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select doctorID, name from doctor where hospital='"+ hospital+"' and department='"+department+"'");

			while (rs.next()) {
				doctorInfo = new HashMap();
				doctorInfo.put("doctorID", rs.getString("doctorID"));
				doctorInfo.put("name", rs.getString("name"));
				doctorList.add(doctorInfo);
			}
			rs.close();
		    st.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("'getDoctorsByHospitalAndDepartment' Exception :" + e.toString());
		} finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return doctorList;
	}
	
	

}
