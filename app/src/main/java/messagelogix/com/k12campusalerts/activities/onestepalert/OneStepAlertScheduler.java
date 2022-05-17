package messagelogix.com.k12campusalerts.activities.onestepalert;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.CampaignStatusActivity;
import messagelogix.com.k12campusalerts.activities.CustomTimePickerDialog;
import messagelogix.com.k12campusalerts.activities.HomeActivity;
import messagelogix.com.k12campusalerts.models.BasicResponse;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OneStepAlertScheduler extends AppCompatActivity {
//    private Context context = this;
    private TextView timeText;
    private TextView dateText;
    private String senderName;
    private String subject;
    private String message;
    private ArrayList<Integer> alertTypes; //array list of alert types
    private String soundURL;
    private final Calendar calendar = Calendar.getInstance();
    private Handler handler = new Handler();
    private String lists;
    private User mUser;

    private ProgressDialog mDialog;
    private String msgType;
    private String campId;
    private String commType;
    private String fileName;
    private boolean isPlaying =false;
    private MediaPlayer mMediaPlayer;
    private ImageButton playButton;
    private final int bufferTime = 5;

    private CustomTimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private TimePickerDialog.OnTimeSetListener onSetTimePickerListener;
    private DialogInterface.OnShowListener onShowTimePickerListener;
    private TimePicker.OnTimeChangedListener onChangedTimePickerListener;
    private com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener onTimeSetListener;

    private Date minDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_schedule);

        //retrieving data from past activity
        mUser = (User) new FunctionHelper(this).retrieveObject(User.class);
        alertTypes = getIntent().getExtras().getIntegerArrayList("alertTypes");
        senderName = getIntent().getExtras().getString("senderName");
        subject = getIntent().getExtras().getString("subject");
        message = getIntent().getExtras().getString("message");
        lists = getIntent().getExtras().getString("lists");
        msgType = getIntent().getExtras().getString("msgtype");
        campId = getIntent().getExtras().getString("camp_id");
        commType = getIntent().getExtras().getString("commType");
        fileName = getIntent().getExtras().getString("sound");
        soundURL = getIntent().getExtras().getString("soundURL");

        Log.d("sound","URL on schedule page: "+soundURL);

        TextView messageEditText = (TextView) findViewById(R.id.scheduleMessage);
        messageEditText.setMovementMethod(new ScrollingMovementMethod());
        messageEditText.setText(message); //setting message in the text field
