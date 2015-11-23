package com.ssn.eps.ssn.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.fragments.MessageDialogFragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import General.Globals;
import model.Event;


public class NewEventWizardActivity extends AppCompatActivity implements OnMarkerDragListener{

    private RelativeLayout r1;
    private RelativeLayout r2;
    private RelativeLayout r3;

    private Button featuresButton;
    private Button fieldButton;
    private Button summaryButton;
    private Button createButton;

    //private FrameLayout flContainer;
    //private LayoutInflater layoutInflater;

    //private int stepWizard = 0;
    private List<String> sportsList = new ArrayList<String>();
    private List<String> zonesList = new ArrayList<String>();
    private List<String> fieldsList = new ArrayList<String>();

    private Spinner sportsSpinner;
    private EditText numMinPlayersEditText;
    private EditText numMaxPlayersEditText;
    private EditText maxPricePlayerEditText;
    private EditText dateHourEditText;

    private RadioGroup radioGroup;
    private RadioButton zoneRadioButton;
    private RadioButton fieldRadioButton;
    private RadioButton mapRadioButton;
    private Spinner zoneSpinner;
    private Spinner fieldSpinner;
    private SeekBar seekBar;
    private GoogleMap mMap;
    private MapView mapView;
    private Circle circle;
    private Marker marker;

    private TextView TVSport;
    private TextView TVMinPlayers;
    private TextView TVMaxPlayers;
    private TextView TVMaxPricePlayer;
    private TextView TVDateHour;
    private TextView TVField;
    private TextView TVFieldTitle;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_wizard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //http://stackoverflow.com/questions/4046644/android-how-to-dynamically-include-a-xml-layout
        //http://stackoverflow.com/questions/22592192/dynamically-include-another-layout-in-fragment-activity
        // Retrieve your container
        //flContainer = (FrameLayout)findViewById(R.id.flContainer);
        //layoutInflater = (LayoutInflater)
                //this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //ViewGroup v = null;
        //flContainer.addView(layoutInflater.inflate(R.layout.content_new_event_wizard_step_1, v));

        sportsList.add("Futbol");
        sportsList.add("Tennis");
        zonesList.add("Lleida");
        zonesList.add("Balaguer");
        fieldsList.add("GYM TONY");
        fieldsList.add("Royal");

