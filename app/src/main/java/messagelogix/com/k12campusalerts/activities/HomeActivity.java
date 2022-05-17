package messagelogix.com.k12campusalerts.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.bnotified.BNotifiedHome;
import messagelogix.com.k12campusalerts.activities.desktop.DesktopAlertsActivity;
import messagelogix.com.k12campusalerts.activities.onestepalert.OneStepAlertActivity;
import messagelogix.com.k12campusalerts.activities.onestepcall.OneStepCallActivity;
import messagelogix.com.k12campusalerts.activities.reports.ReportsActivity;
import messagelogix.com.k12campusalerts.activities.smartbuttonalerts.SendSmartButtonAlertsActivity;
import messagelogix.com.k12campusalerts.activities.smartbuttonreports.SmartButtonReportsActivity;
import messagelogix.com.k12campusalerts.activities.tts.TextToSpeechActivity;
import messagelogix.com.k12campusalerts.models.Features;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.Permission;
import messagelogix.com.k12campusalerts.utils.Preferences;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class HomeActivity extends BaseActivity implements View.OnClickListener {

	private static final String LOG_TAG = HomeActivity.class.getSimpleName();
	private ArrayList<Integer> homeButtonTags;
	private Context context = this;
	private boolean shouldReloadHome = false;

	public interface ApiService {
		@FormUrlEncoded
		@POST(ServiceGenerator.API_BASE_URL)
		Call<Features> request(@FieldMap HashMap<String,String> parameters);
	}

	LinearLayout linear;

	private User mUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Preferences.init(context);
		shouldReloadHome = Preferences.getBoolean("shouldReloadHome");

		checkPermissions();
		mUser = (User)new FunctionHelper(context).retrieveObject(User.class);

		/**
		 * Adding our layout to parent class frame layout.
		 */
		getLayoutInflater().inflate(R.layout.activity_home, frameLayout);

		TextView copyRightTV = (TextView) findViewById(R.id.patent_label_tv);
		copyRightTV.setText(FunctionHelper.getDynamicCopyRightNotice());

		linear = (LinearLayout) findViewById(R.id.linear_layout);

		/**
		 * Setting title and itemChecked  
		 */
		mDrawerList.setItemChecked(position, true);
		//setTitle(navDrawerItems.get(position).getTitle());
		setTitle(mUser.getData().getAccountName());

/*		Features f = (Features)new FunctionHelper(context).retrieveObject(Features.class);
		if (f != null){
			createMenuButtons(f);
		}else{
			Log.d(LOG_TAG, "f==null");
		}*/

		ApiService client = ServiceGenerator.createService(ApiService.class);

		HashMap<String, String> params = ServiceGenerator.getApiMap();
		params.put("controller", "User");
		params.put("action", "GetUserMenuSettings");
		params.put("accountId", mUser.getData().getAcctId());
		params.put("pinId", mUser.getData().getPinId());

		Call<Features> call = client.request(params);
		call.enqueue(new Callback<Features>() {
			@Override
			public void onResponse(Response<Features> response) {
				Log.d("GetMenuSettings","response(): "+response.body().getResponseObj());
				if(response.isSuccess()){
					Features features = response.body();
					if(features.getSuccess()){
						new FunctionHelper(context).saveObject(features);
						createMenuButtons(features);

						if(shouldReloadHome){
							//reload home page
							Preferences.putBoolean("shouldReloadHome", false);
							Log.d("shouldReloadHome:", "Refresh home");
							finish();
							startActivity(getIntent());
						}

						else{
							Log.d("shouldReloadHome:", "Did not refresh home");
						}
					}
				}
			}
			@Override
			public void onFailure(Throwable t) {
				Log.e(LOG_TAG, "message =" + t.getMessage());
			}
		});

		Log.d("End","END OF HOME ONCREATE");
		//this.getParent().finish();
		//startActivity(this.getParentActivityIntent());
	}

	private void checkPermissions(){
		if (ContextCompat.checkSelfPermission(HomeActivity.this,
				Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(HomeActivity.this,
					new String[]{Manifest.permission.INTERNET},
					Permission.INTERNET_REQUEST_CODE);

		}
	}

	private void createMenuButtons(Features features) {
		if(linear.getChildCount() > 0){
			linear.removeAllViews();
		}

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(linear.getLayoutParams());
		layoutParams.setMargins(20, 10, 20, 10);

		//init array list of button tags
		homeButtonTags = new ArrayList<>();


		if(features.getData().get1StepAlert() != null){
			if(features.getData().get1StepAlert().equals("1")){
				Button menuButton = new Button(this);
				menuButton.setTag(NAV_DRAWER_ITEM_1STEP_ALERT);
				menuButton.setText(R.string.one_step_alert);
				menuButton.setOnClickListener(this);
				menuButton.setBackgroundColor(Color.parseColor("#d52227"));
				menuButton.setBackground(getResources().getDrawable(R.drawable.bg_home_buttons_red));
				menuButton.setTextColor(Color.parseColor("#ffffff"));
				menuButton.setTypeface(null, Typeface.BOLD);
				menuButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
				menuButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_white_24dp, 0, 0, 0);
				menuButton.setCompoundDrawablePadding(10);
				menuButton.setPadding(25,0,0,0);
				linear.addView(menuButton,layoutParams);

				//add tag to array list
				homeButtonTags.add(NAV_DRAWER_ITEM_1STEP_ALERT);
			}
		}

		if(features.getData().get1StepCall() != null){
			if(features.getData().get1StepCall().equals("1")){
				Button menuButton = new Button(this);
				menuButton.setHeight(50);
				menuButton.setWidth(50);
				menuButton.setTag(NAV_DRAWER_ITEM_1STEP_CALL);
				menuButton.setText(R.string.one_step_call);
				menuButton.setOnClickListener(this);
				menuButton.setBackgroundColor(Color.parseColor("#d52227"));
				menuButton.setBackground(getResources().getDrawable(R.drawable.bg_home_buttons_red));
				menuButton.setTextColor(Color.parseColor("#ffffff"));
				menuButton.setTypeface(null, Typeface.BOLD);
				menuButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
				menuButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mic_white_24dp, 0, 0, 0);
				menuButton.setCompoundDrawablePadding(10);
				menuButton.setPadding(25,0,0,0);
				linear.addView(menuButton,layoutParams);

				//add tag to array list
				homeButtonTags.add(NAV_DRAWER_ITEM_1STEP_CALL);
			}
		}

		if(features.getData().getTextToSpeech() != null){
			if(features.getData().getTextToSpeech().equals("1")){
				Button menuButton = new Button(this);
				menuButton.setHeight(50);
				menuButton.setWidth(50);
				menuButton.setTag(NAV_DRAWER_ITEM_TTS);
				menuButton.setText(R.string.text_to_speech);
				menuButton.setOnClickListener(this);
				menuButton.setBackgroundColor(Color.parseColor("#d52227"));
				menuButton.setBackground(getResources().getDrawable(R.drawable.bg_home_buttons_red));
				menuButton.setTextColor(Color.parseColor("#ffffff"));
				menuButton.setTypeface(null, Typeface.BOLD);
				menuButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
				menuButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_white_24dp, 0, 0, 0);
				menuButton.setCompoundDrawablePadding(10);
				menuButton.setPadding(25,0,0,0);
				linear.addView(menuButton,layoutParams);

				//add tag to array list
				homeButtonTags.add(NAV_DRAWER_ITEM_TTS);
			}
		}

		if(features.getData().getBNotified() != null){
			if(features.getData().getBNotified().equals("1")){
				Button menuButton = new Button(this);
				menuButton.setHeight(50);
				menuButton.setWidth(50);
				menuButton.setTag(NAV_DRAWER_ITEM_BNOTIFIED);
				menuButton.setText(R.string.b_notified);
				menuButton.setOnClickListener(this);
				menuButton.setBackgroundColor(Color.parseColor("#0d64aa"));
				menuButton.setBackground(getResources().getDrawable(R.drawable.bg_home_buttons));
				menuButton.setTextColor(Color.parseColor("#ffffff"));
				menuButton.setTypeface(null, Typeface.BOLD);
				menuButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
				menuButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phonelink_ring_white_24dp, 0, 0, 0);
				menuButton.setCompoundDrawablePadding(10);
				linear.addView(menuButton,layoutParams);
				menuButton.setPadding(25,0,0,0);

				//add tag to array list
				homeButtonTags.add(NAV_DRAWER_ITEM_BNOTIFIED);
			}
		}

		if(features.getData().getViewReports() != null){
			if(features.getData().getViewReports().equals("1")){
				Button menuButton = new Button(this);
				menuButton.setHeight(50);
				menuButton.setWidth(50);
				menuButton.setTag(NAV_DRAWER_ITEM_REPORTS);
				menuButton.setText(R.string.reports);
				menuButton.setOnClickListener(this);
				menuButton.setBackgroundColor(Color.parseColor("#0d64aa"));
				menuButton.setBackground(getResources().getDrawable(R.drawable.bg_home_buttons));
				menuButton.setTextColor(Color.parseColor("#ffffff"));
				menuButton.setTypeface(null, Typeface.BOLD);
				menuButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
				menuButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_insert_chart_white_24dp, 0, 0, 0);
				menuButton.setCompoundDrawablePadding(10);
				menuButton.setPadding(25,0,0,0);
				linear.addView(menuButton,layoutParams);

				//add tag to array list
				homeButtonTags.add(NAV_DRAWER_ITEM_REPORTS);
			}
		}

		if(features.getData().getRssFeed() != null){
			if(features.getData().getRssFeed().equals("1")){
				Button menuButton = new Button(this);
				menuButton.setHeight(50);
				menuButton.setWidth(50);
				menuButton.setTag(NAV_DRAWER_ITEM_DESKTOP_ALERTS);
				menuButton.setText(R.string.desktop_alerts_menu);
				menuButton.setOnClickListener(this);
				menuButton.setBackgroundColor(Color.parseColor("#0d64aa"));
				menuButton.setBackground(getResources().getDrawable(R.drawable.bg_home_buttons));
				menuButton.setTextColor(Color.parseColor("#ffffff"));
				menuButton.setTypeface(null, Typeface.BOLD);
				menuButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
				menuButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_desktop_mac_white_24dp, 0, 0, 0);
				menuButton.setCompoundDrawablePadding(10);
				menuButton.setPadding(25,0,0,0);
				linear.addView(menuButton,layoutParams);

				//add tag to array list
				homeButtonTags.add(NAV_DRAWER_ITEM_DESKTOP_ALERTS);
			}
		}

