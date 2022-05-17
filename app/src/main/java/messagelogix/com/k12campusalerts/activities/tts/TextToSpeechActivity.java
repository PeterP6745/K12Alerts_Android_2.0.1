package messagelogix.com.k12campusalerts.activities.tts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.models.TTSMessage;
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


public class TextToSpeechActivity extends BaseActivity {


	private static final String LOG_TAG = TextToSpeechActivity.class.getSimpleName();
	private static final String FEMALE_VOICE = "F";
	private static final String MALE_VOICE = "M";

	private EditText messageEditText;
	private int selectedVoiceIndex;
	private User mUser;
	private List<TTSMessage.Data> messages = new ArrayList<>();
	//private FloatingActionButton fab;
	private Button cannedMessageButton;
	private Context context = this;
	private int selectedMessageIndex;
	private ProgressDialog mDialog;
	private Button voiceButton;

	public interface TTSMessageService {
		@FormUrlEncoded
		@POST(ServiceGenerator.API_BASE_URL)
		Call<TTSMessage> request(@FieldMap HashMap<String, String> parameters);
	}

	public interface PreviewTTSMessageService {
		@FormUrlEncoded
		@POST(ServiceGenerator.API_BASE_URL)
		Call<TTSPreviewResponse> request(@FieldMap HashMap<String, String> parameters);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Adding our layout to parent class frame layout.
		 */
		getLayoutInflater().inflate(R.layout.activity_text_to_speech, frameLayout);

		mUser = (User) new FunctionHelper(this).retrieveObject(User.class);

		/**
		 * Setting title and itemChecked
		 */
		mDrawerList.setItemChecked(position, true);
		setTitle(navDrawerItems.get(position).getTitle());

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}


		selectedVoiceIndex = 0;
		selectedMessageIndex = -1;


		messageEditText = (EditText)findViewById(R.id.message_edit_text);

		voiceButton = (Button) findViewById(R.id.voiceButton);
		voiceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showVoiceDialog();
			}
		});


		Button previewButton = (Button) findViewById(R.id.previewButton);
		previewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(validate()){
					String message = messageEditText.getText().toString();
					String voice = selectedVoiceIndex == 0 ? MALE_VOICE : FEMALE_VOICE;
					String ttsId = (selectedMessageIndex == -1) ? "0" : messages.get(selectedMessageIndex).getTtvId();

					previewMessage(message, voice, ttsId);
				}
			}
		});


