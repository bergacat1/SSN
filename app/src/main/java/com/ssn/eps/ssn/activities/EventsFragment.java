package com.ssn.eps.ssn.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssn.eps.model.Event;
import com.ssn.eps.model.Filters;
import com.ssn.eps.model.Result;
import com.ssn.eps.ssn.R;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import General.Globals;
import lists.EventItemAdapter;

import com.ssn.eps.ssn.wscaller.SoapWSCaller;
import com.ssn.eps.ssn.wscaller.WSCallbackInterface;

/**
 * Created by alber on 17/11/2015.
 */
public class EventsFragment extends Fragment{

    public static final int TABEVENTS = 0;
    public static final int TABMYEVENTS = 1;
    public static final int TABHISTORYEVENTS = 2;

    private int tab = -1;
    private int lastExpandedPosition = -1;

    private ExpandableListView listView;
    private View rootView;
    private TextView tvNoEvents;
    private FragmentsCommunicationInterface mCallback;
    private ProgressDialog progress;
    private EventItemAdapter adapter;
    private Button filtersButton;
    private Button clearFiltersButton;
    private Filters filter;
    private FragmentsCommunicationInterface communicationInterface;

    public EventsFragment(){}

    public static EventsFragment getInstance(int tab, FragmentsCommunicationInterface communicationInterface){
        EventsFragment e = new EventsFragment();
        e.setTab(tab);
        e.setCommunicationInterface(communicationInterface);
        return e;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        this.listView = (ExpandableListView) rootView.findViewById(R.id.eventsList);
        this.adapter = new EventItemAdapter(this.getContext(), new ArrayList<Event>(), tab, this);
        this.listView.setAdapter(adapter);
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
        this.tvNoEvents = (TextView) rootView.findViewById(R.id.tv_no_events);

        progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.loading));
        progress.show();

        obtainEvents(new Filters());
        filtersButton = (Button)rootView.findViewById(R.id.button_filters);
        filtersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FiltersActivity.class);
                startActivityForResult(i, 1);
            }
        });
        clearFiltersButton = (Button)rootView.findViewById(R.id.button_clear_filters);
        clearFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.clear();
                clearFiltersButton.setEnabled(false);
                obtainEvents(filter);
            }
        });
        if(tab != TABEVENTS) {
            filtersButton.setVisibility(View.GONE);
            clearFiltersButton.setVisibility(View.GONE);
        }
        if(tab == TABEVENTS)
            this.filter = new Filters();

        int scheduleRatio = 10 + (tab != TABEVENTS ? 50 : 0);
        ScheduledExecutorService schedule = new ScheduledThreadPoolExecutor(1);
        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                obtainEvents(filter);
            }
        }, scheduleRatio, scheduleRatio, TimeUnit.SECONDS);


        return rootView;
    }

    private void obtainEvents(Filters f){
        SharedPreferences prefs = getActivity().getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        if(f != null)
            f.setUserId(prefs.getInt(Globals.PROPERTY_USER_ID, -1)); //TODO: prefs.getInt("userid", -1)

        switch(tab){
            case TABEVENTS:
                SoapWSCaller.getInstance().getEventsByFiltersCall(getActivity(), f != null ? f : new Filters(), new WSCallbackInterface() {
                    @Override
                    public void onProcesFinished(Result res) {
                        adapter.setEvents(res.getData());
                        progress.dismiss();
                        adapter.notifyDataSetChanged();
                        if(res.getData().isEmpty())
                            tvNoEvents.setVisibility(View.VISIBLE);
                        else
                            tvNoEvents.setVisibility(View.GONE);
                    }
                    @Override
                    public void onProcessError() {
                        showToast(getString(R.string.server_error));
                    }
                });
                break;
            case TABMYEVENTS:
                SoapWSCaller.getInstance().getJoinedEventsCall(getActivity(), prefs.getInt(Globals.PROPERTY_USER_ID, -1), new WSCallbackInterface() {
                    @Override
                    public void onProcesFinished(Result res) {
                        adapter.setEvents(res.getData());
                        progress.dismiss();
                        adapter.notifyDataSetChanged();
                        if(res.getData().isEmpty())
                            tvNoEvents.setVisibility(View.VISIBLE);
                        else
                            tvNoEvents.setVisibility(View.GONE);
                    }
                    @Override
                    public void onProcessError() {
                        showToast(getString(R.string.server_error));
                    }
                });
                break;
            case TABHISTORYEVENTS:
                SoapWSCaller.getInstance().getHistoricalEventsCall(getActivity(), prefs.getInt(Globals.PROPERTY_USER_ID, -1), new WSCallbackInterface() {
                    @Override
                    public void onProcesFinished(Result res) {
                        adapter.setEvents(res.getData());
                        progress.dismiss();
                        adapter.notifyDataSetChanged();
                        if(res.getData().isEmpty())
                            tvNoEvents.setVisibility(View.VISIBLE);
                        else
                            tvNoEvents.setVisibility(View.GONE);
                    }
                    @Override
                    public void onProcessError() {
                        showToast(getString(R.string.server_error));
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 0){
            this.filter = (Filters)data.getSerializableExtra("filter");
            obtainEvents(filter);
            if(!this.filter.isCleared())
                clearFiltersButton.setEnabled(true);
        }
    }

    public void refreshEvents(){
        obtainEvents(filter);
    }

    public void refreshTab(int tab){
        communicationInterface.refreshTab(tab);
    }

    public void collapseAllList(){
        listView.collapseGroup(lastExpandedPosition);
        lastExpandedPosition = -1;
    }

    public void setTab(int tab)
    {
        this.tab = tab;
    }

    public void setCommunicationInterface(FragmentsCommunicationInterface communicationInterface) {
        this.communicationInterface = communicationInterface;
    }

    private void showToast(CharSequence text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }
}
