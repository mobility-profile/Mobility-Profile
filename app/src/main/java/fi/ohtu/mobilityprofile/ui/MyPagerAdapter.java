package fi.ohtu.mobilityprofile.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fi.ohtu.mobilityprofile.ui.fragments.YourPlacesFragment;
import fi.ohtu.mobilityprofile.ui.fragments.InfoFragment;
import fi.ohtu.mobilityprofile.ui.fragments.SettingsFragment;

/**
 * This class manages all the fragments of the program.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    /**
     * The number of fragments.
     */
    private static final int NUM_ITEMS = 3;

    /**
     * Creates MyPagerAdapter.
     * @param fragmentManager FragmentManager
     */
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
                return InfoFragment.newInstance();
            case 1:
                return YourPlacesFragment.newInstance();
            case 2:
                return SettingsFragment.newInstance();
            default:
                return new Fragment();
        }
    }
}