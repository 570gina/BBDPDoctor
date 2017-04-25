package bbdp.doctor.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import bbdp.doctor.model.QuestionnaireModuleServer;
import bbdp.doctor.model.QuestionnairePoolServer;
import bbdp.db.model.DBConnection;

@WebServlet("/QuestionnaireModuleServlet")
public class QuestionnaireModuleServlet extends HttpServlet {
	public QuestionnaireModuleServlet() {
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
		if(state.equals("searchQMType")){
			result = QuestionnaireModuleServer.searchQMType(db,doctorID);
			response.getWriter().write(gson.toJson(result));
			System.gc();
		}else if(state.equals("selectQMType")){
			String selectType = request.getParameter("selectType");
			result = QuestionnaireModuleServer.selectQMType(db,doctorID,selectType);
			response.getWriter().write(gson.toJson(result));
			System.gc();
		}else if(state.equals("getQuestionnaireTempStorage")){
			result = QuestionnaireModuleServer.getQuestionnaireTempStorage(db,doctorID);
			response.getWriter().write(gson.toJson(result));
			System.gc();
		}else if(state.equals("addQuestionnaire")){
			String QMname = request.getParameter("QMname");
			String QMType = request.getParameter("QMType");
			String questions = request.getParameter("questions");
			String scoring = request.getParameter("scoring");
			int rs = QuestionnaireModuleServer.addQuestionnaire(db,doctorID,QMname,QMType,questions,scoring); 
			response.getWriter().println(rs);			
			
		}else if(state.equals("selectQID")){
			String selectName = request.getParameter("selectName");
			String QID = QuestionnaireModuleServer.selectQID(db,doctorID,selectName);
			response.getWriter().println(QID);
			
		}else if(state.equals("editQuestionnaire")){
			String nowQ = request.getParameter("nowQ");
			String edit = request.getParameter("edit");
			if(edit.equals("name")){
				String editRS = QuestionnaireModuleServer.editNoJson(db,nowQ,"name");
				response.getWriter().println(editRS);
			}else if(edit.equals("type")){
				String editRS = QuestionnaireModuleServer.editNoJson(db,nowQ,"type");
				response.getWriter().println(editRS);								
			}else{
				String editRS = QuestionnaireModuleServer.editJson(db,nowQ);
				response.getWriter().write(editRS);
				System.gc();								
			}

		}else if(state.equals("selectQP")){
			String QPname = request.getParameter("QPname");
			result = QuestionnaireModuleServer.selectQP(db,doctorID,QPname);
			response.getWriter().write(gson.toJson(result));
			System.gc();
		}else if(state.equals("updateQuestionnaire")){
			String QMname = request.getParameter("QMname");
			String QMType = request.getParameter("QMType");
			String questions = request.getParameter("questions");
			String nowQ = request.getParameter("nowQ");
			String scoring = request.getParameter("scoring");
			int rs = QuestionnaireModuleServer.updateQuestionnaire(db,doctorID,QMname,QMType,questions,nowQ,scoring); 
			response.getWriter().println(rs);			
		}else if(state.equals("deleteQuestionnaire")){
			String nowQ = request.getParameter("nowQ");
			int rs = QuestionnaireModuleServer.deleteQuestionnaire(db,doctorID,nowQ); 
			response.getWriter().println(rs);			
			
			
		}
	}
}
