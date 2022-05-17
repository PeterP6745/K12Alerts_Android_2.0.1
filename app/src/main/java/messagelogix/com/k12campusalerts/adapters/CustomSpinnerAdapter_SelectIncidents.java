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
public class CustomSpinnerAdapter_SelectIncidents extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> messages;

    public CustomSpinnerAdapter_SelectIncidents(Context appContext, ArrayList<String> message){
        context = appContext;
        messages = message;
        inflater = (LayoutInflater.from(appContext));
    }

    @Override
    public int getCount() {
        return messages.size();
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
        convertView = inflater.inflate(R.layout.custom_spinner_incidents, null);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.spinner_incident_item);

        nameTextView.setText(messages.get(position));

        return convertView;
    }
}
