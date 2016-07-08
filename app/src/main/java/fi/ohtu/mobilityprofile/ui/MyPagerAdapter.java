package fi.ohtu.mobilityprofile.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * This class manages all the fragments of the program.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    /**
     * The number of fragments.
     */
    private static final int NUM_ITEMS = 4;


    /**
     * Creates MyPagerAdapter.
     * @param fragmentManager
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
                return PrivacyFragment.newInstance();
            case 1:
                return ProfileFragment.newInstance();
            case 2:
                return InfoFragment.newInstance();
            case 3:
                return FavouritesFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PRIVACY";
            case 1:
                return "PROFILE";
            case 2:
                return "INFO";
            case 3:
                return "FAVOURITES";
            default:
                return "";
        }
    }
}