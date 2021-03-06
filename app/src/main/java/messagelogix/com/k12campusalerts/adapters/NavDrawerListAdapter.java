package messagelogix.com.k12campusalerts.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.NavDrawerItem;


public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private List<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, List<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imageIcon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.menuTitle);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);


        int color = Color.parseColor("#ffffff");
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        imgIcon.setColorFilter(color);
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if(navDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }

}