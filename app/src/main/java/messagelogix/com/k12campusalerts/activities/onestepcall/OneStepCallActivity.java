package messagelogix.com.k12campusalerts.activities.onestepcall;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import java.util.HashMap;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.adapters.OneStepCallExpandableAdapter;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.models.VoiceMessage;
import messagelogix.com.k12campusalerts.utils.Config;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.Permission;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class OneStepCallActivity extends BaseActivity {

    private static final String LOG_TAG = OneStepCallActivity.class.getSimpleName() ;
    public static final int RECORD_MESSAGE = 0;
    public static final int SELECT_MESSAGE = 1;
    private User mUser;
    private Context context = this;

    private VoiceMessage messages;

    private ExpandableListView expandableListView;
    private OneStepCallExpandableAdapter adapter;
    private ProgressDialog mDialog;

    public interface OneStepCallService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<VoiceMessage> request(@FieldMap HashMap<String, String> parameters);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_one_step_call, frameLayout);

        /**
         * Setting title and itemChecked
         */
        mDrawerList.setItemChecked(position, true);
        setTitle(navDrawerItems.get(position).getTitle());

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);

        mUser = (User) new FunctionHelper(context).retrieveObject(User.class);

        downloadMessages();
    }

    private void showProgress(boolean show) {
        if(show){
            if (mDialog == null) {
                mDialog = new ProgressDialog(this);
                mDialog.setMessage("Please wait...");
                mDialog.setIndeterminate(true);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
            }
            mDialog.show();
        }else{
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.cancel();
                mDialog.dismiss();
            }
        }
    }

    private void downloadMessages() {
        showProgress(true);

        OneStepCallService client = ServiceGenerator.createService(OneStepCallService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "Phone");
        params.put("action", "GetPreRecordedLibrary");
        params.put("account", mUser.getData().getAccount());
        params.put("pin", mUser.getData().getPin());

        Call<VoiceMessage> call = client.request(params);
        call.enqueue(new Callback<VoiceMessage>() {
            @Override
            public void onResponse(Response<VoiceMessage> response) {
                showProgress(false);

                Log.d("OneStepCallActivity","downloadMessage() --> parameterrs - account: "+mUser.getData().getAccount()+"\npin: "+mUser.getData().getPin());
                Log.d("OneStepCallActivity","downloadMessage() --> response: "+response.body().getResponseObj());
                if (response.isSuccess()) {
                    messages = response.body();
                    if (messages.getSuccess()) {
                        adapter = new OneStepCallExpandableAdapter(context, messages);
                        expandableListView.setAdapter(adapter);

                        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                return true;
                            }
                        });

                        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                switch (groupPosition){
                                    case RECORD_MESSAGE:

                                        if (ContextCompat.checkSelfPermission(context,
                                                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                                            Log.d("recordButton","requesting perm1");

                                            ActivityCompat.requestPermissions(OneStepCallActivity.this,
                                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                                    Permission.RECORD_PERMISSION_REQUEST_CODE);
                                        } else {
                                            if((Build.VERSION.SDK_INT < Build.VERSION_CODES.R) && (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                                                    Log.d("recordButton","requesting perm2");

                                                    ActivityCompat.requestPermissions(OneStepCallActivity.this,
                                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                            Permission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);

//                                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                                    Permission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                                            } else {
                                                Log.d("recordButton","select");
                                                Intent recordIntent = new Intent(OneStepCallActivity.this, OneStepCallRecordActivity.class);
                                                startActivity(recordIntent);
                                            }
                                        }
                                        break;
                                    case SELECT_MESSAGE:
                                        Log.d("recordButton","selectreal");

                                        VoiceMessage.Data voiceMessage = messages.getData().get(childPosition);
                                        Intent i = new Intent(OneStepCallActivity.this, OneStepCallPlayActivity.class);
                                        i.putExtra(Config.VOICE_MESSAGE, new Gson().toJson(voiceMessage));
                                        startActivity(i);
                                        break;
                                }

                                return true;
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        adapter = new OneStepCallExpandableAdapter(context, messages);
                        expandableListView.setAdapter(adapter);

                        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                return true;
                            }
                        });

                        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                switch (groupPosition){
                                    case RECORD_MESSAGE:
                                        if (ContextCompat.checkSelfPermission(context,
                                                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                                            ActivityCompat.requestPermissions(OneStepCallActivity.this,
                                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                                    Permission.RECORD_PERMISSION_REQUEST_CODE);

                                        }
                                        else if (ContextCompat.checkSelfPermission(context,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                            ActivityCompat.requestPermissions(OneStepCallActivity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    Permission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);

                                        }
                                        else{
                                            Intent recordIntent = new Intent(OneStepCallActivity.this, OneStepCallRecordActivity.class);
                                            startActivity(recordIntent);}
                                        break;
                                    case SELECT_MESSAGE:
                                        Log.d("recordButton","selectreal");
                                        VoiceMessage.Data voiceMessage = messages.getData().get(childPosition);
                                        Intent i = new Intent(OneStepCallActivity.this, OneStepCallPlayActivity.class);
                                        i.putExtra(Config.VOICE_MESSAGE, new Gson().toJson(voiceMessage));
                                        startActivity(i);
                                        break;
                                }

                                return true;
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                Log.e(LOG_TAG, "onFailure =" + t.getMessage());
            }
        });
    }
}
