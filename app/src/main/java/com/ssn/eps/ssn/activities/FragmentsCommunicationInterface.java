package com.ssn.eps.ssn.activities;

import java.util.List;

import com.ssn.eps.model.Event;

/**
 * Created by alber on 17/11/2015.
 */
public interface FragmentsCommunicationInterface {
    public List<Event> getEvents();
    public void goToEventById(int id);
}
