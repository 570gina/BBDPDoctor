package bbdp.doctor.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.NotificationServer;

public class NotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String option = request.getParameter("option");
		String doctorID = request.getParameter("doctorID");
		
		if (option.equals("clearAllNotification")) {
			System.out.println("in NotificationServlet clearAllNotification condition start");
			DBConnection db = (DBConnection) getServletContext().getAttribute("db");
			String searchResult = "";
			searchResult = NotificationServer.clearAllNotification(db, doctorID);
			if (searchResult.equals("SQLException")) {
				response.getWriter().println("SQLException");
			} else {
				response.getWriter().println(searchResult);
			}
			System.out.println("in NotificationServlet clearAllNotification condition end");
		} else if (option.equals("click")) {
			HttpSession session = request.getSession();
			session.setAttribute("patientID", doctorID);
			
		} else if (option.equals("getData")) {
			DBConnection db = (DBConnection) getServletContext().getAttribute("db");
			String searchResult = "";
			searchResult = NotificationServer.getNotification(db, doctorID);
			if (searchResult.equals("SQLException")) {
				response.getWriter().println("SQLException");
			} else {
				response.getWriter().println(searchResult);
			}
		}
	}
}