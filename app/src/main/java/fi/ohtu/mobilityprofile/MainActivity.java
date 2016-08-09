package fi.ohtu.mobilityprofile;

import android.content.Intent;
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

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.TransportMode;
import fi.ohtu.mobilityprofile.util.SecurityCheck;
import fi.ohtu.mobilityprofile.ui.MyPagerAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFERENCES = "fi.ohtu.mobilityprofile";
    public final static String CONFLICT_APPS = "conflictApps";

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
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_action_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_list);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_action_star_10);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_action_info);

        checkSecurity();
        createTransportModes();
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
                //TODO
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
            case R.id.action_backup:
                Intent intentBackup = new Intent(this, BackUpActivity.class);
                startActivity(intentBackup);
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
}
