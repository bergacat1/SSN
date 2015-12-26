package model;

public class Report {
	private int idReport;
	private int idReportType;
	private int idUser;
	private int idField;
	private int idReporter;
	private long date;
	private String comment;
	public int getIdReport() {
		return idReport;
	}
	public void setIdReport(int idReport) {
		this.idReport = idReport;
	}
	public int getIdReportType() {
		return idReportType;
	}
	public void setIdReportType(int idReportType) {
		this.idReportType = idReportType;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public int getIdField() {
		return idField;
	}
	public void setIdField(int idField) {
		this.idField = idField;
	}
	public int getIdReporter() {
		return idReporter;
	}
	public void setIdReporter(int idReporter) {
		this.idReporter = idReporter;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
