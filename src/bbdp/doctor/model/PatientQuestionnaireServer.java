package bbdp.doctor.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import bbdp.db.model.DBConnection;

public class PatientQuestionnaireServer {

	public static String getQuestionnaire(DBConnection conn, String doctorID, String Qname) {
		String result="";
		try {
			ResultSet rs = conn.runSql("select question FROM questionnaire where doctorID = '"+doctorID+"' and name = '"+Qname+"'");
			while (rs.next()) {
				result = rs.getString("question");
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}			
		return result;
	}	
	
	public static ArrayList selectQP(DBConnection conn, String doctorID ,String QPname) {
		ArrayList selectResult = new ArrayList();
		
		try {
			ResultSet rs = conn.runSql("select * FROM question where doctorID = '"+doctorID+"' and 	question = '"+QPname+"'");
			while (rs.next()) {
				selectResult.add(rs.getString("question"));
				selectResult.add(rs.getString("kind"));
				selectResult.add(rs.getString("option"));
			}
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return selectResult;
	}
	
	public static int sendQuestionnaire(DBConnection conn, String doctorID, String patientID, String QName, String QCycle, String QCycleType, String QTotalTimes) {
		int dbReturn=-2;
		String questionnaireID="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance(); 
		int cycle = Integer.parseInt(QCycle);
		int totalTimes = Integer.parseInt(QTotalTimes);
		int addDay = 0;
		int addMonth = 0;
		System.out.println(cycle+"  "+totalTimes);
		try {
			ResultSet rs = conn.runSql("select questionnaireID FROM questionnaire where doctorID='"+doctorID+"'and name = '"+QName+"'");
			if(rs.next())
				questionnaireID = rs.getString("questionnaireID");
		} catch (SQLException e) {			
			System.out.println("QuestionnaireServer錯誤");
		}
		
		if(QCycleType.equals("週"))
			addDay = cycle*7;
		else if(QCycleType.equals("天"))
			addDay = cycle;
		else if(QCycleType.equals("月"))
			addMonth = cycle;
		
		System.out.println("HERE");
		System.out.println(addDay+"  "+addMonth);
		
		for(int i=0;i<totalTimes;i++){
			
			now.add(Calendar.MONTH, addMonth); 
			now.add(Calendar.DATE, addDay);
			
			System.out.println(sdf.format(now.getTime()));
			
			try {
				dbReturn = conn.updateSql("INSERT INTO unfilledquestionnaire (questionnaireID,patientID,sendDate) VALUES('"+questionnaireID+"','"+patientID+"','"+sdf.format(now.getTime())+"')");
			} catch (SQLException e) {
				
				System.out.println("QuestionnaireServer錯誤");
			}
			
			
		}

		return dbReturn;
	}
	
}
