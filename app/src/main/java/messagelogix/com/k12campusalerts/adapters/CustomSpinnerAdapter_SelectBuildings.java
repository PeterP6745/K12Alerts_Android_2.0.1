package messagelogix.com.k12campusalerts.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import messagelogix.com.k12campusalerts.R;

/**
 * Created by Program on 3/8/2018.
 */
public class CustomSpinnerAdapter_SelectBuildings extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> names;
    private ArrayList<String> ids;


    public CustomSpinnerAdapter_SelectBuildings(Context appContext, ArrayList<String> buildingNames, ArrayList<String> buildingIds) {
        context = appContext;
        names = buildingNames;
        ids = buildingIds;
        inflater = (LayoutInflater.from(appContext));
    }

    @Override
    public int getCount() {

        return names.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.custom_spinner_buildings, null);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.spinner_building_item);

        nameTextView.setText(names.get(position));

        return convertView;
    }
}
