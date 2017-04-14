package bbdp.doctor.model;

public class Notification {
	private String doctorID;
	private String patientID;
	private String time;
	private String hyperlink;
	private String content;

	public Notification(String doctorID, String patientID, String time, String hyperlink, String content) {
		this.doctorID = doctorID;
		this.patientID = patientID;
		this.time = time;
		this.hyperlink = hyperlink;
		this.content = content;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}