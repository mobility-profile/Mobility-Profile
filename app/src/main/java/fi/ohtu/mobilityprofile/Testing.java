package fi.ohtu.mobilityprofile;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class Testing extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static GoogleApiClient mApiClient;

    public Testing() {
        super("Testing");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mApiClient.connect();
        }
        System.out.println("KISUFISU");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("KISSAKALA");
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000);

        Intent locationIntent = new Intent(getApplicationContext(), TestingService.class);
        PendingIntent locationPendingIntent = PendingIntent
                .getService(getApplicationContext(), 0, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, locationRequest, locationPendingIntent);;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    protected void onHandleIntent(Intent intent) {}
}

