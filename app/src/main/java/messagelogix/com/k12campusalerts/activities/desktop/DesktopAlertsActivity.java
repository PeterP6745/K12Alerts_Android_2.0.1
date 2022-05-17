package messagelogix.com.k12campusalerts.activities.desktop;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.models.ApiResponse;
import messagelogix.com.k12campusalerts.models.BasicResponse;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class DesktopAlertsActivity extends BaseActivity {

    private static final String LOG_TAG = DesktopAlertsActivity.class.getSimpleName();
    private User user;
    private TextView mAlertTextView;
    private FloatingActionButton clearFloatingActionButton;
    private EditText subjectEditText;
    private EditText messageEditText;

    public interface DesktopAlertsService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<BasicResponse> request(@FieldMap HashMap<String, String> parameters);
    }

    public interface CurrentRssFeedService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<DesktopResponse> request(@FieldMap HashMap<String, String> parameters);
    }

    public interface ClearRssFeedService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<BasicResponse> request(@FieldMap HashMap<String, String> parameters);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_desktop_alerts, frameLayout);

        user = (User) new FunctionHelper(this).retrieveObject(User.class);

        mDrawerList.setItemChecked(position, true);
        setTitle(navDrawerItems.get(position).getTitle());

        clearFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        clearFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear(view);
            }
        });
        clearFloatingActionButton.setVisibility(View.INVISIBLE);

        mAlertTextView = (TextView)findViewById(R.id.alertTextView);
        mAlertTextView.setSelected(true);
        mAlertTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mAlertTextView.setSingleLine(true);


       subjectEditText = (EditText) findViewById(R.id.subjectEditText);
       messageEditText = (EditText) findViewById(R.id.messageEditText);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectEditText.getText().toString();
                String message = messageEditText.getText().toString();
                String accountId = user.getData().getAcctId();
                String pinId = user.getData().getPinId();

                if(!subject.isEmpty() && !message.isEmpty()){
                    sendAlert(accountId, pinId, subject, message);
                }else{
                    showAlertDialog(
                            getString(R.string.bnotified_validation_title),
                            getString(R.string.bnotified_validation_message)
                    );
                }
            }
        });

        getCurrentFeed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    private void clear(View view) {
        Snackbar.make(view, "Clearing active alert", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        ClearRssFeedService client = ServiceGenerator.createService(ClearRssFeedService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "RSS");
        params.put("action", "ClearRss");
        params.put("accountId", user.getData().getAcctId());
        params.put("pinID", user.getData().getPinId());

        Call<BasicResponse> call = client.request(params);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Response<BasicResponse> response) {
                if (response.isSuccess() && response.body().getSuccess()) {
                    clearFloatingActionButton.setVisibility(View.INVISIBLE);
                    mAlertTextView.setText(R.string.no_alerts);
                    showAlertDialog(
                            "Alert cleared",
                            "The active alert has been cleared"
                    );
                }else{
                    clearFloatingActionButton.setVisibility(View.VISIBLE);
                    showAlertDialog(
                            "Error",
                            "Failed to clear"
                    );
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "onFailure =" + t.getMessage());
                clearFloatingActionButton.setVisibility(View.VISIBLE);
                showAlertDialog(
                        "Error",
                        "Failed to clear"
                );
            }
        });
    }

    private void getCurrentFeed() {
        CurrentRssFeedService client = ServiceGenerator.createService(CurrentRssFeedService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "RSS");
        params.put("action", "GetCurrentFeed");
        params.put("accountId", user.getData().getAcctId());

        Call<DesktopResponse> call = client.request(params);
        call.enqueue(new Callback<DesktopResponse>() {
            @Override
            public void onResponse(Response<DesktopResponse> response) {
                if (response.isSuccess() && response.body().getSuccess()) {
                    clearFloatingActionButton.setVisibility(View.VISIBLE);
                    mAlertTextView.setText(response.body().getData().getTextbod());
                }else{
                    clearFloatingActionButton.setVisibility(View.INVISIBLE);
                    mAlertTextView.setText(R.string.no_alerts);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "onFailure =" + t.getMessage());
            }
        });




    }

    private void sendAlert(String accountId, String pinId, String subject, final String message) {
        DesktopAlertsService client = ServiceGenerator.createService(DesktopAlertsService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "RSS");
        params.put("action", "CreateRss");
        params.put("accountId", accountId);
        params.put("pinId", pinId);
        params.put("title", subject);
        params.put("description", message);

        Log.d("DesktopAlertActivity","RSS-CreateRss - parameters: accountId: "+accountId+"\npindId: "+pinId+"\ntitle: "+subject+"\ndescription: "+message);

        Call<BasicResponse> call = client.request(params);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Response<BasicResponse> response) {
                Log.d("DesktopAlertActivity","RSS-CreateRss - response: "+response.body().getResponseObj());
                if (response.isSuccess() && response.body().getSuccess()) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(subjectEditText.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);
                    clearFloatingActionButton.setVisibility(View.VISIBLE);
                    mAlertTextView.setText(message);
                    subjectEditText.getText().clear();
                    messageEditText.getText().clear();
                    showAlertDialog(
                            getString(R.string.success_title),
                            getString(R.string.success_message)
                    );
                }else{
                    showAlertDialog(
                            "Error",
                            "Failed to send the message"
                    );
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "onFailure =" + t.getMessage());
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DesktopAlertsActivity.this);
        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class DesktopResponse extends ApiResponse<DesktopResponse.Data> {
        public class Data {
            @SerializedName("textbod")
            @Expose
            private String textbod;

            public String getTextbod() {
                return textbod;
            }

            public void setTextbod(String textbod) {
                this.textbod = textbod;
            }
        }
    }
}
