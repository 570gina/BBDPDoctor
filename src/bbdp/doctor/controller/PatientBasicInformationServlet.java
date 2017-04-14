package bbdp.doctor.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.PatientBasicInformationServer;

public class PatientBasicInformationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String doctorID = request.getParameter("doctorID");

		DBConnection db = (DBConnection) getServletContext().getAttribute("db");
		HttpSession session = request.getSession();
		String patientID = (String) session.getAttribute("patientID");
		if (doctorID.equals("IWantToGetPatientName")) {
			response.getWriter().println(PatientBasicInformationServer.getPatientName(db, patientID));
		} else {
			System.out.println("PatientBasicInformationServlet getPatientBasicInformation: " + PatientBasicInformationServer.getPatientBasicInformation(db, patientID, doctorID));
			response.getWriter().println(PatientBasicInformationServer.getPatientBasicInformation(db, patientID, doctorID));
		}
	}
}