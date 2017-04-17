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

import bbdp.db.model.DBConnection;

public class PatientFolderServer {
	//取得特定醫生檔案資訊
	public static String getDoctorFileInfo(Connection conn, String patientID, String doctorID){
		String jsonString = "";
		 try {
	            // queries the database
	            String sql = "SELECT * FROM file WHERE patientID = ? AND doctorID = ? ORDER BY time DESC";
	            
	            PreparedStatement statement = conn.prepareStatement(sql);
	            statement.setString(1, patientID);
	            statement.setString(2, doctorID);
	            
	            ResultSet resultSet = statement.executeQuery();
	            while (resultSet.next()) {
					//第一個物件
					if(jsonString.equals("")){
						jsonString = "{\"patientID\":\"" + resultSet.getString("patientID") + 
										"\",\"video\":\"" + resultSet.getString("video")+
										"\",\"doctorID\":\"" + resultSet.getString("doctorID")+
										"\",\"time\":\"" + resultSet.getString("time");
						
						if(resultSet.getString("description") == null){
							jsonString += "\",\"description\":\"" + "" + "\"}";
						}
						else{
							jsonString += "\",\"description\":\"" + resultSet.getString("description") + "\"}";
						}
						
					}
					else{
						jsonString += ",{\"patientID\":\"" + resultSet.getString("patientID") + 
								"\",\"video\":\"" + resultSet.getString("video")+
								"\",\"doctorID\":\"" + resultSet.getString("doctorID")+
								"\",\"time\":\"" + resultSet.getString("time"); 
						
						if(resultSet.getString("description") == null){
							jsonString += "\",\"description\":\"" + "" + "\"}";
						}
						else{
							jsonString += "\",\"description\":\"" +  resultSet.getString("description") + "\"}";
						}
					}
				}
	            
	            jsonString = "[" + jsonString +"]";
	            System.out.println(jsonString);
	            return jsonString;
	        } catch (SQLException ex) {
	            System.out.println("發生SQLException");
	        }
			return jsonString;
		}
	
	//顯示圖片
	static public InputStream getPhoto(Connection conn, String patientID, String time){
			try {
	           String sql = "SELECT * FROM file WHERE patientID = ? AND time = ?";
		            PreparedStatement statement = conn.prepareStatement(sql);
		            statement.setString(1, patientID);
		            statement.setString(2, time);
		            ResultSet result = statement.executeQuery();
		            
		            if (result.next()) {
		                Blob blob = result.getBlob("picture");		//picture欄位
		                InputStream inputStream = blob.getBinaryStream();
		                          
		                return inputStream;
		            }
		            else {
		            	System.out.println("找不到"+ patientID+"的檔案");
		            }
		        }
				catch (SQLException ex) {
		        	System.out.println("發生SQLException");
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
