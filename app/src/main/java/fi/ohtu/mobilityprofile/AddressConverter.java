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

public class AddressConverter {

    private static String address;

    /**
     * Converts GPS coordinates to an address.
     *
     * @param location coordinates of the location
     * @param context for new request queue
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

                            //different options:
                            //  "name":"Karamzins strand 4"
                            //  "street":"Karamzins strand"
                            //  "label":"Karamzins strand 4, Helsinki, Finland"

                            JSONObject json = new JSONObject(response);
                            JSONArray features = new JSONArray(json.get("features").toString());
                            JSONObject zero = new JSONObject(features.get(0).toString());
                            JSONObject properties = new JSONObject(zero.get("properties").toString());
                            String name = (properties.get("name").toString());
                            address = name;
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

        System.out.println("address is ->>> " + address);

        return address;
    }
}


/* If you'd like to test this class, here is an example location:
   AddressConverter.convertToAddress(new PointF(new Float(60.1756),new Float(24.9342)), context);

   And the address is Karamzins strand 4
 */