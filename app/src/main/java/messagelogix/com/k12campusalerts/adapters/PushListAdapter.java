package messagelogix.com.k12campusalerts.adapters;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.PushMessagesList;


/**
 * Created by Richard on 3/10/2017.
 */
public class PushListAdapter extends ArrayAdapter<PushMessagesList.Data> {

    ArrayList<PushMessagesList.Data> pushItems =  new ArrayList<>();

    //    public PushListAdapter(Context context, int TvContentResId, int TvDateResId, ArrayList<PushContent.PushItem> objects){
//        super(context, TvContentResId,TvDateResId, objects);
//
//        pushItems = objects;
//    }
    public PushListAdapter(Context context, int resId ,ArrayList<PushMessagesList.Data> objects){
        super(context, resId ,objects);

        pushItems = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item_push, null);
        TextView tvContent = (TextView) v.findViewById(R.id.push_message_content);
        TextView tvDate = (TextView) v.findViewById(R.id.push_item_date);

        tvContent.setText(pushItems.get(position).getSubject());
        tvDate.setText(pushItems.get(position).getDateTime());

        final String campaign_id = pushItems.get(position).getCampId();

        v.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Toast.makeText(context, "hellooo", Toast.LENGTH_SHORT).show();
                //  Log.d("Campaign ID: ",campaign_id);
                //  Toast.makeText(context, campaign_id, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context,PushDetails.class);
//                intent.putExtra("ID",campaign_id);
//                context.startActivity(intent);
            }
        });

        return v;
    }

//    public void clearData() {
//        int size = this.mValues.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                this.mValues.remove(0);
//            }
//
//            this.notifyItemRangeRemoved(0, size);
//        }
//    }

}