package fi.ohtu.mobilityprofile;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.ui.MyPagerAdapter;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    FragmentPagerAdapter adapterViewPager;

    private CalendarConnection calendarConnection;
    private ArrayList<String> calendarEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarEvents = new ArrayList<>();
        calendarConnection = new CalendarConnection(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        SugarContext.init(this);
    }

    /**
     * Receives event data from the calendar
     * @param code
     * @param data
     */
    public void onResult(int code, Intent data) {
        if (code == 1) {
            Intent resultIntent = data;
            this.calendarEvents = resultIntent.getStringArrayListExtra("events");
            /* for (String e : calendarEvents) {
                 System.out.println(e);
             }*/
        }
    }

    public List<String> getEventData() {
        return this.calendarEvents;
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        calendarConnection.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        calendarConnection.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        calendarConnection.onPermissionsDenied(requestCode, perms);
    }
}
