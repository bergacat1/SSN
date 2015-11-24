package com.ssn.eps.ssn.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.fragments.MessageDialogFragment;

import java.util.Date;
import java.util.List;

import General.Globals;
import model.Event;
import model.Sport;
import model.User;

public class EventDetailActivity extends AppCompatActivity {

    private List<User> listPlayers = new ArrayList<>();

    ListView listView;

    Event event;

    TextView tv_sport;
    TextView tv_datetime;
    TextView tv_numplayers;
    TextView tv_maxprice;
    ListView listView_players;
    Button report_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR,2015);
        c.set(Calendar.MONTH,11);
        c.set(Calendar.DAY_OF_MONTH,2);
        c.set(Calendar.HOUR, 12);
        c.set(Calendar.MINUTE, 30);

        event = new Event(new Sport(1, "Futbol Sala"), 8, 12, 5, c, c, c, Event.State.NEW, "Lleida");
        tv_sport = (TextView) findViewById(R.id.tvSport_value);
        tv_datetime = (TextView) findViewById(R.id.tvDateTime_value);
        tv_numplayers = (TextView) findViewById(R.id.tvNumPlayers_value);
        tv_maxprice = (TextView) findViewById(R.id.tvMaxprice_value);
        //listView_players = (ListView) findViewById(R.id.listPlayers);

        event.addPlayer(new User("Guillem","Lleida",22,"Futbol","guillembarbosa@gmail.com"));
        event.addPlayer(new User("Lluís","Benavent",28,"Petanca","lluis.eche@gmail.com"));
        event.addPlayer(new User("Albert","Torrefarrera",22,"Padel","abergacat@gmail.com"));
        event.addPlayer(new User("Eduardo", "Lleida", 32, "Baloncesto", "eduardooo@gmail.com"));

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
            values.add(listPlayers.get(i).getName());
        }

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = listPlayers.get(position);
                final Dialog dialog = new Dialog(EventDetailActivity.this);
                dialog.setTitle(getString(R.string.user_info));
                dialog.setContentView(R.layout.content_window_user_detail);

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

                TextView userCity = (TextView) dialog.findViewById(R.id.tv_userCity_value);
                userCity.setText(String.valueOf(user.getCity()));

                TextView userAge = (TextView) dialog.findViewById(R.id.tv_userAge_value);
                userAge.setText(String.valueOf(user.getAge()));

                TextView favSportUser = (TextView) dialog.findViewById(R.id.tv_favSportUser_value);
                favSportUser.setText(String.valueOf(user.getFav_sport()));

                TextView emailUser = (TextView) dialog.findViewById(R.id.tv_emailUser_value);
                emailUser.setText(String.valueOf(user.getEmail()));

                dialog.show();
            }
        });

    }

}