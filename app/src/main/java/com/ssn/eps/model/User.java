package com.ssn.eps.model;


import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class User implements KvmSerializable{
	private int id;
	private String username;
	private String email;
	private String name;
	private String surname1;
	private String surname2;
	private int currentAccount;
	private int telephone;
	private int type;
	private String gcmId;

	//Configuració
	private String mapType;
	private boolean edificios3D;
	private boolean notifNewEventManaged;
	private boolean notifNewEventUnmanaged;
	private boolean notifNewMemberEvent;


	public User() {
		this.id = 0;
		this.username = "";
		this.email = "";
		this.name = "";
		this.surname1 = "";
		this.surname2 = "";
		this.currentAccount = 0;
		this.telephone = 0;
		this.type = 0;
		this.gcmId = "";
		this.mapType = "1";
		this.edificios3D = true;
		this.notifNewEventManaged = true;
		this.notifNewEventUnmanaged = true;
		this.notifNewMemberEvent = true;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getCurrentAccount() {
		return currentAccount;
	}
	public void setCurrentAccount(int currentAccount) {
		this.currentAccount = currentAccount;
	}
	public int getTelephone() {
		return telephone;
	}
	public void setTelephone(int telephone) {
		this.telephone = telephone;
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
	public String getSurname1() {
		return surname1;
	}
	public void setSurname1(String surname1) {
		this.surname1 = surname1;
	}
	public String getSurname2() {
		return surname2;
	}
	public void setSurname2(String surname2) {
		this.surname2 = surname2;
	}
	public String getGcmId() {
		return gcmId;
	}
	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	public boolean isEdificios3D() {
		return edificios3D;
	}

	public void setEdificios3D(boolean edificios3D) {
		this.edificios3D = edificios3D;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public boolean isNotifNewEventManaged() {
		return notifNewEventManaged;
	}

	public void setNotifNewEventManaged(boolean notifNewEventManaged) {
		this.notifNewEventManaged = notifNewEventManaged;
	}

	public boolean isNotifNewEventUnmanaged() {
		return notifNewEventUnmanaged;
	}

	public void setNotifNewEventUnmanaged(boolean notifNewEventUnmanaged) {
		this.notifNewEventUnmanaged = notifNewEventUnmanaged;
	}

	public boolean isNotifNewMemberEvent() {
		return notifNewMemberEvent;
	}

	public void setNotifNewMemberEvent(boolean notifNewMemberEvent) {
		this.notifNewMemberEvent = notifNewMemberEvent;
	}

	@Override
	public String toString() {
		return username;
	}

	@Override
	public Object getProperty(int i) {
		switch(i){
			case 0:
				return id;
			case 1:
				return username;
			case 2:
				return email;
			case 3:
				return name;
			case 4:
				return surname1;
			case 5:
				return surname2;
			case 6:
				return currentAccount;
			case 7:
				return telephone;
			case 8:
				return type;
			case 9:
				return gcmId;
			case 10:
				StringBuilder sb = new StringBuilder();
				sb.append(mapType);
				sb.append(edificios3D ? "1" : "0");
				sb.append(notifNewEventManaged ? "1" : "0");
				sb.append(notifNewEventUnmanaged ? "1" : "0");
				sb.append(notifNewMemberEvent ? "1" : "0");
				return sb.toString();
		}
        return null;
	}

	@Override
	public int getPropertyCount() {
		return 11;
	}

	@Override
	public void setProperty(int i, Object o) {
        switch(i){
            case 0:
                this.id = Integer.parseInt(o.toString());
                break;
            case 1:
                this.username = o.toString();
                break;
            case 2:
                this.email = o.toString();
                break;
            case 3:
                this.name = o.toString();
                break;
            case 4:
                this.surname1 = o.toString();
                break;
            case 5:
                this.surname2 = o.toString();
                break;
            case 6:
                this.currentAccount = Integer.parseInt(o.toString());
                break;
            case 7:
                this.telephone = Integer.parseInt(o.toString());
                break;
            case 8:
                this.type = Integer.parseInt(o.toString());
                break;
            case 9:
                this.gcmId = o.toString();
                break;
			case 10:
				String settings = o.toString();
				this.mapType = String.valueOf(settings.charAt(0));
				this.edificios3D = settings.charAt(1) == '1';
				this.notifNewEventManaged = settings.charAt(2) == '1';
				this.notifNewEventUnmanaged = settings.charAt(3) == '1';
				this.notifNewMemberEvent = settings.charAt(4) == '1';
				break;
        }
	}

	@Override
	public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i){
            case 0:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "id";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "username";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "email";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "name";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "surname1";
                break;
            case 5:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "surname2";
                break;
            case 6:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "currentAccount";
                break;
            case 7:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "telephone";
                break;
            case 8:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "type";
                break;
            case 9:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "gcmId";
                break;
			case 10:
				propertyInfo.type = PropertyInfo.STRING_CLASS;
				propertyInfo.name = "settings";
        }
	}
}
