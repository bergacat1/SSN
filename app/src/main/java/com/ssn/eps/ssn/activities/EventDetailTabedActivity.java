package com.ssn.eps.ssn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ssn.eps.model.Event;
import com.ssn.eps.model.ManagerEntity;
import com.ssn.eps.model.Result;
import com.ssn.eps.model.User;
import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.wscaller.SoapWSCaller;
import com.ssn.eps.ssn.wscaller.WSCallbackInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import General.Globals;

public class EventDetailTabedActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private SharedPreferences prefs;

    private static Event event;

    private static int eventid;
    private static int userid;

    private static boolean enableMap = false;

    private static String mapType;
    private static boolean buildings;

    private static EventDetailMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_tabed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        event = null;
        eventid = getIntent().getIntExtra("idevent", -1);
        userid = prefs.getInt(Globals.PROPERTY_USER_ID, -1);

        mapType = prefs.getString("mapType", "1");
        buildings = prefs.getBoolean("buildings_map_checkbox",true);

        if(userid < 0 || eventid < 0){
            showToast(getString(R.string.internal_error));
            return ;
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        /*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 0 && !enableMap)
                    return;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class EventDetailInformationFragment extends Fragment {

        private View rootView;

        private List<User> listPlayers = new ArrayList<>();
        private ListView listView;

        private ArrayAdapter<User> userAdapter;

        private LinearLayout zoneL;

        private TextView tvState;
        private TextView tv_sport;
        private TextView tv_datetime;
        private TextView tv_datetimeDuration;
        private TextView tv_numplayers;
        private TextView tv_maxprice;
        private TextView tv_zone;
        private ListView listView_players;
        private Button report_button;
        private Button close_button;
        private Button actionButton;

        private ProgressDialog progress;
        private int ch = 0;

        public EventDetailInformationFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static EventDetailInformationFragment newInstance(int sectionNumber) {
            EventDetailInformationFragment fragment = new EventDetailInformationFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_event_detail_info, container, false);

            initializeViews(savedInstanceState);

            userAdapter = new ArrayAdapter<User>(getContext(), R.layout.event_list_item, R.id.event_compressed_description, listPlayers);

            progress = new ProgressDialog(getActivity());
            progress.setMessage(getString(R.string.loading));
            progress.show();

            getEventCall(eventid);
            getUsersCall(eventid);

            return rootView;
        }

        private void initializeViews(Bundle savedInstanceState) {

            zoneL = (LinearLayout) rootView.findViewById(R.id.zoneL);

            tvState = (TextView) rootView.findViewById(R.id.event_state);

            tv_sport = (TextView) rootView.findViewById(R.id.tvSport_value);
            tv_datetime = (TextView) rootView.findViewById(R.id.tvDateTime_value);
            tv_datetimeDuration = (TextView) rootView.findViewById(R.id.tvDateTimeDuration_value);
            tv_numplayers = (TextView) rootView.findViewById(R.id.tvNumPlayers_value);
            tv_maxprice = (TextView) rootView.findViewById(R.id.tvMaxprice_value);
            tv_zone = (TextView) rootView.findViewById(R.id.tvZone_value);

            listView = (ListView) rootView.findViewById(R.id.list_players);

            actionButton = (Button) rootView.findViewById(R.id.action_button);

            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (event.isJoined()) {
                        new android.app.AlertDialog.Builder(getActivity())
                                .setTitle(getResources().getText(R.string.leave))
                                .setMessage(getResources().getText(R.string.leave_confirmation))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        SoapWSCaller.getInstance().leaveEventCall((Activity) getActivity(), userid, event.getIdEvent(), new WSCallbackInterface() {
                                            @Override
                                            public void onProcesFinished(Result res) {
                                                if (res.isValid()) {
                                                    Toast.makeText(getActivity(), getText(R.string.leaveOk).toString(), Toast.LENGTH_LONG).show();
                                                } else {
                                                    new android.app.AlertDialog.Builder(getActivity())
                                                            .setTitle(R.string.atencion)
                                                            .setMessage(getText(R.string.serverError).toString() + res.getError())
                                                            .setPositiveButton(getText(R.string.ok).toString(), new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    getActivity().finish();
                                                                }
                                                            })
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .show();
                                                }

                                            }

                                            @Override
                                            public void onProcessError() {
                                                ((EventDetailTabedActivity) getActivity()).showToast(getText(R.string.server_error));
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();
                    } else {
                        new android.app.AlertDialog.Builder(getActivity())
                                .setTitle(getResources().getText(R.string.join))
                                .setMessage(getResources().getText(R.string.join_confirmation))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        SoapWSCaller.getInstance().joinEventCall(getActivity(), userid, event.getIdEvent(), new WSCallbackInterface() {
                                            @Override
                                            public void onProcesFinished(Result res) {
                                                if (res.isValid()) {
                                                    Toast.makeText(getActivity(), getActivity().getText(R.string.joinOk).toString(), Toast.LENGTH_LONG).show();
                                                } else {
                                                    new android.app.AlertDialog.Builder(getActivity())
                                                            .setTitle(R.string.atencion)
                                                            .setMessage(getActivity().getText(R.string.serverError).toString() + res.getError())
                                                            .setPositiveButton(getActivity().getText(R.string.ok).toString(), new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    getActivity().finish();
                                                                }
                                                            })
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .show();
                                                }

                                            }

                                            @Override
                                            public void onProcessError() {
                                                ((EventDetailTabedActivity) getActivity()).showToast(getActivity().getText(R.string.server_error));
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();
                    }
                }
            });

        }

        public void getEventCall(int eventid){

            SoapWSCaller.getInstance().getEventByIDCall(getActivity(), eventid, userid, new WSCallbackInterface() {
                @Override
                public void onProcesFinished(Result res) {
                    if (!res.isValid()) {
                        ((EventDetailTabedActivity) getActivity()).showToast(getString(R.string.server_error) + ": " + res.getError());
                        return;
                    }

                    event = (Event) res.getData().get(0);

                    if (event.getIdReservation() <= 0 && !event.getCity().equals("")) {
                        // NO RESERVAT: ciutat/zona
                        tv_zone.setText(event.getCity());
                        zoneL.setVisibility(View.VISIBLE);
                        enableMap = false;
                    }else if (event.getLatitude() != 0 && event.getLongitude() != 0 && event.getRange() != 0) {
                        // NO RESERVAT: punt i radi al mapa
                        if (mapFragment!=null){
                            mapFragment.printCircle(event);
                        }
                    }

                    tvState.setText(getString(getResources().getIdentifier(event.getState().toString(), "string", getActivity().getPackageName())));
                    tvState.setTextColor(getHeavyColorByState(event.getState()));

                    tv_sport.setText(event.getSportName());
                    tv_datetime.setText(Globals.sdf.format(event.getStartDate().getTime()));
                    tv_datetimeDuration.setText(Double.toString((event.getEndDate().getTimeInMillis() - event.getStartDate().getTimeInMillis()) / (1000 * 60)));
                    tv_numplayers.setText(event.getMinPlayers() + " / " + event.getMaxPlayers());
                    tv_maxprice.setText(event.getMaxPrice() + " €");

                    //Tractar si l'usuari esta ja unit a l'event
                    if (event.getState() == Event.States.OPEN || event.getState() == Event.States.RESERVED) {
                        actionButton.setText(R.string.join);
                        if (event.isJoined()) {
                            actionButton.setText(R.string.leave);
                        }
                        actionButton.setVisibility(View.VISIBLE);
                    }


                    check();
                }

                @Override
                public void onProcessError() {
                    ((EventDetailTabedActivity) getActivity()).showToast(getString(R.string.server_error));
                }
            });
        }

        private void getUsersCall(int eventid){
            SoapWSCaller.getInstance().getUsersByEvent(getActivity(), eventid, new WSCallbackInterface() {
                @Override
                public void onProcesFinished(Result res) {
                    if (!res.isValid()) {
                        ((EventDetailTabedActivity) getActivity()).showToast(getString(R.string.server_error) + ": " + res.getError());
                        return;
                    }

                    for (Iterator it = res.getData().iterator(); it.hasNext(); ) {
                        User user = (User) it.next();
                        listPlayers.add(user);
                    }

                    listView.setAdapter(userAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            User user = listPlayers.get(position);
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setTitle(getString(R.string.user_info));
                            dialog.setContentView(R.layout.content_window_user_detail);

                            TextView userName = (TextView) dialog.findViewById(R.id.tv_userName_value);
                            userName.setText(String.valueOf(user.getUsername()));

                            report_button = (Button) dialog.findViewById(R.id.button_report);
                            report_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(v.getContext())
                                            .setTitle(getString(R.string.confirmar_reporte))
                                            .setMessage(getString(R.string.confirmar_reporte_text))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //todo reportar
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .show();
                                    dialog.dismiss();
                                }

                            });

                            dialog.setCancelable(true);

                            dialog.show();
                        }
                    });
                    check();
                }

                @Override
                public void onProcessError() {
                    ((EventDetailTabedActivity) getActivity()).showToast(getString(R.string.server_error));
                }
            });
        }

        private void check(){
            ch ++;
            if(ch >= 2){
                progress.dismiss();
            }
        }

        private int getHeavyColorByState(Event.States state){

            switch(state){
                case OPEN:
                    return getResources().getColor(R.color.heavy_green);
                case RESERVED:
                    return getResources().getColor(R.color.heavy_orange);
                case FINISHED:
                    return getResources().getColor(R.color.heavy_blue);
                case CANCELED:
                    return getResources().getColor(R.color.heavy_red);
            }
            return 0;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class EventDetailUsersListFragment extends Fragment {

        public EventDetailUsersListFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static EventDetailUsersListFragment newInstance(int sectionNumber) {
            EventDetailUsersListFragment fragment = new EventDetailUsersListFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event_detail_users_list, container, false);
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class EventDetailMapFragment extends Fragment {

        private GoogleMap mMap;

        public EventDetailMapFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static EventDetailMapFragment newInstance(int sectionNumber) {
            EventDetailMapFragment fragment = new EventDetailMapFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event_detail_map, container, false);

            MapView mapView = (MapView) rootView.findViewById(R.id.mapview_eventdetail);

            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mMap = mapView.getMap();
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(Integer.parseInt(mapType));
            mMap.setBuildingsEnabled(buildings);

            MapsInitializer.initialize(getContext());

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            if(event != null && event.getLatitude() != 0 && event.getLongitude() != 0 && event.getRange() != 0){
                // NO RESERVAT: punt i radi al mapa

                LatLng latlon = new LatLng(event.getLatitude(), event.getLongitude());
                mMap.addMarker(new MarkerOptions()
                                .position(latlon)
                                .title(getString(R.string.marker_title))
                );
                mMap.addCircle(new CircleOptions()
                                .center(latlon)
                                .radius(event.getRange())
                                .strokeWidth(2)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.argb(140, 36, 4, 218))
                );
            }else{
                managerEntitiesCall();
            }
        }

        public void managerEntitiesCall(){
            SoapWSCaller.getInstance().getManagerEntitiesByEventCall(getActivity(), eventid, new WSCallbackInterface() {
                @Override
                public void onProcesFinished(Result res) {
                    if (!res.isValid()) {
                        ((EventDetailTabedActivity) getActivity()).showToast(getString(R.string.server_error) + ": " + res.getError());
                        return;
                    }
                    boolean included = false;
                    LatLngBounds.Builder bld = new LatLngBounds.Builder();

                    for (Iterator it = res.getData().iterator(); it.hasNext(); ) {
                        ManagerEntity me = (ManagerEntity) it.next();

                        if (!me.isValidForPrint()) continue;

                        Marker m = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(me.getLatitude(), me.getLongitude()))
                                .title(me.getName())
                                .visible(true));

                        bld.include(new LatLng(me.getLatitude(), me.getLongitude()));

                        if (me.getType() == 0) {
                            m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_managed_selected));
                        } else if (me.getType() == 1) {
                            m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_no_managed_selected));
                        }

                        if (!included) included = true;
                    }
                    if (included) {
                        LatLngBounds bounds = bld.build();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 170));
                    }
                }

                @Override
                public void onProcessError() {
                    ((EventDetailTabedActivity) getActivity()).showToast(getString(R.string.server_error));
                }
            });
        }

        public void printCircle(Event event) {
            if(mMap==null)return;
            LatLng latlon = new LatLng(event.getLatitude(), event.getLongitude());
            mMap.addMarker(new MarkerOptions()
                            .position(latlon)
                            .title(getString(R.string.marker_title))
            );
            mMap.addCircle(new CircleOptions()
                            .center(latlon)
                            .radius(event.getRange())
                            .strokeWidth(2)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(140, 36, 4, 218))
            );

            // Zoom in the Google Map
            mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
            // Show the location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlon));
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position){
                case 0:
                    return EventDetailInformationFragment.newInstance(eventid);
                case 1:
                    mapFragment = EventDetailMapFragment.newInstance(eventid);
                    return mapFragment;
                case 2:

                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.information);
                case 1:
                    return getString(R.string.localization);
                case 2:
                    return "";
            }
            return null;
        }
    }

    public void showToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
