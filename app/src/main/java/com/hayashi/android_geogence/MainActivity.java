package com.hayashi.android_geogence;

// 参考
// https://developer.android.com/training/location/geofencing

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import static com.hayashi.android_geogence.GeofenceTransitionsIntentService.sendNotification;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "----";
    private final int GEOFENCE_EXPIRATION_IN_MILLISECONDS = 1000 * 60 * 60 * 60 * 24;
    private final int GEOFENCE_RADIUS_IN_METERS = 100;

    private GeofencingClient geofencingClient = null;
    private List<Geofence> geofenceList = new ArrayList<>();
    private PendingIntent geofencePendingIntent = null;

    private TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        this.textView = findViewById(R.id.text_view);
        MyLog.init(this);
        initGeoFence();
    }

    private void initGeoFence() {
        GeofenceTransitionsIntentService.activity = this;

        geofencingClient = LocationServices.getGeofencingClient(this);

        LatLng latLngTokyoStation
                = new LatLng(35.681167, 139.767052);
        LatLng latLngOsakaStation
                = new LatLng(34.702485, 135.495951);
        LatLng latLngGfoOsaka
                = new LatLng(34.704113, 135.494831);
        LatLng latLngSannomiyaStation
                = new LatLng(34.694139, 135.194221);
        LatLng latLngCityTower
                = new LatLng(34.696748, 135.198056);
        LatLng latLngKosiengutiStation
                = new LatLng(34.7390762,135.3726983);

        addGeofence(
                "TokyoStation",
                latLngTokyoStation);
        addGeofence(
                "OsakaStation",
                latLngOsakaStation);
        addGeofence(
                "GfoOsaka",
                latLngGfoOsaka);
        addGeofence(
                "SannomiyaStation",
                latLngSannomiyaStation);
        addGeofence(
                "CityTower",
                latLngCityTower);
        addGeofence(
                "KosiengutiStation",
                latLngKosiengutiStation);

        Boolean isFineGranted =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!isFineGranted) {
            // fine location のリクエストコード（値は他のパーミッションと被らなければ、なんでも良い）
            final int requestCode = 1;
            // いずれも得られていない場合はパーミッションのリクエストを要求する
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    requestCode );
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sendNotification("ジオフェンス追加成功");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        sendNotification("ジオフェンス追加失敗");
                    }
                });
    }

    private void addGeofence(String id, double lat, double lng) {
        Geofence geofence = new Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(
                        lat,
                        lng,
                        GEOFENCE_RADIUS_IN_METERS)
                .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        geofenceList.add(geofence);
    }

    private GeofencingRequest getGeofencingRequest() {
        MyLog.getInstance().debug("ジオフェンシングリクエスト");
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        geofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        MyLog.getInstance().debug("ペンディングインテント作成");
        return geofencePendingIntent;
    }

    private void addGeofence(String id, LatLng latLng) {
        addGeofence(id,
                latLng.latitude,
                latLng.longitude);
    }
}
