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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ssn.eps.model.Result;
import com.ssn.eps.model.Event;
import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.fragments.MessageDialogFragment;
import com.ssn.eps.ssn.gcmservices.GCMIntentService;
import com.ssn.eps.ssn.wscaller.SoapWSCaller;
import com.ssn.eps.ssn.wscaller.WSCallbackInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import lists.EventItemAdapter;
import General.Globals;
import model.Event_OLD;
import model.Sport_OLD;

public class MainActivity extends AppCompatActivity implements FragmentsCommunicationInterface, GoogleApiClient.OnConnectionFailedListener{

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
    private TabLayout tabLayout;

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

        tabLayout = (TabLayout) findViewById(R.id.tabs);
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

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("type")){ // SHOW NOTIFICATION DIALOG

            //MessageDialogFragment dialog = new MessageDialogFragment();
            //Bundle bundle = new Bundle();

            switch (extras.getString("type")){
                case "0": // New event
                    /*bundle.putSerializable("title",getString(R.string.notif_type_0));
                    bundle.putSerializable("message", getString(R.string.notif_type_0_text));
                    bundle.putSerializable("positiveButtonTextId",R.string.event_detail);
                    bundle.putSerializable("cancellButton",true);
                    bundle.putSerializable("eventId",Integer.parseInt(extras.getString("idEvent")));
                    break;*/
                case "1": // Join event
                    /*bundle.putSerializable("title",getString(R.string.notif_type_1));
                    bundle.putSerializable("message", getString(R.string.notif_type_1_text));
                    bundle.putSerializable("positiveButtonTextId",R.string.event_detail);
                    bundle.putSerializable("cancellButton",true);
                    bundle.putSerializable("eventId",Integer.parseInt(extras.getString("idEvent")));
                    break;*/
                case "2": // Event canceled
                    /*bundle.putSerializable("title",getString(R.string.notif_type_2));
                    bundle.putSerializable("message", getString(R.string.notif_type_2_text));
                    bundle.putSerializable("positiveButtonTextId",R.string.event_detail);
                    bundle.putSerializable("cancellButton",true);
                    bundle.putSerializable("eventId",Integer.parseInt(extras.getString("idEvent")));
                    break;*/
                case "3": // Event reserved
                    /*bundle.putSerializable("title",getString(R.string.notif_type_3));
                    bundle.putSerializable("message", getString(R.string.notif_type_3_text));
                    bundle.putSerializable("positiveButtonTextId",R.string.event_detail);
                    bundle.putSerializable("cancellButton",true);
                    bundle.putSerializable("eventId",Integer.parseInt(extras.getString("idEvent")));*/
                    Intent i = new Intent(this,EventDetailTabedActivity.class);
                    i.putExtra("idevent", Integer.parseInt(extras.getString("idEvent")));
                    startActivity(i);
                    break;
                default:
                    return;
            }

            //dialog.setArguments(bundle);
            //dialog.show(getSupportFragmentManager(), "notification_dialog");
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*switch (requestCode){
            case GCMIntentService.COME_FROM_NOTIF:

                break;
        }*/
    }

    private void logout(){

        int userId = myPreference.getInt(Globals.PROPERTY_USER_ID, -1);

        if(userId < 0){
            showToast(getString(R.string.internal_error));
            return;
        }

        SoapWSCaller.getInstance().unRegisterUserCall(this, userId, new WSCallbackInterface() {
            @Override
            public void onProcesFinished(Result res) {
                if (!res.isValid()) {
                    showToast(getString(R.string.server_error) + ": " + res.getError());
                    return;
                }
                int id = (Integer) res.getData().get(0);

                if (id > 0) {
                    setUnRegistrationId();

                    getActivity().finish();

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                } else {
                    showToast(getString(R.string.server_error));
                }
            }
            @Override
            public void onProcessError() {
                showToast(getString(R.string.server_error));
            }
        });
    }

    private void setUnRegistrationId()
    {
        SharedPreferences.Editor editor = myPreference.edit();
        editor.putString(Globals.PROPERTY_USER, "user");
        editor.putString(Globals.PROPERTY_USER_NAME, "userName");
        editor.putString(Globals.PROPERTY_REG_ID, "regId");
        editor.putInt(Globals.PROPERTY_USER_ID, -1);

        editor.commit();
    }

    private Activity getActivity(){
        return this;
    }

    private Context getContext(){
        return this;
    }

    @Override
    public void refreshTab(int tab) {

    }

    public void goToEventById(int id){
        if(id < 0){
            showToast(getString(R.string.internal_error));
            return;
        }

        mSectionsPagerAdapter.refreshTab(EventsFragment.TABEVENTS);

        Intent intent = new Intent(getContext(),EventDetailTabedActivity.class);
        intent.putExtra("idevent",id);

        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements FragmentsCommunicationInterface {
        private int activePage = -1;
        private HashMap<Integer, EventsFragment> fragmentsMap;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentsMap = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {
            if(!fragmentsMap.containsKey(position)) {
                EventsFragment fragment = EventsFragment.getInstance(position, this);
                fragmentsMap.put(position, fragment);
            }
            return fragmentsMap.get(position);


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
                case EventsFragment.TABEVENTS:
                    return getString(R.string.eventos);
                case EventsFragment.TABMYEVENTS:
                    return getString(R.string.mis_eventos);
                case EventsFragment.TABHISTORYEVENTS:
                    return getString(R.string.historico);
            }
            return null;
        }

        public int getActivePage(){
            return this.activePage;
        }


        @Override
        public void refreshTab(int tab) {
            fragmentsMap.get(tab).refreshEvents();
        }

        @Override
        public void goToEventById(int id) {

        }
    }

    private void showToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
