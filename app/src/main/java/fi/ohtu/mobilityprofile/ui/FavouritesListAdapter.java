package fi.ohtu.mobilityprofile.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.FavouritePlace;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;

/**
 * This class adapts a list of FavouritePlace to ListView.
 */
public class FavouritesListAdapter extends ArrayAdapter<FavouritePlace> {

    private List<FavouritePlace> items;
    private int resourceId;
    private Context context;
    private Fragment fragment;

    /**
     * Creates favouritesListAdapter
     * @param context context of the app
     * @param resourceId resourceId
     * @param items list of favourite places
     * @param fragment fragment
     */
    public FavouritesListAdapter(Context context, int resourceId, List<FavouritePlace> items, Fragment fragment) {
        super(context, resourceId, items);
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

        TextView listItemText = (TextView) view.findViewById(R.id.favourites_item);
        listItemText.setText(items.get(position).toString());

        ImageButton deleteButton = (ImageButton)view.findViewById(R.id.favourites_delete);
        ImageButton editButton = (ImageButton)view.findViewById(R.id.favourites_edit);

        setListeners(position, deleteButton, editButton);

        return view;
    }

    /**
     * Sets listeners for delete and edit button
     * @param position position of the favourite place item
     * @param deleteButton delete button
     * @param editButton edit button
     */
    private void setListeners(final int position, ImageButton deleteButton, ImageButton editButton) {
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle(R.string.favourites_delete_title)
                        .setPositiveButton(R.string.reset_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                
                                final List<FavouritePlace> favourites = Select.from(FavouritePlace.class)
                                        .where(Condition.prop("id").eq(getItemId(position)))
                                        .limit("1")
                                        .list();

                                if (favourites.size() == 1) {
                                    favourites.get(0).delete();
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

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder
                        .setView(LayoutInflater.from(context).inflate(R.layout.favourites_edit_dialog, null))
                        .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                final List<FavouritePlace> favourites = Select.from(FavouritePlace.class)
                                        .where(Condition.prop("id").eq(getItemId(position)))
                                        .limit("1")
                                        .list();

                                if (favourites.size() == 1) {

                                    EditText editTextName = (EditText) ((AlertDialog) dialog).findViewById(R.id.editFavouriteName);
                                    EditText editTextAddress = (EditText) ((AlertDialog) dialog).findViewById(R.id.editFavouriteAddress);

                                    FavouritePlace fav = favourites.get(0);

                                    String name = editTextName.getText().toString();
                                    String address = editTextAddress.getText().toString();

                                    if (!name.equals("")) {
                                        fav.setName(name);
                                    }

                                    if (!address.equals("")) {
                                        fav.setAddress(address);
                                    }

                                    fav.save();
                                    updateView();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setTitle(R.string.favourites_edit_title);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * Updates the favourites fragment view
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
    public FavouritePlace getItem(int pos) {
        return items.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return items.get(pos).getId();
    }

}
