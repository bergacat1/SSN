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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.ssn.eps.ssn.R;

import java.util.Calendar;

public class NewEventWizardActivity extends AppCompatActivity {

    private Button featuresButton;
    private Button fieldButton;
    private Button summaryButton;

    private FrameLayout flContainer;
    private LayoutInflater layoutInflater;

    private int stepWizard = 0;

    private Spinner sportsSpinner;
    private EditText numPlayersEditText;
    private EditText maxPricePlayerEditText;
    private EditText dateHourEditText;

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
            case (R.id.date_hour_edit_text):
                showDatePickerDialog(null);
                break;
            default:
                break;
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //showTimePickerDialog(null);
        }
    }
}
