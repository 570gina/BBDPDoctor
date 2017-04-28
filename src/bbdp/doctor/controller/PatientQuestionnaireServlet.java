package bbdp.doctor.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.PatientQuestionnaireServer;
import bbdp.push.fcm.PushToFCM;

@WebServlet("/PatientQuestionnaireServlet")
public class PatientQuestionnaireServlet extends HttpServlet {
	public PatientQuestionnaireServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String state = request.getParameter("state");
		String doctorID = request.getParameter("doctorID");
		DBConnection db = (DBConnection) getServletContext().getAttribute("db");
		HttpSession session = request.getSession();
		String patientID = (String) session.getAttribute("patientID");
        Gson gson = new Gson();
        ArrayList result = new ArrayList();	
        if(state.equals("getQuestionnaire")){
			String Qname = request.getParameter("Qname");
			String editRS = PatientQuestionnaireServer.getQuestionnaire(db,doctorID,Qname);
			response.getWriter().write(editRS);
											

		}else if(state.equals("selectQP")){
			String questionID = request.getParameter("questionID");
			result = PatientQuestionnaireServer.selectQP(db,doctorID,questionID);
			response.getWriter().write(gson.toJson(result));
			
		}else if(state.equals("sendQuestionnaire")){
			String QName = request.getParameter("QName");
			String QCycle = request.getParameter("QCycle");
			String QCycleType = request.getParameter("QCycleType");
			String QTotalTimes = request.getParameter("QTotalTimes");

			PushToFCM.sendNotification("問卷填寫提醒", "您收到一份"+QName+"，請記得填寫", patientID);
			
			int rs = PatientQuestionnaireServer.sendQuestionnaire(db,doctorID,patientID,QName,QCycle,QCycleType,QTotalTimes); 
			response.getWriter().println(rs);		
		}else if(state.equals("searchPatientQMType")){
			result = PatientQuestionnaireServer.searchPatientQMType(db,doctorID,patientID);
			response.getWriter().write(gson.toJson(result));
			
		}else if(state.equals("searchPatientQdateRange")){
			String edit = request.getParameter("edit");
			result = PatientQuestionnaireServer.searchPatientQdateRange(db,doctorID,patientID,edit);
			response.getWriter().write(gson.toJson(result));
			
		}else if(state.equals("selectPatientQMType")){
			result = PatientQuestionnaireServer.selectPatientQMType(db,doctorID,patientID);
			response.getWriter().write(gson.toJson(result));
			
		}else if(state.equals("changeQuestionnaire")){
			String type = request.getParameter("type");
			String dateType = request.getParameter("dateType");
			String date = request.getParameter("date");
			result = PatientQuestionnaireServer.changeQuestionnaire(db,doctorID,patientID,type,dateType,date);
			response.getWriter().write(gson.toJson(result));
			
			
		}else if(state.equals("searchAnswerQuestionnaire")){
			String answerID = request.getParameter("answerID");
			String select = request.getParameter("select");
			if(select.equals("Question")){
				String editRS = PatientQuestionnaireServer.searchQuestion(db,doctorID,answerID);
				response.getWriter().write(editRS);
			}else if(select.equals("Answer")){
				String editRS = PatientQuestionnaireServer.searchAnswer(db,doctorID,answerID);
				response.getWriter().write(editRS);
			}else if(select.equals("TitleData")){
				result = PatientQuestionnaireServer.searchTitleData(db,doctorID,answerID);
				response.getWriter().write(gson.toJson(result));					
			}
			
		}else if(state.equals("searchDoctorQ")){
			String answerID = request.getParameter("answerID");		
			String editRS = PatientQuestionnaireServer.searchDoctorQ(db,doctorID,answerID);
			response.getWriter().write(editRS);
		}else if(state.equals("searchPatientQuestionnaire")){
			String answerID = request.getParameter("answerID");
			String editRS = PatientQuestionnaireServer.searchPatientQuestionnaire(db,doctorID,answerID);
			response.getWriter().write(editRS);						
		}else if(state.equals("saveDoctorAnswer")){
			String answerID = request.getParameter("answerID");
			String answerString = request.getParameter("answerString");
			int rs = PatientQuestionnaireServer.saveDoctorAnswer(db,doctorID,answerID,answerString); 
			response.getWriter().println(rs);		
		}else if(state.equals("getMedicalrecord")){
			String answerID = request.getParameter("answerID");
			String editRS = PatientQuestionnaireServer.getMedicalrecord(db,doctorID,answerID);
			response.getWriter().write(editRS);			
		}else if(state.equals("sendMedicalrecord")){
			String medicalrecord = request.getParameter("medicalrecord");
			int rs = PatientQuestionnaireServer.sendMedicalrecord(db,doctorID,patientID,medicalrecord);			
			response.getWriter().println(rs);	
		}
	}
}
