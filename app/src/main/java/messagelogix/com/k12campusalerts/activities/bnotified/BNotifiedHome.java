package messagelogix.com.k12campusalerts.activities.bnotified;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.models.TotalDevices;
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
 * Created by Program on 4/3/2017.
 */
public class BNotifiedHome extends BaseActivity {

    private static final String LOG_TAG = BNotifiedHome.class.getSimpleName();

    private Button sendPushButton;
    private Button viewPushButton;

    public Context context;

    private User mUser;

    public interface GetTotalDevicesService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<ResponseBody> request(@FieldMap HashMap<String, String> parameters);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_b_notified_2, frameLayout);
        context = BNotifiedHome.this;

        mDrawerList.setItemChecked(position, true);
        setTitle(navDrawerItems.get(position).getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initWidgets();

        mUser = (User)new FunctionHelper(context).retrieveObject(User.class);
        if(mUser != null) {
            startGetTotalDevices();
        }

    }

    private void startGetTotalDevices(){
        GetTotalDevicesService client = ServiceGenerator.createService(GetTotalDevicesService.class);

        HashMap<String,String> params = ServiceGenerator.getApiMap();
        params.put("controller", "Push");
        params.put("action", "GetTotalDevices");
        params.put("accountId", mUser.getData().getAcctId());

        Call<ResponseBody> call = client.request(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    try {
                     //   Gson gson = new Gson();
                        TotalDevices report = new Gson().fromJson(response.body().string(), TotalDevices.class);
                        if (report.getSuccess()) {
                            TextView iosDevices = (TextView) findViewById(R.id.tv_ios_devices);
                            iosDevices.setText(report.getData().getiOSDevices());

                            TextView otherDevices = (TextView) findViewById(R.id.tv_other_devices);
                            otherDevices.setText(report.getData().getAndroidDevices());

                            TextView totalDevices = (TextView) findViewById(R.id.tv_total_devices);
                            totalDevices.setText(report.getData().getTotalDevices());
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

    public void initWidgets(){
        sendPushButton = (Button) findViewById(R.id.b_send_push_notifications);
        sendPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BnotifiedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        viewPushButton = (Button) findViewById(R.id.b_view_push_notifications);
        viewPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BNotifiedViewPushActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }



}
