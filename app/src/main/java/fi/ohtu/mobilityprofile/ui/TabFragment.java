package fi.ohtu.mobilityprofile.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fi.ohtu.mobilityprofile.R;


public class TabFragment extends Fragment {
    private String title;
    private int page;

    public static TabFragment newInstance(String title, Integer page) {
        TabFragment tabFragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putString("title", title);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("page");
        title = getArguments().getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;

        switch (getArguments().getInt("page")) {
            case 0:
                view = inflater.inflate(R.layout.privacy_fragment, container, false);
                return view;
            case 1:
                view = inflater.inflate(R.layout.profile_fragment, container, false);
                return view;
            case 2:
                view = inflater.inflate(R.layout.info_fragment, container, false);
                return view;
            default:
                view = null;
                return view;
        }
    }
}