//		//TODO update toggle
//		if(features.getData().getSbAlerts() != null){
//			if(features.getData().getSbAlerts().equals("1")){
//				Button menuButton = new Button(this);
//				menuButton.setHeight(50);
//				menuButton.setWidth(50);
//				menuButton.setTag(NAV_DRAWER_SBREPORTS);
//				menuButton.setText("View Smart Button Reports");
//				menuButton.setOnClickListener(this);
//				menuButton.setBackgroundColor(Color.parseColor("#0d64aa"));
//				menuButton.setBackground(getResources().getDrawable(R.drawable.bg_home_buttons));
//				menuButton.setTextColor(Color.parseColor("#ffffff"));
//				menuButton.setTypeface(null, Typeface.BOLD);
//				menuButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
//				menuButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_desktop_mac_white_24dp, 0, 0, 0);
//				menuButton.setCompoundDrawablePadding(10);
//				menuButton.setPadding(25,0,0,0);
//				linear.addView(menuButton,layoutParams);
//
//				//add tag to array list
//				homeButtonTags.add(NAV_DRAWER_SBREPORTS);
//			}
//		} else {
//			Log.d("tag", "SB Toggle Is Null");
//		}
//
//		if(features.getData().getSbAlerts() != null){
//			if(features.getData().getSbAlerts().equals("1")){
//				Button menuButton = new Button(this);
//				menuButton.setHeight(50);
//				menuButton.setWidth(50);
//				menuButton.setTag(NAV_DRAWER_SBALERTS);
//				menuButton.setText(R.string.title_sendSBAlerts);
//				menuButton.setOnClickListener(this);
//				menuButton.setBackgroundColor(Color.parseColor("#0d64aa"));
//				menuButton.setBackground(getResources().getDrawable(R.drawable.bg_home_buttons));
//				menuButton.setTextColor(Color.parseColor("#ffffff"));
//				menuButton.setTypeface(null, Typeface.BOLD);
//				menuButton.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
//				menuButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_desktop_mac_white_24dp, 0, 0, 0);
//				menuButton.setCompoundDrawablePadding(10);
//				menuButton.setPadding(25,0,0,0);
//				linear.addView(menuButton,layoutParams);
//
//				//add tag to array list
//				homeButtonTags.add(NAV_DRAWER_SBALERTS);
//			}
//		} else{
//			Log.d("tag", "SB Toggle Is Null");
//		}

		//add HOME tag to array list at index 0
		homeButtonTags.add(0,NAV_DRAWER_ITEM_HOME);
		Log.d("Data: ","\nArray List Tags: " + homeButtonTags);
	}

	protected void openActivity(int position, int id) {
		Log.d(LOG_TAG, "position = " + position);

		/**
		 * We can set title & itemChecked here but as this BaseActivity is parent for other activity,
		 * So whenever any activity is going to launch this BaseActivity is also going to be called and
		 * it will reset this value because of initialization in onCreate method.
		 * So that we are setting this in child activity.
		 */
		mDrawerList.setItemChecked(position, true);
//		setTitle(listArray[position]);
		BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.
		switch (id) {
			case NAV_DRAWER_ITEM_HOME:
				startActivity(new Intent(this, HomeActivity.class));
				break;
			case NAV_DRAWER_ITEM_1STEP_ALERT:
				startActivity(new Intent(this, OneStepAlertActivity.class));
				break;
			case NAV_DRAWER_ITEM_1STEP_CALL:
				startActivity(new Intent(this, OneStepCallActivity.class));
				break;
			case NAV_DRAWER_ITEM_TTS:
				startActivity(new Intent(this, TextToSpeechActivity.class));
				break;
			case NAV_DRAWER_ITEM_REPORTS:
				startActivity(new Intent(this, ReportsActivity.class));
				break;
			case NAV_DRAWER_ITEM_DESKTOP_ALERTS:
				startActivity(new Intent(this, DesktopAlertsActivity.class));
				break;
//			case NAV_DRAWER_SBREPORTS:
//				//start send smart button alerts activity
//				startActivity(new Intent(this, SmartButtonReportsActivity.class));
//				break;
//			case NAV_DRAWER_SBALERTS:
//				//start send smart button alerts activity
//				startActivity(new Intent(this, SendSmartButtonAlertsActivity.class));
//				break;
			case NAV_DRAWER_ITEM_BNOTIFIED:
				startActivity(new Intent(this, BNotifiedHome.class));
				break;
			case NAV_DRAWER_CAMP_STATUS:
				startActivity(new Intent(this, CampaignStatusActivity.class));
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		//Get Tag of button clicked
		Object tag = v.getTag();

		//Use tag to get the index(position) of button clicked
		int position = homeButtonTags.indexOf((int)tag);
//		Log.d(LOG_TAG, "Button Position = " + position);

		//use index and tag to open activity and highlight correct side bar item
		openActivity(position,(int)tag);

		//int buttonCount =linear.getChildCount();
		//Log.d(LOG_TAG, "Button Count = " + buttonCount);
	//	Toast.makeText(context,"Linear.Count: " + buttonCount, Toast.LENGTH_SHORT).show();

		/*if(Integer.valueOf((int)tag) == NAV_DRAWER_ITEM_REPORTS)
		{
			//openActivity(buttonCount,Integer.valueOf((int)tag));
			openActivity(Integer.valueOf((int)tag));
		}
		else
		{
		openActivity(Integer.valueOf((int)tag));
		}*/
	}
}


