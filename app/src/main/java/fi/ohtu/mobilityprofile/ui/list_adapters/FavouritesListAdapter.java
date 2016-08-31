package fi.ohtu.mobilityprofile.ui.list_adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.ui.activities.FavouriteListItemActivity;
import fi.ohtu.mobilityprofile.ui.activities.SuggestionListItemActivity;

/**
 * This class adapts a list of SignificantPlace to ListView.
 */
public class FavouritesListAdapter extends ArrayAdapter<Place> {

    private List<Place> items;
    private int resourceId;
    private Context context;
    private Fragment fragment;

    private TextView listItemText;
    private ImageButton starUnfilled;
    private ImageButton starFilled;

    /**
     * Creates favouritesListAdapter
     *
     * @param context    context of the app
     * @param resourceId resourceId
     * @param items      list of favourite places
     * @param fragment   fragment
     */
    public FavouritesListAdapter(Context context, int resourceId, List<Place> items, Fragment fragment) {
        super(context, resourceId, items);
        this.resourceId = resourceId;
        this.context = context;
        this.items = items;
        this.fragment = fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = ((Activity) context).getLayoutInflater().inflate(resourceId, parent, false);
        }

        listItemText = (TextView) view.findViewById(R.id.favourites_item);
        starFilled = (ImageButton) view.findViewById(R.id.favourites_star_filled);
        starUnfilled = (ImageButton) view.findViewById(R.id.favourites_star_unfilled);

        if (getItem(position).isFavourite()) {
            starUnfilled.setVisibility(View.GONE);
            starFilled.setVisibility(View.VISIBLE);
            setColorsForFavourite(view);
            if (items.get(position).getName().equals("")) {
                listItemText.setText(items.get(position).toString());
            } else {
                listItemText.setText(items.get(position).getName() + ": " + items.get(position).toString());
            }

        } else {
            starFilled.setVisibility(View.GONE);
            starUnfilled.setVisibility(View.VISIBLE);
            setColorsForPlace(view);
            listItemText.setText(items.get(position).toString());
            if (items.get(position).getName().equals("")) {
                listItemText.setText(items.get(position).toString());
            } else {
                listItemText.setText(items.get(position).getName() + ": " + items.get(position).toString());
            }
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
                place.save();
                notifyDataSetChanged();
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
                place.save();
                notifyDataSetChanged();
                updateView();

            }
        });
    }


    private void itemTextListener(final int position) {
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getItem(position).isFavourite()) {
                    Intent favouriteIntent = new Intent(context, FavouriteListItemActivity.class);
                    favouriteIntent.putExtra("favouriteId", getItemId(position) + "");
                    fragment.getActivity().startActivity(favouriteIntent);
                } else {
                    Intent placeIntent = new Intent(context, SuggestionListItemActivity.class);
                    placeIntent.putExtra("placeId", getItemId(position) + "");
                    fragment.getActivity().startActivity(placeIntent);
                }

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

    private void setColorsForFavourite(View view) {
        int white = ContextCompat.getColor(context, R.color.color_white);
        view.setBackgroundColor(white);
    }

    private void setColorsForPlace(View view) {
        int white = ContextCompat.getColor(context, R.color.color_white);
        view.setBackgroundColor(white);
        starUnfilled.setBackgroundColor(white);
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