        initializeViews(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void handleClick(View v){
        ViewGroup vg = null;
        switch (v.getId()){
            case (R.id.features_button):
                /*flContainer.removeAllViews();
                flContainer.addView(layoutInflater.inflate(R.layout.content_new_event_wizard_step_1, vg));
                stepWizard = 0;
                initializeStepViews(stepWizard);*/
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.GONE);
                createButton.setVisibility(View.GONE);
                /*featuresButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                fieldButton.setBackgroundResource(android.R.drawable.btn_default);
                summaryButton.setBackgroundResource(android.R.drawable.btn_default_small);*/
                break;
            case (R.id.zone_button):
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.GONE);
                createButton.setVisibility(View.GONE);
                /*featuresButton.setBackgroundResource(android.R.drawable.btn_default_small);
                fieldButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                summaryButton.setBackgroundResource(android.R.drawable.btn_default_small);*/
                break;
            case (R.id.summary_button):
                buildSummary();
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.VISIBLE);
                createButton.setVisibility(View.VISIBLE);
                /*featuresButton.setBackgroundResource(android.R.drawable.btn_default_small);
                fieldButton.setBackgroundResource(android.R.drawable.btn_default_small);
                summaryButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));*/
                break;
            case (R.id.radio_button_zone):
                zoneRadioButton.setChecked(true);
                fieldRadioButton.setChecked(false);
                mapRadioButton.setChecked(false);
                zoneSpinner.setEnabled(true);
                fieldSpinner.setEnabled(false);
                seekBar.setEnabled(false);
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setAllGesturesEnabled(false);
                break;
            case (R.id.radio_button_field):
                zoneRadioButton.setChecked(false);
                fieldRadioButton.setChecked(true);
                mapRadioButton.setChecked(false);
                zoneSpinner.setEnabled(false);
                fieldSpinner.setEnabled(true);
                seekBar.setEnabled(false);
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setAllGesturesEnabled(false);
                break;
            case (R.id.radio_button_map):
                zoneRadioButton.setChecked(false);
                fieldRadioButton.setChecked(false);
                mapRadioButton.setChecked(true);
                zoneSpinner.setEnabled(false);
                fieldSpinner.setEnabled(false);
                seekBar.setEnabled(true);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                break;
            default:
                break;
        }
    }

    private void initializeViews(Bundle savedInstanceState){

        featuresButton = (Button) findViewById(R.id.features_button);
        fieldButton = (Button) findViewById(R.id.zone_button);
        summaryButton = (Button) findViewById(R.id.summary_button);
        createButton = (Button) findViewById(R.id.new_event_create_button);
        createButton.setVisibility(View.GONE);
        r1 = (RelativeLayout) findViewById(R.id.new_event_wizard_step_1);
        r2 = (RelativeLayout) findViewById(R.id.new_event_wizard_step_2);
        r3 = (RelativeLayout) findViewById(R.id.new_event_wizard_step_3);

        sportsSpinner = (Spinner)findViewById(R.id.sports_spinner);
        //http://stackoverflow.com/questions/867518/how-to-make-an-android-spinner-with-initial-text-select-one
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sportsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapter);

        numMinPlayersEditText = (EditText) findViewById(R.id.num_min_players_edit_text);
        numMaxPlayersEditText = (EditText) findViewById(R.id.num_max_players_edit_text);

        maxPricePlayerEditText =(EditText) findViewById(R.id.max_price_player_edit_text);

        dateHourEditText = (EditText) findViewById(R.id.date_hour_edit_text);
        dateHourEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(null);
                }
            }
        });

        zoneRadioButton = (RadioButton) findViewById(R.id.radio_button_zone);
        fieldRadioButton = (RadioButton) findViewById(R.id.radio_button_field);
        mapRadioButton = (RadioButton) findViewById(R.id.radio_button_map);
        zoneSpinner = (Spinner)findViewById(R.id.zone_spinner);
        zoneRadioButton.setChecked(true);
        //http://stackoverflow.com/questions/867518/how-to-make-an-android-spinner-with-initial-text-select-one
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, zonesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zoneSpinner.setAdapter(adapter);

        fieldSpinner = (Spinner)findViewById(R.id.field_spinner);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, fieldsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner.setAdapter(adapter);
        fieldSpinner.setEnabled(false);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) progress = 1;
                if (circle != null) circle.setRadius(progress * 1000 / 4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mMap = mapView.getMap();
        mMap.getUiSettings().setAllGesturesEnabled(false);
        MapsInitializer.initialize(this);
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mapRadioButton.isChecked();
            }
        });
        initializeMap();

        TVSport = (TextView) findViewById(R.id.TV_sport);
        TVMinPlayers = (TextView) findViewById(R.id.TV_num_min_players);
        TVMaxPlayers = (TextView) findViewById(R.id.TV_num_max_players);
        TVMaxPricePlayer = (TextView) findViewById(R.id.TV_max_price_player);
        TVDateHour = (TextView) findViewById(R.id.TV_date_hour);
        TVField = (TextView) findViewById(R.id.TV_field);
        TVFieldTitle = (TextView) findViewById(R.id.TV_field_title);
    }

    private void initializeMap() {
        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            mMap.setOnMarkerDragListener(this);
            // Enable MyLocation Layer of Google Map
            //mMap.setMyLocationEnabled(true);

            // set map type
            //String myListPreference = myPreference.getString("map_type_list", "1");
            //mMap.setMapType(Integer.parseInt(myListPreference));

            // Set 3D buildings
            //boolean buildings = myPreference.getBoolean("buildings_map_checkbox",true);
            //mMap.setBuildingsEnabled(buildings);

            //Nos registramos para recibir actualizaciones de la posición
            //locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {


                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    LatLng pos = new LatLng(latLng.latitude, latLng.longitude);
                    if(marker == null){
                        marker = mMap.addMarker(new MarkerOptions()
                                        .position(pos)
                                        .title(getString(R.string.marker_title))
                                        .draggable(true)
                        );
                    }else{
                        marker.setPosition(pos);
                    }
                    // Instantiates a new CircleOptions object and defines the center and radius
                    // Get back the mutable Circle
                    if(circle == null){
                        circle = mMap.addCircle(new CircleOptions()
                                        .center(pos)
                                        .radius(250)
                                        .strokeWidth(2)
                                        .strokeColor(Color.BLUE)
                                        .fillColor(Color.argb(140,36,4,218))
                        );
                    }else{
                        circle.setCenter(latLng);
                    }
                }
            });

            // Try center map on my device
            /*Location myLoc = mMap.getMyLocation();
            if(myLoc != null){
                centerMapOnPosition(new LatLng(myLoc.getLatitude(),myLoc.getLongitude()));
            }else{
                LatLng myLatlon = getDeviceLocation();
                if(myLatlon != null){
                    centerMapOnPosition(myLatlon);
                }
            }*/

        }
    }

    private void buildSummary(){

        TVSport.setText(sportsList.get(sportsSpinner.getSelectedItemPosition()));
        TVMinPlayers.setText(numMinPlayersEditText.getText());
        TVMaxPlayers.setText(numMaxPlayersEditText.getText());
        TVMaxPricePlayer.setText(maxPricePlayerEditText.getText());
        TVDateHour.setText(dateHourEditText.getText());

        if(zoneRadioButton.isChecked()){
            TVFieldTitle.setText(getString(R.string.radio_button_zone)+":");
            TVField.setText(zonesList.get(zoneSpinner.getSelectedItemPosition()));
        }else if(fieldRadioButton.isChecked()){
            TVFieldTitle.setText(getString(R.string.radio_button_field)+":");
            TVField.setText(fieldsList.get(fieldSpinner.getSelectedItemPosition()));
        }else{
            TVFieldTitle.setText(getString(R.string.radio_button_map)+":");
            if(marker != null && circle != null)
                TVField.setText("  lat: " + new DecimalFormat("#0.00").format(marker.getPosition().latitude)
                    + "º long: " + new DecimalFormat("#0.00").format(marker.getPosition().longitude)
                    + "º R: " + circle.getRadius()/1000 + "Km");
        }

    }

    public void createEvent(View v){
        MessageDialogFragment dialog = new MessageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("title",getString(R.string.new_event));
        if(!validate()){
            bundle.putSerializable("message",getString(R.string.error_validate_event));
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(),"error_message_new_event");
            return;
        }

        /*event = new Event(sportsList.get(sportsSpinner.getSelectedItemPosition())
                , numMinPlayersEditText.getText()
                , numMaxPlayersEditText.getText()
                , maxPricePlayerEditText.getText()
                , );*/

        bundle.putSerializable("message", getString(R.string.ok_creation_event));
        bundle.putBoolean("finish",true);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(),"ok_message_new_event");
    }

    private boolean validate(){

        if(sportsSpinner.getSelectedItemPosition() < 0) return false;
        if(numMinPlayersEditText.getText() == null || numMinPlayersEditText.getText().toString().isEmpty() || !isNumeric(numMinPlayersEditText.getText().toString())) return false;
        if(numMaxPlayersEditText.getText() == null || numMaxPlayersEditText.getText().toString().isEmpty() || !isNumeric(numMaxPlayersEditText.getText().toString())) return false;
        if(Integer.parseInt(numMaxPlayersEditText.getText().toString()) < Integer.parseInt(numMinPlayersEditText.getText().toString())) return false;
        if(maxPricePlayerEditText.getText() == null || maxPricePlayerEditText.getText().toString().isEmpty() || !isNumeric(maxPricePlayerEditText.getText().toString())) return false;
        if(dateHourEditText.getText() == null || dateHourEditText.getText().toString().isEmpty()) return false;
        if(zoneRadioButton.isChecked() && zoneSpinner.getSelectedItemPosition() < 0) return false;
        if(fieldRadioButton.isChecked() && fieldSpinner.getSelectedItemPosition() < 0) return false;
        if(mapRadioButton.isChecked() && (marker == null || circle == null)) return false;

        return true;
    }

    public void setDateEditText(Calendar calendar){
        dateHourEditText.setText(Globals.sdf.format(calendar.getTime()));
        dateHourEditText.clearFocus();
    }
    public String getDateEditText(){
        return dateHourEditText.getText().toString();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //circle.setVisible(false);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        circle.setCenter(marker.getPosition());
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        circle.setCenter(marker.getPosition());
        //circle.setVisible(true);
    }

    public boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    private static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private Calendar c;
        public TimePickerFragment(Calendar calendar){
            super();
            this.c = calendar;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE,minute);
            ((NewEventWizardActivity)getActivity()).setDateEditText(c);
        }
    }

    private static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private Calendar c;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            String date = ((NewEventWizardActivity)getActivity()).getDateEditText();
            c = Calendar.getInstance();
            if(!date.isEmpty()){
                try {
                    c.setTime(Globals.sdf.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if(view.isShown()){
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);
                DialogFragment newFragment = new TimePickerFragment(c);
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        }
    }
}
