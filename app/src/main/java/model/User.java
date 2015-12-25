package model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by lluis on 1/11/15.
 */
public class User extends User_ implements KvmSerializable{

    private int id;

    private String email;
    private String userName;
    private String GCMId;

    public User(){}

    public User(String email, String userName, String GCMId){
        this.email = email;
        this.userName = userName;
        this.GCMId = GCMId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGCMId() {
        return GCMId;
    }

    public void setGCMId(String GCMId) {
        this.GCMId = GCMId;
    }

    @Override
    public Object getProperty(int i) {
        switch (i){
            case 0:
                return email;
            case 1:
                return userName;
            case 2:
                return GCMId;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 3;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i){
            case 0:
                email = o.toString();
                break;
            case 1:
                userName = o.toString();
                break;
            case 2:
                GCMId = o.toString();
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i){
            case 0:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "email";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "userName";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "GCMId";
                break;
        }
    }
}
