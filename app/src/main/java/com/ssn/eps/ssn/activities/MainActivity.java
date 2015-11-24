package com.ssn.eps.ssn.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.ssn.eps.ssn.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import lists.EventItemAdapter;
import model.Event;
import model.Sport;

public class MainActivity extends AppCompatActivity implements FragmentsCommunicationInterface{

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

                break;
            case R.id.new_event:
                intent = new Intent(this, NewEventWizardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<Event> getEvents() {
        Sport sport = new Sport(0, "Futbol");
        List<Event> events = new ArrayList<>();
        events.add(new Event(sport, 12, 22, 10, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Event.State.FILLING, "Lleida"));
        events.add(new Event(sport, 12, 22, 10, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Event.State.FILLING, "Lleida"));
        events.add(new Event(sport, 12, 22, 10, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Event.State.FILLING, "Lleida"));
        events.add(new Event(sport, 12, 22, 10, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), Event.State.FILLING, "Lleida"));
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
}
