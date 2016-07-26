package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import fi.ohtu.mobilityprofile.PermissionManager;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;

/**
 * PlaceRecorder listens to location changes.
 */
public class PlaceRecorder extends Service {
    private static final String TAG = "PlaceRecorder";
    private android.location.LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000; //300000 = 5 minutes
    private static final float LOCATION_DISTANCE = 0f;
    LocationListener[] mLocationListeners = null;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
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