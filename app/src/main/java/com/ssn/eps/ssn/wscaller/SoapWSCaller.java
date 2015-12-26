package com.ssn.eps.ssn.wscaller;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import com.ssn.eps.model.Result;

/**
 * Created by lluis on 24/12/15.
 */
public class SoapWSCaller {

    private final String NAMESPACE = "http://192.168.1.105:8080/SSN_WS/";
    private final String URL = "http://192.168.1.105:8080/SSN_WS/SSNWS";
    private String soapAction = "";
    private String methodName = "";

    private List<PropertyInfo> piList;
    private List<Mapping> mapList;

    private WSCallbackInterface callback;

    // Callbacks http://stackoverflow.com/questions/16800711/passing-function-as-a-parameter-in-java

    public SoapWSCaller(String methodName, List<PropertyInfo> piList, List<Mapping> mapList, WSCallbackInterface callback){
        this.soapAction = this.NAMESPACE + methodName;
        this.methodName = methodName;
        this.piList = piList;
        this.mapList = mapList;
        this.callback = callback;
    }

    public void makeCall(){
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }

    private class AsyncCallWS extends AsyncTask <String, Void, Result> {

        @Override
        protected Result doInBackground(String... params) {

            Result res = null;

            SoapObject request = new SoapObject(NAMESPACE,methodName);

            if(piList != null)
                for(PropertyInfo pi : piList)
                    request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            if(mapList != null)
                for(Mapping m : mapList)
                    envelope.addMapping(NAMESPACE,m.getName(),m.getClass());

            envelope.addMapping(NAMESPACE,"Result",Result.class);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try{

                androidHttpTransport.call(soapAction, envelope);

                res = (Result)envelope.bodyIn;

                //SoapObject response = (SoapObject) envelope.getResponse();
                //res.setData(response.getProperty(0));

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
