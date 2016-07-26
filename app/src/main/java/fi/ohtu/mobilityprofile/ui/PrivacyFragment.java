package fi.ohtu.mobilityprofile.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.PlaceRecorder;
import fi.ohtu.mobilityprofile.PermissionManager;
import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;

/**
 * The class creates a component called PrivacyFragment.
 *
 * PrivacyFragment handles everything concerning the PRIVACY tab in the UI.
 */
public class PrivacyFragment extends Fragment {
    /**
     * The title of the fragment.
     */
    private static final String title = "PRIVACY";

    /**
     * The position of the fragment in the "queue" of all fragments.
     */
    private static final int page = 0;

    private static final int ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST = 1;
    private static final int READ_CALENDAR_PERMISSIONS_REQUEST = 2;

    private CheckBox gpsCheckBox;
    private CheckBox calendarCheckBox;
    private CheckBox trackingCheckBox;
    private Button resetbutton;

    private Context context;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Creates a new instance of PrivacyFragment.
     * @return a Privacy Fragment
     */
    public static PrivacyFragment newInstance() {
        PrivacyFragment privacyFragment = new PrivacyFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("title", title);
        privacyFragment.setArguments(args);
        return privacyFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.privacy_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        gpsCheckBox = (CheckBox) view.findViewById(R.id.checkbox_GPS);
        calendarCheckBox = (CheckBox) view.findViewById(R.id.checkbox_calendar);
        trackingCheckBox = (CheckBox) view.findViewById(R.id.checkbox_tracking);
        resetbutton = (Button) view.findViewById(R.id.resetbutton);

        setChecked();
        setListenerForGPSCheckBox();
        setListenerForCheckBoxCalendar();
        setListenerForCheckBoxTracking();
        resetbutton.setOnClickListener(onClickResetButton);
    }

    /**
     * Sets a listener for gpsCheckbox.
     */
    private void setListenerForGPSCheckBox() {
        gpsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !PermissionManager.permissionToFineLocation(context)) {
                    getPermissionToAccessFineLocation();
                    if (PermissionManager.permissionToFineLocation(context)) {
                        trackingCheckBox.setEnabled(true);
                    }
                } else if (isChecked) {
                    Toast.makeText(context, "SignificantPlace tracking is used again", Toast.LENGTH_SHORT).show();
                    trackingCheckBox.setEnabled(true);
                } else {
                    Toast.makeText(context, "SignificantPlace tracking will not be used", Toast.LENGTH_SHORT).show();
                    trackingCheckBox.setEnabled(false);
                }
            }
        });
    }

    /**
     * Sets a listener for calendarCheckBox.
     */
    private void setListenerForCheckBoxCalendar() {
        calendarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !PermissionManager.permissionToReadCalendar(context)) {
                    getPermissionToReadCalendar();
                } else if (isChecked) {
                    Toast.makeText(context, "Calendar is used again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Calendar will not be used", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Sets a listener for trackingCheckBox.
     */
    private void setListenerForCheckBoxTracking() {
        trackingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    context.startService(new Intent(context, PlaceRecorder.class));
                } else {
                    context.stopService(new Intent(context, PlaceRecorder.class));
                }
            }
        });
    }

    /**
     * Checks if we have permission to access location, and then if not, requests it.
     */
    private void getPermissionToAccessFineLocation() {
        if (!PermissionManager.permissionToFineLocation(context)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST);
        }
    }

    /**
     * Checks if we have permission to read calendar, and then if not, requests it.
     */
    private void getPermissionToReadCalendar() {
        if (!PermissionManager.permissionToReadCalendar(context)) {
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR},
                    READ_CALENDAR_PERMISSIONS_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "GPS permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    gpsCheckBox.setChecked(false);
                }
                break;
            case READ_CALENDAR_PERMISSIONS_REQUEST:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Read Calendar permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    calendarCheckBox.setChecked(false);
                }
                break;
        }
    }

    /**
     * Sets the checkboxes checked or unchecked based on the states of the permissions.
     */
    private void setChecked() {
        if (!PermissionManager.permissionToFineLocation(context)) {
            gpsCheckBox.setChecked(false);
            trackingCheckBox.setEnabled(false);
        } else {
            gpsCheckBox.setChecked(true);
        }

        if (!PermissionManager.permissionToReadCalendar(context)) {
            calendarCheckBox.setChecked(false);
        } else {
            calendarCheckBox.setChecked(true);
        }

        if (isLocationServiceRunning()) {
            trackingCheckBox.setChecked(true);
        } else {
            trackingCheckBox.setChecked(false);
        }
    }

    /**
     * Checks if PlaceRecorder is running.
     * @see PlaceRecorder
     * @return true/false
     */
    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (PlaceRecorder.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates alert dialog to confirm resetting of the app when reset button is clicked.
     */
    View.OnClickListener onClickResetButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.reset_title).setMessage(R.string.reset_message);

            builder.setPositiveButton(R.string.reset_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    deleteAllDataFromDatabase();
                }
            });
            builder.setNegativeButton(R.string.reset_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // user clicked cancel button
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }};

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

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     * @return true if the device has Goole Play Services APK, false if not.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("PrivacyFragment", "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}

