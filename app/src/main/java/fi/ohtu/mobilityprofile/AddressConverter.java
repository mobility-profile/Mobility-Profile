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

import fi.ohtu.mobilityprofile.data.Visit;

/**
 * This class is used for converting GPS coordinates to an actual address and save that address.
 */
public class AddressConverter {
    /**
     * Converts GPS coordinates to an address and saves it.
     *
     * @param location coordinates of the location
     * @param context for new request queue
     * @return an address
     */
    public static void convertToAddressSave(PointF location, Context context) {

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

                                if (label == null) label = "";

                                Log.i("AddressConverter", "converted address -> " + label );

                                Visit lastLocation = new Visit(System.currentTimeMillis(), label);
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
}