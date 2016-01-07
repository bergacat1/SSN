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
import com.ssn.eps.model.Filters;
import com.ssn.eps.model.Result;
import com.ssn.eps.model.Sport;
import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.wscaller.SoapWSCaller;
import com.ssn.eps.ssn.wscaller.WSCallbackInterface;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import General.Globals;

public class FiltersActivity extends AppCompatActivity {

    private Activity context;
    private List<Sport> sportsList;
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
        this.sportsList = new ArrayList<>();

        setSupportActionBar(toolbar);
        SoapWSCaller.getInstance().getSportsCall(this, new WSCallbackInterface() {
            @Override
            public void onProcesFinished(Result res) {
                if(res.isValid())
                    createSportsSpinner(res.getData());
            }
        });
        sportsSpinner = (Spinner) findViewById(R.id.sport_filter);

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
                Filters f = null;
                try {
                    f = new Filters(0, ((Sport)sportsSpinner.getSelectedItem()).getIdSport()
                            , numMinPlayersEditText.getText().length() > 0  ? Integer.parseInt(numMinPlayersEditText.getText().toString()) : 0
                            , maxPricePlayerEditText.getText().length() > 0 ? Integer.parseInt(maxPricePlayerEditText.getText().toString()) : 0
                            , dateFromET.getText().length() > 0 ? Globals.sdfNoHour.parse(dateFromET.getText().toString()).getTime() : 0
                            , dateToET.getText().length() > 0 ? Globals.sdfNoHour.parse(dateToET.getText().toString()).getTime() : 0);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                i.putExtra("filter", f);


                setResult(0, i);
                finish();
            }
        });
    }
    private void createSportsSpinner(List<Sport> sports){
        Sport fakeSport = new Sport();
        fakeSport.setIdSport(-1);
        sportsList.add(fakeSport);
        sportsList.addAll(sports);
        ArrayAdapter<Sport> adapter = new ArrayAdapter<Sport>(this, android.R.layout.simple_spinner_item, sportsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapter);
    }
}
