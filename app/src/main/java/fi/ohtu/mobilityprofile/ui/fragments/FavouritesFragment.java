package fi.ohtu.mobilityprofile.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.ui.list_adapters.FavouritesListAdapter;
import fi.ohtu.mobilityprofile.util.AddressConverter;

/**
 * The class creates a component called FavouritesFragment.
 *
 * FavouritesFragment handles everything concerning the FAVOURITES tab in the UI.
 */
public class FavouritesFragment extends Fragment {

    private static final String title = "FAVOURITES";
    private static final int page = 1;
    private Context context;
    private FavouritesListAdapter adapter;

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

        setFavouritesListView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onresume fav");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String dataChanged = sharedPref.getString("dataChanged", "Not Available");

        if (dataChanged.equals("true") ) {
            updateView();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("dataChanged", "false");
            editor.commit();
        }

    }

    private void setFavouritesListView(View view) {
        List<Place> favouritePlaces = Place.listAll(Place.class);

        adapter = new FavouritesListAdapter(context, R.layout.list_your_places_item, favouritePlaces, this);
        ListView listView = (ListView) view.findViewById(R.id.your_places_listview);
        listView.setAdapter(adapter);

        addButtonListener(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_your_places, container, false);
    }

    /**
     * Listener to add favourite place button
     * @param view view inside the fragment
     */
    private void addButtonListener(final View view) {

        Button button = (Button) view.findViewById(R.id.your_places_new_place_add);
        final EditText addFavouriteName = (EditText) view.findViewById(R.id.your_places_new_place_name);
        final EditText addFavouriteAddress = (EditText) view.findViewById(R.id.your_places_new_place_address);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!addFavouriteName.getText().toString().equals("") && !addFavouriteAddress.getText().toString().equals("")) {
                    Place fav = new Place(addFavouriteName.getText().toString(), addFavouriteAddress.getText().toString());

                    Coordinate coordinate = AddressConverter.convertToCoordinates(context, fav.getAddress());
                    if (coordinate != null) {
                        coordinate.save();

                        fav.setCoordinate(coordinate);
                        fav.setFavourite(true);
                        fav.save();
                    } else {
                        Toast.makeText(context, "No coordinates were found for the address", Toast.LENGTH_LONG).show();
                    }

                    updateView();
                    addFavouriteName.setText("");
                    addFavouriteAddress.setText("");
                }
            }

        });

    }

    /**
     * Updates the favourites fragment view
     */
    private void updateView() {
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        tr.detach(this);
        tr.attach(this);
        tr.commit();
        adapter.notifyDataSetChanged();
    }

}