package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by lluis on 1/11/15.
 */
public class Event implements Serializable{

    public enum State {
        NEW, FILLING, FULL, RESERVED
    }

    private User_ owner;
    private Sport sport;

    private int minPlayers;
    private int maxPlayers;

    private Date eventDate;
    private Date creationDate;
    private Date limitDate;

    private State state;
    private int price;
    private String zone;

    private List<User> players_list;

    public Event(Sport sport, int minPlayers, int maxPlayers, int price, Date eventDate, Date creationDate, Date limitDate, State state, String zone) {
        this.sport = sport;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.price = price;
        this.eventDate = eventDate;
        this.creationDate = creationDate;
        this.limitDate = limitDate;
        this.state = state;
        this.zone = zone;
    }

    public User_ getOwner() {
        return owner;
    }

    public void setOwner(User_ owner) {
        this.owner = owner;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void addPlayer(User user){
        players_list.add(user);
    }

    public List<User> getPlayers_list(){
        return players_list;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
