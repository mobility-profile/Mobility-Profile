package fi.ohtu.mobilityprofile.ui.list_adapters;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.R;

public class AddressSuggestionAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Address> resultList = new ArrayList<Address>();

    public AddressSuggestionAdapter(Context c) {
        context = c;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Address getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_addresses_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.address_item)).setText(getItem(position).getAddressLine(0));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Address> addresses = findAddresses(context, constraint.toString());

                    filterResults.values = addresses;
                    filterResults.count = addresses.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Address>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private List<Address> findAddresses(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        try {
            return  geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
