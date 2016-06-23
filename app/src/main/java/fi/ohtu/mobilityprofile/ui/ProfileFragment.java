package fi.ohtu.mobilityprofile.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.RouteSearch;

/**
 * The class creates a component called ProfileFragment.
 *
 * ProfileFragment handles everything concerning the PROFILE tab in the UI.
 */
public class ProfileFragment extends Fragment {

    /**
     * The title of the fragment.
     */
    private static final String title = "PROFILE";

    /**
     * The position of the fragment in the "queue" of all fragments.
     */
    private static final int page = 1;
    private Context context;

    /**
     * Creates a new instance of ProfileFragment.
     * @return
     */
    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("title", title);
        profileFragment.setArguments(args);
        return profileFragment;
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

        List<RouteSearch> searches = Select.from(RouteSearch.class).orderBy("timestamp DESC").list();

        ArrayList<String> routes = new ArrayList<>();

        for (RouteSearch r: searches) {
            routes.add(r.toString());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.profile_list_item, routes);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }
}