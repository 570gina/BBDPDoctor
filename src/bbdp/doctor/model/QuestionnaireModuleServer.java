package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import bbdp.db.model.DBConnection;

public class QuestionnaireModuleServer {

	public static ArrayList searchQMType(DBConnection conn, String doctorID) {
		ArrayList searchResult = new ArrayList();
		
		try {
			ResultSet rs = conn.runSql("select distinct type FROM questionnaire where doctorID='"+doctorID+"'");
			
			while (rs.next()) {
				searchResult.add(rs.getString("type"));
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
		
		return searchResult;
	}
	
	public static ArrayList selectQMType(DBConnection conn, String doctorID, String QMType) {
		ArrayList selectResult = new ArrayList();
		if(QMType.equals("all")){
			try {
				ResultSet rs = conn.runSql("select name FROM questionnaire where doctorID = '"+doctorID+"' ORDER BY CAST(questionnaireID AS UNSIGNED) DESC");
				while (rs.next()) {
					selectResult.add(rs.getString("name"));
				}
			
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}
		}else
			try {
				ResultSet rs = conn.runSql("select name FROM questionnaire where type='"+QMType+"' and doctorID = '"+doctorID+"' ORDER BY CAST(questionnaireID AS UNSIGNED) DESC");
				while (rs.next()) {
					selectResult.add(rs.getString("name"));
				}
			
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}			
		
		return selectResult;
	}
	public static ArrayList getQuestionnaireTempStorage(DBConnection conn, String doctorID) {
		ArrayList selectResult = new ArrayList();
		
		try {
			ResultSet rs = conn.runSql("select * FROM question where doctorID = '"+doctorID+"' and 	tempstorage = 1 ORDER BY CAST(questionID AS UNSIGNED)");
			while (rs.next()) {
				selectResult.add(rs.getString("questionID"));
				selectResult.add(rs.getString("question"));
				selectResult.add(rs.getString("kind"));
				selectResult.add(rs.getString("option"));
			}		
			int	dbReturn = conn.updateSql("UPDATE question SET tempstorage = 0 where doctorID = '"+doctorID+"' and 	tempstorage");
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return selectResult;
	}

	public static int addQuestionnaire(DBConnection conn, String doctorID, String QMname, String QMType, String questions, String scoring) {
		int dbReturn=-2;
		String result;
		int score = Integer.parseInt(scoring);
		try {
				ResultSet rs = conn.runSql("select * FROM questionnaire where doctorID='"+doctorID+"'and name = '"+QMname+"'");
			if(rs.next()){
				dbReturn=0;
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
		if(dbReturn!=0 && dbReturn!=-1){
			try {
				dbReturn = conn.updateSql("INSERT INTO questionnaire (questionnaireID,doctorID,name,type,question,scoring) select ifNULL(max(questionnaireID+0),0)+1,'"+doctorID+"','"+QMname+"','"+QMType+"','"+questions+"','"+score+"' FROM questionnaire");
			} catch (SQLException e) {
				
				System.out.println("QuestionnaireServer錯誤");
			}
		}
		return dbReturn;
	}	

	public static int updateQuestionnaire(DBConnection conn, String doctorID, String QMname, String QMType, String questions, String questionnaireID,String scoring,String medicalRecord) {
		int dbReturn=-1;
		String result;
		int score = Integer.parseInt(scoring);
		try {
			ResultSet rs = conn.runSql("select * FROM questionnaire where doctorID='"+doctorID+"'and name = '"+QMname+"'");
			if(rs.next()){
				result = rs.getString("questionnaireID");
				if(!result.equals(questionnaireID)) dbReturn=0;
			}		
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}		
		if(dbReturn!=0){
			try {
				dbReturn = conn.updateSql("UPDATE questionnaire SET name = '"+QMname+"', type = '"+QMType+"', question = '"+questions+"', scoring ='"+score+"', medicalRecord = '"+medicalRecord+"' WHERE questionnaireID = '"+questionnaireID+"'");	
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}
		}
		return dbReturn;
	}	
	
	public static String selectQID(DBConnection conn, String doctorID, String name) {
		String QID="";
		try {
			ResultSet rs = conn.runSql("select questionnaireID FROM questionnaire where doctorID = '"+doctorID+"' and name = '"+name+"'");
			while (rs.next()) {
				QID = rs.getString("questionnaireID");
	
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}				
		return QID;
	}	

	public static String editNoJson(DBConnection conn, String doctorID, String questionnaireID, String search) {
		String result="";
		
		try {
			ResultSet rs = conn.runSql("select "+search+" FROM questionnaire where questionnaireID = '"+questionnaireID+"' and doctorID = '"+doctorID+"'");
			while (rs.next()) {
				result = rs.getString(search);
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}			
		
		return result;
	}

	public static String editJson(DBConnection conn, String doctorID, String questionnaireID) {
		String result="";
		
		try {
			ResultSet rs = conn.runSql("select question FROM questionnaire where questionnaireID = '"+questionnaireID+"' and doctorID = '"+doctorID+"'");
			while (rs.next()) {
				result = rs.getString("question");
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return result;
	}
	
	public static ArrayList selectQP(DBConnection conn, String doctorID ,String questionID) {
		ArrayList selectResult = new ArrayList();
		
		try {
			ResultSet rs = conn.runSql("select * FROM question where doctorID = '"+doctorID+"' and 	questionID = '"+questionID+"'");
			while (rs.next()) {
				selectResult.add(rs.getString("questionID"));
				selectResult.add(rs.getString("question"));
				selectResult.add(rs.getString("kind"));
				selectResult.add(rs.getString("option"));
			}
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return selectResult;
	}

	public static int deleteQuestionnaire(DBConnection conn, String doctorID, String questionnaireID) {
		int dbReturn=-1;
		
		try {
			dbReturn = conn.updateSql("delete from questionnaire WHERE display = 0 and questionnaireID = '"+questionnaireID+"'");	
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return dbReturn;
	}
	
	public static ArrayList searchSymptom(DBConnection conn, String doctorID) {
		ArrayList searchResult = new ArrayList();
		try {
			ResultSet rs = conn.runSql("select symptom.name FROM symptom join doctor where doctorID = '"+doctorID+"' and doctor.department = symptom.department");
			
			while (rs.next()) {
				searchResult.add(rs.getString("symptom.name"));
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
		
		return searchResult;
	}
	
	public static ArrayList searchFVQ(DBConnection conn, String doctorID) {
		ArrayList searchResult = new ArrayList();
	 
		try {
			ResultSet rs = conn.runSql("select * FROM questionnaire join symptom where questionnaire.symptomID = symptom.symptomID and doctorID = '"+doctorID+"' ORDER BY CAST(questionnaireID AS UNSIGNED)");

			
			while (rs.next()) {
				searchResult.add(rs.getString("symptom.name"));
				searchResult.add(rs.getString("questionnaire.name"));
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
		
		return searchResult;
	}
	
	public static int addQFVQ(DBConnection conn, String doctorID, String QMname, String QMSymptom) {
		int dbReturn=-1;
		String result="";
		String department="";
		try {
			ResultSet rs = conn.runSql("select department FROM doctor where doctorID = '"+doctorID+"'");
			rs.next();
			department = rs.getString("department");
		
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		try {
			ResultSet rs = conn.runSql("select symptomID FROM symptom where name = '"+QMSymptom+"' and department = '"+department+"'");
			if(rs.next()){
			}
			else{
				dbReturn = conn.updateSql("INSERT INTO symptom (symptomID,department,name) select ifNULL(max(symptomID+0),0)+1,'"+department+"','"+QMSymptom+"'FROM symptom");
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		try {
			ResultSet rs = conn.runSql("select symptomID FROM symptom where name = '"+QMSymptom+"' and department = '"+department+"'");
			if(rs.next()){
				result = rs.getString("symptomID");
			}
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
	
		
		try {
			ResultSet rs = conn.runSql("select * FROM questionnaire where symptomID = '"+result+"' and doctorID = '"+doctorID+"'");
			if(rs.next())
				dbReturn = 0;
			else
				dbReturn = conn.updateSql("UPDATE questionnaire SET symptomID = '"+result+"' WHERE doctorID = '"+doctorID+"' and name = '"+QMname+"'");	
						
		
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		
		return dbReturn;
	}
	
	public static int deleteQFVQ(DBConnection conn, String doctorID, String QMname) {
		int dbReturn=-1;
		String result="";
		try {
			ResultSet rs = conn.runSql("select symptomID FROM questionnaire where name = '"+QMname+"' and doctorID = '"+doctorID+"'");
			if(rs.next()){
				result = rs.getString("symptomID");
			}
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}			
		
		try {
			dbReturn = conn.updateSql("UPDATE questionnaire SET symptomID = NULL WHERE doctorID = '"+doctorID+"' and name = '"+QMname+"'");	
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}

		try {
			dbReturn = conn.updateSql("DELETE FROM symptom WHERE symptomID = '"+result+"'");	
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		
		
		return dbReturn;
	}
	public static String clickScoring(DBConnection conn, String doctorID, String questionnaireID) {
		String result="";
		
		try {
			ResultSet rs = conn.runSql("select scoring FROM questionnaire where questionnaireID = '"+questionnaireID+"' and doctorID = '"+doctorID+"'");
			while (rs.next()) {
				result = rs.getString("scoring");
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}			
		
		return result;
	}

}
