package bbdp.doctor.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.PushServer;

public class PushServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String option = request.getParameter("option");

		if (option.equals("putSession")) {		//放病患ID到session裡
			String patientID = request.getParameter("patient");
			HttpSession session = request.getSession();
			session.setAttribute("patientID", patientID); // 將病患ID放入session中
		} else if (option.equals("clinicPush")) {		//診間推播(拿病患姓名、存推播)
			String json = request.getParameter("json");
			
			String doctorID = null;
			String patientID = null;
			try {
				JSONObject obj = new JSONObject(json);
				doctorID = obj.getString("doctorID");
				patientID = obj.getString("patientID");
			} catch (JSONException e) {
				System.out.println("JSONObject exception!");
			}
	
			DBConnection db = (DBConnection) getServletContext().getAttribute("db");
			// 存通知
			PushServer.newClinicPush(db, doctorID, patientID);
			// 取得病患姓名
			String result = "";
			result = PushServer.getPatientName(db, patientID);
			if (result.equals("fail")) {
				response.getWriter().println("病患姓名");
			} else if (result.equals("SQLException")) {
				response.getWriter().println("病患姓名");
			} else {
				response.getWriter().println(result);
			}
		} else if (option.equals("remindPush")) {		//提醒推播(拿病患姓名、存推播)
			String json = request.getParameter("json");
			
			String doctorID = null;
			String patientID = null;
			String title = null;
			String body = null;
			String hyperlink = null;
			try {
				JSONObject obj = new JSONObject(json);
				doctorID = obj.getString("doctorID");
				patientID = obj.getString("patientID");
				title = obj.getString("title");
				body = obj.getString("body");
				hyperlink = obj.getString("hyperlink");
			} catch (JSONException e) {
				System.out.println("JSONObject exception!");
			}
	
			DBConnection db = (DBConnection) getServletContext().getAttribute("db");
			// 存通知
			PushServer.newRemindPush(db, doctorID, patientID, title, body, hyperlink);
			// 取得病患姓名
			String result = "";
			result = PushServer.getPatientName(db, patientID);
			if (result.equals("fail")) {
				response.getWriter().println("病患姓名");
			} else if (result.equals("SQLException")) {
				response.getWriter().println("病患姓名");
			} else {
				response.getWriter().println(result);
			}
		} else if (option.equals("getPatientName")) {		//拿病患姓名
			String json = request.getParameter("json");
			
			String patientID = null;
			try {
				JSONObject obj = new JSONObject(json);
				patientID = obj.getString("patientID");
			} catch (JSONException e) {
				System.out.println("JSONObject exception!");
			}
	
			DBConnection db = (DBConnection) getServletContext().getAttribute("db");
			// 取得病患姓名
			String result = "";
			result = PushServer.getPatientName(db, patientID);
			if (result.equals("fail")) {
				response.getWriter().println("病患姓名");
			} else if (result.equals("SQLException")) {
				response.getWriter().println("病患姓名");
			} else {
				response.getWriter().println(result);
			}
		}
	}
}