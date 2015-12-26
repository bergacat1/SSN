package com.ssn.eps.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import model.FieldSports;

public class Field implements KvmSerializable{
	private int idField;
	private int idManagerEntity;
	private String name;
	private List<FieldSports> sports;
	
	public Field()
	{
		idField = 0;
		idManagerEntity = 0;
		name = "";
		sports = new ArrayList<>();
	}

	public Field(int idField, int idManagerEntity, String name) {
		this.idField = idField;
		this.idManagerEntity = idManagerEntity;
		this.name = name;
		sports = new ArrayList<>();
	}

	public int getIdField() {
		return idField;
	}

	public void setIdField(int idField) {
		this.idField = idField;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FieldSports> getSports() {
		return sports;
	}

	public void setSports(List<FieldSports> sports) {
		this.sports = sports;
	}

	public int getIdManagerEntity() {
		return idManagerEntity;
	}

	public void setIdManagerEntity(int idManagerEntity) {
		this.idManagerEntity = idManagerEntity;
	}


	@Override
	public Object getProperty(int i) {
		switch(i){
			case 0:
				return idField;
			case 1:
				return idManagerEntity;
			case 2:
				return name;
			case 3:
				return sports;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 4;
	}

	@Override
	public void setProperty(int i, Object o) {
        switch(i){
            case 0:
                this.idField = Integer.parseInt(o.toString());
                break;
            case 1:
                this.idManagerEntity = Integer.parseInt(o.toString());
                break;
            case 2:
                this.name = o.toString();
                break;
            case 3:
                this.sports = (List<FieldSports>)o;
                break;
        }
	}

	@Override
	public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

	}
}
