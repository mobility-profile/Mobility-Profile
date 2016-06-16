package fi.ohtu.mobilityprofile.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import fi.ohtu.mobilityprofile.LocationService;
import fi.ohtu.mobilityprofile.R;

public class PrivacyFragment extends Fragment {
    private static final String title = "PRIVACY";
    private static final int page = 0;

    private static final int ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST = 1;
    private static final int READ_CALENDAR_PERMISSIONS_REQUEST = 2;

    private int permissionGPS;
    private int permissionCal;

    private CheckBox gpsCheckBox;
    private CheckBox calendarCheckBox;
    private CheckBox trackingCheckBox;

    private Context context;

    /**
     * Creates a new instance of PrivacyFragment (it is a tab in UI).
     *
     * @return Privacy Fragment
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
        checkPermissions();
    }

    private void checkPermissions() {
        permissionGPS = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        permissionCal = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR);
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

        setChecked();
        setListenerForCheckBoxGPS();
        setListenerForCheckBoxCalendar();
        setListenerForCheckBoxTracking();
    }

    /**
     * Sets OnCheckedChangeListener for gpsCheckBox.
     */
    private void setListenerForCheckBoxGPS() {
        gpsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * If checkbox is changed to CHECKED -mode, will ask permission to access fine location
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkPermissions();
                if (isChecked && permissionGPS != PackageManager.PERMISSION_GRANTED) {
                    getPermissionToAccessFineLocation();
                } else if (isChecked) {
                    Toast.makeText(context, "Location tracking is used again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Location tracking will not be used", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Sets OnCheckedChangeListener for calendarCheckBox.
     */
    private void setListenerForCheckBoxCalendar() {
        calendarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * If checkbox is changed to CHECKED -mode, will ask permission to read calendar
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkPermissions();
                if (isChecked && permissionCal != PackageManager.PERMISSION_GRANTED) {
                    getPermissionToReadCalendar();
                } else if (isChecked) {
                    Toast.makeText(context, "Calendar is used again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Calendar will not be used", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setListenerForCheckBoxTracking() {
        trackingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    context.startService(new Intent(context, LocationService.class));
                } else {
                    context.stopService(new Intent(context, LocationService.class));
                }
            }
        });
    }

    /**
     * Checks if we have permission to fine location, and then if not, requests it.
     */
    private void getPermissionToAccessFineLocation() {
        if (permissionGPS != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST);
        }
    }

    /**
     * Checks if we have permission to read calendar, and then if not, requests it.
     */
    private void getPermissionToReadCalendar() {
        if (permissionCal != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR},
                    READ_CALENDAR_PERMISSIONS_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST:
                actGPSResult(grantResults);
                return;
            case READ_CALENDAR_PERMISSIONS_REQUEST:
                actCalendarResult(grantResults);
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Depending on grantResults creates a Toast and updates the setting of gpsCheckBox
     *
     * @param grantResults
     */
    private void actGPSResult(@NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "GPS permission granted", Toast.LENGTH_SHORT).show();
        } else {
            gpsCheckBox.setChecked(false);
        }
    }

    /**
     * Depending on grantResults creates a Toast and updates the setting of calendarCheckBox
     *
     * @param grantResults
     */
    private void actCalendarResult(@NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Read Calendar permission granted", Toast.LENGTH_SHORT).show();
        } else {
            calendarCheckBox.setChecked(false);
        }
    }

    /**
     * Sets checkboxes checked if the permission is already granted
     * or not checked if permission is denied.
     */
    private void setChecked() {
        checkPermissions();
        if (permissionGPS != PackageManager.PERMISSION_GRANTED) {
            gpsCheckBox.setChecked(false);
        } else {
            gpsCheckBox.setChecked(true);
        }

        if (permissionCal != PackageManager.PERMISSION_GRANTED) {
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

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
