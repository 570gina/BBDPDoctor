package bbdp.doctor.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.LoginVerification;

@WebServlet("/LoginVerificationServlet")
public class LoginVerificationServlet extends HttpServlet {
	public LoginVerificationServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		String account = request.getParameter("account");
		String password = request.getParameter("password");

		HashMap loginResult = new HashMap();	// 登入結果

		// 登入驗證
		DBConnection db = (DBConnection) getServletContext().getAttribute("db");

		LoginVerification login = new LoginVerification();
		loginResult = login.verification(db, account, password);
		
		//if(result.equals("登入成功")){
		//	request.getSession().setAttribute("login", account);
		//	System.out.println("Session login : " + account);
		//}

		// 回傳json型態
		Gson gson = new Gson();
		
		System.out.println("response in servlet" + loginResult);
		
		response.getWriter().write(gson.toJson(loginResult));
	}
}
