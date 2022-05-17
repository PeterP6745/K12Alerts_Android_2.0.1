package messagelogix.com.k12campusalerts.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.GroupList;

/**
 * Created by Ahmed Daou on 2/24/2016.
 */

public class GroupListAdapter extends BaseAdapter {
    private static final String LOG_TAG = GroupListAdapter.class.getSimpleName();
    List<GroupList.Data> mGroupLists;
    private LayoutInflater mInflater;

    public GroupListAdapter (Context c, List<GroupList.Data> groupLists) {
        mGroupLists = groupLists;

        //Cache a reference to avoid looking it up on every getView() call
        mInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount () {
        return mGroupLists.size();
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public Object getItem (int position) {
        return mGroupLists.get(position);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate (R.layout.dialog_custom_listitem, parent, false);
        }

        GroupList.Data listItem = (GroupList.Data)getItem(position);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxItem);
        checkBox.setText(listItem.getLName());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        return convertView;
    }
}
