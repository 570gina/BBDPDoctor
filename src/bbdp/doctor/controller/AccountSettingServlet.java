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
import bbdp.doctor.model.AccountSettingServer;


@WebServlet("/AccountSettingServlet")
public class AccountSettingServlet extends HttpServlet {
    public AccountSettingServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// 設置原本所需參數
		String state = request.getParameter("state");
		String doctorID = request.getParameter("doctorID");
		// 設置修改所需參數
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		String passwordCheck = request.getParameter("passwordCheck");
		String name = request.getParameter("name");
		String hospital = request.getParameter("hospital");
		String department = request.getParameter("department");

		HashMap result = new HashMap(); // 設置結果
		HashMap accountDefault = new HashMap(); // 設置原本
		HashMap accountChange = new HashMap(); // 設置修改
		AccountSettingServer setting = new AccountSettingServer();

		System.out.println("servlet 參數: " + state + doctorID + account + password+passwordCheck+name+hospital+department);
		// 設置驗證
		DBConnection db = (DBConnection) getServletContext().getAttribute("db");

		// 設置原本
		if (state.equals("Default")) {
			accountDefault = setting.settingDefault(db, doctorID);

			System.out.println("在servlet中的accountInfo : " + accountDefault);
			result = accountDefault; // 把設置原本放進結果
		}
		
		// 設置修改
		if (state.equals("Change")) {
			String show;
			show = setting.settingChange(db, account, password, passwordCheck, name, hospital, department);	//修改完畢
			accountChange = setting.settingDefault(db, doctorID);	//再從db取得修改後的資訊
			accountChange.put("show", show);
			
			System.out.println("在servlet中的accountChange : " + accountChange);
			result = accountChange; // 把設置修改放進結果
		}

		// 設置結果
		System.out.println("在servlet中的result : " + result);

		// 回傳json型態
		Gson gson = new Gson();
		response.getWriter().write(gson.toJson(result));
	}
}
