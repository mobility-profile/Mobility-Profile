package fi.ohtu.mobilityprofile;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GoogleAPILocationService extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "GoogleAPILocationS";
    private static GoogleApiClient mApiClient;

    public GoogleAPILocationService() {
        super("GoogleAPILocationService");
    }

    @Override
    public void onCreate() {
        Log.i(TAG,"onCreate");
        super.onCreate();

        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG,"onConnected");

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000);

        Intent locationIntent = new Intent(getApplicationContext(), TestingService.class);
        PendingIntent locationPendingIntent = PendingIntent
                .getService(getApplicationContext(), 0, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("moiiiii");
            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, locationRequest, locationPendingIntent);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(new Activity(), 9000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy");
        super.onDestroy();

    }


    @Override
    protected void onHandleIntent(Intent intent) {


        System.out.println("hehehehehehahahahaah");

    }

}

