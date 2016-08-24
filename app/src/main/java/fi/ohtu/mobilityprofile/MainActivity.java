package fi.ohtu.mobilityprofile;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.orm.SugarContext;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.TransportMode;
import fi.ohtu.mobilityprofile.util.ProfileBackup;
import fi.ohtu.mobilityprofile.util.SecurityCheck;
import fi.ohtu.mobilityprofile.ui.MyPagerAdapter;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String SHARED_PREFERENCES = "fi.ohtu.mobilityprofile";
    public final static String CONFLICT_APPS = "conflictApps";
    public static final String TAG = "Mobility Profile";
    
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SugarContext.init(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_action_home).setContentDescription("Privacy");
        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_list).setContentDescription("Last searches");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_star_10).setContentDescription("Favourite places");
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_action_info).setContentDescription("Info");

        checkSecurity();
        createTransportModes();
        // new ProfileBackup(this).handleBackup("import");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    /**
     * Checks if there are any security problems with other applications. Check SecurityCheck.java
     * for more information.
     */
    private void checkSecurity() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        boolean securityOk = true;

        // If this is the first time the application is run or security check is not ok, we should
        // run the security check.
        if (sharedPreferences.getBoolean("firstrun", true) || !SecurityCheck.securityCheckOk(sharedPreferences)) {
            securityOk = SecurityCheck.doSecurityCheck(this, sharedPreferences);
            sharedPreferences.edit().putBoolean("firstrun", false).apply();
        }

        if (!securityOk) {
            processSecurityConflicts(SecurityCheck.getConflictInfo(this));
        }
    }

    /**
     * Informs the user about security problems.
     *
     * @param conflictApps List of applications that are causing the problems.
     */
    private void processSecurityConflicts(List<PackageInfo> conflictApps) {
        ArrayList<String> appNames = new ArrayList<>();

        for (PackageInfo packageInfo : conflictApps) {
            appNames.add(getPackageManager().getApplicationLabel(packageInfo.applicationInfo).toString());
        }

        Intent intent = new Intent(this, SecurityProblemActivity.class);
        intent.putStringArrayListExtra(CONFLICT_APPS, appNames);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
            case R.id.action_backup:
                Intent intentBackup = new Intent(this, BackUpActivity.class);
                startActivity(intentBackup);
                return true;
            case R.id.action_licenses:
                Intent intentLicenses = new Intent(this, LicensesActivity.class);
                startActivity(intentLicenses);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void createTransportModes() {
        if (TransportMode.count(TransportMode.class) == 0) {
            Log.i("MainActivity", "creating transport modes");

            TransportMode walking = new TransportMode("walking");
            walking.save();
            TransportMode bike = new TransportMode("bike");
            bike.save();
            TransportMode car = new TransportMode("car");
            car.save();
            TransportMode bus = new TransportMode("bus");
            bus.save();
            TransportMode metro = new TransportMode("metro");
            metro.save();
            TransportMode tram = new TransportMode("tram");
            tram.save();
            TransportMode train = new TransportMode("train");
            train.save();
            TransportMode boat = new TransportMode("boat");
            boat.save();
            TransportMode plane = new TransportMode("plane");
            plane.save();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // showMessage("Connected to Google Drive");
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {            
            mGoogleApiClient.connect();
            // showMessage("Connected to Google Drive");
        }
    }
    
    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient connected");
    }

    /**
     * Called when mGoogleApiClient is disconnected.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    /**
     * Called when mGoogleApiClient is trying to connect but failed.
     * Handle result.getResolution() if there is a resolution is
     * available.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, 1);
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    /**
     * Shows a toast message.
     */
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Getter for the GoogleApiClient.
     */
    public GoogleApiClient getGoogleApiClient() {
      return mGoogleApiClient;
    }

}
