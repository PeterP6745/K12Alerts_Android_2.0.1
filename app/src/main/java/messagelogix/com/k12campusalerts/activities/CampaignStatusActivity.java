package messagelogix.com.k12campusalerts.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.adapters.CampStatusExpListAdapter;
import messagelogix.com.k12campusalerts.models.CampPhone;
import messagelogix.com.k12campusalerts.models.ReportEmailText;
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
public class CampaignStatusActivity extends BaseActivity {
    CampStatusExpListAdapter listAdapter;
    ExpandableListView expListView;
    public List<String> listDataHeader;
   // public List<CampaignStatusItems> campStatusItems;
    public HashMap<String, List<CampaignStatusItems>> listDataChild;

    public ProgressDialog mDialog;
    public Context context = CampaignStatusActivity.this;

    public interface GetStatusCampService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<ResponseBody> request(@FieldMap HashMap<String, String> parameters);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_campaign_status);
        getLayoutInflater().inflate(R.layout.activity_campaign_status, frameLayout);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.camp_exp_lv);

        // preparing list data
        prepareListData();

        mDrawerList.setItemChecked(position, true);
        setTitle(navDrawerItems.get(position).getTitle());

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//
//        }

//        expListView.expandGroup(0);
//        expListView.expandGroup(1);
//        expListView.expandGroup(2);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<CampaignStatusItems>>();
        // Adding HeaderTitles
        listDataHeader.add("EMAIL MESSAGE STATUS");
        listDataHeader.add("TEXT-TO-CELL MESSAGE STATUS");
        listDataHeader.add("PHONE MESSAGE STATUS");

        downloadEmailCampaigns();
        downloadTTSCampaigns();
        downloadPhoneCampaigns();

        listAdapter = new CampStatusExpListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
