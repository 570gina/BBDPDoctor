package bbdp.push.fcm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class PushToFCM {
	private static String POST_URL = "https://fcm.googleapis.com/fcm/send";
	private static String FCM_KEY = "AIzaSyDkFFJMsw8_XllqXljoSfW3lcdzv96-BA8";
	
	public static void sendNotification(String title, String body, String patientID) {
		String message = "{\"data\": {\"body\": \"" + body + "\",\"title\": \"" + title + "\"},\"notification\": {\"body\": \"" + body + "\",\"title\": \"" + title + "\"},\"to\": \"/topics/patient" + patientID + "\"}";
		
		try {
			URL obj = new URL(POST_URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", "key=" + FCM_KEY);

			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(message.getBytes());
			os.flush();
			os.close();

			int responseCode = con.getResponseCode();
			System.out.println("POST Response Code: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {		//success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				System.out.println(response.toString());		//print result
			} else {
				System.out.println("POST request not worked");
			}
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException");
		} catch (ProtocolException e) {
			System.out.println("ProtocolException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}
}