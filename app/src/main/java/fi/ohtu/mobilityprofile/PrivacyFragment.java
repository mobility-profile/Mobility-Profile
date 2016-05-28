package fi.ohtu.mobilityprofile;

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
import android.widget.TextView;
import android.widget.Toast;

public class PrivacyFragment extends Fragment {
    private static final String title = "PRIVACY";
    private static final int page = 0;
    private int permissionCheckGPS;
    private int permissionCheckCal;
    private Switch switchGPS;
    private Switch switchCal;
    private Context context;

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
        permissionCheckGPS = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        permissionCheckCal = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR);
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

        switchGPS = (Switch) view.findViewById(R.id.switchGPS);
        switchCal = (Switch) view.findViewById(R.id.switchCal);

        if (permissionCheckGPS != PackageManager.PERMISSION_GRANTED) {
            switchGPS.setChecked(false);
        } else {
            switchGPS.setChecked(true);
        }

        if (permissionCheckCal != PackageManager.PERMISSION_GRANTED) {
            switchCal.setChecked(false);
        } else {
            switchCal.setChecked(true);
        }

        final TextView switchStatus = (TextView) view.findViewById(R.id.testing);

        switchGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(permissionCheckGPS);
                if(isChecked){
                    getPermissionToAccessFineLocation();
                    switchStatus.setText("ON");
                } else{
                    switchStatus.setText("OFF");
                }
            }
        });


        switchCal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(permissionCheckCal);
                if(isChecked){
                    getPermissionToReadCalendar();
                    switchStatus.setText("ON");
                } else{
                    switchStatus.setText("OFF");
                }
            }
        });


    }

    private static final int ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST = 1;
    private static final int READ_CALENDAR_PERMISSIONS_REQUEST = 2;

    public void getPermissionToAccessFineLocation() {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST);
        }
    }

    public void getPermissionToReadCalendar() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
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
                    switchGPS.setChecked(false);
                    Toast.makeText(context, "GPS permission denied", Toast.LENGTH_SHORT).show();
                }
            case READ_CALENDAR_PERMISSIONS_REQUEST:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Read Calendar permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    switchCal.setChecked(false);
                    Toast.makeText(context, "Read Calendar permission denied", Toast.LENGTH_SHORT).show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
