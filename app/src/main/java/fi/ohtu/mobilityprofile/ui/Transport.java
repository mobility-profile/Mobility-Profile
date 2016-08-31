package fi.ohtu.mobilityprofile.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.TransportModeDao;
import fi.ohtu.mobilityprofile.domain.TransportMode;

public class Transport {

    private Context context;

    private ImageButton walking;
    private ImageButton bike;
    private ImageButton car;
    private ImageButton bus;
    private ImageButton metro;
    private ImageButton tram;
    private ImageButton train;
    private ImageButton boat;
    private ImageButton plane;

    public Transport(Context c) {
        context = c;
    }

    /**
     * Initializes transportmode buttons.
     * @param view View
     */
    public void initializeTransportModes(View view) {
        walking = (ImageButton) view.findViewById(R.id.settings_transport_imagebutton_walking);
        bike = (ImageButton) view.findViewById(R.id.settings_transport_imagebutton_bike);
        car = (ImageButton) view.findViewById(R.id.settings_transport_imagebutton_car);
        bus = (ImageButton) view.findViewById(R.id.settings_transport_imagebutton_bus);
        metro = (ImageButton) view.findViewById(R.id.settings_transport_imagebutton_metro);
        tram = (ImageButton) view.findViewById(R.id.settings_transport_imagebutton_tram);
        train = (ImageButton) view.findViewById(R.id.settings_transport_imagebutton_train);
        boat = (ImageButton) view.findViewById(R.id.settings_transport_imagebutton_boat);
        plane = (ImageButton) view.findViewById(R.id.settings_transport_imagebutton_plane);

        setCheckedTransportModes();
        setListenersForTransportModes();
    }

    private void setCheckedTransportModes() {

        if (TransportModeDao.getByName("walking").isFavourite()) {
            setColorForTransport(walking, R.color.color_orange);
        } else {
            setColorForTransport(walking, R.color.color_grey);
        }

        if (TransportModeDao.getByName("bike").isFavourite()) {
            setColorForTransport(bike, R.color.color_orange);
        } else {
            setColorForTransport(bike, R.color.color_grey);
        }

        if (TransportModeDao.getByName("car").isFavourite()) {
            setColorForTransport(car, R.color.color_orange);
        } else {
            setColorForTransport(car, R.color.color_grey);
        }

        if (TransportModeDao.getByName("bus").isFavourite()) {
            setColorForTransport(bus, R.color.color_orange);
        } else {
            setColorForTransport(bus, R.color.color_grey);
        }

        if (TransportModeDao.getByName("metro").isFavourite()) {
            setColorForTransport(metro, R.color.color_orange);
        } else {
            setColorForTransport(metro, R.color.color_grey);
        }

        if (TransportModeDao.getByName("tram").isFavourite()) {
            setColorForTransport(tram, R.color.color_orange);
        } else {
            setColorForTransport(tram, R.color.color_grey);
        }

        if (TransportModeDao.getByName("train").isFavourite()) {
            setColorForTransport(train, R.color.color_orange);
        } else {
            setColorForTransport(train, R.color.color_grey);
        }

        if (TransportModeDao.getByName("boat").isFavourite()) {
            setColorForTransport(boat, R.color.color_orange);
        } else {
            setColorForTransport(boat, R.color.color_grey);
        }

        if (TransportModeDao.getByName("plane").isFavourite()) {
            setColorForTransport(plane, R.color.color_orange);
        } else {
            setColorForTransport(plane, R.color.color_grey);
        }
    }

    private void setListenersForTransportModes() {
        setListenerForImageButton(walking);
        setListenerForImageButton(bike);
        setListenerForImageButton(car);
        setListenerForImageButton(bus);
        setListenerForImageButton(metro);
        setListenerForImageButton(tram);
        setListenerForImageButton(train);
        setListenerForImageButton(boat);
        setListenerForImageButton(plane);
    }

    private void setListenerForImageButton(final ImageButton button) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TransportModeDao.getByName(button.getContentDescription().toString().toLowerCase()).isFavourite()) {
                    saveTransportPreference(button.getContentDescription().toString().toLowerCase(), false);
                    setColorForTransport(button, R.color.color_grey);
                } else {
                    saveTransportPreference(button.getContentDescription().toString().toLowerCase(), true);
                    setColorForTransport(button, R.color.color_orange);
                }
            }
        });
    }

    private void saveTransportPreference(String name, boolean preference) {
        TransportMode mode = TransportModeDao.getByName(name);
        mode.setFavourite(preference);
        mode.save();
    }

    private void setColorForTransport(ImageButton button, int color) {
        button.setBackgroundColor(ContextCompat.getColor(context, color));
    }
}
