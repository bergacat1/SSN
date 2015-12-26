package model;


public class FieldSports {
	
	private int idSport;
	private String sportName;
	private double hourPrice;
	
	public double getHourPrice() {
		return hourPrice;
	}
	public void setHourPrice(double hourPrice) {
		this.hourPrice = hourPrice;
	}
	public int getIdSport() {
		return idSport;
	}
	public void setIdSport(int idSport) {
		this.idSport = idSport;
	}
	public String getSportName() {
		return sportName;
	}
	public void setSportName(String sportName) {
		this.sportName = sportName;
	}
}
