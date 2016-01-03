package com.ssn.eps.ssn.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ssn.eps.model.Sport;
import com.ssn.eps.model.User;
import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.wscaller.Mapping;
import com.ssn.eps.ssn.wscaller.SoapWSCaller;
import com.ssn.eps.ssn.wscaller.WSCallbackInterface;

import org.ksoap2.serialization.PropertyInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import General.Globals;
import com.ssn.eps.model.Result;
import model.User_OLD;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener {

    public static boolean logged = false;

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleCloudMessaging gcm;

    private String regid;

    // UI references.
    private View mProgressView;
    private View mLoginFormView;

    private SharedPreferences myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // todo Powered by Google
        super.onCreate(savedInstanceState);
        //if(logged)finish();
        setContentView(R.layout.activity_login);

        myPreference = PreferenceManager.getDefaultSharedPreferences(this);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        checkPlayServices();
        /*if(MyDevice.getInstance().isOnline()){
            startMainActivity();
            finish();
        }*/
    }

    private Context getContext(){
        return this;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        boolean cancel = false;

        // Check if network is active
        if(!checkNetwork()){
            showToast(getString(R.string.activateNetwork));
            cancel = true;
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        }

        if(!cancel){
            //showProgress(true);
            signIn();
        }
    }
    private boolean checkNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        Globals.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else{
                Log.i("--->", "Dispositivo no soportado.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                if(!checkPlayServices())return;

                GoogleSignInAccount gsa = result.getSignInAccount();

                gcm = GoogleCloudMessaging.getInstance(this);

                // Obtenemos el registration ID
                regid = getRegistrationId(getApplicationContext(),gsa.getEmail());

                // Si no disponemos de registration ID comenzamos el registro
                if(regid.equals("")){
                    TareaRegistroGCM tarea = new TareaRegistroGCM();
                    tarea.execute(gsa.getEmail());
                }else{
                    Log.d(Globals.TAG, "YA Registrado en GCM: registration_id=" + regid);
                    registerUserInServer(gsa.getEmail(), regid, false);
                }

            }else{
                showToast("Error al registrar con Google +");
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private String getRegistrationId(Context context, String email)
    {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(Globals.PROPERTY_REG_ID, "");

        if (registrationId.length() == 0)
        {
            Log.d(Globals.TAG, "Registro GCM no encontrado.");
            return "";
        }

        String registeredUser =
                prefs.getString(Globals.PROPERTY_USER, "user");

        int registeredVersion =
                prefs.getInt(Globals.PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        long expirationTime =
                prefs.getLong(Globals.PROPERTY_EXPIRATION_TIME, -1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String expirationDate = sdf.format(new Date(expirationTime));

        Log.d(Globals.TAG, "Registro GCM encontrado (usuario=" + registeredUser +
                ", version=" + registeredVersion +
                ", expira=" + expirationDate + ")");

        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion)
        {
            Log.d(Globals.TAG, "Nueva versión de la aplicación.");
            return "";
        }
        else if (System.currentTimeMillis() > expirationTime)
        {
            Log.d(Globals.TAG, "Registro GCM expirado.");
            return "";
        }
        else if (!email.equals(registeredUser))
        {
            Log.d(Globals.TAG, "Nuevo nombre de usuario.");
            return "";
        }

        return registrationId;
    }

    private static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }

    private void setRegistrationId(Context context, String user, String userName, String regId)
    {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Globals.PROPERTY_USER, user);
        editor.putString(Globals.PROPERTY_USER_NAME, userName);
        editor.putString(Globals.PROPERTY_REG_ID, regId);
        //editor.putString(Globals.PROPERTY_SERVER_ID, serverId);
        editor.putInt(Globals.PROPERTY_APP_VERSION, appVersion);
        editor.putLong(Globals.PROPERTY_EXPIRATION_TIME,
                System.currentTimeMillis() + Globals.EXPIRATION_TIME_MS);

        editor.commit();
    }

    private void registerUserInServer(final String email, final String regid, final boolean comeFromGCMTask){

        final String userName = myPreference.getString("userName", email);

        User me = new User();
        me.setEmail(email);
        me.setUsername(userName);
        me.setGcmId(regid);

        SoapWSCaller.getInstance().registerUserCall(this, me, new WSCallbackInterface() {
            @Override
            public void onProcesFinished(Result res) {
                if (!res.isValid()) {
                    showToast(getString(R.string.server_error) +": " + res.getError());
                    return;
                }
                int id = (Integer) res.getData().get(0);

                if (id > 0) {
                    if (comeFromGCMTask)
                        setRegistrationId(getApplicationContext(), email, userName, regid);
                    myPreference.edit().putInt("userid", id);
                    LoginActivity.logged = true;
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    showToast(getString(R.string.server_error));
                }
            }
        });

    }

    private class TareaRegistroGCM extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";

            try
            {
                if (gcm == null)
                {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(Globals.SENDER_ID);


                Log.d(Globals.TAG, "Registrado en GCM: registration_id=" + regid);

                //Nos registramos en nuestro servidor
                registerUserInServer(params[0], regid,true);

            }
            catch (IOException ex)
            {
                Log.d(Globals.TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return msg;
        }
    }

    private void showToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}

