package messagelogix.com.k12campusalerts.activities.onestepcall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.BasicResponse;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.models.VoiceMessage;
import messagelogix.com.k12campusalerts.utils.Config;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class OneStepCallPlayActivity extends AppCompatActivity {


    private static final String LOG_TAG = OneStepCallPlayActivity.class.getSimpleName();
    private MediaPlayer mMediaPlayer;
    private ImageButton mPlayButton;
    private VoiceMessage.Data voiceMessage;
    private ProgressDialog mDialog;
    private User mUser;
    private Context context = this;
    private EditText messageNameEditText;

    private boolean isPlaying =false;

    public interface OneStepCallSaveSlotService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<BasicResponse> request(@FieldMap HashMap<String, String> parameters);
    }

    public interface OneStepCallDeleteSlotService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<BasicResponse> request(@FieldMap HashMap<String, String> parameters);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_step_call_play);

        String voiceMessageJson =  getIntent().getStringExtra(Config.VOICE_MESSAGE);
        voiceMessage = new Gson().fromJson(voiceMessageJson, VoiceMessage.Data.class);

        mUser = (User) new FunctionHelper(this).retrieveObject(User.class);

        messageNameEditText = (EditText)findViewById(R.id.messageNameEditText);
        messageNameEditText.setText(voiceMessage.getCannedName());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initializePlayer(voiceMessage.getUrl());

        mPlayButton =(ImageButton)findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrStop();
            }
        });

        Button saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        Button scheduleButton =(Button)findViewById(R.id.scheduleButton);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteVoiceMessage(view);
            }
        });
    }

    private void save() {
        showProgress(true);

        OneStepCallSaveSlotService client = ServiceGenerator.createService(OneStepCallSaveSlotService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "Phone");
        params.put("action", "UpdateVoiceMessageName");
        params.put("account", mUser.getData().getAccount());
        params.put("pin", mUser.getData().getPin());
        params.put("canned_slot", voiceMessage.getCannedSlot());
        params.put("canned_name", messageNameEditText.getText().toString());

        Call<BasicResponse> call = client.request(params);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Response<BasicResponse> response) {
                showProgress(false);

                Log.d("OneStepCallPlayActivity","save() --> response: "+response.body().getResponseObj());

                BasicResponse responseData  = response.body();
                if (response.isSuccess() && responseData.getSuccess()) {
                    new MaterialDialog.Builder(context)
                            .iconRes(R.drawable.ic_check_circle_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title(R.string.message_saved)
                            .content(R.string.success_message_saved)
                            .positiveText(R.string.ok)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
//            Intent intent = new Intent(this, OneStepCallActivity.class);
//            navigateUpTo(intent);
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        this.stop();
    }

    private void schedule() {
        Intent intent = new Intent(OneStepCallPlayActivity.this, OneStepCallScheduleActivity.class);
        intent.putExtra(Config.VOICE_MESSAGE, new Gson().toJson(voiceMessage));
        startActivity(intent);
    }

    private void deleteVoiceMessage(View view) {
        showProgress(true);
        OneStepCallDeleteSlotService client = ServiceGenerator.createService(OneStepCallDeleteSlotService.class);
        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "Phone");
        params.put("action", "DeleteVoiceMessage");
        params.put("account", mUser.getData().getAccount());
        params.put("pin", mUser.getData().getPin());
        params.put("canned_slot", voiceMessage.getCannedSlot());

        Call<BasicResponse> call = client.request(params);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Response<BasicResponse> response) {
                showProgress(false);

                Log.d("OneStepCallPlayActivity","deleteVoiceMessage() --> response: "+response.body().getResponseObj());

                BasicResponse responseData  = response.body();
                if (response.isSuccess() && responseData.getSuccess()) {
                    new MaterialDialog.Builder(context)
                            .cancelable(false)
                            .canceledOnTouchOutside(false)
                            .iconRes(R.drawable.ic_check_circle_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title(R.string.message_deleted)
                            .content(R.string.success_deleted_message)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    startActivity(new Intent(OneStepCallPlayActivity.this, OneStepCallActivity.class));
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

    private void initializePlayer(String url) {
        try {
            mMediaPlayer = new MediaPlayer();
         //   mMediaPlayer.setDataSource(url);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


//    private void playOrStop(){
//        if(mMediaPlayer.isPlaying()){
//            stop();
//        }else{
//            play();
//        }
//    }

    private void playOrStop() {
        if (!isPlaying) {
            if (mMediaPlayer != null) {
                play();
                isPlaying=true;

            }

            //Toast.makeText(context, "If isPlaying", Toast.LENGTH_SHORT).show();
        } else {
            if (mMediaPlayer!= null){
                stop();
            }



            //Toast.makeText(context, "IF !Playing", Toast.LENGTH_SHORT).show();
        }
    }

    private void stop(){

        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
           // mMediaPlayer.release();
        }
        isPlaying =false;
        mPlayButton.setImageResource(R.drawable.play_sound);

    }

    private void play() {
        try {
            mPlayButton.setImageResource(R.drawable.stop_playing);
            mMediaPlayer.setDataSource(voiceMessage.getUrl());
            mMediaPlayer.prepareAsync();
          //  mMediaPlayer.start();
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
                // mPlayButton.setImageResource(R.drawable.play_sound);
            }
        });
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



}
