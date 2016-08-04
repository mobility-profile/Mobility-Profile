package fi.ohtu.mobilityprofile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.domain.TransportMode;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox walking;
    private CheckBox bike;
    private CheckBox car;
    private CheckBox bus;
    private CheckBox metro;
    private CheckBox tram;
    private CheckBox train;
    private CheckBox boat;
    private CheckBox plane;

    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        walking = (CheckBox) findViewById(R.id.checkbox_walking);
        bike = (CheckBox) findViewById(R.id.checkbox_bike);
        car = (CheckBox) findViewById(R.id.checkbox_car);
        bus = (CheckBox) findViewById(R.id.checkbox_bus);
        metro = (CheckBox) findViewById(R.id.checkbox_metro);
        tram = (CheckBox) findViewById(R.id.checkbox_tram);
        train = (CheckBox) findViewById(R.id.checkbox_train);
        boat = (CheckBox) findViewById(R.id.checkbox_boat);
        plane = (CheckBox) findViewById(R.id.checkbox_plane);
        resetButton = (Button) findViewById(R.id.resetbutton);

        setChecked();
        setListeners();
    }

    private void setChecked() {
        walking.setChecked(TransportMode.getByName("walking").isFavourite());
        bike.setChecked(TransportMode.getByName("bike").isFavourite());
        car.setChecked(TransportMode.getByName("car").isFavourite());
        bus.setChecked(TransportMode.getByName("bus").isFavourite());
        metro.setChecked(TransportMode.getByName("metro").isFavourite());
        tram.setChecked(TransportMode.getByName("tram").isFavourite());
        train.setChecked(TransportMode.getByName("train").isFavourite());
        boat.setChecked(TransportMode.getByName("boat").isFavourite());
        plane.setChecked(TransportMode.getByName("plane").isFavourite());
    }

    private void setListeners() {
        setListenerForCheckBox(walking);
        setListenerForCheckBox(bike);
        setListenerForCheckBox(car);
        setListenerForCheckBox(bus);
        setListenerForCheckBox(metro);
        setListenerForCheckBox(tram);
        setListenerForCheckBox(train);
        setListenerForCheckBox(boat);
        setListenerForCheckBox(plane);
        setListenerForResetButton(this);
    }


    private void setListenerForCheckBox(final CheckBox box) {
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    savePreference(box.getText().toString().toLowerCase(), true);
                } else {
                    savePreference(box.getText().toString().toLowerCase(), false);
                }
            }
        });

    }

    private void savePreference(String name, boolean preference) {
        TransportMode mode = TransportMode.getByName(name);
        mode.setFavourite(preference);
        mode.save();
    }

    /**
     * Creates alert dialog to confirm resetting of the app when reset button is clicked.
     */
    private void setListenerForResetButton(final Context context) {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.reset_title).setMessage(R.string.reset_message);

                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAllDataFromDatabase();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

    /**
     * Deletes all data from the database.
     */
    private void deleteAllDataFromDatabase() {
        PlaceDao.deleteAllData();
        SignificantPlaceDao.deleteAllData();
        CalendarTagDao.deleteAllData();
        RouteSearchDao.deleteAllData();
        FavouritePlaceDao.deleteAllData();
    }
}