//		fab = (FloatingActionButton) findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				new MaterialDialog.Builder(context)
//						.title(R.string.choose_message)
//						.items(messages)
//						.itemsCallbackSingleChoice(selectedMessageIndex, new MaterialDialog.ListCallbackSingleChoice() {
//							@Override
//							public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//								selectedMessageIndex = which;
//								messageEditText.setText(text);
//								return true; // allow selection
//							}
//						})
//						.positiveText(R.string.md_choose_label)
//						.show();
//
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
//			}
//		});


		cannedMessageButton = (Button) findViewById(R.id.canned_message_button);
		cannedMessageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new MaterialDialog.Builder(context)
						.title(R.string.choose_message)
						.items(messages)
						.itemsCallbackSingleChoice(selectedMessageIndex, new MaterialDialog.ListCallbackSingleChoice() {
							@Override
							public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
								selectedMessageIndex = which;
								messageEditText.setText(text);
								return true; // allow selection
							}
						})
						.positiveText(R.string.md_choose_label)
						.show();
			}
		});

		setTouchListener();
		downloadCannedMessages();
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

	private void downloadCannedMessages() {
		TTSMessageService client = ServiceGenerator.createService(TTSMessageService.class);

		HashMap<String, String> params = ServiceGenerator.getApiMap();
		params.put("controller", "TextToSpeech");
		params.put("action", "GetTTSMessages");
		params.put("accountId", mUser.getData().getAcctId());
		params.put("pinId", mUser.getData().getPinId());

		Call<TTSMessage> call = client.request(params);
		call.enqueue(new Callback<TTSMessage>() {
			@Override
			public void onResponse(Response<TTSMessage> response) {
				Log.d("TextToSpeechActivity","downloadCannedMessages() --> response: "+response.body().getResponseObj());

				TTSMessage messagesData  = response.body();
				if (response.isSuccess() && messagesData.getSuccess()) {
					messages = messagesData.getData();
				}else{
					//fab.setVisibility(View.GONE);
					cannedMessageButton.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(Throwable t) {
				//fab.setVisibility(View.GONE);
				cannedMessageButton.setVisibility(View.GONE);
				Log.e(LOG_TAG, "message =" + t.getMessage());
			}
		});
	}

	private void showVoiceDialog() {

		new MaterialDialog.Builder(this)
				.title(R.string.choose_voice)
				.items(R.array.tts_voices)
				.itemsCallbackSingleChoice(selectedVoiceIndex, new MaterialDialog.ListCallbackSingleChoice() {
					@Override
					public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
						selectedVoiceIndex = which;
							if(selectedVoiceIndex==1){
								voiceButton.setBackgroundResource(R.drawable.bg_button_female);
								voiceButton.setText(R.string.Female_Voice);}
						else{
								voiceButton.setText(R.string.Male_Voice);
								voiceButton.setBackgroundResource(R.drawable.bg_button_male);
							}

						Log.d(LOG_TAG, "selectedVoiceIndex "  + selectedVoiceIndex);

						return true; // allow selection
					}
				})
				.positiveText(R.string.md_choose_label)
				.show();
	}

	private void previewMessage(String message, final String voice, final String ttsId) {

		showProgress(true);
		PreviewTTSMessageService client = ServiceGenerator.createService(PreviewTTSMessageService.class);

		HashMap<String, String> params = ServiceGenerator.getApiMap();
		params.put("controller", "TextToSpeech");
		params.put("action", "PreviewVoice");
		params.put("account", mUser.getData().getAccount());
		params.put("pin", mUser.getData().getPin());
		params.put("accountId", mUser.getData().getAcctId());
		params.put("pinId", mUser.getData().getPinId());
		params.put("ttv_message", message);
		params.put("voice", voice);
		params.put("ttv_id", ttsId);

		Call<TTSPreviewResponse> call = client.request(params);
		call.enqueue(new Callback<TTSPreviewResponse>() {
			@Override
			public void onResponse(Response<TTSPreviewResponse> response) {
				showProgress(false);
				Log.d("TextToSpeechActivity",
						"previewMessage() --> parameters: account: "+mUser.getData().getAccount()+
								"\npin: "+mUser.getData().getPin()+
								"\naccountId: "+mUser.getData().getAcctId()+
						"\npinId: "+mUser.getData().getPinId()+
						"\nvoice: "+voice+
						"\nttv_id: "+ttsId);
				Log.d("TextToSpeechActivity","previewMessage() --> response: "+response.body().getResponseObj());

				TTSPreviewResponse responseData  = response.body();
				if (response.isSuccess() && responseData.getSuccess()) {
					Intent i = new Intent(TextToSpeechActivity.this, TextToSpeechScheduleActivity.class);
					i.putExtra(TTSPreviewResponse.FILENAME, responseData.getData().getFilename());
					i.putExtra(TTSPreviewResponse.URL, responseData.getData().getUrl());
					startActivity(i);
				}
			}

			@Override
			public void onFailure(Throwable t) {
				showProgress(false);
				Log.e(LOG_TAG, "message =" + t.getMessage());
			}
		});
	}

	private boolean validate() {
		// Reset errors.
		messageEditText.setError(null);

		// Store values at the time of the send attempt.
		String message = messageEditText.getText().toString();

		// Check for a valid message
		if (TextUtils.isEmpty(message)) {
			messageEditText.setError(getString(R.string.error_field_required));
			messageEditText.requestFocus();
			return false;
		}

		return true;
	}

	public void setTouchListener() {
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.ttsLayout);
		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motion) {
				InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				return false;
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
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

}
