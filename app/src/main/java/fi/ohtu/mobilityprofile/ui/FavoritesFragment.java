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
import fi.ohtu.mobilityprofile.data.FavouritePlace;

/**
 * The class creates a component called FavoritesFragment.
 *
 * FavoritesFragment handles everything concerning the FAVORITES tab in the UI.
 */
public class FavoritesFragment extends Fragment {

    /**
     * The title of the fragment.
     */
    private static final String title = "FAVORITES";

    /**
     * The position of the fragment in the "queue" of all fragments.
     */
    private static final int page = 3;
    private Context context;

    /**
     * Creates a new instance of FavouritesFragment.
     *
     * @return
     */
    public static FavoritesFragment newInstance() {
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("title", title);
        favoritesFragment.setArguments(args);
        return favoritesFragment;
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

        final FavoritesListAdapter adapter = new FavoritesListAdapter(context, R.layout.favorites_list_item, favouritePlaces, this);
        ListView listView = (ListView) view.findViewById(R.id.favorites_listView);
        listView.setAdapter(adapter);
        addListenerOnButton(view, adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }


    private void addListenerOnButton(final View view, final FavoritesListAdapter adapter) {

        Button button = (Button) view.findViewById(R.id.add_favorite_button);
        final EditText addFavoriteName = (EditText) view.findViewById(R.id.add_favorite_name);
        final EditText addFavoriteAddress = (EditText) view.findViewById(R.id.add_favorite_address);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                FavouritePlace fav = new FavouritePlace(addFavoriteName.getText().toString(), addFavoriteAddress.getText().toString());
                fav.save();
                updateView(adapter);
            }

        });

    }

    private void updateView(FavoritesListAdapter adapter) {
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        tr.detach(this);
        tr.attach(this);
        tr.commit();
        adapter.notifyDataSetChanged();
    }

}