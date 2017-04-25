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
        
        DBConnection db = (DBConnection) getServletContext().getAttribute("db");
        Connection conn = db.getConnection();
        
        //特定醫生檔案列表
        if(option.equals("getDoctorFileInfo")){
        	response.setContentType("application/json;charset=UTF-8");
        	String jsonString = PatientFolderServer.getDoctorFileInfo(conn, patientID, doctorID);
            response.getWriter().write(jsonString);
        }
        //顯示圖片
        else if(option.equals("getPhoto")){	
        	response.setContentType("image/*");
        	
        	InputStream inputStream = null;
        	OutputStream outStream = null;
        	
        	try{
	        	inputStream = PatientFolderServer.getPhoto(conn, patientID, time);
	        	outStream = response.getOutputStream();
	        	byte[] buffer = new byte[BUFFER_SIZE];
	        	int bytesRead = -1;
	                 
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                    outStream.write(buffer, 0, bytesRead);
	            }
        	}
        	finally {
        		if (inputStream != null){
        			inputStream.close(); 
        			System.out.println("顯示圖片後關閉inputStream");
        	    }
        	    if (outStream != null) {
        	    	outStream.close();
        	    	System.out.println("顯示圖片後關閉outputStream");
        	    }
        	}
        }
        
      //顯示影片
        if(option.equals("getVideo")){
        	System.out.println("顯示病患影片");
            
        	FileInputStream inputStream = null;             
            OutputStream outStream = null;
        	
            try{
	            inputStream = PatientFolderServer.getVideo(video);             
	            outStream = response.getOutputStream();
	             
	            byte[] buffer = new byte[4096];
	            int bytesRead = -1;
	             
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outStream.write(buffer, 0, bytesRead);
	            }
            }
            finally {
	    	    if (inputStream != null){
	    	    	inputStream.close(); 
	    	    	System.out.println("顯示影片後關閉inputStream");
	    	    }
	    	    if (outStream != null) {
	    	    	outStream.close();
	    	    	System.out.println("顯示影片後關閉outputStream");
	    	    }
            }    
        } 
        
        
    }
}