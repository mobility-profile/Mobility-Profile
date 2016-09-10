package fi.ohtu.mobilityprofile.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.MainActivity;
import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.ui.list_adapters.AddressSuggestionAdapter;
import fi.ohtu.mobilityprofile.ui.list_adapters.FavouritesListAdapter;

/**
 * YourPlacesFragment handles everything concerning the YOUR PLACES tab in the UI.
 */
public class YourPlacesFragment extends Fragment {

    private static final String title = "YOUR PLACES";
    private static final int page = 1;

    private FavouritesListAdapter favouritesListAdapter;
    private AddressSuggestionAdapter addressSuggestionAdapter;
    private AutoCompleteTextView autoCompleteTextView;
    private Address address;

    /**
     * Creates a new instance of YourPlacesFragment.
     * @return YourPlacesFragment
     */
    public static YourPlacesFragment newInstance() {
        YourPlacesFragment yourPlacesFragment = new YourPlacesFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("title", title);
        yourPlacesFragment.setArguments(args);
        return yourPlacesFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_your_places, container, false);
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        setHelpListener(view);
        setAddressSuggestions(view);
        addButtonListener(view);
        setFavouritesListView(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        String dataChanged = sharedPref.getString("dataChanged", "Not Available");

        if (dataChanged.equals("true") ) {
            updateView();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("dataChanged", "false");
            editor.commit();
        }

    }

    /**
     * Listener for Help button.
     * Shows info about Your Places tab.
     * @param view View
     */
    private void setHelpListener(View view) {
        ImageButton help = (ImageButton) view.findViewById(R.id.your_places_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getActivity());

                builder
                        .setTitle(R.string.your_places_title)
                        .setMessage(R.string.your_places_info)
                        .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * Creates {@link AddressSuggestionAdapter} and listener for AutoCompleteTextView
     * which shows addresses.
     * @param view View
     */
    private void setAddressSuggestions(View view) {
        addressSuggestionAdapter = new AddressSuggestionAdapter(R.layout.list_addresses_item);

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.your_places_new_place_address);
        autoCompleteTextView.setAdapter(addressSuggestionAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int p, long id) {
                Address a = (Address) adapterView.getItemAtPosition(p);
                autoCompleteTextView.setText(a.getAddressLine(0));
                address = a;
                System.out.println(a);
            }
        });
    }

    /**
     * Listener for Add button.
     * Saves new places.
     * @param view View
     */
    private void addButtonListener(final View view) {
        Button button = (Button) view.findViewById(R.id.your_places_new_place_add);
        final EditText nameEditText = (EditText) view.findViewById(R.id.your_places_new_place_name);
        final Context context = MainActivity.getContext();

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String name = nameEditText.getText().toString();

                if (address == null) {
                    Toast.makeText(context, R.string.your_places_choose_address, Toast.LENGTH_LONG).show();
                } else {
                    if (name.equals("")) {
                        Place fav = new Place(address.getAddressLine(0), address);
                        fav.setFavourite(true);
                        PlaceDao.insertPlace(fav);
                    } else {
                        Place fav = new Place(name, address);
                        fav.setFavourite(true);
                       PlaceDao.insertPlace(fav);
                    }
                }

                updateView();
                nameEditText.setText("");
                autoCompleteTextView.setText("");
            }

        });

    }

    /**
     * Creates {@link FavouritesListAdapter} for listing favourite places.
     * @param view View
     */
    private void setFavouritesListView(View view) {
        List<Place> favouritePlaces = Place.listAll(Place.class);
        List<Place> places = new ArrayList<>();

        for (Place p : favouritePlaces) {
            if (!p.isHidden()) {
                places.add(p);
            }
        }

        favouritesListAdapter = new FavouritesListAdapter(R.layout.list_your_places_item, places, this);
        ListView listView = (ListView) view.findViewById(R.id.your_places_listview);
        listView.setAdapter(favouritesListAdapter);
    }

    /**
     * Updates the favourites fragment view
     */
    private void updateView() {
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        tr.detach(this);
        tr.attach(this);
        tr.commit();
        favouritesListAdapter.notifyDataSetChanged();
    }

}