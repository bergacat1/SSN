package model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lluis on 1/11/15.
 */
public abstract class ManagerEntity implements Serializable{

    private String name;

    private String address;
    private double latitude;
    private double longitude;

    private List<Field> fields;
}
