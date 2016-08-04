package fi.ohtu.mobilityprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

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
}
