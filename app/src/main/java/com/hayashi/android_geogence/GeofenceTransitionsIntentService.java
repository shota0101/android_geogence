package com.hayashi.android_geogence;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static Activity activity = null;

    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification("onHandleIntent() called");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            sendNotification("geofencingEvent.hasError()");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            String message = getMessage(geofenceTransition, triggeringGeofences);
            MyLog.getInstance().debug(message);
            sendNotification(message);
        } else {
            sendNotification("onHandleIntent error");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.getInstance().debug("onDestroy()");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        MyLog.getInstance().debug("onConnected() " + connectionHint.toString());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        MyLog.getInstance().debug("onConnectionSuspended() " + String.valueOf(cause));
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        MyLog.getInstance().debug("onConnectionFailed() " + result.toString());
    }

    public static void sendNotification(String text) {
        MyNotification myNotification = new MyNotification(
                GeofenceTransitionsIntentService.activity
        );
        myNotification.title = "ジオフェンステスト";
        myNotification.text = text;
        myNotification.run();
    }

    private String getMessage(int geofenceTransition, List<Geofence> geofenceList) {
        String message = "";

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            message += "enter";
            sendNotification("");
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            message += "exit";
        } else {
            message += "else";
        }
        message += " , count : " + geofenceList.size() + " ( ";

        for (Geofence geofence : geofenceList) {
            message += geofence.getRequestId() + " ";
        }
        message += ")";

        return message;
    }
}
