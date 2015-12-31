package com.ssn.eps.ssn.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ssn.eps.model.Result;
import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.wscaller.SoapWSCaller;
import com.ssn.eps.ssn.wscaller.WSCallbackInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import lists.EventItemAdapter;
import General.Globals;
import model.Event_OLD;
import model.Sport_OLD;

public class MainActivity extends AppCompatActivity implements FragmentsCommunicationInterface{

    private SharedPreferences myPreference;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPreference = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                Intent intent = new Intent();
                intent = new Intent(MainActivity.this, NewEventWizardActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.leave))
                        .setMessage(getString(R.string.leave_app_text))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

                break;
            case R.id.new_event:
                intent = new Intent(this, NewEventWizardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){

        String email = myPreference.getString(Globals.PROPERTY_USER,"");

        if(email.equals("")){
            showToast(getString(R.string.internal_error));
            return;
        }

        SoapWSCaller.getInstance().unRegisterUserCall(this, email, new WSCallbackInterface() {
            @Override
            public void onProcesFinished(Result res) {
                if (!res.isValid()) {
                    showToast(getString(R.string.server_error) + ": " + res.getError());
                    return;
                }
                int id = (Integer) res.getData().get(0);

                if (id > 0) {

                    getActivity().finish();

                    LoginActivity.logged = false;

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                } else {
                    showToast(getString(R.string.server_error));
                }
            }
        });
    }

    private Activity getActivity(){
        return this;
    }

    private Context getContext(){
        return this;
    }

    @Override
    public List<Event_OLD> getEvents() {
        Sport_OLD sport = new Sport_OLD(0, "Futbol");
        List<Event_OLD> events = new ArrayList<>();
        events.add(new Event_OLD(sport, 12, 22, 10, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Event_OLD.State.FILLING, "Lleida"));
        events.add(new Event_OLD(sport, 12, 22, 10, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Event_OLD.State.FILLING, "Lleida"));
        events.add(new Event_OLD(sport, 12, 22, 10, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Event_OLD.State.FILLING, "Lleida"));
        events.add(new Event_OLD(sport, 12, 22, 10, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Event_OLD.State.FILLING, "Lleida"));
        return events;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int activePage = -1;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return EventsFragment.getNewInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            this.activePage = position;
            switch (position) {
                case 0:
                    return getString(R.string.eventos);
                case 1:
                    return getString(R.string.mis_eventos);
                case 2:
                    return getString(R.string.historico);
            }
            return null;
        }

        public int getActivePage(){
            return this.activePage;
        }
    }

    private void showToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
