package com.ssn.eps.ssn.wscaller;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * Created by lluis on 9/1/16.
 */
public class SerializerIntArray extends Vector<Integer> implements Serializable, KvmSerializable {

    @Override
    public Object getProperty(int i) {
        return this.get(i);
    }

    @Override
    public int getPropertyCount() {
        return this.size();
    }

    @Override
    public void setProperty(int i, Object o) {
        this.add((Integer)o);
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        propertyInfo.type = Integer.class;
        propertyInfo.name = "managerEntities";
    }

}
