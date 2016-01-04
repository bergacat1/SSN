package com.ssn.eps.ssn.gcmservices;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ssn.eps.ssn.R;
import com.ssn.eps.ssn.activities.MainActivity;

import General.Globals;

/**
 * Created by Lluís on 19/12/2015.
 */
public class GCMIntentService extends IntentService
{
    private static final int NOTIF_ALERTA_ID = 1;

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                if(!validateNotification(extras))return;
                mostrarNotification(extras);
            }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private boolean validateNotification(Bundle extras) {

        if(!extras.containsKey("type")) {
            Log.e(Globals.TAG, "Invalid push notification");
            return false;
        }

        switch (extras.getInt("type")){
            case 0:
            case 1:
                if(extras.containsKey("eventId") && extras.getInt("eventId") > 0) return true;
                break;
            default:
                Log.e(Globals.TAG, "Invalid push notification");
                return false;
        }
        Log.e(Globals.TAG, "Invalid push notification");
        return false;
    }

    private void mostrarNotification(Bundle extras)
    {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo1)
                        .setContentTitle(getString(R.string.app_name))
                        .setAutoCancel(true);

        switch (extras.getInt("type")){ // todo: mirar si els usuaris volen rebre les notificacions
            case 0: // NEW EVENT
                mBuilder.setContentText(getString(R.string.notif_type_0));
                break;
            case 1: // USER JOINED IN EVENT
                mBuilder.setContentText(getString(R.string.notif_type_1));
                break;
            default:
                return;
        }

        Intent notIntent =  new Intent(this, MainActivity.class);
        notIntent.putExtras(extras);
        notIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent contIntent = PendingIntent.getActivity(
                this, 0, notIntent, 0);

        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }
}
