package fi.ohtu.mobilityprofile.ui.list_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import fi.ohtu.mobilityprofile.MainActivity;
import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.ui.activities.YourPlaceActivity;

/**
 * This class adapts a list of SignificantPlace to ListView.
 */
public class FavouritesListAdapter extends ArrayAdapter<Place> {

    private List<Place> items;
    private int resourceId;
    private Fragment fragment;

    private TextView listItemText;
    private ImageButton starUnfilled;
    private ImageButton starFilled;

    /**
     * Creates FavouritesListAdapter
     *
     * @param resourceId resourceId
     * @param items      list of favourite places
     * @param fragment   fragment
     */
    public FavouritesListAdapter(int resourceId, List<Place> items, Fragment fragment) {
        super(MainActivity.getContext(), resourceId, items);
        this.resourceId = resourceId;
        this.items = items;
        this.fragment = fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(MainActivity.getContext()).inflate(resourceId, parent, false);
        }

        listItemText = (TextView) view.findViewById(R.id.your_places_item);
        starFilled = (ImageButton) view.findViewById(R.id.your_places_star_filled);
        starUnfilled = (ImageButton) view.findViewById(R.id.your_places_star_unfilled);

        if (getItem(position).isFavourite()) {
            starUnfilled.setVisibility(View.GONE);
            starFilled.setVisibility(View.VISIBLE);

        } else {
            starFilled.setVisibility(View.GONE);
            starUnfilled.setVisibility(View.VISIBLE);
        }

        if (items.get(position).getName().equals("")) {
            listItemText.setText(items.get(position).getAddressLine(0));
        } else {
            listItemText.setText(items.get(position).getName() + ": " + items.get(position).getAddressLine(0));
        }

        itemTextListener(position);
        starUnfilledListener(position);
        starFilledListener(position);

        return view;
    }

    private void starUnfilledListener(final int position) {
        starUnfilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Place place = PlaceDao.getPlaceById(getItemId(position));
                place.setFavourite(true);
                PlaceDao.insertPlace(place);
                updateView();
            }
        });

    }

    private void starFilledListener(final int position) {
        starFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Place place = PlaceDao.getPlaceById(getItemId(position));
                place.setFavourite(false);
                PlaceDao.insertPlace(place);
                updateView();

            }
        });
    }


    private void itemTextListener(final int position) {
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.getContext();
                Intent placeIntent = new Intent(context, YourPlaceActivity.class);
                placeIntent.putExtra("placeId", getItemId(position) + "");
                fragment.getActivity().startActivity(placeIntent);
            }
        });

    }

    /**
     * Updates the favourites fragment view.
     */
    private void updateView() {
        FragmentTransaction tr = fragment.getFragmentManager().beginTransaction();
        tr.detach(fragment);
        tr.attach(fragment);
        tr.commit();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Place getItem(int pos) {
        return items.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return items.get(pos).getId();
    }
}