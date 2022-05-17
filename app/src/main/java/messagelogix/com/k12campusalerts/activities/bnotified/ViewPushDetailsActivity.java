package messagelogix.com.k12campusalerts.activities.bnotified;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.PushDetails;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Program on 4/5/2017.
 */
public class ViewPushDetailsActivity extends AppCompatActivity {

    private String campId;

    public Context context = this;
   // private User mUser;

    public interface GetDetailsService{
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<ResponseBody> request(@FieldMap HashMap<String, String> parameters);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sent_reports_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //initWidgets();

        Intent intent = getIntent();
        campId = intent.getStringExtra("ID");

        getPushDetails();

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

    public void initWidgets(){
        TextView tv_date = (TextView) findViewById(R.id.push_detail_date);
        TextView tv_subject = (TextView) findViewById(R.id.push_detail_subject);
        TextView tv_message = (TextView) findViewById(R.id.push_detail_message);
        TextView tv_ios = (TextView) findViewById(R.id.push_detail_ios_devices);
        TextView tv_other = (TextView) findViewById(R.id.push_detail_other_devices);
        TextView tv_devices = (TextView) findViewById(R.id.push_detail_devices);
    }

    private void getPushDetails(){
       // mUser = (User)new FunctionHelper(context).retrieveObject(User.class);

        GetDetailsService client = ServiceGenerator.createService(GetDetailsService.class);

        HashMap<String,String> params = ServiceGenerator.getApiMap();
        params.put("controller", "Push");
        params.put("action", "GetPushAlertDetails");
        params.put("campaignId", campId);

        Call<ResponseBody> call = client.request(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if(response.isSuccess()){
                    try {

                        PushDetails report = new Gson().fromJson(response.body().string(), PushDetails.class);
                        if (report.getSuccess()) {


                            TextView tv_date = (TextView) findViewById(R.id.push_detail_date);
                            tv_date.setText(report.getData().getDateTime());

                            TextView tv_subject = (TextView) findViewById(R.id.push_detail_subject);
                            tv_subject.setText(report.getData().getSubject());

                            TextView tv_message = (TextView) findViewById(R.id.push_detail_message);
                            tv_message.setText(report.getData().getMessage());

                            TextView tv_ios = (TextView) findViewById(R.id.push_detail_ios_devices);
                            tv_ios.setText(report.getData().getIosDevices());

                            TextView tv_other = (TextView) findViewById(R.id.push_detail_other_devices);
                            tv_other.setText(report.getData().getOtherDevices());

                            TextView tv_devices = (TextView) findViewById(R.id.push_detail_devices);
                            tv_devices.setText(report.getData().getTotalDevices());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

}
