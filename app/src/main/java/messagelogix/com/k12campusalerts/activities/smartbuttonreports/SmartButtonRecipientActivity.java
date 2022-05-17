package messagelogix.com.k12campusalerts.activities.smartbuttonreports;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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


import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.adapters.ExpandableListAdapter;
import messagelogix.com.k12campusalerts.models.Item;
import messagelogix.com.k12campusalerts.models.SmartButtonRecipient;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class SmartButtonRecipientActivity extends BaseActivity {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String REPORT_ID = "reportId";

    private static final String LOG_TAG = SmartButtonRecipientActivity.class.getSimpleName();

    private String mReportId;

    ExpandableListAdapter listAdapter;

    ExpandableListView expListView;

    private Context context = this;

    public SmartButtonRecipientActivity() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param reportId Parameter 1.
//     * @return A new instance of fragment SmartButtonRecipientActivity.
//     */
//    public static SmartButtonRecipientActivity newInstance(String reportId) {
//
//        SmartButtonRecipientActivity fragment = new SmartButtonRecipientActivity();
//        Bundle args = new Bundle();
//        args.putString(REPORT_ID, reportId);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.fragment_smart_button_recipient, frameLayout);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mReportId = extras.getString(REPORT_ID);
            new GetReportDetailsTask().execute();
        }
    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        //return inflater.inflate(R.layout.fragment_smart_button_recipient, container, false);
//        View view = inflater.inflate(R.layout.fragment_smart_button_recipient, container, false);
//        //context = getActivity();
//        // get the listview
//
//        return view;
//    }

//    @Override
//    public void onAttach(Context context) {
//
//        super.onAttach(context);
//    }
//
//    @Override
//    public void onDetach() {
//
//        super.onDetach();
//    }

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
            postDataParams.put("action", "GetReportRecipients");
            postDataParams.put("accountId", this.user.getData().getAcctId());
            postDataParams.put("reportId", mReportId);
            return FunctionHelper.apiCaller(postDataParams);
        }

        @Override
        protected void onPostExecute(final String responseData) {

            Log.d("SBRecipientActivity","getReportDetailsTask() --> response: "+responseData);

            if (responseData != null) {
                Gson gson = new GsonBuilder().create();
                SmartButtonRecipient recipient = gson.fromJson(responseData, SmartButtonRecipient.class);
                if (recipient.getSuccess()) {
                    prepareListData(recipient.getData());
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

    private void prepareListData(List<SmartButtonRecipient.Data> data) {

        HashMap<String, List<Item>> listDataChild = new HashMap<>();
        List<String> listDataHeader = new ArrayList<>();
        for (SmartButtonRecipient.Data recipient : data) {
            //this is the header
            String fullName = recipient.getFname() + ' ' + recipient.getLname();
            fullName = fullName.trim().isEmpty() ? "Name is not available" : fullName;
            listDataHeader.add(fullName);
            //this is the content
            List<Item> identityInfo = new ArrayList<>();
            identityInfo.add(new Item("Date", recipient.getTimestamp()));
            identityInfo.add(new Item("Sent to", recipient.getSentTo()));
            listDataChild.put(fullName, identityInfo);
        }
        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }
}
