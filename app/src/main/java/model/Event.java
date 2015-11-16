package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lluis on 1/11/15.
 */
public class Event implements Serializable{

    private static enum State {
        NEW, FILLING, FULL, RESERVED
    }

    private User_ owner;
    private Sport sport;

    private int minPlayers;
    private int maxPlayers;

    private Date creationDate;
    private Date limitDate;

    private State state;

}
