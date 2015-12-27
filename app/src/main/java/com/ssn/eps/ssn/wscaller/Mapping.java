package com.ssn.eps.ssn.wscaller;

/**
 * Created by lluis on 24/12/15.
 */
public class Mapping {
    private String name;
    private Class aClass;

    public Mapping() {
    }

    public Mapping(String name, Class aClass) {
        this.name = name;
        this.aClass = aClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
