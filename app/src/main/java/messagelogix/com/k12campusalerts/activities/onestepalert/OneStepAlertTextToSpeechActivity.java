package messagelogix.com.k12campusalerts.activities.onestepalert;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.CampaignStatusActivity;
import messagelogix.com.k12campusalerts.activities.HomeActivity;
import messagelogix.com.k12campusalerts.activities.tts.TextToSpeechActivity;
import messagelogix.com.k12campusalerts.activities.tts.TextToSpeechScheduleActivity;
import messagelogix.com.k12campusalerts.models.BasicResponse;
import messagelogix.com.k12campusalerts.models.TTSPreviewResponse;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OneStepAlertTextToSpeechActivity extends AppCompatActivity {

    private Context context = this;
    private static final String LOG_TAG = OneStepAlertTextToSpeechActivity.class.getSimpleName();
    private ProgressDialog mDialog;
    private User mUser;
    private MediaPlayer mMediaPlayer;
    private ImageButton mPlayButton;
    private String url;
    private String fileName;
    private String listIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_step_alert_text_to_speech);
        mUser = (User) new FunctionHelper(this).retrieveObject(User.class);

        String message = getIntent().getStringExtra("message");
        listIds = getIntent().getStringExtra("lists");


        mPlayButton =(ImageButton)findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrStop();
            }
        });

        Button yesButton = (Button) findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        Button noButton = (Button) findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_check_circle_black_24dp)
                        .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                        .title(R.string.success_title_schedule)
                        .content("Would you like to check the campaign status?")
                        .positiveText("Yes")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent(context, CampaignStatusActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .neutralText("No")
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent(context, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });


        previewMessage(message);
    }

    private void initializePlayer(String url) {
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayButton.setImageResource(R.drawable.play_sound);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playOrStop(){
        if(mMediaPlayer.isPlaying()){
            stop();
        }else{
            play();
        }
    }

    private void stop(){
        mPlayButton.setImageResource(R.drawable.play_sound);
        mMediaPlayer.stop();
        //mMediaPlayer.release();
    }

    private void play() {
        try {
            mPlayButton.setImageResource(R.drawable.stop_playing);
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
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

    private void previewMessage(String message) {
        showProgress(true);
        TextToSpeechActivity.PreviewTTSMessageService client = ServiceGenerator.createService(TextToSpeechActivity.PreviewTTSMessageService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "TextToSpeech");
        params.put("action", "PreviewVoice");
        params.put("account", mUser.getData().getAccount());
        params.put("pin", mUser.getData().getPin());
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());
        params.put("ttv_message", message);
        params.put("voice", "M");
        params.put("ttv_id", "0");

        Call<TTSPreviewResponse> call = client.request(params);
        call.enqueue(new Callback<TTSPreviewResponse>() {
            @Override
            public void onResponse(Response<TTSPreviewResponse> response) {
                Log.d("OSATextToSpeechAct","previewMessage() --> response: "+response.body().getResponseObj());

                TTSPreviewResponse responseData  = response.body();
                if (response.isSuccess() && responseData.getSuccess()) {
                    showProgress(false);
                    url = responseData.getData().getUrl();
                    fileName = responseData.getData().getFilename();
                    initializePlayer(url);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                Log.e(LOG_TAG, "message =" + t.getMessage());
            }
        });
    }

    private void send() {
        showProgress(true);
        TextToSpeechScheduleActivity.TTSScheduleService client = ServiceGenerator.createService(TextToSpeechScheduleActivity.TTSScheduleService.class);

        String currentDate = new SimpleDateFormat("MMddyy", Locale.US).format(new Date());
        String currentTime = new SimpleDateFormat("HHmm", Locale.US).format(new Date());

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "TextToSpeech");
        params.put("action", "ScheduleTTS");
        params.put("account", mUser.getData().getAccount());
        params.put("pin", mUser.getData().getPin());
        params.put("date", currentDate);
        params.put("time", currentTime);
        params.put("lists", this.listIds);
        params.put("fileName", this.fileName);

        Call<BasicResponse> call = client.request(params);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Response<BasicResponse> response) {
                Log.d("OSATextToSpeechAct","send() --> response: "+response.body().getResponseObj());

                showProgress(false);
                BasicResponse responseData  = response.body();
                if (response.isSuccess() && responseData.getSuccess()) {
                    String content = getResources().getString(R.string.success_schedule) + " Would you like to check the campaign status?";

                    new MaterialDialog.Builder(context)
                            .iconRes(R.drawable.ic_check_circle_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title(R.string.success_title_schedule)
                            .content(content)
                            .positiveText("Yes")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(context, CampaignStatusActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            })
                            .neutralText("No")
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                Log.e(LOG_TAG, "message =" + t.getMessage());
            }
        });
    }



}
