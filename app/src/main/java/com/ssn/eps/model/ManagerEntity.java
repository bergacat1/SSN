package com.ssn.eps.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class ManagerEntity implements KvmSerializable {
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

		if(this.latitude == 0 && this.longitude == 0) return false;
		if(this.name.equals("")) return false;
		if(this.type < 0 || this.type > 1) return false;

		return true;
	}

	@Override
	public Object getProperty(int i) {
		switch (i){
			case 0:
				return idManagerEntity;
			case 1:
				return  idUser;
			case 2:
				return  type;
			case 3:
				return  name;
			case 4:
				return  address;
			case 5:
				return  city;
			case 6:
				return  latitude;
			case 7:
				return  longitude;
			case 8:
				return  telephone;
			case 9:
				return  email;
			case 10:
				return  web;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 11;
	}

	@Override
	public void setProperty(int i, Object o) {
		switch (i) {
			case 0:
				this.idManagerEntity = Integer.parseInt(o.toString());
				break;
			case 1:
				this.idUser = Integer.parseInt(o.toString());
				break;
			case 2:
				this.type = Integer.parseInt(o.toString());
				break;
			case 3:
				this.name = o.toString();
				break;
			case 4:
				this.address = o.toString();
				break;
			case 5:
				this.city = o.toString();
				break;
			case 6:
				this.latitude = Double.parseDouble(o.toString());
				break;
			case 7:
				this.longitude = Double.parseDouble(o.toString());
				break;
			case 8:
				this.telephone = Integer.parseInt(o.toString());
				break;
			case 9:
				this.email = o.toString();
				break;
			case 10:
				this.web = o.toString();
				break;
		}
	}

	@Override
	public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
		switch (i) {
			case 0:
				propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "idManagerEntity";
				break;
			case 1:
				propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "idUser";
				break;
			case 2:
				propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "type";
				break;
			case 3:
				propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "name";
				break;
			case 4:
				propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "address";
				break;
			case 5:
				propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "city";
				break;
			case 6:
				propertyInfo.type = Double.TYPE;
				propertyInfo.name = "latitude";
				break;
			case 7:
				propertyInfo.type = Double.TYPE;
				propertyInfo.name = "longitude";
				break;
			case 8:
				propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "telephone";
				break;
			case 9:
				propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "email";
				break;
			case 10:
				propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "web";
				break;
		}
	}
}
