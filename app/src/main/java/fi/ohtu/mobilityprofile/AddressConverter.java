package fi.ohtu.mobilityprofile;

import android.content.Context;
import android.graphics.PointF;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddressConverter {

    /**
     * Converts GPS coordinates to an address.
     *
     * @param location coordinates of the location
     * @return
     */
    public static String convertToAddress(PointF location, Context context) {

        String url = "https://search.mapzen.com/v1/reverse?api_key=search-xPjnrpR&point.lat=" + location.x +
                "&point.lon=" + location.y + "&layers=address&size=1";

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //"name":"Karamzins strand 4"
                            //"street":"Karamzins strand"
                            //"label":"Karamzins strand 4, Helsinki, Finland"

                            JSONObject json = new JSONObject(response);
                            System.out.println(json.get("name").toString());
                        } catch (Exception e) {
                            System.out.println("error in addressconverter");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error in addressconverter");
            }
        });
        queue.add(stringRequest);

        return location.x + "" + location.y;
    }

}