package fi.ohtu.mobilityprofile.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.ui.list_adapters.AddressSuggestionAdapter;
import fi.ohtu.mobilityprofile.ui.list_adapters.FavouritesListAdapter;
import fi.ohtu.mobilityprofile.util.AddressConverter;

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
    private FavouritesListAdapter adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private Address address;

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

        setAddressSuggestions(view);

        setFavouritesListView(view);
    }

    @Override
    public void onResume() {
        Log.i(title, "onResumed");
        super.onResume();

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

        adapter = new FavouritesListAdapter(context, R.layout.favourites_list_item, favouritePlaces, this);
        ListView listView = (ListView) view.findViewById(R.id.favourites_listView);
        listView.setAdapter(adapter);

        addButtonListener(view);
    }

    private void setAddressSuggestions(View view) {
        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.add_favourite_address);
        autoCompleteTextView.setAdapter(new AddressSuggestionAdapter(context));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Address a = (Address) adapterView.getItemAtPosition(position);
                autoCompleteTextView.setText(a.getAddressLine(0));
                address = a;
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
     */
    private void addButtonListener(final View view) {

        Button button = (Button) view.findViewById(R.id.add_favourite_button);
        final EditText addFavouriteName = (EditText) view.findViewById(R.id.add_favourite_name);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!addFavouriteName.getText().toString().equals("") && address != null) {
                    Place fav = new Place(addFavouriteName.getText().toString(), address);

                    Coordinate coordinate = AddressConverter.getCoordinatesFromAddress(context, fav.getAddress().getAddressLine(0));
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
                    //addFavouriteAddress.setText("");
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