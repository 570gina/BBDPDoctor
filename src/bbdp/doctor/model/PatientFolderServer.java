package bbdp.doctor.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PatientFolderServer {
	//取得特定醫生檔案資訊
	public static String getDoctorFileInfo(Connection conn, String patientID, String doctorID){
		String jsonString = "";
		JSONArray fileArray = new JSONArray();
		PreparedStatement statement = null;
		
		try {
			String sql = "SELECT video, time, description FROM file WHERE patientID = ? AND doctorID = ? ORDER BY time DESC";
			statement = conn.prepareStatement(sql);
			statement.setString(1, patientID);
			statement.setString(2, doctorID);		            
			ResultSet resultSet = statement.executeQuery();
			//patientID video time description
			while (resultSet.next()) {
				JSONObject fileObject = new JSONObject();
				fileObject.put("patientID", patientID);
				
				if(resultSet.getString("video").equals("")){
					fileObject.put("video", "");            		
				}
				else{
					fileObject.put("video", resultSet.getString("video"));
				}
				
				if(resultSet.getString("description") == null){
					fileObject.put("description", "");
				}
				else{
					fileObject.put("description", resultSet.getString("description"));
				}
				
				fileObject.put("time", resultSet.getString("time"));
				
				fileArray.put(fileObject);
			}
			if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	            
			jsonString = fileArray.toString();
			//System.out.println(jsonString);
			return jsonString;
		} catch (SQLException ex) {
	        System.out.println("發生SQLException");
		}
		catch (JSONException e) {
			System.out.println("發生JSONException: " + e);
		}
		finally {
			 if (statement != null) try {statement.close();}catch (SQLException ignore) {}
			 if (conn!=null) try {conn.close();}catch (Exception ignore) {}
		 }
			return jsonString;
		}
	
	static public String getSelectFileInfo(Connection conn, String patientID, String time){
		String jsonString = "";
		JSONArray fileArray = new JSONArray();
		PreparedStatement statement = null;

		try {
			String sql = "SELECT video, time, description FROM file WHERE patientID = ? AND time = ?";
			statement = conn.prepareStatement(sql);
			statement.setString(1, patientID);
			statement.setString(2, time);		            
			ResultSet resultSet = statement.executeQuery();
			//patientID video time description
			while (resultSet.next()) {
				JSONObject fileObject = new JSONObject();
				fileObject.put("patientID", patientID);
				
				if(resultSet.getString("video").equals("")){
					fileObject.put("video", "");            		
				}
				else{
					fileObject.put("video", resultSet.getString("video"));
				}
				
				if(resultSet.getString("description") == null){
					fileObject.put("description", "");
				}
				else{
					fileObject.put("description", resultSet.getString("description"));
				}
				
				fileObject.put("time", resultSet.getString("time"));
				
				fileArray.put(fileObject);
			}
			if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	            
			jsonString = fileArray.toString();
			//System.out.println("getSelectFileInfo:"+jsonString);
			return jsonString;
		} catch (SQLException ex) {
	            System.out.println("發生SQLException");
		}
		catch (JSONException e) {
			System.out.println("發生JSONException: " + e);
		}
		finally {
			 if (statement != null) try { statement.close();}catch (SQLException ignore) {}
			 if (conn!=null) try {conn.close();}catch (Exception ignore) {}
		 }
			return jsonString;
	}
	
	//顯示圖片
	static public InputStream getPhoto(Connection conn, String patientID, String time){
		PreparedStatement statement = null;
		InputStream inputStream = null;
		try {
            String sql = "SELECT picture FROM file WHERE patientID = ? AND time = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, patientID);
            statement.setString(2, time);
            ResultSet resultSet = statement.executeQuery();
            
            
            if (resultSet.next()) {
                Blob blob = resultSet.getBlob("picture");
                if(blob!=null){
                	inputStream = blob.getBinaryStream();
                }
                if (resultSet != null) try {resultSet.close();} catch (SQLException ignore) {}          
            }
            else {
            	System.out.println("找不到"+ patientID+"的檔案");
            }
            
        }
		catch (SQLException ex) {
        	System.out.println("發生SQLException");
        }
		finally {
            if (statement != null) try {statement.close();}catch (SQLException ignore) {}
            if (conn!=null) try {conn.close();}catch (Exception ignore) {}
        }
		return inputStream; 
	}
	//顯示縮圖
	static public InputStream getSmallPhoto(Connection conn, String patientID, String time){
			PreparedStatement statement = null;
			InputStream inputStream = null;	
			
			try {
				String sql = "SELECT preview FROM file WHERE patientID = ? AND time = ?";
				statement = conn.prepareStatement(sql);
				statement.setString(1, patientID);
				statement.setString(2, time);
				ResultSet resultSet = statement.executeQuery();				 
				 
				if (resultSet.next()) {
					Blob blob = resultSet.getBlob("preview");		//preview欄位
					if(blob!=null){
						inputStream = blob.getBinaryStream();
					}    
					if (resultSet != null) try {resultSet.close();} catch (SQLException ignore) {}          
				}
				else {
					System.out.println("找不到"+ patientID+"的檔案");
				}
		            
			}
			catch (SQLException ex) {
				System.out.println("顯示縮圖發生SQLException");
			}
			finally {
				if (statement != null) try {statement.close();}catch (SQLException ignore) {}
				if (conn!=null) try {conn.close();}catch (Exception ignore) {}
			}
			return inputStream; 
	}
	//顯示影片
	static public FileInputStream getVideo(String videoPath){
		File downloadFile = new File(videoPath);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(downloadFile);
		} catch (FileNotFoundException e) {
			System.out.println("發生FileNotFoundException");
		}
		return inputStream;
	}
	
}
