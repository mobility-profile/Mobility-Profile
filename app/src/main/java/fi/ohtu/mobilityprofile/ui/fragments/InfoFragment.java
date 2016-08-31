package fi.ohtu.mobilityprofile.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fi.ohtu.mobilityprofile.R;

/**
 * The class creates a component called InfoFragment.
 *
 * InfoFragment handles everything concerning the INFO tab in the UI.
 */
public class InfoFragment extends Fragment {

    private static final String title = "INFO";
    private static final int page = 0;

    /**
     * Creates a new instance of InfoFragment.
     * @return an InfoFragment
     */
    public static InfoFragment newInstance() {
        InfoFragment infoFragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("title", title);
        infoFragment.setArguments(args);
        return infoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }
}

