package messagelogix.com.k12campusalerts.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;


import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.VoiceMessage;


public class OneStepCallExpandableAdapter extends BaseExpandableListAdapter{


    private LayoutInflater inflater;
    private VoiceMessage mParent;
    private Context mContext;

    public OneStepCallExpandableAdapter(Context context, VoiceMessage messages) {
        mParent = messages;
        inflater = LayoutInflater.from(context);
        mContext= context;
    }

    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        if(mParent.getData()!=null){
            return mParent.getData().size() > 0 ? 2 : 1;
        }
        else{
            return 1;
        }
    }

    //counts the number of children items so the list knows how many times calls getChildView() method
    @Override
    public int getChildrenCount(int i) {
        if(mParent.getData()!=null){
            return (i == 0) ? 1 : mParent.getData().size();
        }
        else{
            return 1;
        }

    }

    //gets the title of each parent/group
    @Override
    public Object getGroup(int i) {
        return (i == 0) ? "Record a voice message" : "Select from your library";
    }

    //gets the name of each item
    @Override
    public Object getChild(int i, int i1) {
        return (i == 0) ? "Record a voice message" : mParent.getData().get(i1).getCannedName();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    //in this method you must set the text to see the parent/group on the list
    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        holder.groupPosition = groupPosition;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_parent, viewGroup, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
        textView.setText(getGroup(groupPosition).toString());

        ExpandableListView mExpandableListView = (ExpandableListView) viewGroup;
        mExpandableListView.expandGroup(groupPosition);

        view.setTag(holder);

        return view;
    }

    //in this method you must set the text to see the children on the list
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        holder.childPosition = childPosition;
        holder.groupPosition = groupPosition;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_child, viewGroup, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.list_item_text_child);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);

        switch (groupPosition){
            case 0:
                textView.setText("Record a voice message");
                imageView.setImageResource(R.drawable.ic_mic_black_24dp);
                break;
            case 1:
                textView.setText(getChild(groupPosition, childPosition).toString());
                imageView.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                break;
        }

        view.setTag(holder);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        /* used to make the notifyDataSetChanged() method work */
        super.registerDataSetObserver(observer);
    }

    protected class ViewHolder {
        protected int childPosition;
        protected int groupPosition;
        protected Button button;
    }
}
