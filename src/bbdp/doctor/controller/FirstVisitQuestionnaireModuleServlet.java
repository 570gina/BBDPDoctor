package bbdp.doctor.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.QuestionnaireModuleServer;
import bbdp.doctor.model.QuestionnairePoolServer;

@WebServlet("/FirstVisitQuestionnaireModuleServlet")
public class FirstVisitQuestionnaireModuleServlet extends HttpServlet {
	public FirstVisitQuestionnaireModuleServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String state = request.getParameter("state");
		String doctorID = request.getParameter("doctorID");
		DBConnection db = (DBConnection) getServletContext().getAttribute("db");
        Gson gson = new Gson();
        ArrayList result = new ArrayList();
        String editRS;
		if(state.equals("searchSymptom")){
			result = QuestionnaireModuleServer.searchSymptom(db,doctorID);
			response.getWriter().write(gson.toJson(result));
		}else if(state.equals("searchFVQ")){
			result = QuestionnaireModuleServer.searchFVQ(db,doctorID);
			response.getWriter().write(gson.toJson(result));
		}else if(state.equals("addQFVQ")){
			String QMname = request.getParameter("QMname");
			String QMSymptom = request.getParameter("QMSymptom");
			int rs = QuestionnaireModuleServer.addQFVQ(db,doctorID, QMname, QMSymptom); 
			response.getWriter().println(rs);	
			
		}else if(state.equals("deleteQFVQ")){
			String QName = request.getParameter("QName");
			int rs = QuestionnaireModuleServer.deleteQFVQ(db,doctorID, QName); 			
			response.getWriter().println(rs);
		}
	}
}
