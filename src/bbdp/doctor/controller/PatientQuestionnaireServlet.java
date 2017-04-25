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
import bbdp.doctor.model.PatientQuestionnaireServer;
import bbdp.doctor.model.QuestionnaireModuleServer;
import bbdp.doctor.model.QuestionnairePoolServer;
import bbdp.db.model.DBConnection;

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
			System.gc();								

		}else if(state.equals("selectQP")){
			String QPname = request.getParameter("QPname");
			result = PatientQuestionnaireServer.selectQP(db,doctorID,QPname);
			response.getWriter().write(gson.toJson(result));
			System.gc();
		}else if(state.equals("sendQuestionnaire")){
			String QName = request.getParameter("QName");
			String QCycle = request.getParameter("QCycle");
			String QCycleType = request.getParameter("QCycleType");
			String QTotalTimes = request.getParameter("QTotalTimes");
			int rs = PatientQuestionnaireServer.sendQuestionnaire(db,doctorID,patientID,QName,QCycle,QCycleType,QTotalTimes); 
			response.getWriter().println(rs);
			
			
		}
	}
}
