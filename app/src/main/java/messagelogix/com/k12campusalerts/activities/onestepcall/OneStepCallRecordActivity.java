package messagelogix.com.k12campusalerts.activities.onestepcall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.ApiResponse;
import messagelogix.com.k12campusalerts.models.BasicResponse;
import messagelogix.com.k12campusalerts.models.User;
import android.media.MediaRecorder;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;

import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import messagelogix.com.k12campusalerts.utils.UploadFileToServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class OneStepCallRecordActivity extends AppCompatActivity {

	private ImageButton mRecordButton;
	private ImageButton mPlayButton;
	private Button mScheduleButton;

	public static final String LOG_TAG = OneStepCallRecordActivity.class.getSimpleName();
	private static String mFileName = null;

	//Standard Media Recorder
	private MediaRecorder standardMediaRecorder;
	private final MediaPlayer mPlayer = new MediaPlayer();

	private CountDownTimer timer;
	private TextView mTimerTextField;
	private User mUser;

	private ProgressDialog mDialog;

	private boolean isRecording = false;

	Uri audioUri;
	private static ParcelFileDescriptor fileParcelDesc = null;

	public interface OneStepCallSaveToLibrary {
		@FormUrlEncoded
		@POST(ServiceGenerator.API_BASE_URL)
		Call<BasicResponse> request(@FieldMap HashMap<String, String> parameters);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_one_step_call_record);

		mUser = (User) new FunctionHelper(this).retrieveObject(User.class);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		initializeUI();

		initializeFile();
	}

	private void initializeFile() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//			ContentValues values = new ContentValues(4);
//			//values.put(MediaStore.Audio.Media.TITLE, getTimeStamp()+".mp4");
//			values.put(MediaStore.Audio.Media.DISPLAY_NAME, getTimeStamp());
//			values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (System.currentTimeMillis() / 1000));
//			values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4");
////			values.put(MediaStore.Audio.Media.MIME_TYPE, "video/mp4");
//
////			values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/*");
//			//values.put(MediaStore.Video.Media.RELATIVE_PATH, "DCIM/Recordings");
//			values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings");
//			Uri audioCollection;
//			audioCollection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
//			audioUri = getContentResolver().insert(audioCollection, values);
//
//			try {
//				fileParcelDesc = getContentResolver().openFileDescriptor(audioUri, "w");
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}

			mFileName = getExternalCacheDir().getAbsolutePath();
			mFileName += File.separator + getTimeStamp() + ".mp4";

			// state = mounted or unmounted
