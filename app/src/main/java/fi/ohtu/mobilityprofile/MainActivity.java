package fi.ohtu.mobilityprofile;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.orm.SugarContext;

import java.util.List;

import fi.ohtu.mobilityprofile.remoteconnection.SecurityCheck;
import fi.ohtu.mobilityprofile.ui.MyPagerAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFERENCES = "fi.ohtu.mobilityprofile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SugarContext.init(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        checkSecurity();
    }

    /**
     * Checks if there are any security problems with other applications. Check SecurityCheck.java
     * for more information.
     */
    private void checkSecurity() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        boolean securityOk = true;

        // If this is the first time the application is run, or security check is not ok, we should
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
        for (PackageInfo packageInfo : conflictApps) {
            System.out.println("AAA " + packageInfo.packageName);
            // TODO: Inform user about security problems.
        }
    }
}
