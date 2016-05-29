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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import fi.ohtu.mobilityprofile.R;

public class PrivacyFragment extends Fragment {
    private static final String title = "PRIVACY";
    private static final int page = 0;

    private static final int ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST = 1;
    private static final int READ_CALENDAR_PERMISSIONS_REQUEST = 2;

    private int permissionGPS;
    private int permissionCal;

    private Switch GPSSwitch;
    private Switch CalendarSwitch;

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

        GPSSwitch = (Switch) view.findViewById(R.id.switchGPS);
        CalendarSwitch = (Switch) view.findViewById(R.id.switchCal);

        setChecked();
        setListenerForGPSSwitch();
        setListenerForCalendarSwitch();
    }


    /**
     * Sets OnCheckedChangeListener for GPSSwitch.
     */
    private void setListenerForGPSSwitch() {
        GPSSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * If switch is changed to ON -mode, will ask permission to access fine location
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getPermissionToAccessFineLocation();
                }
            }
        });
    }

    /**
     * Sets OnCheckedChangeListener for CalendarSwitch.
     */
    private void setListenerForCalendarSwitch() {
        CalendarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            /**
             * If switch is changed to ON -mode, will ask permission to read calendar
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getPermissionToReadCalendar();
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
     * Depending on grantResults creates a Toast and updates the setting of GPSSwitch
     * @param grantResults
     */
    private void actGPSResult(@NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            GPSSwitch.setEnabled(false);
            GPSSwitch.setText("GPS - granted");
            Toast.makeText(context, "GPS permission granted", Toast.LENGTH_SHORT).show();
        } else {
            GPSSwitch.setChecked(false);
            Toast.makeText(context, "GPS permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Depending on grantResults creates a Toast and updates the setting of CalendarSwitch
     * @param grantResults
     */
    private void actCalendarResult(@NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            CalendarSwitch.setEnabled(false);
            CalendarSwitch.setText("Read Calendar - granted");
            Toast.makeText(context, "Read Calendar permission granted", Toast.LENGTH_SHORT).show();
        } else {
            CalendarSwitch.setChecked(false);
            Toast.makeText(context, "Read Calendar permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets switches checked if the permission is already granted
     * or not checked if permission is denied.
     */
    private void setChecked() {
        if (permissionGPS != PackageManager.PERMISSION_GRANTED) {
            GPSSwitch.setChecked(false);
        } else {
            GPSSwitch.setChecked(true);
            GPSSwitch.setText("GPS - granted");
            GPSSwitch.setEnabled(false);
        }

        if (permissionCal != PackageManager.PERMISSION_GRANTED) {
            CalendarSwitch.setChecked(false);
        } else {
            CalendarSwitch.setChecked(true);
            CalendarSwitch.setText("Read Calendar - granted");
            CalendarSwitch.setEnabled(false);
        }
    }
}
