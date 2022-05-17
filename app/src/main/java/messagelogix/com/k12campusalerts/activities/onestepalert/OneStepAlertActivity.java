package messagelogix.com.k12campusalerts.activities.onestepalert;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
//import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.activities.tts.TextToSpeechActivity;
import messagelogix.com.k12campusalerts.fragments.ListFragment;
import messagelogix.com.k12campusalerts.fragments.item.ListContent;
import messagelogix.com.k12campusalerts.models.BasicResponse;
import messagelogix.com.k12campusalerts.models.CannedMessage;
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

public class OneStepAlertActivity extends BaseActivity implements ListFragment.OnListFragmentInteractionListener {
    private static final String LOG_TAG = OneStepAlertActivity.class.getSimpleName();

    private EditText senderNameEditText;
    private EditText subjectEditText;
    private EditText messageEditText;

    private ArrayList<Integer> alertTypes = new ArrayList<>(Arrays.asList(0,0,0)); //creating list to store alert types

    private Button messageButton;

    private Button scheduleButton;
    public  Context context = this;
    private Button sendButton;
    private Button listButton;
    private CannedMessage messages;

//    private Toast mToast;
    private List<GroupList.Data> groupLists;

    private Integer[] selectedIndices = new Integer[]{};
    private List<String> selectedListIds = new ArrayList<>();

    private ProgressDialog previewProgressDialog;
    private ProgressDialog alertProgressDialog;

    private ImageButton playButton;
    private CheckBox checkboxEmail;
    private CheckBox checkboxText;
    private CheckBox checkboxVoice;
    private String previousMessage="";
    private boolean isPlaying =false;
    private MediaPlayer mMediaPlayer;
    private String soundURL;
    private String fileName;
    
    @Override
    public void onListFragmentInteraction(ListContent.ListItem item) {

    }

