package fi.ohtu.mobilityprofile.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.R;
import fi.ohtu.mobilityprofile.data.FavouritePlace;

public class MyListAdapter extends ArrayAdapter<FavouritePlace> {

    private List<FavouritePlace> items;
    private int resourceId;
    private Context context;

    public MyListAdapter(Context context, int resourceId, List<FavouritePlace> items) {
        super(context, resourceId, items);
        this.resourceId = resourceId;
        this.context = context;
        this.items = items;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.favorites_item);
        listItemText.setText(items.get(position).toString());

        ImageButton deleteButton = (ImageButton)view.findViewById(R.id.favorites_delete);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.favorites_delete_title);

                builder.setPositiveButton(R.string.reset_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        final List<FavouritePlace> favourites = Select.from(FavouritePlace.class)
                                .where(Condition.prop("id").eq(getItemId(position)))
                                .limit("1")
                                .list();

                        if (favourites.size() == 1) {
                            FavouritePlace fav = favourites.get(0);
                            fav.delete();
                            items.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton(R.string.reset_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        return view;
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
