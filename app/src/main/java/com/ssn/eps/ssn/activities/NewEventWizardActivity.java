package com.ssn.eps.ssn.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.ssn.eps.ssn.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import General.Globals;
import model.Sport;

public class NewEventWizardActivity extends AppCompatActivity {

    private Button featuresButton;
    private Button fieldButton;
    private Button summaryButton;

    private FrameLayout flContainer;
    private LayoutInflater layoutInflater;

    private int stepWizard = 0;
    private List<String> sportsList = new ArrayList<String>();

    private Spinner sportsSpinner;
    private EditText numPlayersEditText;
    private EditText maxPricePlayerEditText;
    public EditText dateHourEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_wizard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //http://stackoverflow.com/questions/4046644/android-how-to-dynamically-include-a-xml-layout
        //http://stackoverflow.com/questions/22592192/dynamically-include-another-layout-in-fragment-activity
        // Retrieve your container
        flContainer = (FrameLayout)findViewById(R.id.flContainer);

        layoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewGroup v = null;
        flContainer.addView(layoutInflater.inflate(R.layout.content_new_event_wizard_step_1, v));

        featuresButton = (Button) findViewById(R.id.features_button);
        fieldButton = (Button) findViewById(R.id.zone_button);
        summaryButton = (Button) findViewById(R.id.summary_button);


        sportsList.add("Futbol");
        sportsList.add("Tennis");
        initializeStepViews(stepWizard);

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
                flContainer.removeAllViews();
                flContainer.addView(layoutInflater.inflate(R.layout.content_new_event_wizard_step_1, vg));
                stepWizard = 0;
                break;
            case (R.id.zone_button):
                flContainer.removeAllViews();
                flContainer.addView(layoutInflater.inflate(R.layout.content_new_event_wizard_step_2, vg));
                stepWizard = 1;
                break;
            case (R.id.summary_button):
                flContainer.removeAllViews();
                flContainer.addView(layoutInflater.inflate(R.layout.content_new_event_wizard_step_3, vg));
                stepWizard = 2;
                break;
            default:
                break;
        }
    }

    private void initializeStepViews(int step){
        switch (step){
            case 0:
                sportsSpinner = (Spinner)findViewById(R.id.sports_spinner);
                //http://stackoverflow.com/questions/867518/how-to-make-an-android-spinner-with-initial-text-select-one
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sportsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sportsSpinner.setAdapter(adapter);

                numPlayersEditText = (EditText) findViewById(R.id.num_players_edit_text);

                maxPricePlayerEditText =(EditText) findViewById(R.id.max_price_player_edit_text);

                dateHourEditText = (EditText) findViewById(R.id.date_hour_edit_text);
                dateHourEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            showDatePickerDialog(null);
                        }
                    }
                });

                break;
            case 1:

                break;
            case 2:

                break;

        }
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
