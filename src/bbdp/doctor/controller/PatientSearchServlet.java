package bbdp.doctor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.PatientSearchVerification;


public class PatientSearchServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String option = request.getParameter("option");
		
		if(option.equals("search")){
			response.setContentType("application/json;charset=UTF-8");
	    	
			String inputString = request.getParameter("account");
	
			//搜尋patientID
			DBConnection db = (DBConnection) getServletContext().getAttribute("db");
	
			String searchResult = "";
			searchResult = PatientSearchVerification.searchVerification(db, inputString);
	
			if(searchResult.equals("fail")){//搜尋失敗
				response.getWriter().println(inputString + "不在資料庫中");
			}
			else if(searchResult.equals("SQLException")){
				response.getWriter().println("SQLException");
			}
			else{	//搜尋成功
				response.getWriter().println(searchResult);
			}
			
		}
		else if(option.equals("select")){
			String selectPatientID = request.getParameter("selectPatient");
			
			HttpSession session = request.getSession();
			session.setAttribute("patientID", selectPatientID);
			System.out.println("patientID(session內容) : " + (String) session.getAttribute("patientID"));
		}
		
	}

}
