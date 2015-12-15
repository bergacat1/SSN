package model;

/**
 * Created by lluis on 1/11/15.
 */
public class User extends User_ {

    private String name;
    private String surname;
    private String city;
    private int age;
    private String fav_sport;
    private String email;

    public User(String name, String surname, String city, int age, String fav_sport, String email){
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.age = age;
        this.fav_sport = fav_sport;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFav_sport() {
        return fav_sport;
    }

    public void setFav_sport(String fav_sport) {
        this.fav_sport = fav_sport;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
