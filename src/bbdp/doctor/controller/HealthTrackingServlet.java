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
import bbdp.doctor.model.HealthTrackingServer;

@WebServlet("/HealthTrackingServlet")
public class HealthTrackingServlet extends HttpServlet {
    public HealthTrackingServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// 所需參數
		String state = request.getParameter("state");
		String doctorID = request.getParameter("doctorID");
		String select = request.getParameter("select");
		String modelName = request.getParameter("modelName");
		String typeName = request.getParameter("typeName");
		String[] nameList = request.getParameterValues("nameList[]");
		String[] unitList = request.getParameterValues("unitList[]");
		String[] range_1_List = request.getParameterValues("range_1_List[]");
		String[] range_2_List = request.getParameterValues("range_2_List[]");
		String[] upperLimitList = request.getParameterValues("upperLimitList[]");
		String[] lowerLimitList = request.getParameterValues("lowerLimitList[]");
		String cycle = request.getParameter("cycle");
		
		
		
		HealthTrackingServer healthTrackingServer=new HealthTrackingServer();
		Gson gson = new Gson();
		
		//System.out.println("在servlet中的傳入參數 state:"+ state +" patientID:"+patientID +" itemNumber :"+itemNumber);
		
		HashMap result = new HashMap();		 // 結果
		HashMap allItem = new HashMap();	 // 所有項目結果
		HashMap typeSelect = new HashMap();	 // 選取分類後的項目結果
		
		// 連接資料庫
		DBConnection db = (DBConnection) getServletContext().getAttribute("db");
		
		// 一進來取得所有項目
		if (state.equals("allItem")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID);
			allItem = healthTrackingServer.allItemDefault(db, doctorID);	//取得項目

			System.out.println("在Servlet中的allItem : " + allItem);
			result = allItem; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}
		
		// 選取分類後的項目
		if (state.equals("typeSelect")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID+" select:"+select);
			typeSelect = healthTrackingServer.typeSelect(db, doctorID, select);	//取得項目

			System.out.println("在Servlet中的typeSelect : " + typeSelect);
			result = typeSelect; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}
		
		// 儲存
		if (state.equals("storage")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID+" modelName:"+modelName+" typeName:"+typeName);
			
			healthTrackingServer.addItemStorage(db, doctorID, modelName, typeName, nameList, unitList, range_1_List, range_2_List, upperLimitList, lowerLimitList, cycle);
			//typeSelect = healthTrackingServer.typeSelect(db, doctorID, select);	//取得項目

			
			//System.out.println("在Servlet中的typeSelect : " + typeSelect);
			//result = typeSelect; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}
		
		
		// 新增紀錄結果
		//System.out.println("在servlet中的result : " + result);
	}
}