    public interface OneStepAlertService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<BasicResponse> request(@FieldMap HashMap<String, String> parameters);
    }

    public interface CannedMessageService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<CannedMessage> request(@FieldMap HashMap<String, String> parameters);
    }

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mUser = (User) new FunctionHelper(context).retrieveObject(User.class);

        /*
         * Adding our layout to parent class frame layout.
         */
        getLayoutInflater().inflate(R.layout.activity_one_step_alert, frameLayout);

        /*
         * Setting title and itemChecked
         */
        mDrawerList.setItemChecked(position, true);
        setTitle(navDrawerItems.get(position).getTitle());
        playButton = (ImageButton) findViewById(R.id.playButton);
        senderNameEditText = (EditText) findViewById(R.id.senderNameEditText);
        subjectEditText = (EditText) findViewById(R.id.subjectEditText);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        scheduleButton = (Button) findViewById(R.id.scheduleButton);
        messageButton = (Button) findViewById(R.id.chooseMessageButton);
        listButton = (Button) findViewById(R.id.listButton);
        checkboxText = (CheckBox) findViewById(R.id.checkBox2);
        checkboxVoice = (CheckBox)findViewById(R.id.checkBox3);
        senderNameEditText.setFilters(new InputFilter[]{EMOJI_FILTER, new InputFilter.LengthFilter(100)});
        subjectEditText.setFilters(new InputFilter[]{EMOJI_FILTER, new InputFilter.LengthFilter(100)});
        messageEditText.setFilters(new InputFilter[]{EMOJI_FILTER,new InputFilter.LengthFilter(300)});
        messageEditText.setMovementMethod(new ScrollingMovementMethod());

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying) {
                    stop();
                }

                //verifying all the fields are filled
                if(validateFields()){
                    //checking if the voice checkbox is checked
                    if (checkboxVoice.isChecked() && wasNewMessageTyped()){
                        //generate new TTS message and send it over to the schedule activity
                        generateTTS(false);
                    } else {
                        //this code will run if voice option not selected
                        scheduleOneStepAlert();
                    }
                }
            }
        });

        messageEditText.setHorizontallyScrolling(false);

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying) {
                    stop();
                }

                //verify all the fields are filled
                if (validateFields()){
                    showConfirmSendDialog();
                }
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectList();
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMessage();
            }
        });

        checkboxEmail = (CheckBox) findViewById(R.id.checkBox1); // setting the alert type button listeners
        Log.d("alertscheck", checkboxEmail.isChecked()+"test");

        findViewById(R.id.emailAlertLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkboxEmail.isChecked()){
                    checkboxEmail.setChecked(false);
                    alertTypes.set(0,0);
                } else {
                    alertTypes.set(0,1);
                    checkboxEmail.setChecked(true);
                }
                Log.d("alertscheck", alertTypes.toString());
                enableSendAndSchedule();
            }
        });

        findViewById(R.id.textAlertLayout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkboxText.isChecked()){
                    checkboxText.setChecked(false);
                    alertTypes.set(1,0);
                } else {
                    checkboxText.setChecked(true);
                    alertTypes.set(1,2);
                }
                Log.d("alerts", alertTypes.toString());

                enableSendAndSchedule();
            }
        });

        findViewById(R.id.voiceAlertPreview).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (checkboxVoice.isChecked()){
                    checkboxVoice.setChecked(false);
                    alertTypes.set(2,0);
                    Log.d("alerts", alertTypes.toString());
                    playButton.setClickable(false); //enabling the play button to be clicked when voice is selected
                    playButton.setAlpha(.5f);
                } else {
                    checkboxVoice.setChecked(true);
                    alertTypes.set(2,3);
                    Log.d("alerts", alertTypes.toString());
                    playButton.setClickable(true); //disabling the play button to be clicked when voice is selected
                    playButton.setAlpha(1.0f);
                }

                enableSendAndSchedule();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    stop();
                } else {
                    String currMessage = messageEditText.getText().toString().trim();
                    Log.d("sound",currMessage);

                    if(currMessage.isEmpty()){
                        showNoMessageError();
                    } else if(wasNewMessageTyped()){
                        previewMessage();
                    } else {
                        playOrStop();
                    }
                }
            }
        });

        sendButton.getBackground().setAlpha(255/2); //setting the opacity of the schedule and send button
        scheduleButton.getBackground().setAlpha(255/2);

        scheduleButton.setClickable(false); //disabling the schedule and send buttons
        sendButton.setClickable(false);
        playButton.setClickable(false); //disabling and dimming the play button
        playButton.setAlpha(.5f);

        downloadCannedMessages();
        downloadUserList();
        initializePlayer();
    }

    private void showConfirmSendDialog(){
        String lists = TextUtils.join(", ", selectedListIds);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Your Alert");
        String alertTypesString="";
        if(alertTypes.contains(1))
            alertTypesString+="Email ";
        if(alertTypes.contains(2))
            alertTypesString+="Text ";
        if(alertTypes.contains(3))
            alertTypesString+="Voice ";

        alertTypesString = alertTypesString.replaceAll("\\s", ", ");
        alertTypesString = alertTypesString.substring(0,alertTypesString.length()-2);

        String message = FunctionHelper.convertToASCII(messageEditText.getText().toString());
        builder.setMessage("Message: "+message+"\nSending to Lists: "+ lists+"\n"+"Sending As: "+alertTypesString);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //send alert here
                if (checkboxVoice.isChecked() && wasNewMessageTyped()) {
                    //generate new URL and send alert
                    generateTTS(true);
                } else
                    sendOneStepAlert();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //this will check to see if there is an existing mediafile or not. This will also check to see if the text changed in the message box
    private boolean wasNewMessageTyped(){
        Log.d("wasNewMessageTyped","before removing whitespaces and newline characters --> @"+messageEditText.getText().toString());
        Log.d("wasNewMessageTyped","length of message --> @"+messageEditText.getText().toString().length());
        String currMessage = messageEditText.getText().toString().trim();//.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");
        String prevMessage = previousMessage.trim();//.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");

        boolean newMessageEntered = !currMessage.equals(prevMessage);
        Log.d("wasNewMessageTyped","after removing whitespaces and newline characters --> @"+currMessage);
        Log.d("wasNewMessageTyped","length of new message --> @"+currMessage.length());
        return newMessageEntered;
    }

    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }

            return null;
        }
    };

    private boolean checkVoiceAndOtherAlert(){//this will check if the voice option has at least one other alter type selected with it
        return checkboxEmail.isChecked() || checkboxText.isChecked();

        // else {
//            showVoiceAndOtherAlertError();
//            return false;
//        }
    }

    private void showVoiceAndOtherAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You must select at least one other mode of communication when sending an alert as a voice message.");
        builder.setTitle("Additional Mode of Communication Required");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNoMessageError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You must enter a message");
        builder.setTitle("Message is missing");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showResponseError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your alert could not be sent at this time. Please try again.");
        builder.setTitle("Failed to send your alert");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void enableSendAndSchedule(){
        Log.d("changing","here");
        if(checkboxEmail.isChecked() || checkboxText.isChecked() || checkboxVoice.isChecked()){
            Log.d("changing","true");
            scheduleButton.setClickable(true);
            sendButton.setClickable(true);
            sendButton.getBackground().setAlpha(255);
            scheduleButton.getBackground().setAlpha(255);
        } else {
            Log.d("changing","false");
            sendButton.setClickable(false);
            scheduleButton.setClickable(false);
            sendButton.getBackground().setAlpha(255/2);
            scheduleButton.getBackground().setAlpha(255/2);
        }
    }

    private void showProgressForPreview(boolean show) {
        if(show) {
            if (previewProgressDialog == null) {
                previewProgressDialog = new ProgressDialog(this);
                previewProgressDialog.setMessage("Preparing your voice message preview...");
                previewProgressDialog.setIndeterminate(true);
                previewProgressDialog.setCancelable(false);
                previewProgressDialog.setCanceledOnTouchOutside(false);
            }
            previewProgressDialog.show();
        } else {
            if(previewProgressDialog != null && previewProgressDialog.isShowing()) {
                previewProgressDialog.dismiss();
            }
        }
    }

    private void manageProgressForAlert(int scenario, boolean show) {
        if(show) {
            if(alertProgressDialog == null) {
                alertProgressDialog = new ProgressDialog(this);
                alertProgressDialog.setIndeterminate(true);
                alertProgressDialog.setCancelable(false);
                alertProgressDialog.setCanceledOnTouchOutside(false);
            }

            if(scenario == 1) {
                alertProgressDialog.setMessage("Preparing your voice preview...");
            } else {
                alertProgressDialog.setMessage("Processing your alert...");
            }

            if(!alertProgressDialog.isShowing())
                alertProgressDialog.show();

        } else {
            if(alertProgressDialog != null && alertProgressDialog.isShowing()) {
                alertProgressDialog.dismiss();
            }
        }
    }

    private void playOrStop(){
        if(isPlaying){
            stop();
        } else {
            play();
            isPlaying = true;
        }
    }

    private void stop(){
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        playButton.setImageResource(R.drawable.play_sound);
        isPlaying = false;
        //mMediaPlayer.release();
    }

    private void generateTTS(final boolean sendRightAway){
        manageProgressForAlert(1,true);

        TextToSpeechActivity.PreviewTTSMessageService client = ServiceGenerator.createService(TextToSpeechActivity.PreviewTTSMessageService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();

        params.put("controller", "TextToSpeech");
        params.put("action", "PreviewVoice");
        params.put("account", mUser.getData().getAccount());
        params.put("pin", mUser.getData().getPin());
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());

        String message = FunctionHelper.convertToASCII(messageEditText.getText().toString());
        params.put("ttv_message", message);

        params.put("voice", "M");
        params.put("ttv_id", "0");

        Call<TTSPreviewResponse> call = client.request(params);
        call.enqueue(new Callback<TTSPreviewResponse>() {
            @Override
            public void onResponse(Response<TTSPreviewResponse> response) {
                TTSPreviewResponse responseData  = response.body();
                Log.d("OneStepAlertActivity","generateTTS() --> response: "+response.body().getResponseObj());
                if (response.isSuccess() && responseData.getSuccess()) {
                    Log.d("sound","in generate send");
                    previousMessage = messageEditText.getText().toString();
                    fileName = responseData.getData().getFilename();
                    soundURL = responseData.getData().getUrl();

                    if(sendRightAway) {
                        sendOneStepAlert();
                    } else {
                        manageProgressForAlert(1,false);
                        scheduleOneStepAlert();
                    }
                } else {
                    manageProgressForAlert(1,false);
                    showPreviewError();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                manageProgressForAlert(1,false);
                showPreviewError();
                Log.e(LOG_TAG, "message =" + t.getMessage());
            }
        });
    }

    private void previewMessage() {
        showProgressForPreview(true);
        TextToSpeechActivity.PreviewTTSMessageService client = ServiceGenerator.createService(TextToSpeechActivity.PreviewTTSMessageService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "TextToSpeech");
        params.put("action", "PreviewVoice");
        params.put("account", mUser.getData().getAccount());
        params.put("pin", mUser.getData().getPin());
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());

        String message = FunctionHelper.convertToASCII(messageEditText.getText().toString());
        params.put("ttv_message", message);

        params.put("voice", "M");
        params.put("ttv_id", "0");

        Call<TTSPreviewResponse> call = client.request(params);
        call.enqueue(new Callback<TTSPreviewResponse>() {
            @Override
            public void onResponse(Response<TTSPreviewResponse> response) {
                showProgressForPreview(false);

                Log.d("OneStepAlertActivity","previewMessage() --> response: "+response.body().getResponseObj());
                TTSPreviewResponse responseData  = response.body();
                if (response.isSuccess() && responseData.getSuccess()) {
                    previousMessage = messageEditText.getText().toString();
                    fileName = responseData.getData().getFilename();
                    soundURL = responseData.getData().getUrl();
                    Log.d("sound",soundURL);
                    playOrStop();
                } else
                    showPreviewError();
            }

            @Override
            public void onFailure(Throwable t) {
                showProgressForPreview(false);
                showPreviewError();
                Log.e(LOG_TAG, "message =" + t.getMessage());
            }
        });
    }

    private void play() {
        try {
            mMediaPlayer.setDataSource(soundURL);
            playButton.setImageResource(R.drawable.stop_playing);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializePlayer() {
        try {
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

        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showPreviewError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Failed to generate a preview of your voice message. Please try again.");
        builder.setTitle("Error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    private void showToast(String message) {
//        if (mToast != null) {
//            mToast.cancel();
//            mToast = null;
//        }
//
//        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
//        mToast.show();
//    }

    private void downloadUserList() {
        UserGroupListService client = ServiceGenerator.createService(UserGroupListService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "User");
        params.put("action", "GetUserList");
        params.put("accountId", mUser.getData().getAcctId());
        params.put("pinId", mUser.getData().getPinId());

        Call<GroupList> call = client.request(params);
        call.enqueue(new Callback<GroupList>() {
            @Override
            public void onResponse(Response<GroupList> response) {
                Log.d("OneStepAlertActivity","downloadUserList() --> parameteres are: accountId: "+mUser.getData().getAcctId()+"\npinId: "+mUser.getData().getPinId());
                Log.d("OneStepAlertActivity","downloadUserList() --> response: "+response.body().getResponseObj());

                if(response.isSuccess()) {
                    GroupList lists = response.body();
                    if(lists.getSuccess()) {
                        groupLists = lists.getData();

                        listButton.setText("Select List(s)");
                        listButton.setAlpha(1.0f);
                        listButton.setEnabled(true);
                    } else {
                        listButton.setText("Lists unavailable");
                    }
                } else {
                    listButton.setText("Lists unavailable");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listButton.setText("Lists unavailable");
            }
        });
    }

    private void downloadCannedMessages() {
        CannedMessageService client = ServiceGenerator.createService(CannedMessageService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "OneStepAlert");
        params.put("action", "GetMessages");
        params.put("accountId", mUser.getData().getAcctId());

        Call<CannedMessage> call = client.request(params);
        call.enqueue(new Callback<CannedMessage>() {
            @Override
            public void onResponse(Response<CannedMessage> response) {
                Log.d("OneStepAlertActivity","downloadCannedMessages() --> response: "+response.body().getResponseObj());
                if (response.isSuccess()) {
                    messages = response.body();
                    Log.d("CANNED","list of messages are: "+messages.getData().get(0).getVal());
                    Log.d("CANNED","list of messages are: "+messages.getData().get(1).getVal());
                    Log.d("CANNED","list of messages are: "+messages.getData().get(2).getVal());
                    if (messages.getSuccess()) {
                        messageButton.setText("Choose a canned message");
                        messageButton.setAlpha(1.0f);
                        messageButton.setEnabled(true);
                    } else {
                        messageButton.setText("Canned messages unavailable");
                    }
                } else {
                    messageButton.setText("Canned messages unavailable");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                messageButton.setText("Canned messages unavailable");
            }
        });
    }

    private void chooseMessage() {
        new MaterialDialog.Builder(this)
                .title(R.string.choose_message)
                .items(messages.getData())
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        messageEditText.setText(text);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback(){
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .positiveText(R.string.md_done_label)
                .show();
    }

    private void selectList() {
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
                        //showToast(str.toString());
                        Log.d(LOG_TAG, "lists = " +  TextUtils.join(", ", selectedListIds));

                        if(selectedListIds.size() > 0 && selectedListIds.size() <= 16)
                            listButton.setText("Selected lists: " + TextUtils.join(", ", selectedListIds));
                        else if(selectedListIds.size() > 16)
                            listButton.setText("Selected lists: " + TextUtils.join(", ", selectedListIds.subList(0,16)) + "...");
                        else
                            listButton.setText("Select List(s)");

                        return true; // allow selection
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback(){
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
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

    private void scheduleOneStepAlert(){
        // Store values at the time of the send attempt.
        Intent intent = new Intent(OneStepAlertActivity.this, OneStepAlertScheduler.class);

        String senderName = senderNameEditText.getText().toString().trim();//.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");
        Log.d("ShceduleOneStepAlert","senderName before is: "+senderNameEditText.getText().toString());
        Log.d("ShceduleOneStepAlert","senderName after is: "+senderName);
        senderName = FunctionHelper.convertToASCII(senderName);

        String subject = subjectEditText.getText().toString().trim();//.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");
        subject = FunctionHelper.convertToASCII(subject);

        String message = FunctionHelper.convertToASCII(messageEditText.getText().toString());

        String lists = TextUtils.join(", ", selectedListIds);

        intent.putExtra("alertTypes",alertTypes);
        intent.putExtra("senderName",senderName);
        intent.putExtra("subject",subject);
        intent.putExtra("message",message);
        intent.putExtra("lists",lists);

        String[] messageTypeAndCampID = isCannedMessage();
        intent.putExtra("msgtype", messageTypeAndCampID[0]);
        intent.putExtra("camp_id", messageTypeAndCampID[1]);

        intent.putExtra("commType", getCommType());

        if(checkboxVoice.isChecked()) {
            intent.putExtra("sound", fileName);
            intent.putExtra("soundURL",soundURL);
        } else {
            intent.putExtra("sound", "");
            intent.putExtra("soundURL","");
        }

        startActivity(intent);
    }

    private boolean validateFields(){
        senderNameEditText.setError(null);
        subjectEditText.setError(null);
        messageEditText.setError(null);
        listButton.setError(null);

        // Store values at the time of the send attempt.
        String senderName = senderNameEditText.getText().toString().trim();
        String subject = subjectEditText.getText().toString().trim();
        String message = messageEditText.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid sender
        if (senderName.isEmpty()) {
            senderNameEditText.setError(getString(R.string.error_field_required));
            focusView = senderNameEditText;
            cancel = true;

        } else if(!FunctionHelper.isAlphanumeric(senderName)) {
            senderNameEditText.setError("Please make sure that the sender field only contains a combination of alphanumeric characters and/or the following characters '.,-()/:;");
            focusView = senderNameEditText;
            cancel = true;
        }

        // Check for a valid subject
        if (subject.isEmpty()) {
            subjectEditText.setError(getString(R.string.error_field_required));
            focusView = subjectEditText;
            cancel = true;
        }

        // Check for a valid message
        if (message.isEmpty()) {
            messageEditText.setError(getString(R.string.error_field_required));
            focusView = messageEditText;
            cancel = true;
        }

        //Check for lists
        if(selectedListIds == null || selectedListIds.size() == 0){
            listButton.setError(getString(R.string.error_field_required_list));
            focusView = listButton;
            cancel = true;
        }

        if(checkboxVoice.isChecked() && !checkVoiceAndOtherAlert()) {
            showVoiceAndOtherAlertError();
            cancel = true;
            focusView = null;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if(focusView != null) focusView.requestFocus();
            Log.d("sound","validate false");
            return false;
        } else {
            Log.d("sound","validate true");
            return true;
        }
    }

    private String[] isCannedMessage(){
        //Remove newline characters from the user's message
        Log.d("OneStepAlertParams","current message before trim -->@"+messageEditText.getText().toString()+"@");
        String currentMessage = messageEditText.getText().toString().trim();//.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");
        Log.d("OneStepAlertParams","current message after trim -->@"+currentMessage+"@");

        for (int i=0; i<messages.getData().size(); i++){
            String cannedMessage = messages.getData().get(i).toString().trim();//.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");
            String campaignID = messages.getData().get(i).getVal();
            Log.d("OneStepAlertParams","The canned message is -->@"+cannedMessage+"@\nCampaign id is - "+campaignID);
            if (currentMessage.equals(cannedMessage)){
                Log.d("OneStepAlertParams","current message is equal to the canned message above");
                return new String[]{"C", campaignID};
            }
        }

        return new String[]{"M", "0"};
    }

    private String getCommType(){
        String commType="";
        commType += checkboxEmail.isChecked() ? "1" : "0";
        commType += checkboxText.isChecked() ? "1" : "0";
        commType += checkboxVoice.isChecked() ? "1" : "0";
        return commType;
    }

    private String createTimeStamp(){
        Date currentTime = Calendar.getInstance().getTime();
        return new SimpleDateFormat( "MM/dd/yyyy hh:mm:ss aa" , Locale.getDefault()).format(currentTime);
    }

    private void sendOneStepAlert() {
        manageProgressForAlert(2,true);

        OneStepAlertService client = ServiceGenerator.createService(OneStepAlertService.class);

        String senderName = senderNameEditText.getText().toString().trim();//.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");
        senderName = FunctionHelper.convertToASCII(senderName);

        String subject = subjectEditText.getText().toString().trim();//.replaceAll("(^[\\r\\n]+|[\\r\\n]+$)", "");
        subject = FunctionHelper.convertToASCII(subject);

        String message = FunctionHelper.convertToASCII(messageEditText.getText().toString());

        String lists = TextUtils.join(", ", selectedListIds);

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

        String[] messageTypeAndCampID = isCannedMessage();
        params.put("msgtype", messageTypeAndCampID[0]);
        params.put("camp_id", messageTypeAndCampID[1]);

        params.put("commType", getCommType());
        params.put("senddate", createTimeStamp());

        String soundFileName = checkboxVoice.isChecked() ? fileName : "";
        params.put("sound", soundFileName);

        params.put("send_imd", "1");
        Log.d("OneStepAlertParams","alert params are: "+params.toString());

//        manageProgressForAlert(1,false);

        Call<BasicResponse> call = client.request(params);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Response<BasicResponse> response) {
                manageProgressForAlert(1,false);

                Log.d("OneStepAlertActivity","sendOneStepAlert() --> response: "+response.body().getResponseObj());

                if (response.isSuccess()) {
                    BasicResponse apiResponse = response.body();
                    Log.d("response",""+response.body().getData()+" "+ apiResponse.getSuccess());

                    if(apiResponse.getSuccess()){
                        new MaterialDialog.Builder(context)
                                .iconRes(R.drawable.ic_check_circle_black_24dp)
                                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                                .title(R.string.success_title)
                                .content(R.string.success_message)
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
                                .positiveText(R.string.ok)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .show();
                    }
                } else {
                    showResponseError();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                manageProgressForAlert(1,false);
                Log.e(LOG_TAG, "Errormessage" + t.getMessage());
                showResponseError();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int[] scrcoords = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())){
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }

        return ret;
    }

//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putIntegerArrayList("alertTypes", alertTypes);
//    }

}
