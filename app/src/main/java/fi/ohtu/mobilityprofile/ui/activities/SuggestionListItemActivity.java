package fi.ohtu.mobilityprofile.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.MainActivity;
import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.ui.MyWebViewClient;

public class SuggestionListItemActivity extends AppCompatActivity {

    private Activity activity;
    private Place place;
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

        place = PlaceDao.getPlaceById(Long.parseLong(getIntent().getStringExtra("placeId")));

        initializeViewElements();
    }

    private void initializeViewElements() {
        name = (TextView) findViewById(R.id.favourite_item_name);
        address = (TextView) findViewById(R.id.favourite_item_address);
        webView = (WebView) findViewById(R.id.webview);
        editButton = (Button) findViewById(R.id.favourite_edit);
        setFavouriteButton = (Button) findViewById(R.id.favourite_set_favourite);
        deleteButton = (Button) findViewById(R.id.favourite_delete);

        name.setText("NAME");
        name.setTextColor(ContextCompat.getColor(this, R.color.colorAccentGrey));
        address.setText(place.getAddress());
        editButton.setVisibility(View.GONE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("http://maps.google.com/?q=" +
                place.getCoordinate().getLatitude() + "," +
                place.getCoordinate().getLongitude() + "&z=15");

        setFavouriteButtonListener();
        deleteButtonListener();
    }

    private void setFavouriteButtonListener() {
        setFavouriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.significant_place_dialog_edit, null);

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

                                    Intent main = new Intent(activity, MainActivity.class);
                                    startActivity(main);
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setTitle(R.string.favourite_set_favourite_title);

                EditText editTextName = (EditText) dialogView.findViewById(R.id.editFavouriteName);
                EditText editTextAddress = (EditText) dialogView.findViewById(R.id.editFavouriteAddress);

                editTextName.setText("");
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
                        .setTitle(R.string.favourites_delete_title)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                PlaceDao.deletePlaceById(place.getId());
                                Intent main = new Intent(activity, MainActivity.class);
                                startActivity(main);
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