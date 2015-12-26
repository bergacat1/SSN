package model;

import java.util.List;

/**
 * Created by lluis on 1/11/15.
 */
public class Field_OLD {

    private String name;

    private int id;

    private List<Sport_OLD> sports;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Sport_OLD> getSports() {
        return sports;
    }

    public void setSports(List<Sport_OLD> sports) {
        this.sports = sports;
    }

    public boolean hasSport(Sport_OLD sport){
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

        Field_OLD field = (Field_OLD) o;

        return id == field.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
