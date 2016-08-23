package fi.ohtu.mobilityprofile.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.util.geocoding.AddressConverter;

public class FavouriteListItemActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Activity activity;
    private Place favouritePlace;
    private TextView name;
    private TextView address;
    private Button editButton;
    private Button setFavouriteButton;
    private Button deleteButton;
    private MapFragment mapFragment;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_significant_place);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        activity = this;
        favouritePlace = PlaceDao.getPlaceById(Long.parseLong(getIntent().getStringExtra("favouriteId")));

        initializeViewElements();

    }

    private void initializeViewElements() {
        name = (TextView) findViewById(R.id.favourite_item_name);
        address = (TextView) findViewById(R.id.favourite_item_address);
        editButton = (Button) findViewById(R.id.favourite_edit);
        setFavouriteButton = (Button) findViewById(R.id.favourite_set_favourite);
        deleteButton = (Button) findViewById(R.id.favourite_delete);

        name.setText(favouritePlace.getName().toUpperCase());
        address.setText(favouritePlace.getAddress());
        setFavouriteButton.setVisibility(View.GONE);

        editButtonListener();
        deleteButtonListener();
    }

    private void editButtonListener() {
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.significant_place_dialog_edit, null);

                builder
                        .setView(dialogView)
                        .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                EditText editTextName = (EditText) ((AlertDialog) dialog).findViewById(R.id.editFavouriteName);
                                EditText editTextAddress = (EditText) ((AlertDialog) dialog).findViewById(R.id.editFavouriteAddress);

                                editFavoritePlace(editTextName.getText().toString(), editTextAddress.getText().toString());

                                AddressConverter.convertFavouriteAddressToCoordinatesAndSave(favouritePlace, getApplicationContext(), googleMap, activity);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setTitle(R.string.favourites_edit_title);

                EditText editTextName = (EditText) dialogView.findViewById(R.id.editFavouriteName);
                EditText editTextAddress = (EditText) dialogView.findViewById(R.id.editFavouriteAddress);

                editTextName.setText(favouritePlace.getName());
                editTextAddress.setText(favouritePlace.getAddress());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    /**
     * Edits the given favourite place.
     * @param name the new name
     * @param address the new address
     */
    private void editFavoritePlace(String name, String address){
        if (!name.equals("")) {
            favouritePlace.setName(name);
        }

        if (!address.equals("")) {
            favouritePlace.setAddress(address);
        }
        favouritePlace.save();
    }

    private void deleteButtonListener() {
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
                builder
                        .setTitle(R.string.favourites_delete_title)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                PlaceDao.deletePlaceById(favouritePlace.getId());
                                backToFragment();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private void setMarker() {
        try {
            LatLng point = new LatLng(favouritePlace.getCoordinate().getLatitude(), favouritePlace.getCoordinate().getLongitude());

            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));

            googleMap.addMarker(new MarkerOptions()
                    .title(favouritePlace.getName())
                    .position(point));
        } catch (Exception e) {
            Toast.makeText(this, "Coordinates for the address were not found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(true);

        setMarker();
    }

    @Override
    public void onBackPressed() {
        backToFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                backToFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void backToFragment() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dataChanged", "true");
        editor.commit();
        finish();
    }
}