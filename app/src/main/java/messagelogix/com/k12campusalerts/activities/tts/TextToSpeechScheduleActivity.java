package messagelogix.com.k12campusalerts.activities.tts;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.activities.CampaignStatusActivity;
import messagelogix.com.k12campusalerts.activities.HomeActivity;
import messagelogix.com.k12campusalerts.models.BasicResponse;
import messagelogix.com.k12campusalerts.models.GroupList;
import messagelogix.com.k12campusalerts.models.TTSPreviewResponse;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class TextToSpeechScheduleActivity  extends AppCompatActivity{

    private static final String LOG_TAG = TextToSpeechScheduleActivity.class.getSimpleName();

    private final Context context = this;

	private static String dateToSend;
	private static String timeToSend;

	public static Button mDateButton;
	public static Button mTimeButton;
    private Button mListButton;
    private ImageButton mPlayButton;

	private MediaPlayer mMediaPlayer;
    private User mUser;

    private Integer[] selectedIndices = new Integer[]{};
    private List<String> selectedListIds = new ArrayList<>();
    private List<GroupList.Data> groupLists;

    private Toast mToast;
    private ProgressDialog mDialog;

    private boolean isPlaying =false;

    public interface TTSScheduleService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<BasicResponse> request(@FieldMap HashMap<String, String> parameters);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech_schedule);

		mUser = (User) new FunctionHelper(this).retrieveObject(User.class);

		setTitle(getString(R.string.tts_schedule));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPlayButton =(ImageButton)findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrStop();
            }
        });

        mTimeButton =(Button)findViewById(R.id.timeButton);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        mDateButton =(Button)findViewById(R.id.dateButton);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        mListButton =(Button)findViewById(R.id.listButton);
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sowListDialog();
            }
        });

        Button mCancelButton = (Button) findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        Button mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                    send();
            }
        });

        downloadUserList();
        initializePlayer();
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {


            super.onBackPressed();
            // overridePendingTransition(R.anim.comming_in, R.anim.comming_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initializePlayer() {
        try {
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    stop();
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void playOrStop(){
        if(isPlaying){
            stop();
        }else{
            play();
            isPlaying =true;
        }
    }

    private void stop(){

        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mPlayButton.setImageResource(R.drawable.play_sound);
        isPlaying = false;
        //mMediaPlayer.release();
    }

    private void play() {
        try {
            mMediaPlayer.setDataSource(getSoundUrl());
            mPlayButton.setImageResource(R.drawable.stop_playing);
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void showProgress(boolean show){
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

    private void sowListDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.select_list_s)
                .items(groupLists)
                .itemsCallbackMultiChoice(selectedIndices, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] lists, CharSequence[] text) {
                        selectedIndices = lists;
                        selectedListIds.clear();
                        StringBuilder str = new StringBuilder();
                        for (int i = 0; i < lists.length; i++) {
                            if (i > 0) str.append('\n');
                            str.append(lists[i]);
                            str.append(": ");
                            str.append(text[i]);
                            selectedListIds.add(groupLists.get(lists[i]).getLGroupId());
                        }
                        showToast(str.toString());
                        mListButton.setText("Selected lists: " + TextUtils.join(", ", selectedListIds) );
                        return true; // allow selection
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback(){
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //startActivity(new Intent(TextToSpeechScheduleActivity.this, HomeActivity.class));
                        dialog.dismiss();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.clearSelectedIndices();
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .positiveText(R.string.md_done_label)
                .autoDismiss(false)
                .neutralText(R.string.clear_selection)
                .show();
    }

    private boolean validate() {
        mTimeButton.setError(null);
        mDateButton.setError(null);
        mListButton.setError(null);

        if (timeToSend == null) {
            mTimeButton.setError(getString(R.string.error_field_required));
            mTimeButton.requestFocus();
            return false;
        }

        if (dateToSend == null) {
            mDateButton.setError(getString(R.string.error_field_required));
            mDateButton.requestFocus();
            return false;
        }

        if(selectedListIds.size() == 0){
            mListButton.setError(getString(R.string.error_field_required_list));
            mListButton.requestFocus();
            return false;
        }

        return true;
    }

    private void send() {
        showProgress(true);
        TTSScheduleService client = ServiceGenerator.createService(TTSScheduleService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "TextToSpeech");
        params.put("action", "ScheduleTTS");
        params.put("account", mUser.getData().getAccount());
        params.put("pin", mUser.getData().getPin());
        params.put("date", dateToSend);
        params.put("time", timeToSend);
        params.put("lists", TextUtils.join(", ", selectedListIds));
        params.put("fileName", this.getFileName());

        Call<BasicResponse> call = client.request(params);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Response<BasicResponse> response) {
                Log.d("TextToSpeechSchedule","send() --> response: "+response.body().getResponseObj());

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
                                    Intent intent = new Intent(TextToSpeechScheduleActivity.this, CampaignStatusActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            })
                            .neutralText("No")
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(TextToSpeechScheduleActivity.this, HomeActivity.class);
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

    private void cancel() {
//        Intent intent = new Intent(context, HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        super.onBackPressed();
    }

    private String getSoundUrl() {
        return  getIntent().getStringExtra(TTSPreviewResponse.URL);
    }

    private String getFileName() {
        return getIntent().getStringExtra(TTSPreviewResponse.FILENAME);
    }

    private void downloadUserList() {
        BaseActivity.UserGroupListService client = ServiceGenerator.createService(BaseActivity.UserGroupListService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "User");
        params.put("action", "GetUserList");
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());

        Call<GroupList> call = client.request(params);
        call.enqueue(new Callback<GroupList>() {
            @Override
            public void onResponse(Response<GroupList> response) {

                Log.d("TextToSpeechSchedule","downloadUserList() --> response: "+response.body().getResponseObj());

                if (response.isSuccess()) {
                    GroupList lists = response.body();
                    if (lists.getSuccess()) {
                        groupLists = lists.getData();
                        mListButton.setVisibility(View.VISIBLE);
                    }else{
                        mListButton.setAlpha(.5f);
                        mListButton.setText("Lists unavailable");
                        mListButton.setEnabled(false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "message =" + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

	public static class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@SuppressLint("SimpleDateFormat")
		public void onDateSet(DatePicker view, int year, int month, int day) {
			// create time string
			month++;
			String timeString = month +"/"+day+"/"+year;

			//create formats
			SimpleDateFormat androidDateFormat = new SimpleDateFormat("M/d/yyyy");
			SimpleDateFormat severDateFormat = new SimpleDateFormat("MMddyy");
			SimpleDateFormat humanDateFormat = new SimpleDateFormat("MM/dd/yyyy");



			//Convert the string to a time
			Date date = new Date();

			try
			{
				date = androidDateFormat.parse(timeString);

				System.out.println("date : "+androidDateFormat.format(date));
			}
			catch (ParseException ex)
			{
				System.out.println("Exception "+ex);
			}


			//back to a string
			dateToSend = severDateFormat.format(date);
			Log.d(LOG_TAG, dateToSend);


			mDateButton.setText("Scheduled date: " + humanDateFormat.format(date));


		}


	}

	public static class TimePickerFragment extends DialogFragment
	implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}


		@SuppressLint("SimpleDateFormat")
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// create time string
			String timeString = hourOfDay + ":" + minute;

			//create formats
			SimpleDateFormat androidTimeFormat = new SimpleDateFormat("H:m");
			SimpleDateFormat serverTimeFormat = new SimpleDateFormat("HHmm");
			SimpleDateFormat humanTimeFormat = new SimpleDateFormat("hh:mm a");

			//Convert the string to a time
			Date date = new Date();

			try
			{
				date = androidTimeFormat.parse(timeString);
				System.out.println("date : "+androidTimeFormat.format(date));
			}
			catch (ParseException ex)
			{
				System.out.println("Exception "+ex);
			}


			//back to a string
			timeToSend = serverTimeFormat.format(date);        
			mTimeButton.setText("Scheduled time: " + humanTimeFormat.format(date));
		}
	}
}
