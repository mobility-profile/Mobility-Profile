package fi.ohtu.mobilityprofile.domain;

import android.location.Location;

import com.orm.SugarRecord;

/**
 * Class is used to save raw gps data.
 */
public class TestLocation extends SugarRecord {
    double latitude;
    double longitude;
    boolean hasAltitude;
    double altitude;
    boolean hasBearing;
    float bearing;
    boolean hasAccuracy;
    float accuracy;
    String provider;
    boolean hasSpeed;
    float speed;
    long time;
    long realTimeNanos;



    Location location;

    public TestLocation() {
        this.location = null;
    }

    public TestLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.hasAccuracy = location.hasAccuracy();
        this.accuracy = location.getAccuracy();
        this.provider = location.getProvider();
        this.hasSpeed = location.hasSpeed();
        this.speed = location.getSpeed();
        this.time = location.getTime();
        this.hasAltitude = location.hasAltitude();
        this.altitude = location.getAltitude();
        this.hasBearing = location.hasBearing();
        this.bearing = location.getBearing();
        this.realTimeNanos = location.getElapsedRealtimeNanos();
    }

}
