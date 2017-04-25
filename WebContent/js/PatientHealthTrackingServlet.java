package bbdp.doctor.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.PatientHealthTrackingServer;


@WebServlet("/PatientHealthTrackingServlet")
public class PatientHealthTrackingServlet extends HttpServlet {
    public PatientHealthTrackingServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession();	
		
		// 所需參數	//PatientHealthTracking.html
		String patientID = (String) session.getAttribute("patientID");
		String state = request.getParameter("state");
		String doctorID = request.getParameter("doctorID");
		// 所需參數	//NewPatientHealthTracking
		String select = request.getParameter("select");
		String itemSelect = request.getParameter("itemSelect");
		// 所需參數	//EditPatientHealthTracking.html
		String itemID = request.getParameter("itemID");
		String dateStart = request.getParameter("dateStart");
		String dateEnd = request.getParameter("dateEnd");
		String[] checkArray = request.getParameterValues("checkArray[]");
		
		
		Gson gson = new Gson();
		PatientHealthTrackingServer patientHealthTrackingServer = new PatientHealthTrackingServer();
		
		HashMap result = new HashMap();				 // 結果
		HashMap allItem = new HashMap();			 // 所有項目結果
		HashMap allType = new HashMap();			 // 所有類別結果
		HashMap typeSelectItem = new HashMap();		 // 選什麼分類，顯示該分類的項目結果
		HashMap addItemToPatient = new HashMap();	 // 按下後新增項目給病患
		HashMap itemAllDetail = new HashMap();		 // 項目的細項資料(itemName、itemTime、detailValue、detailID)
		HashMap changeChart = new HashMap();		 // 變化圖表
		HashMap deleteHealthTracking = new HashMap();// 刪除該追蹤項目
		
		//連接資料庫
		DBConnection db = (DBConnection) getServletContext().getAttribute("db");
		
		//一進來取得所有項目//PatientHealthTracking.html
		if (state.equals("allItem")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID+" patientID:"+patientID);
			allItem = patientHealthTrackingServer.allItemDefault(db, doctorID, patientID);	//取得項目

			System.out.println("在Servlet中的allItem : " + allItem);
			result = allItem; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}
		
		//取得下拉選單的值//NewPatientHealthTracking.html
		if (state.equals("allType")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID+" patientID:"+patientID);
			allType = patientHealthTrackingServer.allTypeDefault(db, doctorID, patientID);	//取得類別

			System.out.println("在Servlet中的allType : " + allType);
			result = allType; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}
		//選什麼分類，顯示該分類的項目//NewPatientHealthTracking.html
		if (state.equals("typeSelectItem")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID+" patientID:"+patientID+" selecct:"+select);
			typeSelectItem = patientHealthTrackingServer.typeSelectItem(db, doctorID, patientID, select);	//取得類別

			System.out.println("在Servlet中的typeSelectItem : " + typeSelectItem);
			result = typeSelectItem; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}
		
		//按下後新增項目給病患//NewPatientHealthTracking.html
		if (state.equals("addItemToPatient")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID+" patientID:"+patientID+" itemSelect:"+itemSelect);
			addItemToPatient = patientHealthTrackingServer.addItemToPatient(db, doctorID, patientID, itemSelect);

			System.out.println("在Servlet中的addItemToPatient : " + addItemToPatient);
			result = addItemToPatient; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}
		
		//取得該項目一些基本資料//EditPatientHealthTracking.html
		if (state.equals("itemAllDetail")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID+" patientID:"+patientID+" itemID:"+itemID);
			itemAllDetail = patientHealthTrackingServer.itemAllDetail(db, doctorID, patientID, itemID);

			System.out.println("在Servlet中的itemAllDetail : " + itemAllDetail);
			result = itemAllDetail; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}
		
		//變化圖表//EditPatientHealthTracking.html
		if (state.equals("changeChart")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID+" patientID:"+patientID+" itemID:"+itemID);
			changeChart = patientHealthTrackingServer.changeChart(db, doctorID, patientID, itemID, dateStart, dateEnd, checkArray);

			System.out.println("在Servlet中的changeChart : " + changeChart);
			result = changeChart; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}
		
		//刪除該追蹤項目//EditPatientHealthTracking.html
		if (state.equals("deleteHealthTracking")) {
			System.out.println("在servlet中的傳入參數 state:"+ state +" doctorID:"+doctorID+" patientID:"+patientID+" itemID:"+itemID);
			deleteHealthTracking = patientHealthTrackingServer.deleteHealthTracking(db, doctorID, patientID, itemID);

			System.out.println("在Servlet中的deleteHealthTracking : " + deleteHealthTracking);
			result = deleteHealthTracking; 
			// 回傳json型態
			response.getWriter().write(gson.toJson(result));
		}

	}
}
