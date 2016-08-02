package fi.ohtu.mobilityprofile.ui.list_adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import fi.ohtu.mobilityprofile.domain.FavouritePlace;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.AddressConverter;

/**
 * This class adapts a list of SignificantPlace to ListView.
 */
public class SignificantsListAdapter extends ArrayAdapter<SignificantPlace> {

    private List<SignificantPlace> items;
    private int resourceId;
    private Context context;
    private Fragment fragment;

    private ImageButton editButton;
    private ImageButton verifyButton;
    private ImageButton deleteButton;
    private ImageButton wizard;

    /**
     * Creates favouritesListAdapter
     * @param context context of the app
     * @param resourceId resourceId
     * @param items list of significant places
     * @param fragment fragment
     */
    public SignificantsListAdapter(Context context, int resourceId, List<SignificantPlace> items, Fragment fragment) {
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
        editButton = (ImageButton)view.findViewById(R.id.favourites_edit);
        deleteButton = (ImageButton)view.findViewById(R.id.favourites_delete);
        verifyButton = (ImageButton)view.findViewById(R.id.favourites_verify);
        wizard = (ImageButton) view.findViewById(R.id.favourite_auto_generated);

        setColorsForSuggestion(view);
        setVisibilitiesForSuggestion();

        listItemText.setText(items.get(position).getAddress());
        setListeners(position);

        return view;
    }

    /**
     * Sets listeners for edit and delete buttons.
     * @param position the position of an item in the list
     */
    private void setListeners(final int position) {
        deleteButtonListener(position);
        verifyButtonListener(position);
        wizardListener();
    }

    private void deleteButtonListener(final int position) {
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle(R.string.favourites_auto_generated_delete_title)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                SignificantPlace sig = getSignificantPlace(position).get(0);
                                sig.setRemoved(true);
                                sig.save();
                                items.remove(position);
                                notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

    private void verifyButtonListener(final int position) {
        verifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final SignificantPlace sig = getSignificantPlace(position).get(0);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.favourites_edit_dialog, null);

                builder
                        .setView(dialogView)
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                EditText editTextName = (EditText) ((AlertDialog) dialog).findViewById(R.id.editFavouriteName);
                                EditText editTextAddress = (EditText) ((AlertDialog) dialog).findViewById(R.id.editFavouriteAddress);

                                if (!editTextName.equals("") && !editTextAddress.equals("")) {
                                    FavouritePlace fav = new FavouritePlace(editTextName.getText().toString(), editTextAddress.getText().toString());
                                    fav.save();
                                    sig.setFavourite(true);
                                    sig.save();
                                    items.remove(position);
                                    notifyDataSetChanged();
                                }

                                updateView();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setTitle(R.string.favourites_verify_title);

                EditText editTextName = (EditText) dialogView.findViewById(R.id.editFavouriteName);
                EditText editTextAddress = (EditText) dialogView.findViewById(R.id.editFavouriteAddress);

                editTextName.setText("");
                editTextAddress.setText(sig.getAddress());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void wizardListener() {
        wizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.favourite_auto_generated_dialog, null);

                builder
                        .setView(dialogView)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setTitle(R.string.favourite_auto_generated_title);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    /**
     * Returns the significant place by the position.
     * @param position the position of the item in the list
     * @return list of one significant place
     */
    private List<SignificantPlace> getSignificantPlace(int position) {
        return Select.from(SignificantPlace.class)
                .where(Condition.prop("id").eq(getItemId(position)))
                .limit("1")
                .list();
    }

    /**
     * Updates the favourites fragment view.
     */
    private void updateView() {
        FragmentTransaction tr = fragment.getFragmentManager().beginTransaction();
        tr.detach(fragment);
        tr.attach(fragment);
        tr.commit();
        notifyDataSetChanged();
    }

    private void setVisibilitiesForSuggestion() {
        wizard.setVisibility(View.VISIBLE);
        verifyButton.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.GONE);
    }

    private void setColorsForSuggestion(View view) {
        int grey = ContextCompat.getColor(context, R.color.colorAccentGrey);
        view.setBackgroundColor(grey);
        verifyButton.setBackgroundColor(grey);
        deleteButton.setBackgroundColor(grey);
        wizard.setBackgroundColor(grey);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public SignificantPlace getItem(int pos) {
        return items.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return items.get(pos).getId();
    }

}