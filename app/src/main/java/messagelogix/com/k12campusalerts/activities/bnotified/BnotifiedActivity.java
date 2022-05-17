package messagelogix.com.k12campusalerts.activities.bnotified;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.HomeActivity;
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


public class BnotifiedActivity extends AppCompatActivity {

    private static final String LOG_TAG = BnotifiedActivity.class.getSimpleName();

    private ProgressDialog mDialog;
    private Context context = this;

    public interface BnotifiedService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<BasicResponse> request(@FieldMap HashMap<String, String> parameters);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bnotified);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //getLayoutInflater().inflate(R.layout.activity_bnotified, frameLayout);

      //  mDrawerList.setItemChecked(position, true);
     //   setTitle(navDrawerItems.get(position).getTitle());


        final User user = (User) new FunctionHelper(this).retrieveObject(User.class);


        final EditText subjectEditText = (EditText) findViewById(R.id.subjectEditText);
        final EditText messageEditText = (EditText) findViewById(R.id.messageEditText);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectEditText.getText().toString();
                String message = messageEditText.getText().toString();
                String accountId = user.getData().getAcctId();

                if(!subject.isEmpty() && !message.isEmpty()){
                    sendAlert(accountId, subject, message);
                }else{
                    showAlertDialog(
                            getString(R.string.bnotified_validation_title),
                            getString(R.string.bnotified_validation_message)
                    );
                }
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
//            // This ID represents the Home or Up button. In the case of this
//            // activity, the Up button is shown. For
//            // more details, see the Navigation pattern on Android Design:
//            //
//            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//            //
//
//
//            Intent intent = new Intent(this, BNotifiedHome.class);
//            navigateUpTo(intent);

            super.onBackPressed();
           // overridePendingTransition(R.anim.comming_in, R.anim.comming_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgress(boolean show){
        if(show){
            if (mDialog == null) {
                mDialog = new ProgressDialog(this);
                mDialog.setMessage("Please wait...");
                mDialog.setIndeterminate(false);
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

    private void sendAlert(String accountId, String subject, String message) {
        showProgress(true);
        BnotifiedService client = ServiceGenerator.createService(BnotifiedService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "Push");
        params.put("action", "SendBNotifPush");
        params.put("accountId", accountId);
        params.put("subject", subject);
        params.put("message", message);

        Call<BasicResponse> call = client.request(params);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Response<BasicResponse> response) {
                showProgress(false);

                Log.d("BNotifiedActivity","sendAlert() --> "+response.body().getResponseObj());

                if (response.isSuccess() && response.body().getSuccess()) {
                    new MaterialDialog.Builder(context)
                            .iconRes(R.drawable.ic_check_circle_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title(R.string.success_title)
                            .content(R.string.success_message)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    startActivity(new Intent(BnotifiedActivity.this, HomeActivity.class));
                                }
                            })
                            .show();
                }else{
                    new MaterialDialog.Builder(context)
                            .iconRes(R.drawable.ic_cancel_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title(R.string.error)
                            .content(R.string.failed_to_send)
                            .positiveText(R.string.ok)
                            .show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                Log.e(LOG_TAG, "onFailure =" + t.getMessage());
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BnotifiedActivity.this);
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

}
