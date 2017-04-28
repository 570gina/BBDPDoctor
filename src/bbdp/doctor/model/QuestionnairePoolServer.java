package bbdp.doctor.model;
import java.sql.ResultSet;
import java.sql.SQLException;

import bbdp.db.model.DBConnection;
import java.util.ArrayList;


public class QuestionnairePoolServer {
	
	public static int addQuestion(DBConnection conn, String doctorID, String QPname, String QType, String QPType, String QPAnswer) {
		int dbReturn=-2;
		String result;
		
		try {
				ResultSet rs = conn.runSql("select * FROM question where doctorID='"+doctorID+"'and question = '"+QPname+"'");
			if(rs.next()){
				result = rs.getString("display");
				dbReturn=0;
				if(result.equals("0")){
					dbReturn = conn.updateSql("UPDATE question SET display= 1 WHERE doctorID='"+doctorID+"'and question = '"+QPname+"'");
					dbReturn=-1;		
				}
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
		if(dbReturn!=0 && dbReturn!=-1){
			try {
				dbReturn = conn.updateSql("INSERT INTO question (questionID,doctorID,type,kind,question,option) select ifNULL(max(questionID+0),0)+1,'"+doctorID+"','"+QType+"','"+QPType+"','"+QPname+"','"+QPAnswer+"'FROM question");
			} catch (SQLException e) {
				
				System.out.println("QuestionnaireServer錯誤");
			}
		}
		return dbReturn;
	}

	public static int updateQuestion(DBConnection conn, String doctorID, String QPname, String QType, String QPType, String QPAnswer, String questionID, String MR) {
		int dbReturn=-1;
		String result;
		
		try {
			ResultSet rs = conn.runSql("select * FROM question where doctorID='"+doctorID+"'and question = '"+QPname+"'");
			if(rs.next()){
				result = rs.getString("questionID");
				if(!result.equals(questionID)) dbReturn=0;
			}		
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}		
		if(dbReturn!=0){
			try {
				dbReturn = conn.updateSql("UPDATE question SET question = '"+QPname+"', type = '"+QType+"', kind = '"+QPType+"', option = '"+QPAnswer+"', medicalRecord = '"+MR+"' WHERE questionID = '"+questionID+"'");	
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}
		}
		return dbReturn;
	}	
	
	public static ArrayList searchQPType(DBConnection conn, String doctorID) {
		ArrayList searchResult = new ArrayList();
		
		try {
			ResultSet rs = conn.runSql("select distinct type FROM question where doctorID='"+doctorID+"' and display = 1");
			
			while (rs.next()) {
				searchResult.add(rs.getString("type"));
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
		
		return searchResult;
	}	

	public static ArrayList selectQPType(DBConnection conn, String doctorID, String QPType) {
		ArrayList selectResult = new ArrayList();
		
		if(QPType.equals("all")){
			try {
				ResultSet rs = conn.runSql("select question FROM question where doctorID = '"+doctorID+"' and display = 1 ORDER BY CAST(questionID AS UNSIGNED) DESC");
				while (rs.next()) {
					selectResult.add(rs.getString("question"));
				}
			
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}
		}else
			try {
				ResultSet rs = conn.runSql("select question FROM question where type='"+QPType+"' and doctorID = '"+doctorID+"' and display = 1 ORDER BY CAST(questionID AS UNSIGNED) DESC");
			
				while (rs.next()) {
					selectResult.add(rs.getString("question"));
				}
			
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}			
		
		return selectResult;
	}
	public static String editNoJson(DBConnection conn, String doctorID, String questionID, String search) {
		String result="";
		
		try {
			ResultSet rs = conn.runSql("select "+search+" FROM question where doctorID = '"+doctorID+"' and questionID = '"+questionID+"'");
			while (rs.next()) {
				result = rs.getString(search);
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}			
		
		return result;
	}

	public static String editJson(DBConnection conn, String doctorID, String questionID) {
		String result="";
		
		try {
			ResultSet rs = conn.runSql("select option FROM question where doctorID = '"+doctorID+"' and questionID = '"+questionID+"'");
			while (rs.next()) {
				result = rs.getString("option");
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}			
		
		return result;
	}
	
	public static String selectQPID(DBConnection conn, String doctorID, String question) {
		String QPID="";	
		try {
			ResultSet rs = conn.runSql("select questionID FROM question where doctorID = '"+doctorID+"' and question = '"+question+"'");
			while (rs.next()) {
				QPID = rs.getString("questionID");
	
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}			
		return QPID;
	}
	
	public static int hideQuestion(DBConnection conn, String doctorID, String nowQP) {
		int dbReturn=-1;
		
		try {
			dbReturn = conn.updateSql("UPDATE question SET display= 0 , tempstorage = 0 WHERE questionID = '"+nowQP+"'");	
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return dbReturn;
	}

	public static int addTempStorage(DBConnection conn, String doctorID, String questionArray) {
		int dbReturn=-1;
		int times = 0;
		String[] QArray = questionArray.split(",");
		for(int i=0;i<QArray.length;i++){
			try {
				dbReturn = conn.updateSql("UPDATE question SET tempstorage = 1 WHERE doctorID = '"+doctorID+"' and tempstorage = 0  and question = '"+QArray[i]+"'");	
				times+=dbReturn;
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}		
			
		}
		return times;
	}	
	
	
}
