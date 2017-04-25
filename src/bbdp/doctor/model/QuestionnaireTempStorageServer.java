package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import bbdp.db.model.DBConnection;
import java.util.ArrayList;

public class QuestionnaireTempStorageServer {
	
	public static ArrayList searchQPType(DBConnection conn, String doctorID) {
		ArrayList searchResult = new ArrayList();
		
		try {
			ResultSet rs = conn.runSql("select distinct type FROM question where doctorID='"+doctorID+"' and tempstorage = 1 ORDER BY CAST(questionID AS UNSIGNED) DESC");
			
			while (rs.next()) {
				searchResult.add(rs.getString("type"));
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
		
		return searchResult;
	}	
	
	public static int deleteTempStorage(DBConnection conn, String doctorID, String questionArray) {
		int dbReturn=-1;
		String[] QArray = questionArray.split(",");
		for(int i=0;i<QArray.length;i++){
			try {
				dbReturn = conn.updateSql("UPDATE question SET tempstorage = 0 WHERE doctorID='"+doctorID+"' and question = '"+QArray[i]+"'");	
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}		
			
		}
		return dbReturn;
	}	

	public static ArrayList selectQPType(DBConnection conn, String doctorID, String QPType) {
		ArrayList selectResult = new ArrayList();
		
		if(QPType.equals("all")){
			try {
				ResultSet rs = conn.runSql("select question FROM question where doctorID = '"+doctorID+"' and tempstorage = 1 ORDER BY CAST(questionID AS UNSIGNED) DESC");
				while (rs.next()) {
					selectResult.add(rs.getString("question"));
				}
			
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}
		}else
			try {
				ResultSet rs = conn.runSql("select question FROM question where type='"+QPType+"' and doctorID = '"+doctorID+"' and tempstorage = 1 ORDER BY CAST(questionID AS UNSIGNED) DESC");
			
				while (rs.next()) {
					selectResult.add(rs.getString("question"));
				}
			
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}			
		
		return selectResult;
	}
	public static String viewQuestion(DBConnection conn, String doctorID, String question) {
		String editRS = "";
		try {
			ResultSet rs = conn.runSql("select * FROM question where doctorID = '"+doctorID+"' and question = '"+question+"'");
			while (rs.next()) {
				editRS = rs.getString("option");
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}

		return editRS;
	}

}
