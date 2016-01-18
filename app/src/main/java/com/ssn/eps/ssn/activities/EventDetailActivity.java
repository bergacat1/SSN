package com.ssn.eps.ssn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ssn.eps.model.Event;
import com.ssn.eps.model.ManagerEntity;
import com.ssn.eps.model.Result;
import com.ssn.eps.model.Sport;
import com.ssn.eps.model.User;
import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.wscaller.SoapWSCaller;
import com.ssn.eps.ssn.wscaller.WSCallbackInterface;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import General.Globals;
import model.Event_OLD;
import model.ManagerEntity_OLD;
import model.Sport_OLD;
import model.User_OLD;

public class EventDetailActivity extends AppCompatActivity {

    private List<User> listPlayers = new ArrayList<>();
    private List<ManagerEntity> listManagerEntities = new ArrayList<>();
    private ListView listView;

    private ArrayAdapter <User> userAdapter;

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

    private GoogleMap mMap;
    private MapView mapView;

    private SharedPreferences prefs;
    private ProgressDialog progress;

    private Event event;
    private int userid;

    private int ch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.loading));
        progress.show();

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        initializeViews(savedInstanceState);

        userAdapter = new ArrayAdapter<User>(this, R.layout.event_list_item, R.id.event_compressed_description, listPlayers);

        userid = prefs.getInt(Globals.PROPERTY_USER_ID, -1);
        final int eventid = getIntent().getIntExtra("idevent", -1);

        if(userid < 0 || eventid < 0){
            showToast(getString(R.string.internal_error));
            return ;
        }

        getEventCall(eventid);
        getUsersCall(eventid);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(event.isJoined()){
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle(getResources().getText(R.string.leave))
                            .setMessage(getResources().getText(R.string.leave_confirmation))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int userId = prefs.getInt(Globals.PROPERTY_USER_ID, 0);
                                    if (userId > 0)
                                        SoapWSCaller.getInstance().leaveEventCall((Activity) getActivity(), userId, event.getIdEvent(), new WSCallbackInterface() {
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
                                                showToast(getText(R.string.server_error));
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
                }else{
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle(getResources().getText(R.string.join))
                            .setMessage(getResources().getText(R.string.join_confirmation))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int userId = prefs.getInt(Globals.PROPERTY_USER_ID, 0);
                                    if (userId > 0)
                                        SoapWSCaller.getInstance().joinEventCall(getActivity(), userId, event.getIdEvent(), new WSCallbackInterface() {
                                            @Override
                                            public void onProcesFinished(Result res) {
                                                if (res.isValid()){
                                                    Toast.makeText(getActivity(), getActivity().getText(R.string.joinOk).toString(), Toast.LENGTH_LONG).show();
                                                }else{
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
                                                showToast(getActivity().getText(R.string.server_error));
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


                /*new android.app.AlertDialog.Builder(EventDetailActivity.this)
                    .setTitle(getResources().getText(R.string.join))
                    .setMessage(getResources().getText(R.string.join_confirmation))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if(event.isJoined()){
                                // todo sortir
                            }else{
                                SoapWSCaller.getInstance().joinEventCall(getActivity(), userid, eventid, new WSCallbackInterface() {
                                    @Override
                                    public void onProcesFinished(Result res) {
                                        if (res.isValid()) {
                                            //fragment.refreshTab(EventsFragment.TABMYEVENTS);
                                            Toast.makeText(getApplicationContext(), EventDetailActivity.this.getText(R.string.joinOk).toString(), Toast.LENGTH_LONG).show();
                                        } else {
                                            new android.app.AlertDialog.Builder(EventDetailActivity.this)
                                                    .setTitle(R.string.atencion)
                                                    .setMessage(EventDetailActivity.this.getText(R.string.serverError).toString() + res.getError())
                                                    .setPositiveButton(EventDetailActivity.this.getText(R.string.ok).toString(), null)
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }

                                    }

                                    @Override
                                    public void onProcessError() {
                                        showToast(getApplicationContext().getText(R.string.server_error));
                                    }
                                });
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                .show();*/
            }
        });
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initializeViews(Bundle savedInstanceState){

        zoneL = (LinearLayout)findViewById(R.id.zoneL);

        tvState = (TextView) findViewById(R.id.event_state);

        tv_sport = (TextView) findViewById(R.id.tvSport_value);
        tv_datetime = (TextView) findViewById(R.id.tvDateTime_value);
        tv_datetimeDuration = (TextView) findViewById(R.id.tvDateTimeDuration_value);
        tv_numplayers = (TextView) findViewById(R.id.tvNumPlayers_value);
        tv_maxprice = (TextView) findViewById(R.id.tvMaxprice_value);
        tv_zone = (TextView) findViewById(R.id.tvZone_value);

        listView = (ListView) findViewById(R.id.list_players);

        mapView = (MapView) findViewById(R.id.mapview_eventdetail);
        actionButton = (Button) findViewById(R.id.action_button);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mMap = mapView.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        MapsInitializer.initialize(this);
    }

    public void getEventCall(int eventid){

        SoapWSCaller.getInstance().getEventByIDCall(this, eventid, userid, new WSCallbackInterface() {
            @Override
            public void onProcesFinished(Result res) {
                if (!res.isValid()) {
                    showToast(getString(R.string.server_error) + ": " + res.getError());
                    return;
                }

                event = (Event) res.getData().get(0);

                if (event.getIdReservation() > 0) {
                    managerEntitiesCall(event);
                } else {
                    if (event.getLatitude() != 0 && event.getLongitude() != 0 && event.getRange() != 0) {
                        // NO RESERVAT: punt i radi al mapa
                        mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(event.getLatitude(), event.getLongitude()))
                                        .title(getString(R.string.marker_title))
                        );
                        mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(event.getLatitude(), event.getLongitude()))
                                        .radius(event.getRange())
                                        .strokeWidth(2)
                                        .strokeColor(Color.BLUE)
                                        .fillColor(Color.argb(140, 36, 4, 218))
                        );
                        mapView.setVisibility(View.VISIBLE);
                    } else if (!event.getCity().equals("")) {
                        // NO RESERVAT: ciutat/zona
                        tv_zone.setText(event.getCity());
                        zoneL.setVisibility(View.VISIBLE);
                    } else {
                        managerEntitiesCall(event);
                    }
                }


                tvState.setText(event.getState().toString());
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
                        actionButton.setText(R.string.unjoin);
                    }
                    actionButton.setVisibility(View.VISIBLE);
                }


                check();
            }

            @Override
            public void onProcessError() {
                showToast(getString(R.string.server_error));
            }
        });
    }

    private void getUsersCall(int eventid){
        SoapWSCaller.getInstance().getUsersByEvent(this, eventid, new WSCallbackInterface() {
            @Override
            public void onProcesFinished(Result res) {
                if (!res.isValid()) {
                    showToast(getString(R.string.server_error) + ": " + res.getError());
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
                        final Dialog dialog = new Dialog(EventDetailActivity.this);
                        dialog.setTitle(getString(R.string.user_info));
                        dialog.setContentView(R.layout.content_window_user_detail);

                        TextView userName = (TextView) dialog.findViewById(R.id.tv_userName_value);
                        userName.setText(String.valueOf(user.getName()));

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
                showToast(getString(R.string.server_error));
            }
        });
    }

    public void managerEntitiesCall(final Event event){
        SoapWSCaller.getInstance().getManagerEntitiesByEventCall(this, event.getIdEvent(), new WSCallbackInterface() {
            @Override
            public void onProcesFinished(Result res) {
                if (!res.isValid()) {
                    showToast(getString(R.string.server_error) + ": " + res.getError());
                    return;
                }
                for (Iterator it = res.getData().iterator(); it.hasNext(); ) {
                    ManagerEntity me = (ManagerEntity) it.next();

                    if (!me.isValidForPrint()) continue;

                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(me.getLatitude(), me.getLongitude()))
                            .title(me.getName())
                            .visible(false));

                    if (me.getType() == 0) {
                        m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_managed_selected));
                    } else if (me.getType() == 1) {
                        m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_no_managed_selected));
                    }
                }
                mapView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProcessError() {
                showToast(getString(R.string.server_error));
            }
        });
    }

    private void check(){
        ch ++;
        if(ch >= 2){
            progress.dismiss();
        }
    }

    private Activity getActivity(){
        return this;
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

    private void showToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}