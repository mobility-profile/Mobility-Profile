package fi.ohtu.mobilityprofile.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 3;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PrivacyFragment.newInstance();
            case 1:
                return ProfileFragment.newInstance();
            case 2:
                return InfoFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PRIVACY";
            case 1:
                return "PROFILE";
            case 2:
                return "INFO";
            default:
                return "";
        }
    }

}