//        messageEditText.setEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopping tts if the sendbutton is pressed
                if(isPlaying)
                    stop();

                showConfirmSendDialog();
            }
        });

        updateAlertTypes();
        initializePlayButton();
        initializeDateAndTimePicker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCounting();
    }

    private void updateAlertTypes(){
        ToggleButton voiceToggle = (ToggleButton) findViewById(R.id.voiceToggle);
        ToggleButton textToggle = (ToggleButton) findViewById(R.id.textToggle);
        ToggleButton emailToggle = (ToggleButton) findViewById(R.id.emailToggle);

        if(alertTypes.contains(1))
            emailToggle.setChecked(true);

        if(alertTypes.contains(2))
            textToggle.setChecked(true);

        if(alertTypes.contains(3))
            voiceToggle.setChecked(true);
    }

    private void initializePlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
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
    }

    private void initializePlayButton() {
        ToggleButton voiceToggle = (ToggleButton) findViewById(R.id.voiceToggle);
        playButton = (ImageButton) findViewById(R.id.playButton);
        //hiding the play button if voice not selected

        if(voiceToggle.isChecked()) {
            initializePlayer();
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playOrStop();
                }
            });
        } else {
            playButton.setVisibility(View.GONE);
        }
    }

    private String hasTimeElapsed(Date finalSendCal){
        Log.d("TimePicker","hasTimeElapsed --> finalSendCal is: "+finalSendCal);
        return new Date().after(finalSendCal) ? "1" : "0";
    }

    private void stop(){
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        playButton.setImageResource(R.drawable.play_sound);
        isPlaying = false;
        //mMediaPlayer.release();
    }

    private void play() {
        try {
            mMediaPlayer.setDataSource(soundURL);
            playButton.setImageResource(R.drawable.stop_playing);
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

    private void playOrStop(){
        if(isPlaying){
            stop();
        } else {
            play();
            isPlaying =true;
        }
    }

    private void showConfirmSendDialog(){
        Calendar tempCal = Calendar.getInstance();
        Date tempfinalSendCal = calendar.getTime();
        tempCal.setTime(tempfinalSendCal);
        tempCal.set(Calendar.SECOND,0);

        final Date finalSendCal = tempCal.getTime();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String scheduledDate = new SimpleDateFormat( "MM/dd/yy" , Locale.getDefault()).format( calendar.getTime());
        String scheduledTime = new SimpleDateFormat( "hh:mm aa" , Locale.getDefault()).format( calendar.getTime());
        builder.setTitle("Confirm Your Alert");
        String alertTypeString = generateAlertTypeString();
        builder.setMessage("Scheduled Date: "+ scheduledDate+ " - "+scheduledTime +"\n"+"Message: "+message+"\n"+"Sending to Lists: "+ lists+"\n"+"Sending As: "+alertTypeString);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //send alert here
                scheduleOneStepAlert(finalSendCal);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String generateAlertTypeString() {
        String alertTypesString="";
        if(alertTypes.contains(1))
            alertTypesString+="Email ";

        if(alertTypes.contains(2))
            alertTypesString+="Text ";

        if(alertTypes.contains(3))
            alertTypesString+="Voice ";

        alertTypesString = alertTypesString.replaceAll("\\s", ", ");
        return alertTypesString.substring(0,alertTypesString.length()-2);
    }

    private void scheduleOneStepAlert(final Date finalSendCal) {
        showProgress(true);

        OneStepAlertActivity.OneStepAlertService client = ServiceGenerator.createService(OneStepAlertActivity.OneStepAlertService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "OneStepAlert");
        params.put("action", "SendMessageV2");

        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());
        params.put("email", mUser.getData().getDfltEmail());
        params.put("lists", lists);
        params.put("sender_name", senderName);
        params.put("subject", subject);
        params.put("message", message);
        params.put("msgtype", msgType);
        params.put("camp_id", campId);
        params.put("commType", commType);

        String scheduledDate = new SimpleDateFormat( "MM/dd/yyyy hh:mm:ss aa", Locale.getDefault()).format(finalSendCal.getTime());
        params.put("senddate", scheduledDate);

        params.put("sound", fileName);
        params.put("send_imd", hasTimeElapsed(finalSendCal));

        Log.d("TimePicker","When executing data request, params are --> "+params);
        //showProgress(false);
        //showSuccessfulAlertDialog();

        Call<BasicResponse> call = client.request(params);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Response<BasicResponse> response) {
                showProgress(false);

                Log.d("OSAScheduleAct","scheduleOneStepAlert() --> response: "+response);

                if (response.isSuccess()) {
                    BasicResponse apiResponse = response.body();
                    Log.d("response",response.isSuccess()+" "+response.body().getData()+" "+ apiResponse.getSuccess());

                    if (apiResponse.getSuccess()) {
                        showSuccessfulAlertDialog();
                    }

                } else
                    showResponseError();
            }

            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                showResponseError();
                Log.e("Error", "Errormessage" + t.getMessage());
            }
        });
    }

    private void showResponseError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your alert could not be sent at this time. Please try again.");
        builder.setTitle("Failed to Send your Alert");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showProgress(boolean show){
        if(show){
            if (mDialog == null) {
                mDialog = new ProgressDialog(this);
                mDialog.setMessage("Processing your alert...");
                mDialog.setIndeterminate(true);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
            }
            mDialog.show();
        } else {
            if (mDialog != null && mDialog.isShowing()) {
                //mDialog.cancel();
                mDialog.dismiss();
            }
        }
    }

    private void initializeDateAndTimePicker(){ //setting up the date and time pickers
        final Context that = this;
        LinearLayout dateButton = (LinearLayout) findViewById(R.id.dateLayout);
        final LinearLayout timeButton = (LinearLayout) findViewById(R.id.timeLayout);
        //temp local calendar object
        final Calendar calTracker = Calendar.getInstance();
        calTracker.set(Calendar.SECOND, 0);
        //calTracker.setTime(minDate);

        onDateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
            //setting values for the temp calendar
            calTracker.set(Calendar.YEAR, year);
            calTracker.set(Calendar.MONTH, monthOfYear);
            calTracker.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//            Log.d("TimePicker","inside DatePicker - onDateSet --> minDate obj is: "+minDate);
//            Log.d("TimePicker","inside DatePicker - onDateSet --> calendar date was originally: "+calendar.getTime());

            if(calTracker.getTime().after(minDate)) {
                //setting values for the year, month and day
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            } else {
                calendar.setTime(minDate);
            }

//            Log.d("TimePicker","inside DatePicker - onDateSet --> calendar date is now: "+calendar.getTime());

            updateDateTextView();
        };

        dateButton.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(that, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.setCancelable(true);
            datePickerDialog.setCanceledOnTouchOutside(true);
            datePickerDialog.show();
        });

        onTimeSetListener = (view, selectedHour, selectedMinute, second) -> {
            calTracker.set(Calendar.HOUR_OF_DAY, selectedHour);
            calTracker.set(Calendar.MINUTE, selectedMinute);

//            Log.d("TimePicker","onTimeSet() --> selectedHour is: "+selectedHour+" --- selectedMinute is: "+selectedMinute);
//            Log.d("TimePicker","inside TimePicker - onTimeSet() --> minDate obj is: "+minDate);
//            Log.d("TimePicker","onTimeSet() --> calTrakcer obj is: "+calTracker.getTime().toString());
            if(calTracker.getTime().after(minDate)){
//                Log.d("TimePicker","onTimeSet() --> calTracker is greater than minDate, updating calendar");
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute/* * 5*/);
            } else {
//                Log.d("TimePicker","onTimeSet() --> calTracker is being set equal to minDate");
                calendar.setTime(minDate);
            }

            updateTimeTextView();

