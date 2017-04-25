package bbdp.doctor.model;

public class Notification {
	private String doctorID;
	private String patientID;
	private String time;
	private String hyperlink;
	private String title;
	private String body;

	public Notification(String doctorID, String patientID, String time, String hyperlink, String title, String body) {
		this.doctorID = doctorID;
		this.patientID = patientID;
		this.time = time;
		this.hyperlink = hyperlink;
		this.title = title;
		this.body = body;
	}

	public String getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(String doctorID) {
		this.doctorID = doctorID;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHyperlink() {
		return hyperlink;
	}

	public void setHyperlink(String hyperlink) {
		this.hyperlink = hyperlink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}