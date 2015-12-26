package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lluis on 1/11/15.
 */
public class Event_OLD implements Serializable{

    public enum State {
        NEW, FILLING, FULL, RESERVED
    }

    private User_ owner;
    private Sport_OLD sport;

    private int minPlayers;
    private int maxPlayers;

    private Calendar eventDate;
    private Calendar creationDate;
    private Calendar limitDate;

    private State state;
    private int price;
    private String zone;

    private List<User_OLD> players_list;

    public Event_OLD(Sport_OLD sport, int minPlayers, int maxPlayers, int price, Calendar eventDate, Calendar creationDate, Calendar limitDate, State state, String zone) {
        this.sport = sport;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.price = price;
        this.eventDate = eventDate;
        this.creationDate = creationDate;
        this.limitDate = limitDate;
        this.state = state;
        this.zone = zone;
        this.players_list = new ArrayList<>();
    }

    public User_ getOwner() {
        return owner;
    }

    public void setOwner(User_ owner) {
        this.owner = owner;
    }

    public Sport_OLD getSport() {
        return sport;
    }

    public void setSport(Sport_OLD sport) {
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

    public Calendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public Calendar getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Calendar limitDate) {
        this.limitDate = limitDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void addPlayer(User_OLD user){
        players_list.add(user);
    }

    public List<User_OLD> getPlayers_list(){
        return players_list;
    }

    public User_OLD getPlayer(int i){
        return players_list.get(i);
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
