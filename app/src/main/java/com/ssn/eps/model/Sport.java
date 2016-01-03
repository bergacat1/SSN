package com.ssn.eps.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Sport implements KvmSerializable {
	private int idSport;
	private String name;
	private int minPlayers, maxPlayers;
	private List<Field> fields;
	
	public Sport()
	{
		this.idSport = 0;
		this.name = "";
		this.maxPlayers = 0;
		this.minPlayers = 0;
		fields = new ArrayList<>();
	}

	public Sport(int idSport, String name, int maxPlayers, int minPlayers) {
		this.idSport = idSport;
		this.name = name;
		this.maxPlayers = maxPlayers;
		this.minPlayers = minPlayers;
		fields = new ArrayList<>();
	}

	public int getIdSport() {
		return idSport;
	}
	public void setIdSport(int idSport) {
		this.idSport = idSport;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMinPlayers() {
		return minPlayers;
	}
	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
	}
	public int getMaxPlayers() {
		return maxPlayers;
	}
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	@Override
	public Object getProperty(int i) {
		switch(i){
			case 0:
				return idSport;
			case 1:
				return name;
			case 2:
				return minPlayers;
			case 3:
				return maxPlayers;
			case 4:
				return fields;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 5;
	}

	@Override
	public void setProperty(int i, Object o) {
		switch(i){
			case 0:
				this.idSport = Integer.parseInt(o.toString());
                break;
			case 1:
				this.name = o.toString();
                break;
			case 2:
				this.minPlayers = Integer.parseInt(o.toString());
                break;
			case 3:
				this.maxPlayers = Integer.parseInt(o.toString());
                break;
			case 4:
				this.fields = (List<Field>)o;
                break;
		}
	}

	@Override
	public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
		switch(i){
			case 0:
				propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "idSport";
                break;
			case 1:
				propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "name";
                break;
			case 2:
				propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "minPlayers";
                break;
			case 3:
				propertyInfo.type = PropertyInfo.INTEGER_CLASS;
				propertyInfo.name = "maxPlayers";
                break;
			case 4:
				propertyInfo.type = List.class;
				propertyInfo.name = "fields";
                break;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
