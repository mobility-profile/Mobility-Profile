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
import fi.ohtu.mobilityprofile.util.AddressConverter;


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
    private Button deleteButton;
    private MapFragment mapFragment;
    private GoogleMap googleMap;private AddressSuggestionAdapter addressSuggestionAdapter;
    private AutoCompleteTextView autoCompleteTextView;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        activity = this;
        place = PlaceDao.getPlaceById(Long.parseLong(getIntent().getStringExtra("placeId")));

        initializeViewElements();


        addressSuggestionAdapter = new AddressSuggestionAdapter(this, R.layout.list_addresses_item);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.edit_address);
        autoCompleteTextView.setAdapter(addressSuggestionAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int p, long id) {
                Address a = (Address) adapterView.getItemAtPosition(p);
                autoCompleteTextView.setText(a.getAddressLine(0));
                position = p;
            }
        });
    }

    private void initializeViewElements() {
        back = (ImageButton) findViewById(R.id.place_back_button);
        name = (TextView) findViewById(R.id.place_name);
        address = (TextView) findViewById(R.id.place_address);
        editButton = (Button) findViewById(R.id.place_edit_button);
        deleteButton = (Button) findViewById(R.id.place_delete_button);

        fancifyNameAndAddress();
        deleteButtonListener();
        backButtonListener();
        editButtonListener();
    }

    private void fancifyNameAndAddress() {
        if (place.getName().equals("")) {
            name.setText("NAME");
        } else {
            name.setText(place.getName().toUpperCase());
        }

        name.setTextColor(ContextCompat.getColor(this, R.color.color_grey_dark));
        address.setText(place.getAddress().getAddressLine(0));
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
                                AutoCompleteTextView editTextAddress = (AutoCompleteTextView) ((AlertDialog) dialog).findViewById(R.id.edit_address);

                                editPlace(editTextName.getText().toString(), addressSuggestionAdapter.getItem(position));

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
                AutoCompleteTextView editTextAddress = (AutoCompleteTextView) dialogView.findViewById(R.id.edit_address);

                editTextName.setText(place.getName());
                editTextAddress.setText(place.getAddress().getAddressLine(0));

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
    private void editPlace(String name, Address address){
        if (!name.equals("")) {
            place.setName(name);
        }

        if (!address.equals("")) {
            place.setAddress(address);
        } else {
            Toast.makeText(this, "Address not valid", Toast.LENGTH_LONG).show();
        }
        place.save();
    }

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