package com.ssn.eps.ssn.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.ssn.eps.ssn.R;

import java.util.ArrayList;
import java.util.List;

import lists.EventItemAdapter;
import model.Event;

/**
 * Created by alber on 17/11/2015.
 */
public class EventsFragment extends Fragment{

    private int tab = -1;
    private int lastExpandedPosition = -1;

    ExpandableListView listView;
    private FragmentsCommunicationInterface mCallback;

    public EventsFragment(){}

    public static EventsFragment getNewInstance(int tab){
        EventsFragment e = new EventsFragment();
        e.setTab(tab);
        return e;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.listView = (ExpandableListView) rootView.findViewById(R.id.eventsList);
        this.listView.setAdapter(new EventItemAdapter(this.getContext(), mCallback != null ? mCallback.getEvents() : new ArrayList<Event>(), tab));
        this.listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    listView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        if(tab != 0)
            rootView.findViewById(R.id.button_filters).setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (FragmentsCommunicationInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentsCommunicationInterface");
        }
    }

    public void setTab(int tab)
    {
        this.tab = tab;
    }

}
