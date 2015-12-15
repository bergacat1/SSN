package model;

import java.util.List;

/**
 * Created by lluis on 1/11/15.
 */
public class Field {

    private String name;

    private int id;

    private List<Sport> sports;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Sport> getSports() {
        return sports;
    }

    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }

    public boolean hasSport(Sport sport){
        return sports.contains(sport);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        return id == field.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
