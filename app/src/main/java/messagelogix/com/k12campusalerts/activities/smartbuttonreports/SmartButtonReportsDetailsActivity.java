package messagelogix.com.k12campusalerts.activities.smartbuttonreports;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.adapters.ExpandableListAdapter;
import messagelogix.com.k12campusalerts.models.Item;
import messagelogix.com.k12campusalerts.models.SmartButtonReportDetails;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class SmartButtonReportsDetailsActivity extends BaseActivity {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_REPORT_ID = "reportId";

    private static final String LOG_TAG = SmartButtonReportsDetailsActivity.class.getSimpleName();

    ExpandableListAdapter listAdapter;

    ExpandableListView expListView;

    private String mReportId;

    private Context context = this;

    private SmartButtonReportDetails.Data data;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment SmartButtonReportsDetailsActivity.
     */
//    public static SmartButtonReportsDetailsActivity newInstance(String mReportId) {
//
//        SmartButtonReportsDetailsActivity fragment = new SmartButtonReportsDetailsActivity();
//        Bundle args = new Bundle();
//        args.putString(ARG_REPORT_ID, mReportId);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public SmartButtonReportsDetailsActivity() {
        // Required empty public constructor
      //  setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.fragment_smart_button_details, frameLayout);
        initExpListView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mReportId = extras.getString(ARG_REPORT_ID);
//            GetReportDetailsTask reportTask = new GetReportDetailsTask();
//            reportTask.execute();
        }
    }

    private void initExpListView(View view){
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent intent;

                switch (groupPosition) {
                    case 1:
                        switch (childPosition) {
                            case 1:
                                //View map
                                String longitude = data.getLongitude();
                                String latitude = data.getLatitude();
                                Log.d("MapFragment","before SmartButtonMapActivity is instantiated --> longitude is: "+longitude+" latitude is: "+latitude);

                                SmartButtonMapActivity sbMapActivity = SmartButtonMapActivity.newInstance(longitude, latitude);
                                if (sbMapActivity != null) {

                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame, sbMapActivity)
                                            .addToBackStack(null)//Add no transaction to the back stack.
                                            .commit();
                                } else {
                                    // error in creating fragment
                                    Log.e(LOG_TAG, "Error in creating fragment");
                                }
                                //FunctionHelper.replaceFragment(fragment, getActivity());
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition) {
                            case 0:
                                //View Recipient
                                //Start Activity
                                intent = new Intent(context, SmartButtonRecipientActivity.class);
                                intent.putExtra("reportId",mReportId);
                                startActivity(intent);
                                //SmartButtonRecipientActivity sbRecipientActivity = SmartButtonRecipientActivity.newInstance(mReportId);

                                //FunctionHelper.replaceFragment(fragment, context);
                                break;
                        }
                        break;
                }
                return false;
            }
        });
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_smart_button_details, container, false);
//        //context = getActivity();
//        // get the listview
//        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
//        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                switch (groupPosition) {
//                    case 1:
//                        switch (childPosition) {
//                            case 1:
//                                //View map
//                                String longitude = data.getLongitude();
//                                String latitude = data.getLatitude();
//                                SmartButtonMapActivity fragment = SmartButtonMapActivity.newInstance(longitude, latitude);
//                                if (fragment != null) {
//                                    FragmentManager fragmentManager = ((Activity) getActivity()).getFragmentManager();
//                                    fragmentManager.beginTransaction()
//                                            .replace(R.id.frame_container, fragment)
//                                            .addToBackStack(null)//Add no transaction to the back stack.
//                                            .commit();
//                                } else {
//                                    // error in creating fragment
//                                    Log.e(LOG_TAG, "Error in creating fragment");
//                                }
//                                //FunctionHelper.replaceFragment(fragment, getActivity());
//                                break;
//                        }
//                        break;
//                    case 2:
//                        switch (childPosition) {
//                            case 0:
//                                //View Recipient
//                                //Start Activity
//                                SmartButtonRecipientActivity fragment = SmartButtonRecipientActivity.newInstance(mReportId);
//                                FunctionHelper.replaceFragment(fragment, context);
//                                break;
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
//        return view;
//    }

    /***
     * Preparing the list data
     */
    private void prepareListData() {

        List<String> listDataHeader = new ArrayList<>();
        HashMap<String, List<Item>> listDataChild = new HashMap<>();
        // Adding child data
        listDataHeader.add("Identity");
        listDataHeader.add("Location");
        listDataHeader.add("Recipient");
        // Adding child data
        List<Item> identityInfo = new ArrayList<>();
        identityInfo.add(new Item("Name", this.data.getName().equals(" ") ? "Not Available" : this.data.getName()));
        identityInfo.add(new Item("Title", this.data.getTitle().equals(" ") ? "Not Available" : this.data.getTitle()));
        identityInfo.add(new Item("School", this.data.getSchool().equals(" ") ? "Not Available" : this.data.getSchool()));
        identityInfo.add(new Item("Incident", this.data.getMessage().equals(" ") ? "Not Available" : this.data.getMessage()));
        identityInfo.add(new Item("Date", this.data.getTimestamp()));
        List<Item> locationInfo = new ArrayList<>();
        locationInfo.add(new Item("Address", this.data.getStreetAddress().equals(" ") ? "Not Available" : this.data.getStreetAddress()));
        locationInfo.add(new Item("View Map", ""));
        List<Item> recipientInfo = new ArrayList<>();
        recipientInfo.add(new Item("View Recipients", "name, contact"));
        listDataChild.put(listDataHeader.get(0), identityInfo);
        listDataChild.put(listDataHeader.get(1), locationInfo);
        listDataChild.put(listDataHeader.get(2), recipientInfo);
        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void onResume() {

        super.onResume();
        GetReportDetailsTask reportTask = new GetReportDetailsTask();
        reportTask.execute();
    }

    /**
     * Represents an asynchronous report task used to fetch report summaries for the database
     */
    public class GetReportDetailsTask extends AsyncTask<String, Void, String> {

        private User user;

        public GetReportDetailsTask() {

        }

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {

            showProgressDialog();
            this.user = (User) new FunctionHelper(context).retrieveObject(User.class);
        }

        @Override
        protected String doInBackground(String... url) {

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("controller", "Locator");
            postDataParams.put("action", "GetReportDetails");
            postDataParams.put("accountId", this.user.getData().getAcctId());
            postDataParams.put("reportId", mReportId);
            return FunctionHelper.apiCaller(postDataParams);
        }

        @Override
        protected void onPostExecute(final String responseData) {

            Log.d("SBReportsDetailsAct","getReportDetailsTask() --> response: "+responseData);

            if (responseData != null) {
                Gson gson = new GsonBuilder().create();
                SmartButtonReportDetails reportDetails = gson.fromJson(responseData, SmartButtonReportDetails.class);
                if (reportDetails.getSuccess()) {
                    data = reportDetails.getData();
                    prepareListData();
                }
            } else {
                Log.d(LOG_TAG, "No response received !");
            }
            dismissProgressDialog();
        }

        @Override
        protected void onCancelled() {

            dismissProgressDialog();
        }

        private void showProgressDialog() {

            if (mDialog == null) {
                mDialog = new ProgressDialog(context);
                mDialog.setMessage("Loading. Please wait...");
                mDialog.setIndeterminate(false);
                mDialog.setCancelable(false);
            }
            mDialog.show();
        }

        private void dismissProgressDialog() {

            try {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.cancel();
                    mDialog.dismiss();
                }
            } catch (IllegalArgumentException ex) {
                //do nothing
            } finally {
                mDialog = null;
            }
        }
    }
}