//            Intent intent = new Intent(context, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadEmailCampaigns() {
        showProgress(true);
        User mUser = (User) new FunctionHelper(context).retrieveObject(User.class);

        GetStatusCampService client = ServiceGenerator.createService(GetStatusCampService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "CampaignStatus");
        params.put("action", "GetEmailStatus");
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());
       // params.put("type", mType);

        Call<ResponseBody> call = client.request(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                Log.d("CampaignStatusActivity","downloadEmailCampaigns --> response: "+response.message());
                //showProgress(false);
                if (response.isSuccess()) {
                    try {
                        ReportEmailText reports;
                        Gson gson = new Gson();

                        reports = gson.fromJson(response.body().string(), ReportEmailText.class);


                        if (reports != null) {

                            CampaignStatusItems mItems = new CampaignStatusItems(reports.getData());
                            List<CampaignStatusItems> campStatusItems = new ArrayList<CampaignStatusItems>();
                            campStatusItems.add(mItems);
                            listDataChild.put(listDataHeader.get(0), campStatusItems);
                            expListView.expandGroup(0);


                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                Log.e("", "message =" + t.getMessage());
            }
        });
    }

    private void downloadTTSCampaigns() {
        showProgress(true);
        User mUser = (User) new FunctionHelper(context).retrieveObject(User.class);

        GetStatusCampService client = ServiceGenerator.createService(GetStatusCampService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "CampaignStatus");
        params.put("action", "GetSMSStatus");
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());
        // params.put("type", mType);


        Call<ResponseBody> call = client.request(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                Log.d("CampaignStatusActivity","downloadTTSCampaigns() --> response: "+response.message());
                //showProgress(false);
                if (response.isSuccess()) {
                    try {
                        ReportEmailText reports;
                        Gson gson = new Gson();

                        reports = gson.fromJson(response.body().string(), ReportEmailText.class);

                        if (reports != null) {
                            CampaignStatusItems mItems = new CampaignStatusItems(reports.getData());
                            List<CampaignStatusItems> campStatusItems = new ArrayList<CampaignStatusItems>();
                            campStatusItems.add(mItems);
                            listDataChild.put(listDataHeader.get(1), campStatusItems);
                            expListView.expandGroup(1);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                Log.e("", "message =" + t.getMessage());
            }
        });
    }

    private void downloadPhoneCampaigns() {
        showProgress(true);
        User mUser = (User) new FunctionHelper(context).retrieveObject(User.class);

        GetStatusCampService client = ServiceGenerator.createService(GetStatusCampService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "CampaignStatus");
        params.put("action", "GetPhoneStatus");
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());
        // params.put("type", mType);


        Call<ResponseBody> call = client.request(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                Log.d("CampaignStatusActivity","downloadPhoneCampaigns() --> response: "+response.message());

                showProgress(false);
                if (response.isSuccess()) {
                    try {
                        CampPhone reports;
                        Gson gson = new Gson();

                        reports = gson.fromJson(response.body().string(), CampPhone.class);
            //            Log.d("q","PHONE: " + reports.getData());
//                        Log.d("w","Array Length: " + reports.getData().size());
             //           Log.d("e","PHONE REPORTS: " + reports.getData().get(0));

                        if (reports != null) {
                            CampaignStatusItems mItems = new CampaignStatusItems(reports.getData(),"tag");
                            List<CampaignStatusItems> campStatusItems = new ArrayList<CampaignStatusItems>();
                            campStatusItems.add(mItems);

                            listDataChild.put(listDataHeader.get(2), campStatusItems);
                            expListView.expandGroup(2);
                           // Log.d("tag", "ListDataChild Size: "+ listDataChild.get(listDataHeader.get(2)).size());

                        }

                    } catch (IOException e ) {
                        e.printStackTrace();
                    }


                }
            }
            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                Log.e("", "message =" + t.getMessage());
            }
        });
    }

    private void showProgress(boolean show){
        if(show){
            if (mDialog == null) {
                mDialog = new ProgressDialog(this);
                mDialog.setMessage("Please wait...");
                mDialog.setIndeterminate(false);
                mDialog.setCancelable(false);
            }
            mDialog.show();
        }else{
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.cancel();
                mDialog.dismiss();
            }
        }
    }


    public class CampaignStatusItems{
        private String sendDate;
        private String campName;
        private String lName;
        private String commonId;

        List<List<String>> listStrings = new ArrayList<>();

        public CampaignStatusItems(List<ReportEmailText.Data> emailItems){



            if(emailItems!= null){
                int arrayLength = emailItems.size();

                for(int i = 0; i<arrayLength; i++){
                    List<String> strings = new ArrayList<String>();
                    strings.add(0, emailItems.get(i).getSenddate());
                    strings.add(1, emailItems.get(i).getCampName());
                    strings.add(2, emailItems.get(i).getLName());
                    strings.add(3, emailItems.get(i).getCommonId());

                    listStrings.add(strings);

                    Log.d("!!!!","LISTSTRINGS: " + listStrings);
                }

            }

        }

        public CampaignStatusItems(List<CampPhone.Data> phoneItems, String tag){

            if(phoneItems!= null){
                int arrayLength = phoneItems.size();

                for(int i = 0; i<arrayLength; i++){
                    List<String> strings = new ArrayList<String>();
                    strings.add(0, phoneItems.get(i).getTimeStamp());
                    strings.add(1, phoneItems.get(i).getName());
                    strings.add(2, phoneItems.get(i).getList());
                    strings.add(3, phoneItems.get(i).getCommon_id());

                    listStrings.add(strings);

                    Log.d("!!!!","LISTSTRINGS: " + listStrings.size());
                }

            }

        }


        public String getStringValue(int parentPosition, int childPosition){
            return listStrings.get(parentPosition).get(childPosition);
        }

        public int getArrayCount(){
            return listStrings.size();
        }

        public void removeChildAt(int parentPosition){
            listStrings.remove(parentPosition);

          //  Toast.makeText(context, "Removed Item at the " +parentPosition+" position", Toast.LENGTH_SHORT).show();
           // listAdapter.notifyDataSetChanged();
           // recreate();
            Intent intent = getIntent();
            finish();
            startActivity(intent);

        }


    }

}
