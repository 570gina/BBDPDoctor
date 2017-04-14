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
import bbdp.doctor.model.ClinicPushServer;

public class ClinicPushServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
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
		//存通知
		ClinicPushServer.newPush(db, doctorID, patientID);
		//取得病患姓名
		String result = "";
		result = ClinicPushServer.getPatientName(db, patientID);
		if (result.equals("fail")) {
			response.getWriter().println("fail");
		} else if (result.equals("SQLException")) {
			response.getWriter().println("SQLException");
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("patientID", patientID); // 將病患ID放入session中
			response.getWriter().println(result);
		}
	}
}