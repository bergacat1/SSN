package model;

import java.util.Date;


public class Reservation {
	private int idReservation;
	private int idEvent;
	private int idField;
	private long startDate, endDate;
	private boolean confirmed;
	private int type;
	
    public String toStartDate(){
		return new Date(startDate).toString(); 
    }
    
    public String toEndDate(){
		return new Date(endDate).toString(); 
    }
    
    public String getDurationMin(){
		return Long.toString((endDate - startDate)/(60*1000)); 
    }
	
	public int getIdReservation() {
		return idReservation;
	}
	public void setIdReservation(int idReservation) {
		this.idReservation = idReservation;
	}
	public int getIdEvent() {
		return idEvent;
	}
	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}
	public int getIdField() {
		return idField;
	}
	public void setIdField(int idField) {
		this.idField = idField;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}	
}
