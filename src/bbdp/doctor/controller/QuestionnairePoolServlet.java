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
import bbdp.db.model.DBConnection;


@WebServlet("/QuestionnairePoolServlet")
public class QuestionnairePoolServlet extends HttpServlet {
	public QuestionnairePoolServlet() {
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
		if(state.equals("addQuestion")){
			String QPname = request.getParameter("QPname");
			String QType = request.getParameter("QType");
			String QPType = request.getParameter("QPType");
			String QPAnswer = request.getParameter("QPAnswer");
			int rs = QuestionnairePoolServer.addQuestion(db,doctorID,QPname,QType,QPType,QPAnswer); 
			response.getWriter().println(rs);
	        	
		}else if(state.equals("searchQPType")){
			result = QuestionnairePoolServer.searchQPType(db,doctorID);
			response.getWriter().write(gson.toJson(result));
			
		}else if(state.equals("selectQPType")){
			String selectType = request.getParameter("selectType");
			result = QuestionnairePoolServer.selectQPType(db,doctorID,selectType);
			response.getWriter().write(gson.toJson(result));
			
		}else if(state.equals("editQuestion")){
			String nowQP = request.getParameter("nowQP");
			String edit = request.getParameter("edit");
			if(edit.equals("Qname")){
				String editRS = QuestionnairePoolServer.editNoJson(db,doctorID,nowQP,"question");
				response.getWriter().println(editRS);
			}else if(edit.equals("Qtype")){
				String editRS = QuestionnairePoolServer.editNoJson(db,doctorID,nowQP,"type");
				response.getWriter().println(editRS);								
			}else if(edit.equals("medicalRecord")){
				String editRS = QuestionnairePoolServer.editNoJson(db,doctorID,nowQP,"medicalRecord");
				response.getWriter().println(editRS);
			}else{
				String editRS = QuestionnairePoolServer.editJson(db,doctorID,nowQP);
				response.getWriter().write(editRS);
												
			}

		}else if(state.equals("selectQPID")){
			String selectName = request.getParameter("selectName");
			System.out.println(selectName);
			String QPID = QuestionnairePoolServer.selectQPID(db,doctorID,selectName);
			response.getWriter().println(QPID);
			
		}else if(state.equals("hideQuestion")){
			String nowQP = request.getParameter("nowQP");
			int rs = QuestionnairePoolServer.hideQuestion(db,doctorID,nowQP); 
			response.getWriter().println(rs);			
			
			
		}else if(state.equals("updateQuestion")){
			String QPname = request.getParameter("QPname");
			String QType = request.getParameter("QType");
			String QPType = request.getParameter("QPType");
			String QPAnswer = request.getParameter("QPanswer");
			String nowQP = request.getParameter("nowQP");
			String MR = request.getParameter("MR");
			int rs = QuestionnairePoolServer.updateQuestion(db,doctorID,QPname,QType,QPType,QPAnswer,nowQP,MR); 
			response.getWriter().println(rs);			
		}else if(state.equals("addTempStorage")){
			String questionArray = request.getParameter("questionArray");
			int rs = QuestionnairePoolServer.addTempStorage(db,doctorID,questionArray); 
			response.getWriter().println(rs);			
		}

	}
}
