package fi.ohtu.mobilityprofile;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    static final int CALENDAR_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent calendar = new Intent(this, CalendarActivity.class);
        this.startActivityForResult(calendar, CALENDAR_REQUEST);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);



    }

    // Vastaanottaa kalenteritiedot CalendarActivitystä
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Intent resultIntent = data;
                ArrayList<String> events = resultIntent.getStringArrayListExtra("events");
               for (String event : events) {
                   System.out.println(event);
               }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("ei onnistunut");
            }
        }
    }

}


