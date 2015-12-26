package model;

public class ReportType {
	private int idReportType;
	private String name;
	private int severity;
	public int getIdReportType() {
		return idReportType;
	}
	public void setIdReportType(int idReportType) {
		this.idReportType = idReportType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSeverity() {
		return severity;
	}
	public void setSeverity(int severity) {
		this.severity = severity;
	}	
}
