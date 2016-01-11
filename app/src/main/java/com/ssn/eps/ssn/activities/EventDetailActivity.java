package com.ssn.eps.ssn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
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
    private List<String> values = new ArrayList<>();

    private ArrayAdapter <String> adapter;
    //private Event_OLD event;
    private ManagerEntity_OLD manager_entity;

    private TextView tv_sport;
    private TextView tv_datetime;
    private TextView tv_numplayers;
    private TextView tv_maxprice;
    private ListView listView_players;
    private Button report_button;
    private Button close_button;
    private Button button;
    private Button butJoin;

    private GoogleMap mMap;
    private MapView mapView;
    private Circle circle;
    private Marker marker;
    private LinearLayout zona_layout;

    private SharedPreferences prefs;
    int idEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeViews(savedInstanceState);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, 2015);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH,2);
        c.set(Calendar.HOUR, 12);
        c.set(Calendar.MINUTE, 30);

        tv_sport = (TextView) findViewById(R.id.tvSport_value);
        tv_datetime = (TextView) findViewById(R.id.tvDateTime_value);
        tv_numplayers = (TextView) findViewById(R.id.tvNumPlayers_value);
        tv_maxprice = (TextView) findViewById(R.id.tvMaxprice_value);

        listView = (ListView) findViewById(R.id.list_players);

        adapter = new ArrayAdapter<String>(this, R.layout.event_list_item, R.id.event_compressed_description, values);

        idEvent = cridaWS();

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        butJoin = (Button) findViewById(R.id.button_id);
        butJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.app.AlertDialog.Builder(EventDetailActivity.this)
                        .setTitle(getResources().getText(R.string.join))
                        .setMessage(getResources().getText(R.string.join_confirmation))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int userId = prefs.getInt(Globals.PROPERTY_USER_ID, 0);

                                if (userId > 0) {
                                    SoapWSCaller.getInstance().joinEventCall((Activity) EventDetailActivity.this, userId, idEvent, new WSCallbackInterface() {
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
                        .show();
            }
        });
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

        mapView = (MapView) findViewById(R.id.mapview_eventdetail);
        zona_layout = (LinearLayout) findViewById(R.id.pistaLayout);
        button = (Button) findViewById(R.id.button_id);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mMap = mapView.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        MapsInitializer.initialize(this);
    }

    public int cridaWS(){

        getIntent().getExtras();
        int id = 0;
        int eventid = getIntent().getIntExtra("idevent",id);

        SharedPreferences prefs = this.getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        int userid = prefs.getInt(Globals.PROPERTY_USER_ID, -1);

        SoapWSCaller.getInstance().getEventByIDCall(this, eventid, userid, new WSCallbackInterface() {
            @Override
            public void onProcesFinished(Result res) {
                if (!res.isValid()) {
                    showToast(getString(R.string.server_error) + ": " + res.getError());
                    return;
                }

                Event event = (Event) res.getData().get(0);
                showToast(event.getSportName());

                cridaWSManagerEntities(event);

                tv_sport.setText(event.getSportName());
                tv_datetime.setText(Globals.sdf.format(event.getStartDate().getTime()));
                tv_numplayers.setText(event.getMinPlayers() + " / " + event.getMaxPlayers());
                tv_maxprice.setText(event.getMaxPrice() + " €");

                //Tractar si l'usuari esta ja unit a l'event
                if (event.isJoined()) {
                    button.setText(R.string.unjoin);
                } else {
                    button.setText(R.string.join);
                }
            }

            @Override
            public void onProcessError() {
                showToast(getString(R.string.server_error));
            }
        });

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
                for (int i = 0; i < listPlayers.size(); i++) {
                    values.add(listPlayers.get(i).getUsername());
                }

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = listPlayers.get(position);
                        final Dialog dialog = new Dialog(EventDetailActivity.this);
                        dialog.setTitle(getString(R.string.user_info));
                        dialog.setContentView(R.layout.content_window_user_detail);

                        close_button = (Button) dialog.findViewById(R.id.button_close);
                        close_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        report_button = (Button) dialog.findViewById(R.id.button_report);
                        report_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(v.getContext())
                                        .setTitle(getString(R.string.confirmar_reporte))
                                        .setMessage(getString(R.string.confirmar_reporte_text))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
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

                        TextView userName = (TextView) dialog.findViewById(R.id.tv_userName_value);
                        userName.setText(String.valueOf(user.getName()));

                        TextView emailUser = (TextView) dialog.findViewById(R.id.tv_emailUser_value);
                        emailUser.setText(String.valueOf(user.getEmail()));

                        dialog.show();
                    }
                });
            }

            @Override
            public void onProcessError() {
                showToast(getString(R.string.server_error));
            }
        });
        return eventid;
    }

    public void cridaWSManagerEntities(final Event event){
        SoapWSCaller.getInstance().getManagerEntitiesByEventCall(this, event.getIdEvent(), new WSCallbackInterface() {
            @Override
            public void onProcesFinished(Result res) {
                if (!res.isValid()) {
                    showToast(getString(R.string.server_error) + ": " + res.getError());
                    return;
                }
                for (Iterator it = res.getData().iterator(); it.hasNext(); ) {
                    ManagerEntity managerEntity = (ManagerEntity) it.next();
                    listManagerEntities.add(managerEntity);
                }
                //Event ja reservat
                if(event.getIdReservation()<0){
                    mapView.setVisibility(View.VISIBLE);

                    if(listManagerEntities.size()==1){
                        showToast("Event Reservat!!");
                        LatLng pos = new LatLng(listManagerEntities.get(0).getLatitude(),
                                listManagerEntities.get(0).getLongitude());
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(pos)
                                .title(listManagerEntities.get(0).getName())
                                .draggable(true));
                    }
                }
                //Event sense reservar encara
                else{
                    //Mapa amb punt i zona pel voltant
                    if(event.getRange()!=0){
                        LatLng pos = new LatLng(event.getLatitude(), event.getLongitude());
                        mapView.setVisibility(View.VISIBLE);

                        marker = mMap.addMarker(new MarkerOptions()
                                .position(pos)
                                .draggable(true));

                        circle = mMap.addCircle(new CircleOptions()
                                        .center(pos)
                                        .radius(250)
                                        .strokeWidth(2)
                                        .strokeColor(Color.BLUE)
                                        .fillColor(Color.argb(140, 36, 4, 218))
                        );
                    }
                    //Mapa amb la o les ManagerEntities la icona i el nom
                    else if(listManagerEntities.size()>0) {
                        mapView.setVisibility(View.VISIBLE);

                        for (int i=0; i<listManagerEntities.size(); i++){
                            LatLng pos = new LatLng(listManagerEntities.get(i).getLatitude(),
                                                    listManagerEntities.get(i).getLongitude());
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(pos)
                                    .title(listManagerEntities.get(i).getName())
                                    .draggable(true));
                        }
                    }

                    else{
                        zona_layout.setVisibility(View.VISIBLE);
                        TextView value_poblacio = (TextView) findViewById(R.id.tv_poblvalue);
                        value_poblacio.setText(event.getCity());
                    }

                }
            }

            @Override
            public void onProcessError() {
                showToast(getString(R.string.server_error));
            }
        });
    }

    private void showToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}