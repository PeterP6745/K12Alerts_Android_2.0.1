package messagelogix.com.k12campusalerts.adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.Item;

import static android.R.attr.gravity;
import static android.R.attr.layout_centerInParent;
import static android.R.attr.type;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by Ahmed Daou on 9/1/2015.
 * This class provides required methods to render listview.
 * getGroupView() – Returns view for the list group header
 * getChildView() – Returns view for list child item
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;

    private List<String> _listDataHeader; // header titles

    // child data in format of header title, child title
    private HashMap<String, List<Item>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Item>> listChildData) {

        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
     //   Log.d(" List Context",context.toString());
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Item setting = (Item) getChild(groupPosition, childPosition);
//        CustomItem altSetting = (CustomItem) getChild(groupPosition,childPosition);
        //get text for title
        final String childText = setting.getTitle();
        //get text for value
        final String childValue = setting.getValue();
        //get image resource file or icon
        final int imgResource = setting.getIconSrc();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          //  convertView = layoutInflater.inflate(R.layout.expandable_list_item, parent, false);
            convertView = layoutInflater.inflate(R.layout.report_detail_list_item, parent, false);
        }
        TextView txtListChild = (TextView) convertView
                   .findViewById(R.id.report_detail_label); //.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        TextView txtListChildValue = (TextView) convertView
                .findViewById(R.id.report_detail_detail); //.findViewById(R.id.lblListItemDetail);
        txtListChildValue.setText(childValue);
        ImageView imgListChildIcon = (ImageView) convertView.findViewById(R.id.report_detail_icon);

        //Center the text for "Incident Message" details and set title color to Purple (as it is in iOS)
        if (childText.equals("Incident Message"))
        {
            txtListChild.setTextColor(Color.parseColor("#ff5f1299"));

            txtListChildValue.setGravity(Gravity.START);
        }
        //set red color to confirmation code value
        if (childText.equals("Confirmation Code")){
            txtListChildValue.setTextColor(Color.parseColor("#CF212A"));
        }
        //else change color back to black for de-queued cells
        else {txtListChildValue.setTextColor(Color.parseColor("#000000"));}
        //set image to row
        if(imgResource != 0){
        imgListChildIcon.setImageResource(imgResource);
        imgListChildIcon.setVisibility(View.VISIBLE);}
        else imgListChildIcon.setVisibility(View.GONE);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_group, parent, false);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        //expand all
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
}