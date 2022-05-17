package messagelogix.com.k12campusalerts.activities.reports;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.ReportEmailText;
import messagelogix.com.k12campusalerts.models.ReportEmailTextDetails;
import messagelogix.com.k12campusalerts.models.ReportPhone;
import messagelogix.com.k12campusalerts.models.ReportPhoneDetails;
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
 * A fragment representing a single ReportItem detail screen.
 * This fragment is either contained in a {@link ReportListActivity}
 * in two-pane mode (on tablets) or a {@link ReportDetailActivity}
 * on handsets.
 */
public class ReportDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_REPORT_TYPE = "report_id";
    public static final String ARG_PHONE_REPORT = "report_item";
    private static final String LOG_TAG = ReportDetailFragment.class.getSimpleName();

    /**
     * The dummy content this fragment is presenting.
     */
    private ReportEmailText mItem;
    private TextView mTextView;
    private CollapsingToolbarLayout appBarLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReportDetailFragment() {
    }


    public interface ReportDetailService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<ResponseBody> request(@FieldMap HashMap<String, String> parameters);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(ARG_REPORT_TYPE)) {

            String reportId = getArguments().getString(ARG_ITEM_ID);
            final String reportType = getArguments().getString(ARG_REPORT_TYPE);
            User mUser = (User) new FunctionHelper(getActivity()).retrieveObject(User.class);

            downloadReport(reportId, reportType, mUser);

            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reportitem_detail, container, false);

        // Show the dummy content as text in a TextView.
        mTextView =  ((TextView) rootView.findViewById(R.id.reportitem_detail));
        return rootView;
    }

    private void downloadReport(String reportId, final String reportType, User mUser) {
        ReportDetailService client = ServiceGenerator.createService(ReportDetailService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "Report");
        params.put("action", "GetReportDetails");
        params.put("accountId", mUser.getData().getAcctId());
        params.put("campId", reportId);
        params.put("type", reportType);


        Call<ResponseBody> call = client.request(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    try {
                        //mTextView.setText(response.body().string());
                        switch (reportType) {
                            case ReportListActivity.EMAIL_REPORT_TYPE:
                            case ReportListActivity.SMS_REPORT_TYPE: {
                                ReportEmailTextDetails report = new Gson().fromJson(response.body().string(), ReportEmailTextDetails.class);
                                if (report.getSuccess()) {
                                    buildEmailSmsReport(report.getData());
                                }
                                break;
                            }
                            case ReportListActivity.PHONE_REPORT_TYPE: {
                                ReportPhoneDetails report = new Gson().fromJson(response.body().string(), ReportPhoneDetails.class);
                                if (report.getSuccess()) {
                                    buildPhoneReport(report.getData());
                                }
                                break;
                            }
                            default:
                                mTextView.setText(R.string.cannot_get_report_details);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "message =" + t.getMessage());
            }
        });
    }

    private void buildPhoneReport(List<ReportPhoneDetails.Data> data) {

        String phoneReportJson = getArguments().getString(ARG_PHONE_REPORT);
        ReportPhone.Data report = new Gson().fromJson( phoneReportJson, ReportPhone.Data.class);

        if (appBarLayout != null) {
            appBarLayout.setTitle(String.format("Alert #%s", report.getCommonId()));
        }

        String htmlString =
                        "<p>Campaign Id: "+ report.getCommonId() + "</p>" +
                        "<p>Campaign Name: "+ report.getCommonId() + "</p>" +
                        "<p>Date: "+ report.getTimestamp() + "</p>" +
                        "<p>Campaign Status: "+ report.getStatus() + "</p>" +
                        "<p>List: "+ report.getList() + "</p>";

        if(data.size() > 0)
        {
            if(data.get(0) != null){
                htmlString +=  "Number of Answered Calls: " + data.get(0).getTtlCalls() + "<br />" ;
            }

//            if(data.get(1) != null) {
//                htmlString +=  "Number of UnAnswered Calls: " + data.get(1).getTtlCalls() + "<br />" ;
//            }
        }

        mTextView.setText(Html.fromHtml(htmlString));


    }

    private void buildEmailSmsReport(ReportEmailTextDetails.Data data) {
        if (appBarLayout != null) {
            appBarLayout.setTitle(data.getCampName());
        }

        mTextView.setText(Html.fromHtml(
                "<p>Campaign Id: "+ data.getCommonId() + "</p>" +
                        "<p>Campaign Name: "+ data.getCampName() + "</p>" +
                        "<p>Date: "+ data.getSenddate() + "</p>" +
                        "<p>Campaign Status: "+ data.getStatus() + "</p>" +
                        "<p>Sent by: "+ data.getSendname() + "</p>" +
                        "<p>Sent to list: "+ data.getLId() + " - " +  data.getLName() + "</p>" +
                        "<p>Total sent: "+ data.getRptSent() + "</p>" +
                        "<p>Total bounced: "+ data.getRptReturned() + "</p>")
        );
    }
}
