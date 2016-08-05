package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import android.content.Context;
import android.graphics.PointF;
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
import java.util.concurrent.Executors;

import fi.ohtu.mobilityprofile.domain.HasAddress;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.Place;

/**
 * This class is used for converting GPS coordinates to an actual address and save that address to the database.
 */
public class AddressConverter {

    //use this RequestQueue instead of Vollew.newRequestQueue(context) in unit tests
    public static RequestQueue newVolleyRequestQueueForTest(final Context context) {
        File cacheDir = new File(context.getCacheDir(), "cache/volley");
        Network network = new BasicNetwork(new HurlStack());
        ResponseDelivery responseDelivery = new ExecutorDelivery(Executors.newSingleThreadExecutor());
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network, 4, responseDelivery);
        queue.start();
        return queue;
    }

    public static void getAddressAndSave(final HasAddress object, Context context) {
        String url = "https://search.mapzen.com/v1/reverse?api_key=search-xPjnrpR&point.lat="
                + object.getCoordinate().getLatitude() + "&point.lon="
                + object.getCoordinate().getLongitude() + "&layers=address&size=1&sources=osm";

        RequestQueue queue = Volley.newRequestQueue(context);
        //RequestQueue queue = newVolleyRequestQueueForTest(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray features = new JSONArray(json.get("features").toString());
                            if (features.length() > 0) {
                                JSONObject zero = new JSONObject(features.get(0).toString());
                                JSONObject properties = new JSONObject(zero.get("properties").toString());
                                String address = (properties.get("label").toString());

                                if (address == null) address = "";

                                Log.i("AddressConverter", "Converted address is: " + address);
                                object.updateAddress(address);
                            }
                        } catch (Exception e) {
                            Log.e("AddressConverter", "Exception in onResponse-method in convertToAddress-method of AddressConverter");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AddressConverter", "Exception in convertToAddress-method of AddressConverter");
                        error.printStackTrace();

                    }
                });
        queue.add(stringRequest);
    }

    /**
     * Converts GPS coordinates to an address and saves it.
     *
     * @param location coordinates of the location
     * @param context for new request queue
     */
    public static void convertToAddressAndSave(final PointF location, Context context) {

        String url = "https://search.mapzen.com/v1/reverse?api_key=search-xPjnrpR&point.lat="
                + location.x + "&point.lon="
                + location.y + "&layers=address&size=1&sources=osm";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray features = new JSONArray(json.get("features").toString());
                            if (features.length() > 0) {
                                JSONObject zero = new JSONObject(features.get(0).toString());
                                JSONObject properties = new JSONObject(zero.get("properties").toString());
                                String address = (properties.get("label").toString());

                                if (address == null) address = "";

                                Log.i("AddressConverter", "Converted address is: " + address);

                                Place lastLocation = new Place(System.currentTimeMillis(), location.x, location.y);
                                lastLocation.save();
                            }
                        } catch (Exception e) {
                            Log.e("AddressConverter", "Exception in onResponse-method in convertToAddress-method of AddressConverter");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("AddressConverter", "Exception in convertToAddress-method of AddressConverter");
                            error.printStackTrace();

                        }
                });
        queue.add(stringRequest);
    }

    /**
     * Converts an address to coordinates and saves it.
     *
     * @param destination the address
     * @param latest latest visit
     * @param context for new request queue
     */
    public static void convertToCoordinatesAndSave(final String destination, final Place latest, Context context) {

        String url = "https://search.mapzen.com/v1/search?api_key=search-xPjnrpR&text="
                + destination + "&layers=address&size=1&sources=osm&boundary.country=FIN";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray features = new JSONArray(json.get("features").toString());
                            if (features.length() > 0) {
                                JSONObject zero = new JSONObject(features.get(0).toString());
                                JSONObject geometry = new JSONObject(zero.get("geometry").toString());
                                JSONArray coordinates = new JSONArray(geometry.get("coordinates").toString());

                                Float lat = Float.parseFloat(coordinates.get(1).toString());
                                Float lon = Float.parseFloat((coordinates.get(0).toString()));

                                RouteSearch route;

                                if (latest != null) {
                                    route = new RouteSearch(System.currentTimeMillis(),
                                            "Kumpula", destination, latest.getLatitude(),
                                            latest.getLongitude(), lat, lon);
                                } else {
                                    route = new RouteSearch(System.currentTimeMillis(),
                                            "None", destination, null, null, lat, lon);

                                }
                                route.save();

                                Log.i("AddressConverter", "Converted coordinates are: " + lat + "," + lon);
                            }
                        } catch (Exception e) {
                            Log.e("AddressConverter", "Exception in onResponse-method in convertToAddress-method of AddressConverter");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AddressConverter", "Exception in convertToCoordinates-method of AddressConverter");
                        error.printStackTrace();

                    }
                });
        queue.add(stringRequest);
    }
}