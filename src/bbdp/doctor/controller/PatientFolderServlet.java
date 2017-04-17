package bbdp.doctor.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bbdp.db.model.DBConnection;
import bbdp.doctor.model.PatientFolderServer;


public class PatientFolderServlet extends HttpServlet {
	private static final int BUFFER_SIZE = 4096; 

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("application/json;charset=UTF-8");
    	HttpSession session = request.getSession();		
		String patientID = (String) session.getAttribute("patientID");
		//String patientID = "1000";
		String option = request.getParameter("option");     
        String time = request.getParameter("time");
        String doctorID = request.getParameter("doctorID");
        String video = request.getParameter("video");
       
        System.out.println("檔案夾patientID(session內容) : " +patientID);
        System.out.println("收到的doctorID:" + doctorID);
        System.out.println("收到的time:" + time);
        
        DBConnection db = (DBConnection) getServletContext().getAttribute("db");
        Connection conn = db.getConnection(); 
        //回傳特定醫生檔案資訊
        if(option.equals("getDoctorFileInfo")){	
        	response.setContentType("application/json;charset=UTF-8");
        	String jsonString = PatientFolderServer.getDoctorFileInfo(conn, patientID, doctorID);
            System.out.println(jsonString);
            response.getWriter().println(jsonString);	//輸出json至前端
        }
        //顯示圖片
        if(option.equals("getPhoto")){	
        	response.setContentType("image/*"); //設置返回的文件類型
        	InputStream inputStream = PatientFolderServer.getPhoto(conn, patientID, time);
        	OutputStream outStream = response.getOutputStream();
        	byte[] buffer = new byte[BUFFER_SIZE];
        	int bytesRead = -1;
                 
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outStream.close(); 
        }
        
      //顯示影片
        if(option.equals("getVideo")){
        	System.out.println("顯示病患影片");
            
            FileInputStream inStream = PatientFolderServer.getVideo(video);             
            OutputStream outStream = response.getOutputStream();
             
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
             
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
             
            inStream.close();
            outStream.close();   
        } 
        
        
    }
}