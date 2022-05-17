package messagelogix.com.k12campusalerts.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.bnotified.BNotifiedHome;
import messagelogix.com.k12campusalerts.activities.desktop.DesktopAlertsActivity;
import messagelogix.com.k12campusalerts.activities.onestepalert.OneStepAlertActivity;
import messagelogix.com.k12campusalerts.activities.onestepcall.OneStepCallActivity;
import messagelogix.com.k12campusalerts.activities.reports.ReportsActivity;
import messagelogix.com.k12campusalerts.activities.smartbuttonalerts.SendSmartButtonAlertsActivity;
import messagelogix.com.k12campusalerts.activities.smartbuttonreports.SmartButtonReportsActivity;
import messagelogix.com.k12campusalerts.activities.tts.TextToSpeechActivity;
import messagelogix.com.k12campusalerts.adapters.NavDrawerListAdapter;
import messagelogix.com.k12campusalerts.models.Features;
import messagelogix.com.k12campusalerts.models.GroupList;
import messagelogix.com.k12campusalerts.models.NavDrawerItem;
import messagelogix.com.k12campusalerts.utils.Config;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.Preferences;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author dipenp
 *         <p/>
 *         This activity will add Navigation Drawer for our application and all the code related to navigation drawer.
 *         We are going to extend all our other activites from this BaseActivity so that every activity will have Navigation Drawer in it.
 *         This activity layout contain one frame layout in which we will add our child activity layout.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = BaseActivity.class.getSimpleName();
    /**
     * Frame layout: Which is going to be used as parent layout for child activity layout.
     * This layout is protected so that child activity can access this
     */
    protected FrameLayout frameLayout;

    /**
     * ListView to add navigation drawer item in it.
     * We have made it protected to access it in child class. We will just use it in child class to make item selected according to activity opened.
     */
    protected ListView mDrawerList;

    /**
     * Static variable for selected item position. Which can be used in child activity to know which item is selected from the list.
     */
    protected static int position;

    /**
     * This flag is used just to check that launcher activity is called first time
     * so that we can open appropriate Activity on launch and make list item position selected accordingly.
     */
    private static boolean isLaunch = true;

    /**
     * Base layout node of this Activity.
     */
    private DrawerLayout mDrawerLayout;

    /**
     * Drawer listner class for drawer open, close etc.
     */
    private ActionBarDrawerToggle actionBarDrawerToggle;

    /**
     * List item array for navigation drawer items.
     */
    public static List<NavDrawerItem> navDrawerItems;

    private TypedArray navMenuIcons;


    protected static final int NAV_DRAWER_ITEM_HOME = 0;
    protected static final int NAV_DRAWER_ITEM_1STEP_ALERT = 1;
    protected static final int NAV_DRAWER_ITEM_1STEP_CALL = 2;
    protected static final int NAV_DRAWER_ITEM_TTS = 3;
    protected static final int NAV_DRAWER_ITEM_BNOTIFIED = 4;
    protected static final int NAV_DRAWER_ITEM_REPORTS = 5;
    protected static final int NAV_DRAWER_CAMP_STATUS = 6;
    protected static final int NAV_DRAWER_ITEM_DESKTOP_ALERTS = 7;
    protected static final int NAV_DRAWER_SBREPORTS = 8;
    protected static final int NAV_DRAWER_SBALERTS = 9;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_base_layout);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        navDrawerItems = new ArrayList<>();
        navDrawerItems.addAll(getDrawerItems());

        // set up the drawer's list view with items and click listener
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listArray));
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                NavDrawerItem navItem = navDrawerItems.get(position);
                openActivity(position, navItem.id);
            }
        });


        Toolbar toolbar = new Toolbar(this);

        // ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                final ActionBar supportActionBar = getSupportActionBar();
                if(supportActionBar!=null){
                    try{
                        supportActionBar.setTitle(navDrawerItems.get(position).getTitle());
                    }
                    catch (IndexOutOfBoundsException e){

                    }
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                final ActionBar supportActionBar = getSupportActionBar();
                if(supportActionBar!=null){
                    supportActionBar.setTitle(getString(R.string.app_name));
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);


        /**
         * As we are calling BaseActivity from manifest file and this base activity is intended just to add navigation drawer in our app.
         * We have to open some activity with layout on launch. So we are checking if this BaseActivity is called first time then we are opening our first activity.
         * */
        if (isLaunch) {
            /**
             *Setting this flag false so that next time it will not open our first activity.
             *We have to use this flag because we are using this BaseActivity as parent activity to our other activity.
             *In this case this base activity will always be call when any child activity will launch.
             */
            isLaunch = false;
            openActivity(0);
        }


        // enable ActionBar app icon to behave as action to toggle nav drawer
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


        InputMethodManager inputManager =
                (InputMethodManager) context.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusView = this.getCurrentFocus();
        if (focusView !=null){
            inputManager.hideSoftInputFromWindow(
                    focusView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        Log.d("End","END OF BASE ONCREATE");

    }

    /**
     * @param position Launching activity when any list item is clicked.
     */
    protected void openActivity(int position) {

        Log.d(LOG_TAG, "position = " + position);

        /**
         * We can set title & itemChecked here but as this BaseActivity is parent for other activity,
         * So whenever any activity is going to launch this BaseActivity is also going to be called and
         * it will reset this value because of initialization in onCreate method.
         * So that we are setting this in child activity.
         */
		mDrawerList.setItemChecked(navDrawerItems.get(BaseActivity.position).id, true);
		//setTitle(navDrawerItems.get(position).getTitle());
        mDrawerLayout.closeDrawer(mDrawerList);
        //BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.
        switch (position) {
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
            case NAV_DRAWER_ITEM_BNOTIFIED:
                startActivity(new Intent(this, BNotifiedHome.class));
                break;
            case NAV_DRAWER_CAMP_STATUS:
                startActivity(new Intent(this, CampaignStatusActivity.class));
                break;
//            case NAV_DRAWER_SBREPORTS:
//                //start activity
//                startActivity(new Intent(this, SmartButtonReportsActivity.class));
//                break;
//            case NAV_DRAWER_SBALERTS:
//                //start activity
//                startActivity(new Intent(this, SendSmartButtonAlertsActivity.class));
//                break;
            default:
                break;
        }
    }

    /**
     * @param position Launching activity when any list item is clicked.
     */
    protected void openActivity(int position, int id) {

        Log.d(LOG_TAG, "position = " + position);

        /**
         * We can set title & itemChecked here but as this BaseActivity is parent for other activity,
         * So whenever any activity is going to launch this BaseActivity is also going to be called and
         * it will reset this value because of initialization in onCreate method.
         * So that we are setting this in child activity.
         */
        mDrawerList.setItemChecked(position, true);
        setTitle(navDrawerItems.get(position).getTitle());
        mDrawerLayout.closeDrawer(mDrawerList);
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
            case NAV_DRAWER_ITEM_BNOTIFIED:
                startActivity(new Intent(this, BNotifiedHome.class));
                break;
            case NAV_DRAWER_CAMP_STATUS:
                startActivity(new Intent(this, CampaignStatusActivity.class));
                break;
//            case NAV_DRAWER_SBREPORTS:
//                //start activity
//                startActivity(new Intent(this, SmartButtonReportsActivity.class));
//                break;
//            case NAV_DRAWER_SBALERTS:
//                //start activity
//                startActivity(new Intent(this, SendSmartButtonAlertsActivity.class));
//                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(BaseActivity.this, SettingsActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                Intent intent1 = new Intent(BaseActivity.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Preferences.putBoolean(Config.IS_LOGGED_IN, false);
                startActivity(intent1);
                return true;
//            case R.id.action_change_language:
//                showChangeLangDialog();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_show, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = (Spinner) dialogView.findViewById(R.id.spinner1);

        dialogBuilder.setTitle(getString(R.string.select_language));

        dialogBuilder.setPositiveButton(getString(R.string.change), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int langpos = spinner1.getSelectedItemPosition();
                switch(langpos) {
                    case 0: //English
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                        setLangRecreate("en");
                        return;
                    case 1: //French
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "fr").commit();
                        setLangRecreate("fr");
                        return;
                    case 2: //Spanish
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "es").commit();
                        setLangRecreate("es");
                        return;
                    case 3: //Chinese
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "zh").commit();
                        setLangRecreate("zh");
                        return;
                    case 4: //Arabic
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "zh").commit();
                        setLangRecreate("ar");
                        return;
                    case 5: //Japanese
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "zh").commit();
                        setLangRecreate("ja");
                        return;
                    default: //By default set to english
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                        setLangRecreate("en");
                        return;
                }
            }
        });
        dialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void setLangRecreate(String langval) {
        Toast.makeText(this, "Language has been updated!", Toast.LENGTH_SHORT).show();
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /* We can override onBackPressed method to toggle navigation drawer*/
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            mDrawerLayout.openDrawer(mDrawerList);
        }
    }

    private List<NavDrawerItem> getDrawerItems() {
        List<NavDrawerItem> items = new ArrayList<>();
        items.add(new NavDrawerItem(NAV_DRAWER_ITEM_HOME, getString(R.string.home), R.drawable.ic_home_white_24dp));
        Features features = (Features) new FunctionHelper(context).retrieveObject(Features.class);
        if(features != null){

            if(features.getData().get1StepAlert() != null){
                if(features.getData().get1StepAlert().equals("1")){
                    items.add(new NavDrawerItem(NAV_DRAWER_ITEM_1STEP_ALERT, getString(R.string.one_step_alert_short), R.drawable.ic_email_white_24dp));
                }
            }


            if(features.getData().get1StepCall() != null){
                if(features.getData().get1StepCall().equals("1")){
                    items.add(new NavDrawerItem(NAV_DRAWER_ITEM_1STEP_CALL, getString(R.string.one_step_call), R.drawable.ic_mic_white_24dp));
                }
            }


            if(features.getData().getTextToSpeech() != null){
                if(features.getData().getTextToSpeech().equals("1")){
                    items.add(new NavDrawerItem(NAV_DRAWER_ITEM_TTS, getString(R.string.text_to_speech), R.drawable.ic_phone_white_24dp));
                }
            }


            if(features.getData().getBNotified() != null){
                if(features.getData().getBNotified().equals("1")){
                    items.add(new NavDrawerItem(NAV_DRAWER_ITEM_BNOTIFIED, getString(R.string.b_notified), R.drawable.ic_phonelink_ring_white_24dp));
                }
            }

            if(features.getData().getViewReports() != null){
                if(features.getData().getViewReports().equals("1")){
                    items.add(new NavDrawerItem(NAV_DRAWER_ITEM_REPORTS, getString(R.string.reports), R.drawable.ic_insert_chart_white_24dp));
                }
            }


            if(features.getData().getRssFeed() != null){
                if(features.getData().getRssFeed().equals("1")){
                    items.add(new NavDrawerItem(NAV_DRAWER_ITEM_DESKTOP_ALERTS, getString(R.string.desktop_alerts), R.drawable.ic_desktop_mac_white_24dp));
                }
            }

//            //TODO update the toggle
//            if(features.getData().getSbAlerts() != null){
//                if(features.getData().getSbAlerts().equals("1")){
//                    items.add(new NavDrawerItem(NAV_DRAWER_SBREPORTS, "View Smart Button Reports", R.drawable.ic_desktop_mac_white_24dp));
//                }
//            }
//
//            else{
//                Log.d("tag", "Toggle Is Null");
//            }
//
//            if(features.getData().getSbAlerts() != null){
//                if(features.getData().getSbAlerts().equals("1")){
//                    items.add(new NavDrawerItem(NAV_DRAWER_SBALERTS, getString(R.string.send_sb_alerts), R.drawable.ic_desktop_mac_white_24dp));
//                }
//            }

            else{
                Log.d("tag", "Toggle Is Null");
            }


            //leave this at the bottom always
            if(features.getData().get1StepAlert() != null){
                if(features.getData().get1StepAlert().equals("1") || features.getData().get1StepCall().equals("1") || features.getData().getTextToSpeech().equals("1")){
                    items.add(new NavDrawerItem(NAV_DRAWER_CAMP_STATUS, "Campaign Status", R.drawable.ic_schedule_white_36dp));
                }
            }
        }

        return items;
    }

    public interface UserGroupListService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<GroupList> request(@FieldMap HashMap<String, String> parameters);
    }
}
