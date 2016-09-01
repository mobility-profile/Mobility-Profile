package fi.ohtu.mobilityprofile.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.ui.list_adapters.AddressSuggestionAdapter;

/**
 * Class is used to create a page for one favourite place.
 */
public class FavouriteListItemActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Activity activity;
    private Place place;
    private ImageButton back;
    private TextView name;
    private TextView address;
    private Button editButton;
    private Button deleteButton;
    private GoogleMap googleMap;
    private Address tempAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        place = PlaceDao.getPlaceById(Long.parseLong(getIntent().getStringExtra("favouriteId")));
        activity = this;

        initializeViewElements();
    }

    private void initializeViewElements() {
        name = (TextView) findViewById(R.id.place_name);
        address = (TextView) findViewById(R.id.place_address);

        editButton = (Button) findViewById(R.id.place_edit_button);
        deleteButton = (Button) findViewById(R.id.place_delete_button);

        back = (ImageButton) findViewById(R.id.place_back_button);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        fancifyNameAndAddress();
        editButtonListener();
        deleteButtonListener();
        backButtonListener();
        mapFragment.getMapAsync(this);
    }

    private void fancifyNameAndAddress() {
        if (place.getName().equals("")) {
            name.setText(getResources().getString(R.string.name));
        } else {
            name.setText(place.getName().toUpperCase());
        }

        name.setTextColor(ContextCompat.getColor(this, R.color.color_grey_dark));
        address.setText(place.getAddress().getAddressLine(0));
    }

    private void editButtonListener() {
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_place_edit, null);

                builder
                        .setView(dialogView)
                        .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                EditText editTextName = (EditText) ((AlertDialog) dialog).findViewById(R.id.edit_name);
                                editFavoritePlace(editTextName.getText().toString(), tempAddress);
                                setMarker();
                                activity.recreate();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setTitle(R.string.dialog_edit_title);

                EditText editTextName = (EditText) dialogView.findViewById(R.id.edit_name);
                final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialogView.findViewById(R.id.edit_address);

                editTextName.setText(place.getName());
                autoCompleteTextView.setText(place.getAddressLine(0));

                AddressSuggestionAdapter addressSuggestionAdapter = new AddressSuggestionAdapter(activity, R.layout.list_addresses_item);
                autoCompleteTextView.setAdapter(addressSuggestionAdapter);

                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int p, long id) {
                        Address a = (Address) adapterView.getItemAtPosition(p);
                        System.out.println(a);
                        autoCompleteTextView.setText(a.getAddressLine(0));
                        tempAddress = a;
                    }
                });



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
    private void editFavoritePlace(String name, Address address){
        System.out.println(place.getCoordinate());
        if (!name.equals("")) {
            place.setName(name);
        }

        if (address != null) {
            place.setAddress(address);
            place.setCoordinate(new Coordinate((float) address.getLatitude(), (float) address.getLongitude()));
        } else {
            Toast.makeText(this, "Address not valid, choose one from the list", Toast.LENGTH_LONG).show();
        }
        place.save();
        System.out.println(place.getCoordinate());
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

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(true);

        setMarker();
    }

    /**
     * Sets a marker on map.
     */
    private void setMarker() {
        try {
            LatLng point = new LatLng(place.getCoordinate().getLatitude(), place.getCoordinate().getLongitude());

            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));

            googleMap.addMarker(new MarkerOptions()
                    .title(place.getName())
                    .position(point));
        } catch (Exception e) {
            Toast.makeText(this, "Coordinates for the address were not found", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Returns back to the fragment where user was before.
     */
    private void backToFragment() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dataChanged", "true");
        editor.commit();
        finish();
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
}