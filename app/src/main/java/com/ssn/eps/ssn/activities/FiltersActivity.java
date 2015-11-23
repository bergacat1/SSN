package com.ssn.eps.ssn.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;
import com.ssn.eps.ssn.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import General.Globals;

public class FiltersActivity extends AppCompatActivity {

    private Activity context;
    private List<String> sportsList = Arrays.asList("Hockey", "Rugby", "Futbol");
    private List<String> zonesList = Arrays.asList("Lleida", "Torrefarrera", "Venavent");
    private List<String> fieldsList = Arrays.asList("EKKE", "Royal", "Trevol");

    private Spinner sportsSpinner;
    private EditText numMinPlayersEditText;
    private EditText maxPricePlayerEditText;
    private EditText dateFromET;
    private EditText dateToET;

    private Button bAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.context = this;

        setSupportActionBar(toolbar);
        sportsSpinner = (Spinner) findViewById(R.id.sport_filter);
        //http://stackoverflow.com/questions/867518/how-to-make-an-android-spinner-with-initial-text-select-one
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sportsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapter);

        numMinPlayersEditText = (EditText) findViewById(R.id.min_players_filter);

        maxPricePlayerEditText = (EditText) findViewById(R.id.max_price_filter);

        final Calendar c = Calendar.getInstance();
        dateFromET = (EditText) findViewById(R.id.date_from_filter);
        dateFromET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog date = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            c.set(year, monthOfYear, dayOfMonth);
                            dateFromET.setText(Globals.sdfNoHour.format(c.getTime()));
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    date.show();
                }
            }
        });
        dateToET = (EditText) findViewById(R.id.date_to_filter);

        dateToET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog date = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            c.set(year, monthOfYear, dayOfMonth);
                            dateToET.setText(Globals.sdfNoHour.format(c.getTime()));
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    date.show();
                }
            }
        });

        bAccept = (Button) findViewById(R.id.but_accept_filters);
        bAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = context.getIntent();
                i.putExtra("SPORT", (String)sportsSpinner.getSelectedItem());
                i.putExtra("MINPLAYERS", numMinPlayersEditText.getText());
                i.putExtra("MAXPRICE", maxPricePlayerEditText.getText());
                try {
                    i.putExtra("DATEFROM", Globals.sdfNoHour.parse(dateFromET.getText().toString()));
                }catch(ParseException e){
                    e.printStackTrace();
                }
                try {
                    i.putExtra("DATETO", Globals.sdfNoHour.parse(dateToET.getText().toString()));
                }catch(ParseException e){
                    e.printStackTrace();
                }


                setResult(0, i);
                finish();
            }
        });
    }
}
