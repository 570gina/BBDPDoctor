package bbdp.doctor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.jdbc.pool.DataSource;

import bbdp.doctor.model.PatientFolderServer;

public class PatientFolderServlet extends HttpServlet {
	private static final int BUFFER_SIZE = 4096; 

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
    	//取得session
    	HttpSession session = request.getSession();		
		String patientID = (String) session.getAttribute("patientID");
		String doctorID = (String) session.getAttribute("doctorID");
		
		String option = request.getParameter("option");     
        String time = request.getParameter("time");
        String video = request.getParameter("video");
       
        //System.out.println("檔案夾patientID(session) : " +patientID);
        //System.out.println("檔案夾doctorID(session) : " + doctorID);
        
        //資料庫連線
        DataSource datasource = (DataSource) getServletContext().getAttribute("db");
        Connection conn = null;
		try {
			conn = datasource.getConnection();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
        
        //特定醫生檔案列表
        if(option.equals("getDoctorFileInfo")){
        	response.setContentType("application/json;charset=UTF-8");
        	String jsonString = PatientFolderServer.getDoctorFileInfo(conn, patientID, doctorID);
            response.getWriter().write(jsonString);
        }
        else if(option.equals("getSelectFileInfo")){
        	response.setContentType("application/json;charset=UTF-8");
        	String jsonString = PatientFolderServer.getSelectFileInfo(conn, patientID, time);
            response.getWriter().write(jsonString);
        }
        else if(option.equals("getPhoto")){	//顯示圖片
        	response.setContentType("image/*");
        	
        	InputStream inputStream = null;
        	OutputStream outStream = null;
	        inputStream = PatientFolderServer.getPhoto(conn, patientID, time);
	       	outStream = response.getOutputStream();
	       	byte[] buffer = new byte[BUFFER_SIZE];
	       	int bytesRead = -1;
	       	
	    	if(inputStream!=null){     
		     	while ((bytesRead = inputStream.read(buffer)) != -1) {
		        	outStream.write(buffer, 0, bytesRead);
		     	}
	    	}

	    	if (inputStream != null){
        		inputStream.close(); 
	    	}
	    	if (outStream != null) {
        		outStream.close();
	    	}
        	
        }
        else if(option.equals("getSmallPhoto")){					//顯示縮圖
        	response.setContentType("image/*");
        	
        	InputStream inputStream = null;
        	OutputStream outStream = null;
        	
        	inputStream = PatientFolderServer.getSmallPhoto(conn, patientID, time);
        	outStream = response.getOutputStream();
	        	
        	byte[] buffer = new byte[BUFFER_SIZE];
        	int bytesRead = -1;
        	
	        if(inputStream!=null){     
	        	while ((bytesRead = inputStream.read(buffer)) != -1) {
	        		outStream.write(buffer, 0, bytesRead);
	        	}
	        }
	        
        	if (inputStream != null){
        		inputStream.close(); 
        	}
        	if (outStream != null) {
        		outStream.close();
        	}          
        }
        if(option.equals("getVideo")){		//顯示影片
        	String videoType = video.substring(video.lastIndexOf(46) + 1, video.length());
        	if(videoType.equals("mov")||videoType.equals("MOV")){
        		response.setContentType("video/quicktime");
        	}
        	else{
        		response.setContentType("video/" + video.substring(video.lastIndexOf(46) + 1, video.length()));
        	}
        	
        	FileInputStream inputStream = null;             
        	ServletOutputStream outputStream = response.getOutputStream();
        	byte[] buffer = new byte[4096];
            int bytesRead;
        	
            String outputType = "firstType";
        	
        	String browserDetails = request.getHeader("User-Agent").toLowerCase();
        	//System.out.println("browserDetails : "+browserDetails);
        	
        	if(browserDetails.indexOf("iphone") >= 0 || browserDetails.indexOf("ipad") >= 0){
        		outputType = "secondType";		//iphone or ipad
        	}
        	else if(browserDetails.indexOf("macintosh") >= 0 && !browserDetails.contains("chrome")){
       		 	outputType = "secondType";		//mac + safari
       	 	}
        	else if (browserDetails.indexOf("windows") >= 0 || browserDetails.toLowerCase().indexOf("android") >= 0){
        		 outputType = "firstType";		//windows or android
        	} 
        	else if(browserDetails.indexOf("macintosh") >= 0 && browserDetails.contains("chrome")){
        		 outputType = "firstType";		//mac + chrome
        	}
        	
        	//System.out.println("輸出類型："+outputType);
        	
            try {
            	inputStream = PatientFolderServer.getVideo(video); 
            	
            	if(outputType.equals("firstType") && inputStream!=null){            		  			
    	    		if(inputStream!=null){
    	        		while ((bytesRead = inputStream.read(buffer)) != -1) {
    	        			outputStream.write(buffer, 0, bytesRead);
    		            }
        			}
            	}
            	else if(outputType.equals("secondType") && inputStream!=null){
            		String range = request.getHeader("Range");
        			//System.out.println("Range:"+range);
        		
	        		if( range != null && !range.equals("bytes=0-")){
	                    //System.out.println("Range:"+range);
	        			//System.out.println("setStatus(206)");
	
	                    String[] ranges = range.split("=")[1].split("-");
	                    int from = Integer.parseInt(ranges[0]);
	                    int to = Integer.parseInt(ranges[1]);
	                    int len = to - from + 1 ;
	
	                    response.setStatus(206);
	                    response.setHeader("Accept-Ranges", "bytes");
	                    File f = new File(video);
	                    String responseRange = String.format("bytes %d-%d/%d", from, to, f.length());
	                    //System.out.println("Content-Range:" + responseRange);
	                    response.setHeader("Connection", "close");
	                    response.setHeader("Content-Range", responseRange);
	                    response.setDateHeader("Last-Modified", new Date().getTime());
	                    response.setContentLength(len);
	                    //System.out.println("length:" + len);
	
	                    byte[] buf = new byte[4096];
	                    inputStream.skip(from);
	                    while( len != 0) {
	                        int read = inputStream.read(buf, 0, len >= buf.length ? buf.length : len);
	                        if( read != -1) {
	                        	outputStream.write(buf, 0, read);
	                            len -= read;
	                        }
	                    }
	                }
            	}
        		
    		}
    		catch (FileNotFoundException e) {
    			System.out.println("發生FileNotFoundException : " + e);
    		}
        	finally{
        		if(inputStream!=null){
        			inputStream.close();
        			System.out.println("顯示影片inputStream.close()");
        		}
        		if (outputStream != null) {
        			outputStream.flush();
        			outputStream.close();
        			System.out.println("顯示影片outStream.close()");
        		}
        	}
               
        } 
        
        if (conn!=null) try {conn.close();}catch (Exception ignore) {}
        
    }
}