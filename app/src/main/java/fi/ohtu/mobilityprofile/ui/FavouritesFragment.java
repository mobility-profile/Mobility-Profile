package fi.ohtu.mobilityprofile.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.domain.FavouritePlace;

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

        List<FavouritePlace> favouritePlaces = new ArrayList<>();
        try {
            favouritePlaces = FavouritePlace.listAll(FavouritePlace.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        List<FavouritePlace> significantPlaces = new ArrayList<>();
//        try {
//            significantPlaces = FavouritePlace.listAll(FavouritePlace.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        for (FavouritePlace f : significantPlaces) {
//            f.setName("auto");
//        }
//
//        favouritePlaces.addAll(significantPlaces);

        final FavouritesListAdapter adapter = new FavouritesListAdapter(context, R.layout.favourites_list_item, favouritePlaces, this);
        ListView listView = (ListView) view.findViewById(R.id.favourites_listView);
        listView.setAdapter(adapter);
        addListenerOnButton(view, adapter);
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
    private void addListenerOnButton(final View view, final FavouritesListAdapter adapter) {

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