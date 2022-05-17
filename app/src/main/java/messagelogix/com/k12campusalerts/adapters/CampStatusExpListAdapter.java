package messagelogix.com.k12campusalerts.adapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;
import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.CampaignStatusActivity;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Program on 4/7/2017.
 */
public class CampStatusExpListAdapter extends BaseExpandableListAdapter {

    public interface CancelCampService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<ResponseBody> request(@FieldMap HashMap<String, String> parameters);
    }

    private Context context;
    //list of headerTitles
    private List<String> listDataHeader;
    //child data in the form: headerTitle, childTitle
    private HashMap<String,List<CampaignStatusActivity.CampaignStatusItems>> listDataChild;

    public CampStatusExpListAdapter(Context context, List<String> listDataHeader, HashMap<String,List<CampaignStatusActivity.CampaignStatusItems>> listDataChild){

        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;

    }


    private void cancelTask(String commonId, String type, final int groupPosition, final int childPosition){
        //        showProgress(true);
        User mUser = (User) new FunctionHelper(context).retrieveObject(User.class);
        CancelCampService client = ServiceGenerator.createService(CancelCampService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "CampaignStatus");
        params.put("action", "CancelCampaign");
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());
        params.put("message_type", type);
        params.put("camp_id", commonId);

        Call<ResponseBody> call = client.request(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                //  showProgress(false);
                if(response.isSuccess()){
                    String listName = listDataChild.get(listDataHeader.get(groupPosition)).get(0).getStringValue(childPosition, 2);
                    String listCode = listDataChild.get(listDataHeader.get(groupPosition)).get(0).getStringValue(childPosition, 1);

                    Toast.makeText(context,"Campaign successfully canceled: " + listCode + "- " + listName, Toast.LENGTH_SHORT).show();

                    //this deletes the "listStrings" item
                    listDataChild.get(listDataHeader.get(groupPosition)).get(0).removeChildAt(childPosition);
                  //  listDataChild.get(listDataHeader.get(groupPosition)).get(0).remove(groupPosition, childPosition);
                   // notifyDataSetChanged();
                } else {
                    Toast.makeText(context,"Failed to cancel campaign, please try again...", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                //  showProgress(false);
                Toast.makeText(context,"Failed to cancel campaign, please try again...", Toast.LENGTH_SHORT).show();
                Log.e("", "onFailure =" + t.getMessage());
            }
        });
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        String headerTitle = (String) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.camp_list_group, null);

        }

//        ExpandableListView expListView = (ExpandableListView) parent.findViewById(R.id.camp_exp_lv);
//        expListView.expandGroup(0);
//        expListView.expandGroup(1);
//        expListView.expandGroup(2);

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //final String childText = (String) getChild(groupPosition,0);
       // final String secondText = (String) getChild(groupPosition,1);
        //childPositions: 0 = senddate  1=campname   2=lName   3=CommonId   Item parentposition = this childposition

        //extract data


        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.camp_list_item_child, null);

        }


        String sendDate = this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(0).getStringValue(childPosition,0);
        String listCode = this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(0).getStringValue(childPosition,2);
        String listName = "- " + this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(0).getStringValue(childPosition,1);

        TextView primeTV = (TextView) convertView.findViewById(R.id.camp_status_primary_text);
        primeTV.setText(sendDate);


        TextView secondTV = (TextView) convertView.findViewById(R.id.camp_status_secondary_text);
        secondTV.setText(listCode);

        TextView thirdTV = (TextView) convertView.findViewById(R.id.camp_status_third_text);

        thirdTV.setText(listName);

        ImageView iconLeft = (ImageView) convertView.findViewById(R.id.camp_status_icon);

        switch (groupPosition){
            case 0:
                iconLeft.setImageResource(R.drawable.ic_email_black_24dp);

                break;
            case 1:
                iconLeft.setImageResource(R.drawable.ic_sms_black_24dp);
                break;
            default:
                break;
        }



        Button cancelButton = (Button) convertView.findViewById(R.id.camp_status_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(context, "Clicked position " + groupPosition, Toast.LENGTH_SHORT).show();

                new MaterialDialog.Builder(context)
                        .title("Cancel Campaign")
                        .content("Are you sure you want to cancel this campaign?")
                        .positiveText("Yes")
                        .neutralText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String campId = listDataChild.get(listDataHeader.get(groupPosition)).get(0).getStringValue(childPosition,3);
                                switch (groupPosition){
                                    case 0:
                                        cancelTask(campId,"E", groupPosition, childPosition);
                                        break;
                                    case 1:
                                        cancelTask(campId,"T", groupPosition, childPosition);
                                        break;
                                    case 2:
                                        cancelTask(campId,"P", groupPosition, childPosition);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .autoDismiss(false)
                        .show();


                // campaignStatusActivity.cancelCampaign(campId, "P");
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //int listCount = this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
        int listCount = this.listDataChild.get(listDataHeader.get(groupPosition)).get(0).getArrayCount();

        if(listCount < 1){
            return 0;
        }

        return listCount;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {

        try{
            int childCount = getChildrenCount(groupPosition);
            switch (groupPosition){
                case 0:
                    if(childCount < 1){
                        return "NO EMAIL CAMPAIGNS ARE SCHEDULED";
                    }
                    break;
                case 1:
                    if(childCount < 1){
                        return "NO SMS CAMPAIGNS ARE SCHEDULED";
                    }
                    break;
                case 2:
                    if(childCount < 1){
                        return "NO PHONE CALLS ARE SCHEDULED";
                    }
                    break;
            }
        }
        catch (NullPointerException e){
            //e.printStackTrace();
            return "Loading...";
        }

        return this.listDataHeader.get(groupPosition);
    }
}
