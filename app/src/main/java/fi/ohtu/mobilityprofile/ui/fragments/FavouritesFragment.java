package fi.ohtu.mobilityprofile.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.FavouritePlace;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.ui.list_adapters.FavouritesListAdapter;
import fi.ohtu.mobilityprofile.ui.list_adapters.PlacesListAdapter;

/**
 * The class creates a component called FavouritesFragment.
 *
 * FavouritesFragment handles everything concerning the FAVOURITES tab in the UI.
 */
public class FavouritesFragment extends Fragment {

    /**
     * The title of the fragment.
     */
    private static final String title = "FAVOURITES";

    /**
     * The position of the fragment in the "queue" of all fragments.
     */
    private static final int page = 2;
    private Context context;
    private Switch seeSuggestions;

    /**
     * Creates a new instance of FavouritesFragment.
     *
     * @return favourites fragment
     */
    public static FavouritesFragment newInstance() {
        FavouritesFragment favouritesFragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("title", title);
        favouritesFragment.setArguments(args);
        return favouritesFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        Log.i(title, "onViewCreated");
        seeSuggestions = (Switch) view.findViewById(R.id.switchSuggestions);
        seeSuggestions.setChecked(false);

        setFavouritesListView(view);
        switchListener(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        seeSuggestions.setChecked(false);
    }

    private void setFavouritesListView(View view) {
        List<FavouritePlace> favouritePlaces = getFavouritePlaces();

        final FavouritesListAdapter adapter = new FavouritesListAdapter(context, R.layout.favourites_list_item, favouritePlaces, this);
        ListView listView = (ListView) view.findViewById(R.id.favourites_listView);
        listView.setAdapter(adapter);

        addButtonListener(view, adapter);
    }

    private void setSuggestionsListView(ListView listView) {
        List<Place> places = getPlaces();

        final PlacesListAdapter adapter = new PlacesListAdapter(context, R.layout.favourites_list_item, places, this);
        listView.setAdapter(adapter);
        listView.setVisibility(View.GONE);
    }

    private List<Place> getPlaces() {
        List<Place> places = new ArrayList<>();
        try {
            places = Place.listAll(Place.class);
            List<Place> remove = new ArrayList<>();

            for (Place place : places) {
                if (place.isFavourite() || place.isUnfavourited()) {
                    remove.add(place);
                }
            }

            places.removeAll(remove);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return places;
    }

    private List<FavouritePlace> getFavouritePlaces() {
        List<FavouritePlace> favouritePlaces = new ArrayList<>();
        try {
            favouritePlaces = FavouritePlace.listAll(FavouritePlace.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favouritePlaces;
    }


    /**
     * Listener for see suggestions switch
     * @param view view inside the fragment
     */
    private void switchListener(final View view) {

        seeSuggestions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ListView listView = (ListView) view.findViewById(R.id.places_listView);
                if (isChecked) {
                    setSuggestionsListView(listView);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourites_fragment, container, false);
    }

    /**
     * Listener to add favourite place button
     * @param view view inside the fragment
     * @param adapter FavouritesListAdapter
     */
    private void addButtonListener(final View view, final FavouritesListAdapter adapter) {

        Button button = (Button) view.findViewById(R.id.add_favourite_button);
        final EditText addFavouriteName = (EditText) view.findViewById(R.id.add_favourite_name);
        final EditText addFavouriteAddress = (EditText) view.findViewById(R.id.add_favourite_address);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!addFavouriteName.getText().toString().equals("") && !addFavouriteAddress.getText().toString().equals("")) {
                    FavouritePlace fav = new FavouritePlace(addFavouriteName.getText().toString(), addFavouriteAddress.getText().toString());
                    fav.save();
                    updateView(adapter);
                    addFavouriteName.setText("");
                    addFavouriteAddress.setText("");
                }
            }

        });

    }

    /**
     * Updates the favourites fragment view
     * @param adapter FavouritesListAdapter
     */
    private void updateView(FavouritesListAdapter adapter) {
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        tr.detach(this);
        tr.attach(this);
        tr.commit();
        adapter.notifyDataSetChanged();
    }

}