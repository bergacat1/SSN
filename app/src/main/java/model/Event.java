package model;

import java.io.Serializable;
import java.util.Date;

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

    private Date creationDate;
    private Date limitDate;

    private State state;

    public Event(Sport sport, int minPlayers, int maxPlayers, Date creationDate, Date limitDate, State state) {
        this.sport = sport;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.creationDate = creationDate;
        this.limitDate = limitDate;
        this.state = state;
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
}
