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
import bbdp.doctor.model.PatientMedicalRecordServer;


@WebServlet("/PatientMedicalRecordServlet")
public class PatientMedicalRecordServlet extends HttpServlet {
	public PatientMedicalRecordServlet() {
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
        if(state.equals("searchPatientMedicalRecord")){
			result = PatientMedicalRecordServer.searchPatientMedicalRecord(db,doctorID,patientID);
			response.getWriter().write(gson.toJson(result));
		}else if(state.equals("searchPatientMRdateRange")){
			String edit = request.getParameter("edit");
			result = PatientMedicalRecordServer.searchPatientMRdateRange(db,doctorID,patientID,edit);
			response.getWriter().write(gson.toJson(result));
			
		}else if(state.equals("changeMedicalRecord")){
			String dateType = request.getParameter("dateType");
			String date = request.getParameter("date");
			result = PatientMedicalRecordServer.changeMedicalRecord(db,doctorID,patientID,dateType,date);
			response.getWriter().write(gson.toJson(result));			
			
		}else if(state.equals("sendNewMR")){
			String MRcomment = request.getParameter("MRcomment");
			int rs = PatientMedicalRecordServer.sendNewMR(db,doctorID,patientID,MRcomment);
			response.getWriter().println(rs);		
			
		}else if(state.equals("getMedicalRecord")){
			String dateTime = request.getParameter("dateTime");
			String editRS = PatientMedicalRecordServer.getMedicalRecord(db,doctorID,patientID,dateTime);
			response.getWriter().write(editRS);			
	
		}else if(state.equals("saveOldMR")){
			String MRcomment = request.getParameter("MRcomment");
			String dateTime = request.getParameter("dateTime");
			int rs = PatientMedicalRecordServer.saveOldMR(db,doctorID,patientID,MRcomment,dateTime);
			response.getWriter().println(rs);				
			
		}
	}
}

