package com.ssn.eps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by alber on 03/01/2016.
 */
public class Filters implements Serializable{
    private int userId;
    private int sportId;
    private int minPlayers;
    private double maxPrice;
    private long fromDate;
    private long toDate;

    public Filters(){
        this.fromDate = 0;
        this.maxPrice = 0;
        this.minPlayers = 0;
        this.sportId = 0;
        this.toDate = 0;
        this.userId = 0;
    }

    public Filters(int userId, int sportId, int minPlayers, double maxPrice, long fromDate, long toDate) {
        this.fromDate = fromDate;
        this.maxPrice = maxPrice;
        this.minPlayers = minPlayers;
        this.sportId = sportId;
        this.toDate = toDate;
        this.userId = userId;
    }

    public boolean isCleared(){
        return this.fromDate == 0 &&
                this.maxPrice == 0 &&
                this.minPlayers == 0 &&
                this.sportId == 0 &&
                this.toDate == 0 &&
                this.userId == 0;
    }

    public void clear(){
        this.fromDate = 0;
        this.maxPrice = 0;
        this.minPlayers = 0;
        this.sportId = 0;
        this.toDate = 0;
        this.userId = 0;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