//			String state = Environment.getExternalStorageState();
//
//			Context ctx = this.getApplicationContext();
//			File audioDir = new File(ctx.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "AudioMemos");
//			audioDir.mkdirs();
//			String audioDirPath = audioDir.getAbsolutePath();
//
//			File recordingFile = new File(audioDirPath + "/" + getTimeStamp() + ".mp4");
//			mFileName = recordingFile.getAbsolutePath();

		} else {
			mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
			mFileName += File.separator + getTimeStamp() + ".mp4";
			Log.e("UploadFile", "mFileName = " +  mFileName);
		}
	}

	private void initializeUI() {
		mTimerTextField = findViewById(R.id.textView4);

		//Record OneStepCall message
		mRecordButton = findViewById(R.id.btnRecord);
		mRecordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("PressingButtons","pressing Record button");
				recordMessage();}
		});

		//Play OneStepCall message
		mPlayButton = findViewById(R.id.btnPlay);
		mPlayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("PressingButtons","pressing Play button");
				playMessage();
			}
		});

		//OneStepAlertScheduler OneStepCall message
		mScheduleButton = findViewById(R.id.uploadBtn);
		mScheduleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPlayer.isPlaying()) {
					stopPlaying();
					timer.cancel();
				}

				scheduleMessage();
			}
		});
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

	@Override
	public void onPause() {
		super.onPause();

		stopRecordingAndPlayback();
	}

	private void stopRecordingAndPlayback() {
		if(isRecording) {
			Log.d("BackPressed","media recorder is recording --> stopping media recorder");
			try {
				standardMediaRecorder.stop();
				standardMediaRecorder.reset();
				standardMediaRecorder.release();
				Log.d("BackPressed","media recorder is recording --> successfully stopped media recorder");
			} catch (Exception e) {
				Log.d("BackPressed","exception --> stopped media recorder");
			}

			mRecordButton.setImageResource(R.drawable.record_sound);
		}

		if(mPlayer.isPlaying()) {
			Log.d("BackPressed","media player is playing --> stopping media player");
			updateTimerTextField(mPlayer.getDuration());

			mPlayer.stop();
			mPlayer.reset();

			mPlayButton.setImageResource(R.drawable.play_sound);
			timer.cancel();
			Log.d("BackPressed","media player is playing --> successfully stopped media player");
		}
	}

	private String getTimeStamp(){
		long tsLong = (System.currentTimeMillis()/1000);
		return Long.toString(tsLong);
	}

	private void recordMessage(){
		if(!isRecording) {
			startRecording();
			showRecorderTimer(true);
			isRecording = true;
		} else {
			stopRecording();
			timer.cancel();
			isRecording = false;
		}
	}

	private void startRecording() {
		try {
			standardMediaRecorder = new MediaRecorder();
			standardMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			standardMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			standardMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//			standardMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				//standardMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
//				standardMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//				standardMediaRecorder.setAudioChannels(2);
//				standardMediaRecorder.setAudioEncodingBitRate(16);
//				standardMediaRecorder.setAudioSamplingRate(44100);
				//standardMediaRecorder.setOutputFile(fileParcelDesc.getFileDescriptor());
				standardMediaRecorder.setOutputFile(mFileName);
			} else {
				standardMediaRecorder.setOutputFile(mFileName);
			}

			standardMediaRecorder.prepare();
			standardMediaRecorder.start();

			mRecordButton.setImageResource(R.drawable.stop_recording);
			mPlayButton.setVisibility(View.INVISIBLE);

			mScheduleButton.setAlpha(0.5f);
			mScheduleButton.setEnabled(false);

			Toast.makeText(this, "Now Recording", Toast.LENGTH_SHORT).show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			Log.e(LOG_TAG, "getRealPathFromURI Exception : " + e.toString());
			return "";
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	private void stopRecording() {
		try {
			standardMediaRecorder.stop();
			standardMediaRecorder.reset();
			standardMediaRecorder.release();
		} catch (Exception e) {
			Log.d("OneStepCall","stopped media recorder");
		}

		mRecordButton.setImageResource(R.drawable.record_sound);
		mPlayButton.setVisibility(View.VISIBLE);

		mScheduleButton.setAlpha(1.0f);
		mScheduleButton.setEnabled(true);
	}

	public void showRecorderTimer(boolean show) {
		if(show){
			long milliseconds = mUser.getData().getCallLength() * 1000;
			timer = new CountDownTimer(milliseconds, 1000) {
				public void onTick(long millisUntilFinished) {
					updateTimerTextField(millisUntilFinished);
				}
				public void onFinish() {
					stopRecording();
				}
			}.start();
		} else {
			timer.cancel();
			stopRecording();
		}
	}

	private void playMessage() {
		if (!mPlayer.isPlaying()) {
			startPlaying();
			showPlayerTimer(true);
		} else {
			stopPlaying();
			timer.cancel();
		}
	}

	private void startPlaying() {
		try {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				//Log.d("AudioURI", fileParcelDesc.toString());
//				mPlayer.setAudioAttributes( new AudioAttributes.Builder()
//						.setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//						.setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
//						.build());
//				mPlayer.setDataSource(fileParcelDesc.getFileDescriptor());
				mPlayer.setDataSource(mFileName);
			} else {
				mPlayer.setDataSource(mFileName);
			}

			mPlayer.prepare();
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					stopPlaying();
				}
			});
			mPlayer.start();

			mPlayButton.setImageResource(R.drawable.stop_playing);
			mRecordButton.setAlpha(0.5f);
			mRecordButton.setEnabled(false);
		} catch(Exception e) {
			Log.d("UploadFile", "startPlaying() --> prepare() failed");
			Log.d("UploadFile", "startPlaying() --> exception is: \n"+e);
		}
	}

	private void stopPlaying() {
		updateTimerTextField(mPlayer.getDuration());

		mPlayer.stop();
		mPlayer.reset();

		mPlayButton.setImageResource(R.drawable.play_sound);
		//isPlaying = false;
		mRecordButton.setAlpha(1.0f);
		mRecordButton.setEnabled(true);
	}

	public void showPlayerTimer(boolean show) {
		if(show) {
			long milliseconds = mPlayer.getDuration();
			timer = new CountDownTimer(milliseconds, 1000) {
				public void onTick(long millisUntilFinished) {
					updateTimerTextField(millisUntilFinished);
				}
				public void onFinish() {
					stopPlaying();
				}
			}.start();
		} else {
			timer.cancel();
			stopPlaying();
		}
	}

	private void updateTimerTextField(long millisUntilFinished) {
		long secondsUntilFinished = millisUntilFinished / 1000;
		long minutes = secondsUntilFinished / 60;
		long seconds = secondsUntilFinished % 60;
		String time = String.format(Locale.US, "%02d:%02d", minutes, seconds);
		mTimerTextField.setText(time);
	}

	private void scheduleMessage() {
		Log.d("UploadFile","inside scheduleStandard() - calling AddVoiceMessageMP4 action");
		File soundFile;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			//soundFile = new File(getRealPathFromURI(this, audioUri));
			soundFile = new File(mFileName);
		} else {
			soundFile = new File(mFileName);
		}

		if(!soundFile.exists()){
			showErrorAlert(
					"Warning",
					"No message was recorded. Please record a message before attempting to schedule a OneStepCall",
					"You must record a message before scheduling a OneStepCall.");
		} else {
			HashMap<String, String> params = ServiceGenerator.getApiMap();
			params.put("controller", "Phone");
			params.put("action", "AddVoiceMessageMP4");
			params.put("account", mUser.getData().getAccount());
			params.put("pin", mUser.getData().getPin());
			params.put("canned_name", "Pre-recorded Alert Message");
			params.put("canned_slot", "0");
			String url = ServiceGenerator.API_BASE_URL + "?" + FunctionHelper.getPostDataString(params);
			Log.d("UploadFile", "url = " + url);
			Log.d("UploadFile","FileName prior to call to UploadSoundFileTask --> "+mFileName);

			String filePath;
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//				filePath = getRealPathFromURI(this, audioUri);
				filePath = mFileName;
			} else
				filePath = mFileName;

			new UploadSoundFileTask(params, filePath).execute(url);
		}
	}

	public void showErrorAlert(String title, String error,  final String toastText){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(OneStepCallRecordActivity.this);
		alertDialog.setTitle(title);
		alertDialog.setMessage(error);
		alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to invoke NO event
				Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});

		alertDialog.show();
	}

	private void showProgress(boolean show){
		if(show){
			if (mDialog == null) {
				mDialog = new ProgressDialog(this);
				mDialog.setMessage("Please wait while your message is processed...");
				mDialog.setIndeterminate(true);
				mDialog.setCancelable(false);
				mDialog.setCanceledOnTouchOutside(false);
			}
			mDialog.show();
		} else {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.cancel();
				mDialog.dismiss();
			}
		}
	}

	public class UploadSoundFileTask extends AsyncTask<String, Void, String> {

		HashMap<String,String> requestParams;
		String soundFilePath;

		public UploadSoundFileTask(HashMap<String,String> params, String filePath){
			this.requestParams = params;
			this.soundFilePath = filePath;
		}

		@Override
		protected void onPreExecute(){
			showProgress(true);
		}

		@Override
		protected String doInBackground(String... urls) {

			UploadFileToServer functions = new UploadFileToServer(getApplicationContext());

			return functions.upload(urls[0], soundFilePath);//functions.uploadFileIOS(requestParams, urls[0], soundFilePath);
		}

		protected void onPostExecute(String response) {
			showProgress(false);
			if(response != null && !response.isEmpty()){
				Log.d("UploadFile", "UploadSoundFileTask response: " + response);

				OneStepCallResponse data = new Gson().fromJson(response, OneStepCallResponse.class);
				if(data.getSuccess()){
					String uLawFile = data.getData().getUlawFile();
					showSaveToLibraryPrompt(uLawFile);
				} else {
					showErrorAlert(
							"Warning",
							"Your message saved but it failed to process. Please try again.",
							"Please try to schedule your message again.");
				}
			} else {
				//Uh-Oh we got some issues
				showErrorAlert(
						"Warning",
						"Your message saved but it failed to process. Please try again.",
						"Please try to schedule your message again.");
//				showErrorAlert(
//						"Network Connection Error",
//						"There was an issue establishing a network connection. Please make sure you have a stable connection before trying again.",
//						"Please try to schedule your message again.");
			}
		}

		public class OneStepCallResponse extends ApiResponse<OneStepCallResponse.Data> {

			public class Data {
				@SerializedName("ulaw_file")
				@Expose
				private String ulaw_file;

				public String getUlawFile() {
					return ulaw_file;
				}

				public void setUlawFile(String ulaw_file) {
					this.ulaw_file = ulaw_file;
				}
			}
		}
	}

	private void showSaveToLibraryPrompt(final String uLawFile) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setCancelable(false);

		builder.setTitle("Message Library");
		builder.setMessage("Would you like to save this message to your pre-recorded library?");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showEnterTitlePrompt(uLawFile);
			}
		});

		builder.setNegativeButton("No (skip)", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goToScheduler(uLawFile);
			}
		});

		builder.show();
	}

	private void showEnterTitlePrompt(final String uLawFile) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setCancelable(false);

		builder.setTitle("Enter a Title");
		builder.setMessage("Please enter a title for this voice message.\n\nPressing the 'Cancel' button will prevent you from saving your voice message.");

		// Set up the input
		final EditText input = new EditText(this);
		input.setGravity(Gravity.CENTER_HORIZONTAL);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
		input.setMaxLines(1);
		input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(54)});
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String messageName = input.getText().toString();
				saveVoiceMessageToLibrary(uLawFile, messageName);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goToScheduler(uLawFile);
			}
		});

		builder.show();
	}

	private void messageSavedSuccessPrompt(final String uLawFile) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setCancelable(false);

		builder.setTitle("Message Successfully Saved");
		builder.setMessage("Your voice message has been successfully added to your library.");

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goToScheduler(uLawFile);
			}
		});

		builder.show();
	}

	private void messageSavedFailPrompt(final String uLawFile, final String messageTitle) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setCancelable(false);

		builder.setTitle("Warning - Voice Message Not Saved");
		builder.setMessage("Your voice message could not be saved to your library at this time.\n\nPlease try again by pressing the 'Yes' button below.\nPressing the 'Cancel' button will prevent you from saving your voice message.");

		builder.setPositiveButton("Yes (try again)", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveVoiceMessageToLibrary(uLawFile, messageTitle);
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goToScheduler(uLawFile);
			}
		});

		builder.show();
	}

	private void saveVoiceMessageToLibrary(final String uLawFile, final String messageTitle) {
		final ProgressDialog progressDialog = new ProgressDialog(this);

		progressDialog.setMessage("Please wait while your message is being saved...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		OneStepCallSaveToLibrary client = ServiceGenerator.createService(OneStepCallSaveToLibrary.class);

		HashMap<String, String> params = ServiceGenerator.getApiMap();
		params.put("controller", "Phone");
		params.put("action", "AddVoiceMessage");
		params.put("account", mUser.getData().getAccount());
		params.put("pin", mUser.getData().getPin());
		params.put("canned_slot", "-1");
		params.put("ulaw_file", uLawFile);
		params.put("canned_name", messageTitle);

		Call<BasicResponse> call = client.request(params);
		call.enqueue(new Callback<BasicResponse>() {

			@Override
			public void onResponse(Response<BasicResponse> response) {
				progressDialog.dismiss();

				Log.d("OSCRecordActivity","saveVoiceMessageToLibrary() --> response: "+response.body().getResponseObj());

				if(response.isSuccess()) {
					BasicResponse responseData  = response.body();
					if(responseData.getSuccess()) {
						messageSavedSuccessPrompt(uLawFile);
					} else {
						messageSavedFailPrompt(uLawFile, messageTitle);
					}
				} else {
					messageSavedFailPrompt(uLawFile, messageTitle);
				}
			}

			@Override
			public void onFailure(Throwable t) {
				progressDialog.dismiss();
				messageSavedFailPrompt(uLawFile, messageTitle);
			}
		});
	}

	private void goToScheduler(final String uLawFile) {
		Intent intent = new Intent(OneStepCallRecordActivity.this, OneStepCallScheduleActivity.class);
		intent.putExtra("ulawFile", uLawFile);
		startActivity(intent);
	}
}
