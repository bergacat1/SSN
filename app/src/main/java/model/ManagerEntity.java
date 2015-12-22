package model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lluis on 1/11/15.
 */
public abstract class ManagerEntity implements Serializable{

    private int id;
    private String name;

    private String address;
    private double latitude;
    private double longitude;

    private List<Field> fields;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean hasSportField(Sport sport){
        return fields.contains(sport);
    }
}
