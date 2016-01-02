package com.ssn.eps.ssn.wscaller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import com.ssn.eps.model.Result;
import com.ssn.eps.model.Sport;
import com.ssn.eps.model.User;
import com.ssn.eps.ssn.R;

/**
 * Created by lluis on 24/12/15.
 */
public class SoapWSCaller {

    private static SoapWSCaller instance = new SoapWSCaller();

    private final static String NAMESPACE = "http://ws.ssn/";
    private final String URL = "http://85.54.226.153:8080/SSN_WS/SSNWS";

    // Callbacks http://stackoverflow.com/questions/16800711/passing-function-as-a-parameter-in-java

    private SoapWSCaller(){}

    private void makeCall(Activity act, String methodName, List<PropertyInfo> piList, List<Mapping> mapList, WSCallbackInterface callback){

        // Check if network is active
        if(!checkNetwork(act)){
            Toast.makeText(act, act.getString(R.string.activateNetwork), Toast.LENGTH_LONG).show();
            act.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            return;
        }

        AsyncCallWS task = new AsyncCallWS(methodName, piList, mapList, callback);
        task.execute();
    }

    public void registerUserCall(Activity act, User user, WSCallbackInterface callback ){

        List<PropertyInfo> piList = new ArrayList<PropertyInfo>();
        PropertyInfo pi = new PropertyInfo();
        pi.setName("user");
        pi.setValue(user);
        piList.add(pi);

        List<Mapping> maList = new ArrayList<Mapping>();
        Mapping m = new Mapping("user", new User().getClass());
        maList.add(m);

        makeCall(act, "registerUser", piList, maList, callback);
    }

    public void unRegisterUserCall(Activity act, String email, WSCallbackInterface callback){
        //todo
    }

    public void getSportsCall(Activity act, WSCallbackInterface callback){

        List<Mapping> maList = new ArrayList<Mapping>();
        Mapping m = new Mapping("sport", new Sport().getClass());
        maList.add(m);

        makeCall(act, "getSports", null, maList, callback);
    }

    private boolean checkNetwork(Activity act){
        ConnectivityManager connectivityManager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static SoapWSCaller getInstance(){
        return instance;
    }

    private class AsyncCallWS extends AsyncTask <String, Void, Result> {

        private String soapAction = "";
        private String methodName = "";

        private List<PropertyInfo> piList;
        private List<Mapping> mapList;

        private WSCallbackInterface callback;

        public AsyncCallWS(String methodName, List<PropertyInfo> piList, List<Mapping> mapList, WSCallbackInterface callback){
            this.methodName = methodName;
            this.piList = piList;
            this.mapList = mapList;
            this.callback = callback;
        }


        @Override
        protected Result doInBackground(String... params) {

            Result res = null;

            SoapObject request = new SoapObject(NAMESPACE,methodName);

            if(piList != null)
                for(PropertyInfo pi : piList)
                    request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = false;
            envelope.setAddAdornments(false);
            envelope.implicitTypes = false;

            envelope.setOutputSoapObject(request);

            if(mapList != null)
                for(Mapping m : mapList)
                    envelope.addMapping(NAMESPACE,m.getName(),m.getaClass());

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            //androidHttpTransport.debug = true;

            try{

                androidHttpTransport.call(soapAction, envelope);
                //Log.d("SOAP Request: ", androidHttpTransport.requestDump);
                //Log.d("SOAP Response: ", androidHttpTransport.responseDump);
                if(envelope.bodyIn instanceof SoapFault){
                    String strFault = ((SoapFault) envelope.bodyIn).faultstring;
                    Log.d("SOAP", "SOAP Request: " + androidHttpTransport.requestDump);
                    Log.d("SOAP", "Fault string: " + strFault);
                } else {
                    SoapObject response = (SoapObject)envelope.getResponse();
                    res = new Result();
                    for (int i = 0; i < response.getPropertyCount() - 2; i++){
                        res.addData((Object)response.getProperty(i));
                    }
                    res.setError(response.getPropertyAsString("error"));
                    res.setValid(Boolean.valueOf(response.getProperty("valid").toString()));
                    return res;
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Result obj) {
            super.onPostExecute(obj);
            if (obj == null) return;
            if (callback != null) callback.onProcesFinished(obj);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}