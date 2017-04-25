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

import bbdp.db.model.DBConnection;

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
			if (resultSet != null) try { resultSet.close();System.out.println("顯示列表後關閉ResultSet"); } catch (SQLException ignore) {}
	            
			jsonString = fileArray.toString();
			System.out.println(jsonString);
			return jsonString;
		} catch (SQLException ex) {
	            System.out.println("發生SQLException");
		}
		catch (JSONException e) {
			System.out.println("發生JSONException: " + e);
		}
		finally {
			 if (statement != null) 
				 try { statement.close();System.out.println("顯示列表後關閉PreparedStatement"); }
			 	catch (SQLException ignore) {}
		 }
			return jsonString;
		}
	
	//顯示圖片
	static public InputStream getPhoto(Connection conn, String patientID, String time){
		PreparedStatement statement = null;
		
		try {
            String sql = "SELECT picture FROM file WHERE patientID = ? AND time = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, patientID);
            statement.setString(2, time);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                Blob blob = resultSet.getBlob("picture");
                InputStream inputStream = blob.getBinaryStream();
                
                if (resultSet != null) try { resultSet.close();System.out.println("顯示圖片後關閉ResultSet"); } catch (SQLException ignore) {}          
                
                return inputStream;
            }
            else {
            	System.out.println("找不到"+ patientID+"的檔案");
            }
            
        }
		catch (SQLException ex) {
        	System.out.println("發生SQLException");
        }
		finally {
            if (statement != null) 
            	try { statement.close();System.out.println("顯示圖片後關閉PreparedStatement"); }
            catch (SQLException ignore) {}
        }
		return null; 
	}
	
	//顯示影片
	static public FileInputStream getVideo(String videoPath){
		File downloadFile = new File(videoPath);
		try {
			FileInputStream inStream = new FileInputStream(downloadFile);
			return inStream;
		} catch (FileNotFoundException e) {
			System.out.println("發生FileNotFoundException");
		}
		return null;
	}
	
}
