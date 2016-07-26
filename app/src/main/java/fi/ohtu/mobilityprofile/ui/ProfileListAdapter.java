package fi.ohtu.mobilityprofile.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.domain.RouteSearch;


/**
 * This class adapts a list of RouteSearches to ListView.
 */
public class ProfileListAdapter extends ArrayAdapter<RouteSearch> {

    private List<RouteSearch> items;
    private int resourceId;
    private Context context;
    private Fragment fragment;

    /**
     * Creates ProfileListAdapter
     * @param context context of the app
     * @param resourceId resourceId
     * @param items list of route searches
     * @param fragment fragment
     */
    public ProfileListAdapter(Context context, int resourceId, List<RouteSearch> items, Fragment fragment) {
        super(context, resourceId);
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

        TextView listItemTextStart = (TextView) view.findViewById(R.id.profile_item_start);
        listItemTextStart.setText(items.get(position).getStartlocation());

        TextView listItemTextDestination = (TextView) view.findViewById(R.id.profile_item_destination);
        listItemTextDestination.setText(items.get(position).getDestination());

        ImageButton deleteButton = (ImageButton)view.findViewById(R.id.profile_item_delete);

        setListeners(position, deleteButton);

        return view;
    }

    /**
     * Sets listeners for edit and delete buttons.
     * @param position the position of an item in the list
     * @param deleteButton item's delete button
     */
    private void setListeners(final int position, ImageButton deleteButton) {
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle(R.string.profile_item_delete_title)
                        .setPositiveButton(R.string.reset_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                final List<RouteSearch> searches = getRouteSearch(position);

                                if (searches.size() == 1) {
                                    searches.get(0).delete();
                                    items.remove(position);
                                    notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton(R.string.reset_cancel, new DialogInterface.OnClickListener() {
                            @Override
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
     * Returns the route search by the position.
     * @param position the position of the item in the list
     * @return list of one route search
     */
    private List<RouteSearch> getRouteSearch(int position) {
        return Select.from(RouteSearch.class)
                .where(Condition.prop("id").eq(getItemId(position)))
                .limit("1")
                .list();
    }

    /**
     * Updates the profile fragment view.
     */
    private void updateView() {
        FragmentTransaction tr = fragment.getFragmentManager().beginTransaction();
        tr.detach(fragment);
        tr.attach(fragment);
        tr.commit();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RouteSearch getItem(int pos) {
        return items.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return items.get(pos).getId();
    }

}