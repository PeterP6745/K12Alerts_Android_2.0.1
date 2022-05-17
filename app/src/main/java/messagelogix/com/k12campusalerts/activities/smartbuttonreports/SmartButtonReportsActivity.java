package messagelogix.com.k12campusalerts.activities.smartbuttonreports;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;


import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.adapters.SmartButtonReportListAdapter;
import messagelogix.com.k12campusalerts.models.SmartButtonReport;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class SmartButtonReportsActivity extends BaseActivity implements AbsListView.OnItemClickListener {

    private static final String LOG_TAG = SmartButtonReportsActivity.class.getSimpleName();

    private Context context = this;

    private View mView = null;

    private List<SmartButtonReport.SmartButtonReportItem> reports;

    private boolean itemsGot = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private String mParam2;

    private View view = null;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private SmartButtonReportListAdapter mAdapter;

//    // TODO: Rename and change types of parameters
//    public static SmartButtonReportsActivity newInstance(String param1, String param2) {
//
//        SmartButtonReportsActivity fragment = new SmartButtonReportsActivity();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
//    public SmartButtonReportsActivity() {
//
//        setRetainInstance(true);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        mView = getLayoutInflater().inflate(R.layout.fragment_smartbuttonreport_list, frameLayout);
        mListView = (AbsListView) mView.findViewById(android.R.id.list);
      //  Bundle extras = getIntent().getExtras();
       // mParam1 = extras.getString(ARG_PARAM1);
     //   mParam2 = extras.getString(ARG_PARAM2);

//        if (view == null) {
////            if (getArguments() != null) {
////                mParam1 = getArguments().getString(ARG_PARAM1);
////                mParam2 = getArguments().getString(ARG_PARAM2);
////            }
//        }
//        GetSmartButtonReportTask reportTask = new GetSmartButtonReportTask(mView);
//        reportTask.execute();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        mView = inflater.inflate(R.layout.fragment_smartbuttonreport_list, container, false);
//        mListView = (AbsListView) mView.findViewById(android.R.id.list);
//        GetSmartButtonReportTask reportTask = new GetSmartButtonReportTask(mView);
//        reportTask.execute();
//        return mView;
//    }

    @Override
    public void onResume() {

        super.onResume();
        GetSmartButtonReportTask reportTask = new GetSmartButtonReportTask(view);
        reportTask.execute();
    }

//    @Override
//    public void onAttach(Context context) {
//
//        super.onAttach(context);
//    }

//    @Override
//    public void onStart() {
//
//        super.onStart();
//        try {
//            mListener = (OnFragmentInteractionListener) getActivity();
//        } catch (ClassCastException e) {
//            throw new ClassCastException(getActivity().toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onSmartButtonFragmentInteraction(reports.get(position).getId());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {

        View emptyView = mListView.getEmptyView();
        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onSmartButtonFragmentInteraction(String id);
    }

    public class GetSmartButtonReportTask extends AsyncTask<String, Void, String> {

        private User user;

        private ProgressDialog mDialog;

        private View view;

        GetSmartButtonReportTask(View view) {

            this.view = view;
        }

        @Override
        protected void onPreExecute() {

            new ProgressDialog(context);
            mDialog = ProgressDialog.show(context, "", "Loading..");
            FunctionHelper function = new FunctionHelper(context);
            this.user = (User) function.retrieveObject(User.class);
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... url) {

            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("controller", "Locator");
            postDataParams.put("action", "GetLocatorReports");
            postDataParams.put("accountId", this.user.getData().getAcctId());
//            postDataParams.put("pinId", this.user.getData().getPinId());
//            postDataParams.put("contactByBuilding", this.user.getData().getContactByBuilding());
//            postDataParams.put("superuser", this.user.getData().getSuperuser());
//            postDataParams.put("search", "");//TODO: change to open or close
            return FunctionHelper.apiCaller(postDataParams);
        }

        @Override
        protected void onPostExecute(final String responseData) {

            Log.d("SBReportsActivity","getSmartButtonReportTask() --> response: "+responseData);

            if (responseData != null) {
                Gson gson = new GsonBuilder().create();
                SmartButtonReport smartButtonReport = gson.fromJson(responseData, SmartButtonReport.class);
                if (smartButtonReport.getSuccess() && context != null) {
                    reports = smartButtonReport.getData();

                    mAdapter = new SmartButtonReportListAdapter(context,
                            R.layout.smart_button_report_list_item, reports) {

                    };
                    mListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String reportId = reports.get(position).getId();
                            Intent intent = new Intent(context, SmartButtonReportsDetailsActivity.class);
                            intent.putExtra("reportId", reportId);
                            startActivity(intent);
                            Log.d(LOG_TAG, "Item was clicked!");
                        }
                    });

                    Log.d(LOG_TAG, "List was updated!");
                } else {
                }
            } else {
                Log.d(LOG_TAG, "No response received !");
            }
            if (mDialog != null && mDialog.isShowing()) {
                //mDialog.dismiss();
                dismissProgressDialog();
            }
            dismissProgressDialog();
            itemsGot = true;
        }

        private void showProgressDialog() {

            if (mDialog == null) {
                mDialog = new ProgressDialog(context);
                mDialog.setMessage("Loading. Please wait...");
                mDialog.setIndeterminate(false);
                mDialog.setCancelable(true);
            }
            mDialog.show();
        }

        private void dismissProgressDialog() {

            try {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.cancel();
                    mDialog.dismiss();
                    itemsGot = true;
                }
            } catch (IllegalArgumentException ex) {
                //do nothing
            } finally {
                mDialog = null;
            }
        }

        @Override
        protected void onCancelled() {

            dismissProgressDialog();
        }
    }
}
