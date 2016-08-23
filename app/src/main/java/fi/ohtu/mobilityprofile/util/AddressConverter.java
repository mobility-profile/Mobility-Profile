package fi.ohtu.mobilityprofile.util;

import android.content.Context;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;

/**
 * This class is used for converting GPS coordinates to an actual address and save that address to the database.
 */
public class AddressConverter {
    /**
     * Converts coordinates to an address. Returns null if no address was found.
     *
     * @param context Context for the geocoder
     * @param coordinate Coordinates to be converted
     * @return Converted address
     */
    public static String convertToAddress(Context context, Coordinate coordinate) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocation(coordinate.getLatitude(), coordinate.getLongitude(), 1);
            if (addresses.size() >= 1) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Converts an address to coordinates. Returns null if no coordinates were found.
     *
     * @param context Context for the geocoder
     * @param address Address to be converted
     * @return Converted coordinates
     */
    public static Coordinate convertToCoordinates(Context context, String address) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() >= 1) {
                return new Coordinate((float) addresses.get(0).getLatitude(), (float) addresses.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCountryCode(Context context, String address) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() >= 1) {
                return addresses.get(0).getCountryCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}