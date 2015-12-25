package model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by lluis on 24/12/15.
 */
public class Result<T> implements KvmSerializable{

    private List<T> data;
    private boolean valid;
    private String error;

    public Result(){
        data = new ArrayList<>();
        valid = true;
        error = "";
    }

    public Result(List<T> data, boolean valid, String error){
        this.data = data;
        this.valid = valid;
        this.error = error;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void addData(T element){
        this.data.add(element);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public Object getProperty(int i) {
        switch (i){
            case 0:
                return data;
            case 1:
                return valid;
            case 2:
                return error;
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
                data = (List<T>) o;
            case 1:
                valid = Boolean.getBoolean(o.toString());
            case 2:
                error = o.toString();
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i){
            case 0:
                propertyInfo.type = PropertyInfo.OBJECT_TYPE;
                propertyInfo.name = "data";
            case 1:
                propertyInfo.type = PropertyInfo.BOOLEAN_CLASS;
                propertyInfo.name = "valid";
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "error";
        }
    }
}
