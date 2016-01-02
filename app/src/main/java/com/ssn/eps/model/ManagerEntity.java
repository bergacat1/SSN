package com.ssn.eps.model;

public class ManagerEntity {
	private int idManagerEntity;
	private int idUser;
	private int type;
	private String name;
	private String address;
	private String city;
	private double latitude;
	private double longitude;
	private int telephone;
	private String email;
	private String web;
	public int getIdManagerEntity() {
		return idManagerEntity;
	}
	public void setIdManagerEntity(int idManagerEntity) {
		this.idManagerEntity = idManagerEntity;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getTelephone() {
		return telephone;
	}
	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public boolean isValidForPrint() {

		if(this.latitude == 0 & this.longitude == 0) return false;
		if(this.name.equals("")) return false;
		if(this.type < 1 || this.type > 2) return false;

		return true;
	}
}
