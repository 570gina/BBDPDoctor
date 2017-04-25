package bbdp.doctor.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.HomepageServer;

public class HomepageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String doctorID = request.getParameter("doctorID");
		DBConnection db = (DBConnection) getServletContext().getAttribute("db");
		System.out.println("HomepageServlet getHomepageData: " + HomepageServer.getHomepageData(db, doctorID));
		response.getWriter().println(HomepageServer.getHomepageData(db, doctorID));
	}
}