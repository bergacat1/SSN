package com.ssn.eps.ssn.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.ssn.eps.ssn.R;

import java.util.List;
import java.util.Random;

import General.Globals;
import model.Event_OLD;
import model.ManagerEntity_OLD;
import model.Sport_OLD;
import model.User_OLD;

public class EventDetailActivity extends AppCompatActivity {

    private List<User_OLD> listPlayers = new ArrayList<>();

    private ListView listView;

    private Event_OLD event;
    private ManagerEntity_OLD manager_entity;

    private TextView tv_sport;
    private TextView tv_datetime;
    private TextView tv_numplayers;
    private TextView tv_maxprice;
    private ListView listView_players;
    private Button report_button;
    private Button close_button;

    private GoogleMap mMap;
    private MapView mapView;
    private Circle circle;
    private Marker marker;
    private LinearLayout zona_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeViews(savedInstanceState);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR,2015);
        c.set(Calendar.MONTH,11);
        c.set(Calendar.DAY_OF_MONTH,2);
        c.set(Calendar.HOUR, 12);
        c.set(Calendar.MINUTE, 30);

        event = new Event_OLD(new Sport_OLD(1, "Futbol Sala"), 8, 12, 5, c, c, c, Event_OLD.State.NEW, "Lleida");

        tv_sport = (TextView) findViewById(R.id.tvSport_value);
        tv_datetime = (TextView) findViewById(R.id.tvDateTime_value);
        tv_numplayers = (TextView) findViewById(R.id.tvNumPlayers_value);
        tv_maxprice = (TextView) findViewById(R.id.tvMaxprice_value);
        //listView_players = (ListView) findViewById(R.id.listPlayers);

        event.addPlayer(new User_OLD("guillembarbosa@gmail.com","Guille",""));
        event.addPlayer(new User_OLD("lluis.eche@gmail.com","Lluís",""));
        event.addPlayer(new User_OLD("abergacat@gmail.com","Albert",""));

        listPlayers = event.getPlayers_list();

        tv_sport.setText(event.getSport().getName());
        tv_datetime.setText(Globals.sdf.format(event.getCreationDate().getTime()));
        tv_numplayers.setText(listPlayers.size() + " / " + event.getMaxPlayers());
        tv_maxprice.setText(event.getPrice() + " €");

        listView = (ListView) findViewById(R.id.list_players);

        Log.d("a", "size " + listPlayers.size());
        //Toast.makeText(getApplicationContext(), "SIZE" +listPlayers.size(), Toast.LENGTH_LONG).show();
        List<String> values = new ArrayList<>();

        for(int i=0;i<listPlayers.size();i++){
            values.add(listPlayers.get(i).getUserName());
        }

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,
                R.layout.event_list_item, R.id.event_compressed_description, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User_OLD user = listPlayers.get(position);
                final Dialog dialog = new Dialog(EventDetailActivity.this);
                dialog.setTitle(getString(R.string.user_info));
                dialog.setContentView(R.layout.content_window_user_detail);

                close_button =  (Button) dialog.findViewById(R.id.button_close);
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
                userName.setText(String.valueOf(user.getUserName()));

                TextView emailUser = (TextView) dialog.findViewById(R.id.tv_emailUser_value);
                emailUser.setText(String.valueOf(user.getEmail()));

                dialog.show();
            }
        });

    }


    private void initializeViews(Bundle savedInstanceState){

        mapView = (MapView) findViewById(R.id.mapview_eventdetail);
        zona_layout = (LinearLayout) findViewById(R.id.pistaLayout);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mMap = mapView.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        MapsInitializer.initialize(this);
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//return mapRadioButton.isChecked();
            }
        });

        LatLng pos = new LatLng(41.6, 0.77);
        Random rnd = new Random();
        int num = rnd.nextInt(3) + 1;

        if(num == 1) {
            mapView.setVisibility(View.VISIBLE);

            marker = mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title("GYM TONIC")
                    .draggable(true));
        }

        else if (num == 2){
            mapView.setVisibility(View.VISIBLE);

            marker = mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title("GYM TONIC")
                    .draggable(true));

            circle = mMap.addCircle(new CircleOptions()
                            .center(pos)
                            .radius(250)
                            .strokeWidth(2)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(140, 36, 4, 218))
            );

        }

        else{
            zona_layout.setVisibility(View.VISIBLE);
        }

    }

}