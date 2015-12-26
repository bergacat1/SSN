package com.ssn.eps.ssn.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import java.util.HashMap;
import java.util.List;


import General.Globals;
import model.Event_OLD;
import model.ManagerEntity_OLD;
import model.ManagerEntityManaged;
import model.ManagerEntityNoManaged;


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
    private SeekBar seekBar;
    private GoogleMap mMap;
    private MapView mapView;
    private Circle circle;
    private Marker marker;

    private HashMap<Marker,Three<ManagerEntity_OLD,Boolean,Boolean>> managerEntityMarkers;

    private TextView TVSport;
    private TextView TVMinPlayers;
    private TextView TVMaxPlayers;
    private TextView TVMaxPricePlayer;
    private TextView TVDateHour;
    private TextView TVField;
    private TextView TVFieldTitle;

    private boolean mapModeField;

    private Event_OLD event;

    private SharedPreferences myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_wizard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myPreference = PreferenceManager.getDefaultSharedPreferences(this);

        //https://developers.google.com/places/android-api/autocomplete

        getSports();
        getZones();
        initializeViews(savedInstanceState);
        getFields();

        //showInitialMessage();

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
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.GONE);
                createButton.setVisibility(View.GONE);
                break;
            case (R.id.zone_button):
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.GONE);
                createButton.setVisibility(View.GONE);
                break;
            case (R.id.summary_button):
                buildSummary();
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.VISIBLE);
                createButton.setVisibility(View.VISIBLE);
                break;
            case (R.id.radio_button_zone):
                zoneRadioButton.setChecked(true);
                fieldRadioButton.setChecked(false);
                mapRadioButton.setChecked(false);
                zoneSpinner.setEnabled(true);
                seekBar.setEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setAllGesturesEnabled(false);
                break;
            case (R.id.radio_button_field):
                zoneRadioButton.setChecked(false);
                fieldRadioButton.setChecked(true);
                mapRadioButton.setChecked(false);
                zoneSpinner.setEnabled(false);
                seekBar.setEnabled(false);

                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                if(mMap.getMyLocation() != null) mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), 15),2000,null);
                showFieldsMap(true);
                break;
            case (R.id.radio_button_map):
                zoneRadioButton.setChecked(false);
                fieldRadioButton.setChecked(false);
                mapRadioButton.setChecked(true);
                zoneSpinner.setEnabled(false);
                seekBar.setEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                if(mMap.getMyLocation() != null) mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), 15),2000,null);
                showFieldsMap(false);
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(mapRadioButton.isChecked()) return false;

                if(managerEntityMarkers.get(marker).second){ //MANAGED ENTITY
                    if(managerEntityMarkers.get(marker).third){ //SELECTED
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_managed_no_selected));
                        managerEntityMarkers.put(marker,new Three<ManagerEntity_OLD, Boolean, Boolean>(managerEntityMarkers.get(marker).first,true,false));
                    }else{
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_managed_selected));
                        managerEntityMarkers.put(marker,new Three<ManagerEntity_OLD, Boolean, Boolean>(managerEntityMarkers.get(marker).first,true,true));
                    }

                }else{// NO MANAGED ENTITY
                    if(managerEntityMarkers.get(marker).third){
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_no_managed_no_selected));
                        managerEntityMarkers.put(marker,new Three<ManagerEntity_OLD, Boolean, Boolean>(managerEntityMarkers.get(marker).first,false,false));
                    }else{
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_no_managed_selected));
                        managerEntityMarkers.put(marker,new Three<ManagerEntity_OLD, Boolean, Boolean>(managerEntityMarkers.get(marker).first,false,true));
                    }
                }

                return false;
            }
        });
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
            mMap.setMyLocationEnabled(true);

            mMap.setOnMarkerDragListener(this);
            // Enable MyLocation Layer of Google Map
            //mMap.setMyLocationEnabled(true);

            // set map type
            String myListPreference = myPreference.getString("mapType", "1");
            mMap.setMapType(Integer.parseInt(myListPreference));

            // Set 3D buildings
            boolean buildings = myPreference.getBoolean("buildings_map_checkbox",true);
            mMap.setBuildingsEnabled(buildings);

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
                    if(!mapRadioButton.isChecked())return;
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

    private void showFieldsMap(boolean mode){
        if (mMap != null) {
            if(marker != null){
                marker.setVisible(!mode);
                circle.setVisible(!mode);
            }
            for(Marker m : managerEntityMarkers.keySet()){
                m.setVisible(mode);
            }
        }
    }

    private void getSports(){
        sportsList.add("Futbol");
        sportsList.add("Tennis");

    }
    private void getZones(){
        zonesList.add("Lleida");
        zonesList.add("Balaguer");

    }
    private void getFields(){

        if(managerEntityMarkers == null) {
            managerEntityMarkers = new HashMap<>();
        }

        ManagerEntity_OLD m1 = new ManagerEntityManaged();
        m1.setLatitude(41.62792);
        m1.setLongitude(0.629101);
        m1.setName("GYM TONY");
        m1.setId(1);

        Marker mm1 = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(m1.getLatitude(),m1.getLongitude()))
                        .title(m1.getName())
                        .draggable(true)
                        .visible(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_managed_no_selected)));

        managerEntityMarkers.put(mm1,new Three<ManagerEntity_OLD, Boolean, Boolean>(m1,true,false));

        ManagerEntity_OLD m2 = new ManagerEntityNoManaged();
        m2.setLatitude(41.61780);
        m2.setLongitude(0.629121);
        m2.setName("Royal Machine");
        m2.setId(1);

        Marker mm2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(m2.getLatitude(),m2.getLongitude()))
                .title(m2.getName())
                .draggable(true)
                .visible(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.field_icon_no_managed_no_selected)));

        managerEntityMarkers.put(mm2, new Three<ManagerEntity_OLD, Boolean, Boolean>(m2, false, false));
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
            String text = "";
            for(Three t : managerEntityMarkers.values()){
                text +=  ((ManagerEntity_OLD)t.first).getName() + " ";
            }
            TVField.setText(text);
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

        /*event = new Event_OLD(sportsList.get(sportsSpinner.getSelectedItemPosition())
                , numMinPlayersEditText.getText()
                , numMaxPlayersEditText.getText()
                , maxPricePlayerEditText.getText()
                , );*/

        bundle.putSerializable("message", getString(R.string.ok_creation_event));
        bundle.putBoolean("finish", true);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "ok_message_new_event");
    }

    private boolean validate(){

        if(sportsSpinner.getSelectedItemPosition() < 0) return false;
        if(numMinPlayersEditText.getText() == null || numMinPlayersEditText.getText().toString().isEmpty() || !isNumeric(numMinPlayersEditText.getText().toString())) return false;
        if(numMaxPlayersEditText.getText() == null || numMaxPlayersEditText.getText().toString().isEmpty() || !isNumeric(numMaxPlayersEditText.getText().toString())) return false;
        if(Integer.parseInt(numMaxPlayersEditText.getText().toString()) < Integer.parseInt(numMinPlayersEditText.getText().toString())) return false;
        if(maxPricePlayerEditText.getText() == null || maxPricePlayerEditText.getText().toString().isEmpty() || !isNumeric(maxPricePlayerEditText.getText().toString())) return false;
        if(dateHourEditText.getText() == null || dateHourEditText.getText().toString().isEmpty()) return false;
        if(zoneRadioButton.isChecked() && zoneSpinner.getSelectedItemPosition() < 0) return false;
        if(fieldRadioButton.isChecked() && !checkSelectedFields()) return false;
        if(mapRadioButton.isChecked() && (marker == null || circle == null)) return false;

        return true;
    }

    private boolean checkSelectedFields(){
        for(Three t : managerEntityMarkers.values()){
            if((Boolean)t.third){
                return true;
            }
        }
        return false;
    }

    private void showInitialMessage(){
        MessageDialogFragment dialog = new MessageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("title",getString(R.string.new_event));
        bundle.putSerializable("message",getString(R.string.new_event));
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "initial_message_new_event");
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

    private class Three<A, B, C> {
        private A first;
        private B second;
        private C third;

        public Three(A first, B second, C third) {
            super();
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public int hashCode() {
            int hashFirst = first != null ? first.hashCode() : 0;
            int hashSecond = second != null ? second.hashCode() : 0;

            return (hashFirst + hashSecond) * hashSecond + hashFirst;
        }

        public boolean equals(Object other) {
            if (other instanceof Three) {
                Three otherPair = (Three) other;
                return
                        ((  this.first == otherPair.first ||
                                ( this.first != null && otherPair.first != null &&
                                        this.first.equals(otherPair.first))) &&
                                (	this.second == otherPair.second ||
                                        ( this.second != null && otherPair.second != null &&
                                                this.second.equals(otherPair.second))) );
            }

            return false;
        }

        public String toString()
        {
            return "(" + first + ", " + second + ")";
        }

        public A getFirst() {
            return first;
        }

        public void setFirst(A first) {
            this.first = first;
        }

        public B getSecond() {
            return second;
        }

        public void setSecond(B second) {
            this.second = second;
        }
    }
}
