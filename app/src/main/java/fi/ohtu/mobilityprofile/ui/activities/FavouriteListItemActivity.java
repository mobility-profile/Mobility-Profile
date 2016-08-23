package fi.ohtu.mobilityprofile.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.ui.MyWebViewClient;
import fi.ohtu.mobilityprofile.util.AddressConverter;

/**
 * Class is used to create a list of favourites in the ui.
 */
public class FavouriteListItemActivity extends AppCompatActivity {

    private Activity activity;
    private Place favouritePlace;
    private TextView name;
    private TextView address;
    private WebView webView;
    private Button editButton;
    private Button setFavouriteButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_significant_place);
        activity = this;

        favouritePlace = PlaceDao.getPlaceById(Long.parseLong(getIntent().getStringExtra("favouriteId")));

        initializeViewElements();
    }

    private void initializeViewElements() {
        name = (TextView) findViewById(R.id.favourite_item_name);
        address = (TextView) findViewById(R.id.favourite_item_address);
        webView = (WebView) findViewById(R.id.webview);
        editButton = (Button) findViewById(R.id.favourite_edit);
        setFavouriteButton = (Button) findViewById(R.id.favourite_set_favourite);
        deleteButton = (Button) findViewById(R.id.favourite_delete);

        name.setText(favouritePlace.getName().toUpperCase());
        address.setText(favouritePlace.getAddress());
        setFavouriteButton.setVisibility(View.GONE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("http://maps.google.com/?q=" + favouritePlace.getAddress() + "&z=15");

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

                                Coordinate coordinate = AddressConverter.convertToCoordinates(getApplicationContext(), favouritePlace.getAddress());
                                if (coordinate != null) {
                                    coordinate.save();

                                    favouritePlace.setCoordinate(coordinate);
                                    favouritePlace.save();
                                }

                                activity.recreate();
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
                                finish();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}