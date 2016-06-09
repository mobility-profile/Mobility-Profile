package fi.ohtu.mobilityprofile.ui;

import android.Manifest;
import android.content.Context;
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

import fi.ohtu.mobilityprofile.R;

public class PrivacyFragment extends Fragment {
    private static final String title = "PRIVACY";
    private static final int page = 0;

    private static final int ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST = 1;
    private static final int READ_CALENDAR_PERMISSIONS_REQUEST = 2;

    private int permissionGPS;
    private int permissionCal;

    private CheckBox GPSCheckBox;
    private CheckBox CalendarCheckBox;

    private Context context;

    /**
     * Creates a new instance of PrivacyFragment (it is a tab in UI).
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

    private void checkPermissions(){
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

        GPSCheckBox = (CheckBox) view.findViewById(R.id.checkbox_GPS);
        CalendarCheckBox = (CheckBox) view.findViewById(R.id.checkbox_calendar);

        setChecked();
        setListenerForCheckBoxGPS();
        setListenerForCheckBoxCalendar();
    }


    /**
     * Sets OnCheckedChangeListener for GPSCheckBox.
     */
    private void setListenerForCheckBoxGPS() {
        GPSCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * If checkbox is changed to CHECKED -mode, will ask permission to access fine location
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkPermissions();
                if(isChecked && permissionGPS != PackageManager.PERMISSION_GRANTED){
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
     * Sets OnCheckedChangeListener for CalendarCheckBox.
     */
    private void setListenerForCheckBoxCalendar() {
        CalendarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * If checkbox is changed to CHECKED -mode, will ask permission to read calendar
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkPermissions();
                if(isChecked && permissionCal != PackageManager.PERMISSION_GRANTED){
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
     * Checks if we have permission to fine location, and then if not, requests it.
     */
    private void getPermissionToAccessFineLocation() {
        if(permissionGPS != PackageManager.PERMISSION_GRANTED) {
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
     * Depending on grantResults creates a Toast and updates the setting of GPSCheckBox
     * @param grantResults
     */
    private void actGPSResult(@NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "GPS permission granted", Toast.LENGTH_SHORT).show();
        } else {
            GPSCheckBox.setChecked(false);
        }
    }

    /**
     * Depending on grantResults creates a Toast and updates the setting of CalendarCheckBox
     * @param grantResults
     */
    private void actCalendarResult(@NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Read Calendar permission granted", Toast.LENGTH_SHORT).show();
        } else {
            CalendarCheckBox.setChecked(false);
        }
    }

    /**
     * Sets checkboxes checked if the permission is already granted
     * or not checked if permission is denied.
     */
    private void setChecked() {
        checkPermissions();
        if (permissionGPS != PackageManager.PERMISSION_GRANTED) {
            GPSCheckBox.setChecked(false);
        } else {
            GPSCheckBox.setChecked(true);
        }

        if (permissionCal != PackageManager.PERMISSION_GRANTED) {
            CalendarCheckBox.setChecked(false);
        } else {
            CalendarCheckBox.setChecked(true);
        }
    }
}
