package fi.ohtu.mobilityprofile;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class is used for converting GPS coordinates to an actual address.
 */
public class AddressConverter {

    private static String address;

    /**
     * Converts GPS coordinates to an address.
     *
     * @param location coordinates of the location
     * @param context for new request queue
     * @return an address
     */
    public static String convertToAddress(PointF location, Context context) {

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
                                String label = (properties.get("label").toString());
                                address = label;
                            }
                        } catch (Exception e) {
                            System.out.println("Exception in onResponse-method in convertToAddress-method of AddressConverter");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println("Exception in convertToAddress-method of AddressConverter");
                    }
                });
        queue.add(stringRequest);

        if (address == null) {
            address = "";
        }

        Log.e("ADDRESSCONVERTER", "converted address -> " + address );

        return address;
    }
}