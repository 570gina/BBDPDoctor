package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bbdp.db.model.DBConnection;

public class PatientMedicalRecordServer {
	
	public static ArrayList searchPatientMedicalRecord(DBConnection conn, String doctorID, String patientID) {
		ArrayList searchResult = new ArrayList();
		
		try {
			ResultSet rs = conn.runSql("select * FROM medicalRecord where doctorID = '"+doctorID+"' and patientID = '"+patientID+"' ORDER BY date DESC");
			
			while (rs.next()) {
				searchResult.add(rs.getString("date"));
				
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return searchResult;		

	}
	
	public static ArrayList searchPatientMRdateRange(DBConnection conn, String doctorID, String patientID, String edit) {
		ArrayList temp = new ArrayList();
		ArrayList searchResult = new ArrayList();
	
		try {
			ResultSet rs = conn.runSql("select distinct date FROM medicalRecord where doctorID = '"+doctorID+"' and patientID = '"+patientID+"' ORDER BY date DESC");
			
			while (rs.next()) {
				temp.add(rs.getString("date"));
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		

		if(edit.equals("month")){
			String tempY="";
			String tempM="";
			for(int i = 0;i<temp.size();i++){
				String[] AfterSplit = ((String)temp.get(i)).split("-");
				if(tempY.equals(AfterSplit[0]) && tempM.equals(AfterSplit[1])){
					
				}else{
					searchResult.add(AfterSplit[0]+"/"+AfterSplit[1]);
					tempY = AfterSplit[0];
					tempM = AfterSplit[1];
				}
			}		
		}else if(edit.equals("year")){
			String tempY="";
			for(int i = 0;i<temp.size();i++){
				String[] AfterSplit = ((String)temp.get(i)).split("-");
				if(tempY.equals(AfterSplit[0])){
					
				}else{
					searchResult.add(AfterSplit[0]);
					tempY = AfterSplit[0];
				}
			}
		}
		return searchResult;
	}
	
	public static ArrayList changeMedicalRecord(DBConnection conn, String doctorID, String patientID, String dateType, String date) {
		ArrayList searchResult = new ArrayList();
		String searchTemp="";
		
		if(date.equals("all")){
			dateType = "all";
			
		}
		switch(dateType) {
			case "all":
				searchTemp = "select date FROM medicalRecord where doctorID = '"+doctorID+"' and patientID = '"+patientID+"' ORDER BY date DESC";
			break;
			case "year":
				searchTemp = "select date FROM medicalRecord where doctorID = '"+doctorID+"' and patientID = '"+patientID+"' and ( date BETWEEN '"+date+"/01/01' and '"+date+"/12/31') ORDER BY date DESC";
			break;
			case "month":
				searchTemp = "select date FROM medicalRecord where doctorID = '"+doctorID+"' and patientID = '"+patientID+"' and ( date BETWEEN '"+date+"/01' and '"+date+"/31') ORDER BY date DESC";
			break;
			default: 
				System.out.println("dateType錯誤");
			
		}

		try {
			ResultSet rs = conn.runSql(searchTemp);
			
			while (rs.next()) {
		
				searchResult.add(rs.getString("date"));
				
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return searchResult;		
	}
	
	public static int sendNewMR(DBConnection conn, String doctorID, String patientID, String medicalrecord) {
		int dbReturn = 0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Calendar now = Calendar.getInstance(); 

		try {
			dbReturn = conn.updateSql("INSERT INTO medicalrecord (date,patientID,doctorID,content) VALUES('"+sdf.format(now.getTime())+"','"+patientID+"','"+doctorID+"','"+medicalrecord+"')");
		} catch (SQLException e) {
				
			System.out.println("QuestionnaireServer錯誤");
		}

		return dbReturn;
	}

	public static String getMedicalRecord(DBConnection conn, String doctorID, String patientID, String dateTime) {
		String mr="";

		try {
			ResultSet rs = conn.runSql("select content FROM medicalRecord where doctorID = '"+doctorID+"' and patientID = '"+patientID+"' and date = '"+dateTime+"'");
			
			while (rs.next()) {
				mr = rs.getString("content");
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}	

		return mr;
	}	
	
	public static int saveOldMR(DBConnection conn, String doctorID, String patientID, String medicalrecord, String dateTime) {
		int dbReturn = 0;

		try {
			dbReturn = conn.updateSql("UPDATE medicalrecord SET content = '"+medicalrecord+"' where doctorID = '"+doctorID+"' and patientID = '"+patientID+"' and date = '"+dateTime+"'");
		} catch (SQLException e) {
				
			System.out.println("QuestionnaireServer錯誤");
		}

		return dbReturn;
	}
}
