package com.ssn.eps.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class Event implements KvmSerializable{
	private int idEvent;
	private int idCreator;
	private int idSport;
    private String sportName;
    private int actualPlayers;
	private int minPlayers;
	private int maxPlayers;
	private Calendar startDate;
	private Calendar endDate;
	private String city;
	private double latitude;
	private double longitude;
	private int range;
	private double maxPrice;
	private List<Integer> managerEntities;

    public Event() {
        this.city = "";
        this.endDate = Calendar.getInstance();
        this.idCreator = 0;
        this.idEvent = 0;
        this.idSport = 0;
        this.sportName = "";
        this.latitude = 0;
        this.longitude = 0;
        this.managerEntities = new ArrayList<Integer>();
        this.actualPlayers = 0;
        this.minPlayers = 0;
        this.maxPlayers = 0;
        this.maxPrice = 0;
        this.range = 0;
        this.startDate = Calendar.getInstance();
    }

    public Event(String city, Calendar endDate, int idCreator, int idEvent, int idSport, double latitude, double longitude,
				 List<Integer> managerEntities, int maxPlayers, double maxPrice, int minPlayers, int range, Calendar startDate) {
		this.city = city;
		this.endDate = endDate;
		this.idCreator = idCreator;
		this.idEvent = idEvent;
		this.idSport = idSport;
		this.latitude = latitude;
		this.longitude = longitude;
		this.managerEntities = managerEntities;
		this.maxPlayers = maxPlayers;
		this.maxPrice = maxPrice;
		this.minPlayers = minPlayers;
		this.range = range;
		this.startDate = startDate;
	}

	public int getIdEvent() {
		return idEvent;
	}
	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}
	public int getIdCreator() {
		return idCreator;
	}
	public void setIdCreator(int idCreator) {
		this.idCreator = idCreator;
	}
	public int getIdSport() {
		return idSport;
	}
	public void setIdSport(int idSport) {
		this.idSport = idSport;
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
	public Calendar getStartDate() {
		return startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	public Calendar getEndDate() {
		return endDate;
	}
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
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
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public List<Integer> getManagerEntities() {
		return managerEntities;
	}
	public void setManagerEntities(List<Integer> managerEntities) {
		this.managerEntities = managerEntities;
	}
	public double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	@Override
	public Object getProperty(int i) {
		switch(i){
            case 0:
                return this.idEvent;
            case 1:
                return this.idCreator;
            case 2:
                return this.idSport;
            case 3:
                return this.minPlayers;
            case 4:
                return this.maxPlayers;
            case 5:
                return this.startDate.getTime();
            case 6:
                return this.endDate.getTime();
            case 7:
                return this.city;
            case 8:
                return this.latitude;
            case 9:
                return this.longitude;
            case 10:
                return this.range;
            case 11:
                return this.maxPrice;
            case 12:
                return this.managerEntities;
            case 13:
                return this.sportName;
            case 14:
                return this.actualPlayers;
		}
        return null;
	}

	@Override
	public int getPropertyCount() {
		return 15;
	}

	@Override
	public void setProperty(int i, Object o) {
        Calendar c = null;
        switch(i){
            case 0:
                this.idEvent = Integer.parseInt(o.toString());
                break;
            case 1:
                this.idCreator = Integer.parseInt(o.toString());
                break;
            case 2:
                this.idSport = Integer.parseInt(o.toString());
                break;
            case 3:
                this.minPlayers = Integer.parseInt(o.toString());
                break;
            case 4:
                this.maxPlayers = Integer.parseInt(o.toString());
                break;
            case 5:
                c = Calendar.getInstance();
                c.setTimeInMillis(Long.parseLong(o.toString()));
                this.startDate = c;
                break;
            case 6:
                c = Calendar.getInstance();
                c.setTimeInMillis(Long.parseLong(o.toString()));
                this.endDate = c;
                break;
            case 7:
                this.city = o.toString();
                break;
            case 8:
                this.latitude = Double.parseDouble(o.toString());
                break;
            case 9:
                this.longitude = Double.parseDouble(o.toString());
                break;
            case 10:
                this.range = Integer.parseInt(o.toString());
                break;
            case 11:
                this.maxPrice = Double.parseDouble(o.toString());
                break;
            case 12:
                this.managerEntities = (List<Integer>)o;
                break;
            case 13:
                this.sportName = o.toString();
                break;
            case 14:
                this.actualPlayers = Integer.parseInt(o.toString());
                break;
        }
	}

	@Override
	public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i){
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idEvent";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idCreator";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "idSport";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "minPlayers";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "maxPlayers";
                break;
            case 5:
                propertyInfo.type = Calendar.class;
                propertyInfo.name = "startDate";
                break;
            case 6:
                propertyInfo.type = Calendar.class;
                propertyInfo.name = "endDate";
                break;
            case 7:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "city";
                break;
            case 8:
                propertyInfo.type = Double.TYPE;
                propertyInfo.name = "latitude";
                break;
            case 9:
                propertyInfo.type = Double.TYPE;
                propertyInfo.name = "longitude";
                break;
            case 10:
                propertyInfo.type = Double.TYPE;
                propertyInfo.name = "range";
                break;
            case 11:
                propertyInfo.type = Double.TYPE;
                propertyInfo.name = "maxPrice";
                break;
            case 12:
                propertyInfo.type = List.class;
                propertyInfo.name = "managerEntities";
                break;
            case 13:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "sportName";
                break;
            case 14:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "actualPlayers";
                break;
        }
	}

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public int getActualPlayers() {
        return actualPlayers;
    }

    public void setActualPlayers(int actualPlayers) {
        this.actualPlayers = actualPlayers;
    }
}
