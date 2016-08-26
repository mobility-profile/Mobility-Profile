package fi.ohtu.mobilityprofile.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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


/**
 * Class is used to create a list of suggested favourite places in the ui.
 */
public class SuggestionListItemActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Activity activity;
    private Place place;
    private ImageButton back;
    private TextView name;
    private TextView address;
    private Button editButton;
    private Button setFavouriteButton;
    private Button deleteButton;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        activity = this;
        place = PlaceDao.getPlaceById(Long.parseLong(getIntent().getStringExtra("placeId")));

        initializeViewElements();
    }

    private void initializeViewElements() {
        back = (ImageButton) findViewById(R.id.favourites_back_button);
        name = (TextView) findViewById(R.id.favourite_item_name);
        address = (TextView) findViewById(R.id.favourite_item_address);

        editButton = (Button) findViewById(R.id.favourite_edit);
        editButton.setVisibility(View.GONE);

        setFavouriteButton = (Button) findViewById(R.id.favourite_set_favourite);
        deleteButton = (Button) findViewById(R.id.favourite_delete);


        fancifyNameAndAddress();
        setFavouriteButtonListener();
        deleteButtonListener();
        backButtonListener();
    }

    private void fancifyNameAndAddress() {
        if (place.getName().equals("")) {
            name.setText("NAME");
        } else {
            name.setText(place.getName().toUpperCase());
        }

        name.setTextColor(ContextCompat.getColor(this, R.color.color_grey_dark));
        address.setText(place.getAddress());
    }

    private void setFavouriteButtonListener() {
        setFavouriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_place_edit, null);

                builder
                        .setView(dialogView)
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                EditText editTextName = (EditText) ((AlertDialog) dialog).findViewById(R.id.editFavouriteName);
                                EditText editTextAddress = (EditText) ((AlertDialog) dialog).findViewById(R.id.editFavouriteAddress);

                                if (!editTextName.equals("") && !editTextAddress.equals("")) {

                                    place.setName(editTextName.getText().toString());
                                    place.setAddress(editTextAddress.getText().toString());
                                    place.setFavourite(true);
                                    place.save();

                                    backToFragment();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setTitle(R.string.dialog_set_favourite_title);

                EditText editTextName = (EditText) dialogView.findViewById(R.id.editFavouriteName);
                EditText editTextAddress = (EditText) dialogView.findViewById(R.id.editFavouriteAddress);

                if (place.getName().equals("name")) {
                    editTextName.setText("");
                } else {
                    editTextName.setText(place.getName());
                }
                editTextAddress.setText(place.getAddress());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void deleteButtonListener() {
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
                builder
                        .setTitle(R.string.dialog_delete_title)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                PlaceDao.deletePlaceById(place.getId());
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

    private void backButtonListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToFragment();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(true);

        try {
            LatLng point = new LatLng(place.getCoordinate().getLatitude(), place.getCoordinate().getLongitude());

            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));

            map.addMarker(new MarkerOptions()
                    .title(place.getName())
                    .position(point));
        } catch (Exception e) {
            Toast.makeText(this, "Coordinates for the address were not found", Toast.LENGTH_LONG).show();
        }

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