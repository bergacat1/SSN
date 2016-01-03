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

import com.ssn.eps.model.Filters;
import com.ssn.eps.model.Result;
import com.ssn.eps.ssn.R;

import java.util.List;

import lists.EventItemAdapter;
import com.ssn.eps.model.Event;
import com.ssn.eps.ssn.wscaller.SoapWSCaller;
import com.ssn.eps.ssn.wscaller.WSCallbackInterface;

/**
 * Created by alber on 17/11/2015.
 */
public class EventsFragment extends Fragment{

    private int tab = -1;
    private int lastExpandedPosition = -1;

    private ExpandableListView listView;
    private View rootView;
    private FragmentsCommunicationInterface mCallback;
    private ProgressDialog progress;

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
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.loading));
        progress.show();

        obtainEvents(new Filters());

        Button filters = (Button)rootView.findViewById(R.id.button_filters);
        if(tab != 0)
            filters.setVisibility(View.GONE);
        else
            filters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), FiltersActivity.class);
                    startActivityForResult(i, 1);
                }
            });

        return rootView;
    }

    private void obtainEvents(Filters f){
        SharedPreferences prefs = getActivity().getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        if(f != null)
            f.setUserId(8); //TODO: prefs.getInt("userid", -1)

        switch(tab){
            case 0:
                SoapWSCaller.getInstance().getEventsByFiltersCall(getActivity(), f != null ? f : new Filters(), new WSCallbackInterface() {
                    @Override
                    public void onProcesFinished(Result res) {
                        createListView(res.getData());
                    }
                });
                break;
            case 1:
                SoapWSCaller.getInstance().getJoinedEventsCall(getActivity(), 8, new WSCallbackInterface() { //TODO:prefs.getInt("userid", -1)
                    @Override
                    public void onProcesFinished(Result res) {
                        createListView(res.getData());
                    }
                });
                break;
            case 2:
                SoapWSCaller.getInstance().getHistoricalEventsCall(getActivity(), 8, new WSCallbackInterface() { //TODO: prefs.getInt("userid", -1)
                    @Override
                    public void onProcesFinished(Result res) {
                        createListView(res.getData());
                    }
                });
                break;
        }
    }

    public void createListView(List<Event> events){
        this.listView = (ExpandableListView) rootView.findViewById(R.id.eventsList);
        this.listView.setAdapter(new EventItemAdapter(this.getContext(), events, tab));
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
        progress.dismiss();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 0){
            obtainEvents((Filters)data.getSerializableExtra("filter"));
        }
    }

    public void setTab(int tab)
    {
        this.tab = tab;
    }

}