//            Log.d("TimePicker","inside TimePicker - onTimeSet --> after update logic, cal obj is now: "+calendar.getTime());
        };

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog  newTimePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        onTimeSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        false
                );
                newTimePickerDialog.setTimeInterval(1,5,1);

                Calendar minDateCalendar = Calendar.getInstance();
                minDateCalendar.setTime(minDate);

                boolean isSameDate = (calendar.get(Calendar.YEAR) == minDateCalendar.get(Calendar.YEAR)) && (calendar.get(Calendar.MONTH) == minDateCalendar.get(Calendar.MONTH)) && (calendar.get(Calendar.DAY_OF_MONTH) == minDateCalendar.get(Calendar.DAY_OF_MONTH));

                if(!isSameDate/* && calTracker.getTime().after(minDate)*/) {
//                    Log.d("TimePicker","not same date - inside if block");
                    newTimePickerDialog.setInitialSelection(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
                }
                else if(calendar.getTime().after(minDate)) {
//                    Log.d("TimePicker","same date - inside else-if block");
//                    Log.d("TimePicker","inside TimePicker minCalendar: " + minDateCalendar.get(Calendar.HOUR)+" : "+ minDateCalendar.get(Calendar.MINUTE) +" : "+minDateCalendar.get(Calendar.SECOND));
//                    Log.d("TimePicker","inside TimePicker calendar object: " + calendar.get(Calendar.HOUR)+" : "+ calendar.get(Calendar.MINUTE) +" : "+calendar.get(Calendar.SECOND));
                    newTimePickerDialog.setMinTime(minDateCalendar.get(Calendar.HOUR_OF_DAY), minDateCalendar.get(Calendar.MINUTE), minDateCalendar.get(Calendar.SECOND));
                    newTimePickerDialog.setInitialSelection(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
                } else {
//                    Log.d("TimePicker","same date - inside else block");
                    Calendar minCalendar = Calendar.getInstance();
                    minCalendar.setTime(minDate);
//                    Log.d("TimePicker","inside TimePicker -" + minCalendar.get(Calendar.HOUR)+" : "+ minCalendar.get(Calendar.MINUTE) +" : "+minCalendar.get(Calendar.SECOND));
                    newTimePickerDialog.setMinTime(minCalendar.get(Calendar.HOUR_OF_DAY), minCalendar.get(Calendar.MINUTE), minCalendar.get(Calendar.SECOND));
                }

                newTimePickerDialog.show(getSupportFragmentManager(),"TimePickerDialog");
            }
        });
    }

    private boolean calcDateDiff(long timeStamp1, long timeStamp2) {
        long diff = timeStamp1 - timeStamp2;

        // Calculate difference in seconds
        long diffSeconds = diff / 1000;

        // Calculate difference in minutes
        long diffMinutes = diff / (60 * 1000);

        // Calculate difference in hours
        //long diffHours = diff / (60 * 60 * 1000);

        // Calculate difference in days
        //long diffDays = diff / (24 * 60 * 60 * 1000);

        Log.d("DateDiff","Difference in milliseconds: " + diff + " milliseconds.");
        Log.d("DateDiff","Difference in seconds: " + diffSeconds + " seconds.");
        Log.d("DateDiff","Difference in minutes: " + diffMinutes + " minutes.");

        if(diffSeconds < 0)
            return (diffSeconds / 60) > -5;
        else
            return true;
    }

    private void updateTimeTextView(){
        timeText.setText( new SimpleDateFormat( "hh:mm aa", Locale.getDefault()).format( calendar.getTime()));
    }

    private void updateDateTextView(){
        dateText.setText( new SimpleDateFormat( "MM/dd/yy", Locale.getDefault()).format( calendar.getTime()));
    }

    private void showSuccessfulAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Successfully Scheduled");
        builder.setMessage("Would you like to review the status of your recently scheduled alert?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OneStepAlertScheduler.this, CampaignStatusActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OneStepAlertScheduler.this, HomeActivity.class);
                startActivity(intent);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void startCounting() {
        timeText = (TextView) findViewById(R.id.timeTextView) ;
        dateText =  (TextView) findViewById(R.id.dateTextView);
        handler.post(timeTicker);
    }

    private final Runnable timeTicker = new Runnable() {
        @Override
        public void run() {
            Date currentTime = Calendar.getInstance().getTime();
            //getting the current time setting the cur
            currentTime.setTime(currentTime.getTime()+(bufferTime * 60000));
            minDate = currentTime;

            Date timeDisplayedByPicker = calendar.getTime();
            if(minDate.after(timeDisplayedByPicker) || minDate.equals(timeDisplayedByPicker)) {
                calendar.setTime(minDate);
                updateTimeTextView();
                updateDateTextView();
            }

            handler.postDelayed(this,  1000);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(timeTicker);
        if(isPlaying)
            stop();
    }

    private int returnBufferedMinutes(int minutes) {
        int timeSlot = minutes/5;
        if(timeSlot == 12)
            return 0;
        else
            return timeSlot;
    }
}
