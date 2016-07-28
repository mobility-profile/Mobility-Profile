package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import fi.ohtu.mobilityprofile.MainActivity;
import fi.ohtu.mobilityprofile.PermissionManager;
import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;

/**
 * PlaceRecorder listens to location changes.
 */
public class PlaceRecorder extends Service {
    private static final int NOTIFICATION_ID = 120;
    private static final String ACTION_STOP_SERVICE = "StopIt!";
    private static final String TAG = "PlaceRecorder";

    private static final int LOCATION_INTERVAL = 2 * 60 * 1000;
    private static final float LOCATION_DISTANCE = 0f;

    private android.location.LocationManager mLocationManager;
    private LocationListener[] mLocationListeners = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            stopSelf();
            return START_STICKY;
        }

        // Create intent for the service
        Intent notificationIntent = new Intent(this, PlaceRecorder.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // Create the notification
        Notification.Builder notification = new Notification.Builder(this)
                .setContentTitle("Location tracking")
                .setContentText("Mobility Profile is tracking your location")
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        // Create intent for resuming to app when user clicks on the notification
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(resultPendingIntent);

        // Create intent for stopping the service
        Intent actionIntent = new Intent(this, PlaceRecorder.class);
        actionIntent.setAction(ACTION_STOP_SERVICE);
        PendingIntent actionPendingIntent = PendingIntent.getService(this, 0, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.addAction(R.mipmap.logo, "Stop tracking", actionPendingIntent);

        // Start the service
        startForeground(NOTIFICATION_ID, notification.build());

        return START_STICKY;
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        /**
         * Creates PlaceRecorder
         *
         * @param provider GPS or Network
         */
        public LocationListener(String provider, Context context, LocationManager locationManager) {
            Log.i(TAG, "LocationListener " + provider);

            try {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    mLastLocation = location;
                    Log.i(TAG, "In constructor of LocationListener " + provider + ", we found location: " + mLastLocation);
                    savePlace(location);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location);
            mLastLocation = location;
            savePlace(location);
            if (PlaceDao.getAll().size() == (10000 / LOCATION_INTERVAL)) { //24 hour interval
                PlaceClusterizer.clusterize(PlaceDao.getAll());
            }
        }

        private void savePlace(Location location) {
            System.out.println(System.currentTimeMillis());
            Place place = new Place(System.currentTimeMillis(), new Float(location.getLatitude()), new Float(location.getLongitude()));
            place.save();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);
        }
    }

    /**
     * Initializes LocationManager.
     */
    private void initializeLocationManager() {
        Log.i(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (android.location.LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    /**
     * Initializes LocationListeners.
     */
    private void initializeLocationListeners() {
        mLocationListeners = new LocationListener[]{
                new LocationListener(android.location.LocationManager.GPS_PROVIDER, this, mLocationManager),
                new LocationListener(android.location.LocationManager.NETWORK_PROVIDER, this, mLocationManager)
        };
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        initializeLocationManager();
        initializeLocationListeners();

        try {
            mLocationManager.requestLocationUpdates(
                    android.location.LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "Fail to request location update", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "Network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    android.location.LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "Fail to request location update", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "GPS provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    if (!PermissionManager.permissionToFineLocation(this) && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                    Log.e(TAG, "Fail to remove location listeners", ex);
                }
            }
        }
    }

}