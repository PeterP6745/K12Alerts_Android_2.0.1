package messagelogix.com.k12campusalerts.activities.reports;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.ReportEmailText;
import messagelogix.com.k12campusalerts.models.ReportPhone;
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
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ReportDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ReportListActivity extends AppCompatActivity {


    private static final String LOG_TAG = ReportListActivity.class.getSimpleName();
    public static final String EMAIL_REPORT_TYPE = "1";
    public static final String SMS_REPORT_TYPE = "2";
    public static final String PHONE_REPORT_TYPE = "3";
    private Context context = this;
    private String mType;
    private ProgressDialog mDialog;


    public interface ReportService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<ResponseBody> request(@FieldMap HashMap<String, String> parameters);
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportitem_list);
       // getLayoutInflater().inflate(R.layout.activity_reportitem_list,frameLayout);

/**
 * Setting title and itemChecked
 */
     //   mDrawerList.setItemChecked(position, true);
    //    setTitle(navDrawerItems.get(position).getTitle());
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);


        }


        final View recyclerView = findViewById(R.id.reportitem_list);

        if (getIntent().getStringExtra("type") != null)
        {
            mType = getIntent().getStringExtra("type");
        }


        downloadReports(recyclerView);

        setActivityTitle();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "This feature will be available soon", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        if (findViewById(R.id.reportitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setActivityTitle() {
        switch (mType) {
            case EMAIL_REPORT_TYPE:
                setTitle(getString(R.string.email_reports));
                break;
            case SMS_REPORT_TYPE:
                setTitle(getString(R.string.text_to_speech));
                break;
            case PHONE_REPORT_TYPE:
                setTitle(getString(R.string.phone_reports));
                break;
        }
    }

    private void downloadReports(final View recyclerView) {
        showProgress(true);
        User mUser = (User) new FunctionHelper(context).retrieveObject(User.class);

        ReportService client = ServiceGenerator.createService(ReportService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "Report");
        params.put("action", "GetReportSummary");
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());
        params.put("type", mType);


        Call<ResponseBody> call = client.request(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                showProgress(false);
                if (response.isSuccess()) {
                    try {
                        Gson gson = new Gson();
                        if (mType.equals(EMAIL_REPORT_TYPE) || mType.equals(SMS_REPORT_TYPE)) {
                            ReportEmailText reports = gson.fromJson(response.body().string(), ReportEmailText.class);
                            if (reports.getSuccess()) {
                                assert recyclerView != null;
                                setupEmailTextRecyclerView((RecyclerView) recyclerView, reports.getData());
                            }
                        } else if(mType.equals(PHONE_REPORT_TYPE)) {
                            ReportPhone reports = gson.fromJson(response.body().string(), ReportPhone.class);
                            if (reports.getSuccess()) {
                                assert recyclerView != null;
                                setupPhoneRecyclerView((RecyclerView) recyclerView, reports.getData());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                Log.e(LOG_TAG, "message =" + t.getMessage());
            }
        });
    }

    private void showProgress(boolean show){
        if(show){
            if (mDialog == null) {
                mDialog = new ProgressDialog(this);
                mDialog.setMessage("Please wait...");
                mDialog.setIndeterminate(false);
                mDialog.setCancelable(true);
            }
            mDialog.show();
        }else{
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.cancel();
                mDialog.dismiss();
            }
        }
    }


    private void setupEmailTextRecyclerView(@NonNull RecyclerView recyclerView, List<ReportEmailText.Data> reports) {
        recyclerView.setAdapter(new ReportEmailTextItemRecyclerViewAdapter(reports));
    }

    private void setupPhoneRecyclerView(@NonNull RecyclerView recyclerView, List<ReportPhone.Data> reports) {
        recyclerView.setAdapter(new ReportPhoneItemRecyclerViewAdapter(reports));
    }

    public class ReportEmailTextItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ReportEmailTextItemRecyclerViewAdapter.ViewHolder> {

        private final List<ReportEmailText.Data> mValues;

        public ReportEmailTextItemRecyclerViewAdapter(List<ReportEmailText.Data> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reportitem_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getSenddate());
            holder.mContentView.setText(mValues.get(position).getCampName());
            if (position % 2 == 0) {
                holder.mView.setBackgroundColor(Color.parseColor("#edebc9"));
            }else{
                holder.mView.setBackgroundColor(Color.parseColor("#eff5fa"));
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ReportDetailFragment.ARG_ITEM_ID, holder.mItem.getCommonId());
                        arguments.putString(ReportDetailFragment.ARG_REPORT_TYPE, mType);
                        ReportDetailFragment fragment = new ReportDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.reportitem_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ReportDetailActivity.class);
                        intent.putExtra(ReportDetailFragment.ARG_ITEM_ID, holder.mItem.getCommonId());
                        intent.putExtra(ReportDetailFragment.ARG_REPORT_TYPE, mType);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public ReportEmailText.Data mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }


    public class ReportPhoneItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ReportPhoneItemRecyclerViewAdapter.ViewHolder> {

        private final List<ReportPhone.Data> mValues;

        public ReportPhoneItemRecyclerViewAdapter(List<ReportPhone.Data> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reportitem_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final ReportPhone.Data data = mValues.get(position);
            holder.mItem = data;
            holder.mIdView.setText(data.getTimestamp());
            holder.mContentView.setText(String.format("Alert #%s", data.getCommonId()));

            if (position % 2 == 0) {
                holder.mView.setBackgroundColor(Color.parseColor("#edebc9"));//even
            }else{
                holder.mView.setBackgroundColor(Color.parseColor("#eff5fa"));//odd
            }
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   String phoneReportJson = new Gson().toJson(data);
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ReportDetailFragment.ARG_PHONE_REPORT,phoneReportJson );
                        arguments.putString(ReportDetailFragment.ARG_ITEM_ID, holder.mItem.getCommonId());
                        arguments.putString(ReportDetailFragment.ARG_REPORT_TYPE, mType);
                        ReportDetailFragment fragment = new ReportDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.reportitem_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ReportDetailActivity.class);
                        intent.putExtra(ReportDetailFragment.ARG_ITEM_ID, holder.mItem.getCommonId());
                        intent.putExtra(ReportDetailFragment.ARG_REPORT_TYPE, mType);
                        intent.putExtra(ReportDetailFragment.ARG_PHONE_REPORT,phoneReportJson );

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public ReportPhone.Data mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
