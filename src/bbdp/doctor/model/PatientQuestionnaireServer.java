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
	
	public static ArrayList selectQP(DBConnection conn, String doctorID ,String questionID) {
		ArrayList selectResult = new ArrayList();

		try {
			ResultSet rs = conn.runSql("select * FROM question where doctorID = '"+doctorID+"' and questionID = '"+questionID+"'");
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
		try {
			ResultSet rs = conn.runSql("select questionnaireID FROM questionnaire where doctorID='"+doctorID+"'and name = '"+QName+"'");
			if(rs.next())
				questionnaireID = rs.getString("questionnaireID");
		} catch (SQLException e) {			
			System.out.println("QuestionnaireServer錯誤");
		}
		
		try {
			int	dbReturn1 = conn.updateSql("UPDATE questionnaire SET display = 1 where questionnaireID = '"+questionnaireID+"'");
		} catch (SQLException e) {			
			System.out.println("QuestionnaireServer錯誤");
		}
		
		if(QCycleType.equals("週"))
			addDay = cycle*7;
		else if(QCycleType.equals("天"))
			addDay = cycle;
		else if(QCycleType.equals("月"))
			addMonth = cycle;
		
		for(int i=0;i<totalTimes;i++){
			
			now.add(Calendar.MONTH, addMonth); 
			now.add(Calendar.DATE, addDay);
			
			
			try {
				dbReturn = conn.updateSql("INSERT INTO unfilledquestionnaire (questionnaireID,patientID,sendDate) VALUES('"+questionnaireID+"','"+patientID+"','"+sdf.format(now.getTime())+"')");
			} catch (SQLException e) {
				
				System.out.println("QuestionnaireServer錯誤");
			}			
		}
		return dbReturn;
	}
	
	public static ArrayList searchPatientQMType(DBConnection conn, String doctorID, String patientID) {
		ArrayList searchResult = new ArrayList();
		try {
			ResultSet rs = conn.runSql("select distinct questionnaire.type FROM questionnaire join answer where questionnaire.questionnaireID = answer.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answer.patientID = '"+patientID+"'");
			
			while (rs.next()) {
				searchResult.add(rs.getString("questionnaire.type"));
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
		
		return searchResult;
	}
	
	public static ArrayList searchPatientQdateRange(DBConnection conn, String doctorID, String patientID, String edit) {
		ArrayList temp = new ArrayList();
		ArrayList searchResult = new ArrayList();

		try {
			ResultSet rs = conn.runSql("select distinct answer.date FROM questionnaire join answer where questionnaire.questionnaireID = answer.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answer.patientID = '"+patientID+"' ORDER BY answer.date DESC");
			
			while (rs.next()) {
				temp.add(rs.getString("answer.date"));
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		

		if(edit.equals("month")){
			String tempY="";
			String tempM="";
			for(int i = 0;i<temp.size();i++){
				String[] AfterSplit = ((String)temp.get(i)).split("-");
				if(tempY == AfterSplit[0] &&tempM == AfterSplit[1]){
					
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
				if(tempY == AfterSplit[0]){
					
				}else{
					searchResult.add(AfterSplit[0]);
					tempY = AfterSplit[0];
				}
			}
		}
		return searchResult;
	}

	public static ArrayList selectPatientQMType(DBConnection conn, String doctorID, String patientID) {
		ArrayList searchResult = new ArrayList();
		
		try {
			ResultSet rs = conn.runSql("select * FROM questionnaire join answer where questionnaire.questionnaireID = answer.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answer.patientID = '"+patientID+"' ORDER BY CAST(answerID AS UNSIGNED) DESC");
			
			while (rs.next()) {
				if(rs.getString("answer.doctorID") == null)
					searchResult.add("#F8B1B0");
				else
					searchResult.add("#D2F898");
				
				searchResult.add(rs.getString("questionnaire.name"));
				searchResult.add(rs.getString("answer.date"));
				searchResult.add(rs.getString("answer.answerID"));
				
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return searchResult;		

	}
	
	public static ArrayList changeQuestionnaire(DBConnection conn, String doctorID, String patientID, String type, String dateType, String date) {
		ArrayList searchResult = new ArrayList();
		String searchTemp="";
		
		if(date.equals("all")){
			dateType = "all";
			
		}
		
		if(type.equals("all")){
			 switch(dateType) {
				case "all":
					searchTemp = "select * FROM questionnaire join answer where questionnaire.questionnaireID = answer.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answer.patientID = '"+patientID+"' ORDER BY CAST(answerID AS UNSIGNED) DESC";
					break;
				case "year":
				{
					searchTemp = "select * FROM questionnaire join answer where questionnaire.questionnaireID = answer.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answer.patientID = '"+patientID+"' and (answer.date BETWEEN '"+date+"/01/01' and '"+date+"/12/31') ORDER BY CAST(answerID AS UNSIGNED) DESC";
				}break;
				case "month":
					searchTemp = "select * FROM questionnaire join answer where questionnaire.questionnaireID = answer.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answer.patientID = '"+patientID+"' and (answer.date BETWEEN '"+date+"/01' and '"+date+"/31') ORDER BY CAST(answerID AS UNSIGNED) DESC";
				break;
				default: 
					System.out.println("dateType錯誤");
			 }
        }else{
			 switch(dateType) {
				case "all":
					searchTemp = "select * FROM questionnaire join answer where questionnaire.questionnaireID = answer.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answer.patientID = '"+patientID+"' and questionnaire.type = '"+type+"' ORDER BY CAST(answerID AS UNSIGNED) DESC";
				break;
				case "year":
					searchTemp = "select * FROM questionnaire join answer where questionnaire.questionnaireID = answer.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answer.patientID = '"+patientID+"' and (answer.date BETWEEN '"+date+"/01/01' and '"+date+"/12/31') and questionnaire.type = '"+type+"' ORDER BY CAST(answerID AS UNSIGNED) DESC";
				break;
				case "month":
					searchTemp = "select * FROM questionnaire join answer where questionnaire.questionnaireID = answer.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answer.patientID = '"+patientID+"' and (answer.date BETWEEN '"+date+"/01' and '"+date+"/31') and questionnaire.type = '"+type+"' ORDER BY CAST(answerID AS UNSIGNED) DESC";
				break;
				default: 
					System.out.println("dateType錯誤");
			 }					
		}

		try {
			ResultSet rs = conn.runSql(searchTemp);
			
			while (rs.next()) {
				if(rs.getString("answer.doctorID") == null)
					searchResult.add("#F8B1B0");
				else
					searchResult.add("#D2F898");
				
				searchResult.add(rs.getString("questionnaire.name"));
				searchResult.add(rs.getString("answer.date"));
				searchResult.add(rs.getString("answer.answerID"));
				
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return searchResult;		
	}

	public static String searchQuestion(DBConnection conn, String doctorID, String answerID) {
		String result="";
		try {
			ResultSet rs = conn.runSql("select questionnaire.question FROM questionnaire join answer where answer.questionnaireID = questionnaire.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answerID = '"+answerID+"'");
			while (rs.next()) {
				result = rs.getString("questionnaire.question");
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return result;
	}
	
	public static String searchAnswer(DBConnection conn, String doctorID, String answerID) {
		String result="";
		try {
			ResultSet rs = conn.runSql("select answer FROM answer where answerID = '"+answerID+"'");
			while (rs.next()) {
				result = rs.getString("answer");
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return result;
	}
	
	public static ArrayList searchTitleData(DBConnection conn, String doctorID, String answerID) {
		ArrayList searchResult = new ArrayList();
		
		try {
			ResultSet rs = conn.runSql("select * FROM questionnaire join answer where answer.questionnaireID = questionnaire.questionnaireID and questionnaire.doctorID = '"+doctorID+"' and answerID = '"+answerID+"'");
			
			while (rs.next()) {
				if(rs.getString("answer.doctorID") == null)
					searchResult.add("#F8B1B0");
				else
					searchResult.add("#D2F898");
				
				searchResult.add(rs.getString("questionnaire.name"));
				searchResult.add(rs.getString("answer.date"));
				searchResult.add(rs.getString("questionnaire.scoring"));
				
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return searchResult;		
	}

	public static String searchDoctorQ(DBConnection conn, String doctorID, String answerID) {
		String result="";
		try {
			ResultSet rs = conn.runSql("select answerID_doctor FROM association where answerID_patient = '"+answerID+"'");
			while (rs.next()) {
				result = rs.getString("answerID_doctor");
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		return result;
	}	
	
	public static String searchPatientQuestionnaire(DBConnection conn, String doctorID, String answerID) {
		String patientAnswerID="";
		String result="";
		
		try {
			ResultSet rs = conn.runSql("select answerID_patient FROM association where answerID_doctor = '"+answerID+"'");
			
			while (rs.next()) {
				patientAnswerID = rs.getString("answerID_patient");		
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}
		
		try {
			ResultSet rs = conn.runSql("select answer FROM answer where answerID = '"+patientAnswerID+"'");
			
			while (rs.next()) {
				result = rs.getString("answer");		
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}				
		return result;		
	}

	public static int saveDoctorAnswer(DBConnection conn, String doctorID, String answerID, String answerString) {
		int dbReturn1 = 0;
		String questionnaireID = "";
		String selfDescription = "";
		String patientID = "";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance(); 

		try {
			ResultSet rs = conn.runSql("select * FROM answer where answerID = '"+answerID+"'");
			
			while (rs.next()) {
				questionnaireID = rs.getString("questionnaireID");
				selfDescription = rs.getString("selfDescription");
				patientID = rs.getString("patientID");			
			}
			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		
		
		
		try {
			int dbReturn = conn.updateSql("INSERT INTO answer (answerID,questionnaireID,patientID,doctorID,date,answer,selfDescription) select ifNULL(max(answerID+0),0)+1,'"+questionnaireID+"','"+patientID+"','"+doctorID+"','"+sdf.format(now.getTime())+"','"+answerString+"','"+selfDescription+"' FROM answer");
		} catch (SQLException e) {
				
			System.out.println("QuestionnaireServer錯誤");
		}

		try {

			dbReturn1 = conn.updateSql("INSERT INTO association (answerID_doctor,answerID_patient) select ifNULL(max(answerID+0),0),'"+answerID+"'FROM association join answer");
		} catch (SQLException e) {
				
			System.out.println("QuestionnaireServer錯誤");
		}

		return dbReturn1;
	}

	public static String getMedicalrecord(DBConnection conn, String doctorID, String answerID) {
		String questionnaireID = "";
		String QmedicalRecord = "";
		String QPmedicalRecord = "";
		String option = "";
		String question = "";
		String QPallMR = "";
		String QallMR = "";
		String answer = "";
		String selfDescription ="";
		ArrayList searchResult = new ArrayList();
		JSONArray answerArray = new JSONArray();
		String[][] optionArrayList = null;
		boolean bool = true;
		try {
			ResultSet rs = conn.runSql("select * FROM answer where answerID = '"+answerID+"'");
			while (rs.next()) {
				questionnaireID = rs.getString("questionnaireID");
				answer = rs.getString("answer");
				selfDescription = rs.getString("selfDescription");
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}

		try {
			answerArray = new JSONArray(answer);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ResultSet rs = conn.runSql("select * FROM questionnaire where questionnaireID = '"+questionnaireID+"'");
			while (rs.next()) {
				QmedicalRecord = rs.getString("medicalRecord");
				question = rs.getString("question");
			}			
		} catch (SQLException e) {
			System.out.println("QuestionnaireServer錯誤");
		}		

		try{
			JSONArray QPArray = new JSONArray(question);
			for(int i=0 ; i < QPArray.length();i++){
			    JSONObject QPObject = QPArray.getJSONObject(i);	//問卷題目物件
			      
			    JSONArray contentArray =  QPObject.getJSONArray("content");
			    for(int j = 0; j<contentArray.length();j++){
			    	searchResult.add(contentArray.get(j));
			    }
			      
			  }
		}
		catch (JSONException e)
		{
		}

		
		for(int k = 0;k<searchResult.size();k++){
			String questionID = (String) searchResult.get(k);
			try {
				ResultSet rs = conn.runSql("select * FROM question where questionID = '"+questionID+"'");
				while (rs.next()) {
					QPmedicalRecord = rs.getString("medicalRecord");
					option = rs.getString("option");
				}			
			} catch (SQLException e) {
				System.out.println("QuestionnaireServer錯誤");
			}
		
			try{
				JSONArray optionArray = new JSONArray(option);
				optionArrayList = new String[searchResult.size()][10];
				for(int i=0 ; i < optionArray.length();i++){
				    JSONObject QPObject = optionArray.getJSONObject(i);
				    optionArrayList[k][i] = QPObject.getString("content");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				bool=false;
			};

			try {
				JSONObject QPMRObject = new JSONObject(QPmedicalRecord);
				System.out.println(k);
				String all = QPMRObject.getString("all");
				if(all.equals("true")){
					String allOption = QPMRObject.getString("0");
					String[] tempString = allOption.split(" ");
					for(int l = 0;l< tempString.length;l++){
						if(tempString[l].equals("病患選擇答案")){
							QPallMR +=optionArrayList[k][Integer.parseInt((String) answerArray.get(k))-1];
						}else{
							
							QPallMR += tempString[l];
						}
					}	
				}else{
					String tempS;
					try{
						tempS = ((String) QPMRObject.getString(answerArray.get(k)+""));
					}catch (JSONException e) {
						tempS =((String) QPMRObject.getString("1"));	
					};
					String[] tempString = tempS.split(" ");
					for(int m = 0;m< tempString.length;m++){
						if(tempString[m].equals("病患輸入答案"))
							QPallMR += answerArray.get(k);
						else QPallMR += tempString[m]+"\n";
					}			
				}
					
			} catch (JSONException e) {
				
				
			};
			
		}
		

		
		if(!QmedicalRecord.isEmpty()){
			String[] tempString = QmedicalRecord.split(" ");
			for(int i = 0;i<tempString.length;i++){	

				if(tempString[i].equals("病患自述")){
					QallMR+=selfDescription+"\n";
				
				
				}else if(tempString[i].equals("題目病歷")){
					QallMR += QPallMR+"\n";
				
				
				}else
					QallMR += tempString[i]+"\n";

			}
		}
		
		return QallMR;
	}

	public static int sendMedicalrecord(DBConnection conn, String doctorID, String patientID, String medicalrecord) {
		int dbReturn = 0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance(); 

		try {
			dbReturn = conn.updateSql("INSERT INTO medicalrecord (date,patientID,doctorID,content) VALUES('"+sdf.format(now.getTime())+"','"+patientID+"','"+doctorID+"','"+medicalrecord+"')");
		} catch (SQLException e) {
				
			System.out.println("QuestionnaireServer錯誤");
		}

		return dbReturn;
	}	
			
}
