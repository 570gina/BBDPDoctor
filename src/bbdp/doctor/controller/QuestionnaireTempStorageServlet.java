package bbdp.doctor.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import bbdp.doctor.model.QuestionnairePoolServer;
import bbdp.doctor.model.QuestionnaireTempStorageServer;
import bbdp.db.model.DBConnection;

@WebServlet("/QuestionnaireTempStorageServlet")
public class QuestionnaireTempStorageServlet extends HttpServlet {
	public QuestionnaireTempStorageServlet() {
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
		if(state.equals("searchQPType")){
			result = QuestionnaireTempStorageServer.searchQPType(db,doctorID);
			response.getWriter().write(gson.toJson(result));
			
		}else if(state.equals("deleteTempStorage")){
			String questionArray = request.getParameter("questionArray");
			int rs = QuestionnaireTempStorageServer.deleteTempStorage(db,doctorID,questionArray); 
			response.getWriter().println(rs);				
			
		}else if(state.equals("selectQPType")){
			String selectType = request.getParameter("selectType");
			result = QuestionnaireTempStorageServer.selectQPType(db,doctorID,selectType);
			response.getWriter().write(gson.toJson(result));
			
		}else if(state.equals("viewQuestion")){
			String QBtnName = request.getParameter("selectName");
			editRS = QuestionnaireTempStorageServer.viewQuestion(db,doctorID,QBtnName);
			response.getWriter().write(editRS);
			
			
		}

	}
}
