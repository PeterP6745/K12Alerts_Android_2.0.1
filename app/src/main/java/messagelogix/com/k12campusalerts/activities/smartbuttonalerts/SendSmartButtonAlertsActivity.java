package messagelogix.com.k12campusalerts.activities.smartbuttonalerts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.activities.BaseActivity;
import messagelogix.com.k12campusalerts.adapters.CustomSpinnerAdapter_SelectIncidents;
import messagelogix.com.k12campusalerts.models.SpinnerItem_Building;
import messagelogix.com.k12campusalerts.models.SpinnerItem_Incidents;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.models.UserGroup;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;

/**
 * Created by Program on 3/6/2018.
 */

public class SendSmartButtonAlertsActivity extends BaseActivity {
    private User user;

    private Button btnSendAlert;
    private Button btnClearAlert;
    private Button btnSelectBuildings;
    private Button btnSelectUserGroups;

    private Spinner spinnerIncidents;
    private EditText etAlertMessage;

    private CheckBox checkboxSendToApps;
    private CheckBox checkboxSendToDesks;

    private Context context;

    private List<String> buildingIds = new ArrayList<>();
    private ArrayList<String> buildingNames = new ArrayList<>();
    private List<String> selectedBuildingIds = new ArrayList<>();
    private List<String> selectedBuildingNames = new ArrayList<>();
    private boolean[] defaultBuildingArrangement;

    private List<String> userGroupIds = new ArrayList<>();
    private ArrayList<String> userGroupNames = new ArrayList<>();
    private List<String> selectedUserGroupIds = new ArrayList<>();
    private List<String> selectedUserGroupNames = new ArrayList<>();
    private boolean[] defaultUserGroupArrangement;

    private String selectedBuildingName = "";

    private final String allBuildingsStr = "All Location Groups";
    private final String buildingButtonDefaultText = "Select Location Group(s)";
    private final String userGroupButtonDefaultText = "Select User Type Group(s)";

    private static final int MAX_CHAR_COUNT = 255;
    private TextView broadcastCounterLabel;

    private int broadcastSettingsDataCallCounter = 3;
    ProgressDialog activityProgressDialog;

    private int broadcastProgressCounter = 0;
    ProgressDialog broadcastProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflate the layout
        this.getLayoutInflater().inflate(R.layout.activity_sb_alerts, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(navDrawerItems.get(position).getTitle());
        context = this;

        //get user data
        user = (User) new FunctionHelper(this).retrieveObject(User.class);

        selectedUserGroupIds.add("1");

        initComponents();
        new GetIncidentsTask().execute();
        new GetBuildingsTask().execute();
        new GetUserGroupsTask().execute();
    }

    private void initComponents(){
        activityProgressDialog = new ProgressDialog(this);
        activityProgressDialog.setMessage("Loading your alert settings, please wait...");
        activityProgressDialog.setIndeterminate(true);
        activityProgressDialog.setCancelable(false);
        activityProgressDialog.setCanceledOnTouchOutside(false);
        activityProgressDialog.show();

        broadcastProgressDialog = new ProgressDialog(this);

        btnSendAlert = this.findViewById(R.id.btn_send_sb_alerts);
        btnClearAlert = this.findViewById(R.id.btn_clear_sb_alerts);
        btnSelectBuildings = this.findViewById(R.id.btn_select_buildings);

        btnSelectUserGroups = this.findViewById(R.id.btn_select_usergroups);
        btnSelectUserGroups.setText(userGroupButtonDefaultText);

        broadcastCounterLabel = findViewById(R.id.broadcastCounterLabel);
        String charCount = "(" + MAX_CHAR_COUNT + ")";
        broadcastCounterLabel.setText(charCount);

        etAlertMessage = this.findViewById(R.id.et_sb_alert_message);
        etAlertMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHAR_COUNT)});
        etAlertMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Update the counter
                String charRemaining = String.valueOf(MAX_CHAR_COUNT - etAlertMessage.getText().length());
                broadcastCounterLabel.setText("("+charRemaining+")");
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        checkboxSendToApps = this.findViewById(R.id.checkbox_send2apps);
        checkboxSendToDesks = this.findViewById(R.id.checkbox_send2desk);

        spinnerIncidents = this.findViewById(R.id.spinner_sb_desktop_incidents);

        initComponentEvents();
    }

    private void initComponentEvents(){
        /*SEND BUTTON CLICK*/
        btnSendAlert.setOnClickListener(v -> {
            //grab data before resetting content after validation
            String alert = etAlertMessage.getText().toString();

            if(selectedBuildingIds.size() > 0 && selectedBuildingIds.get(0).equals(allBuildingsStr)) {
                Log.d("sendingBroadcast","before removing first index: "+selectedBuildingIds);
                selectedBuildingIds.remove(0);
                Log.d("sendingBroadcast","after removing first index: "+selectedBuildingIds);
            }

            String buildIds = TextUtils.join(", ",selectedBuildingIds);
            String userGroups = TextUtils.join(", ", selectedUserGroupIds);

            //validate alert message
            boolean alertIsValid = validateAlert(alert);
            if(alertIsValid){
                //check checkboxes
                boolean appsSelected = checkboxSendToApps.isChecked();
                boolean desktopsSelected = checkboxSendToDesks.isChecked();

                //check if push to app only feature is active
//                    if(pushToAppOnly){
//                        broadcastProgressDialog.setMessage("Sending your broadcast to other app users, please wait...");
//                        broadcastProgressDialog.setIndeterminate(true);
//                        broadcastProgressDialog.setCancelable(false);
//                        broadcastProgressDialog.setCanceledOnTouchOutside(false);
//                        broadcastProgressDialog.show();
//                        broadcastProgressCounter = 1;
//                        SendAppAlerts sendAppAlerts = new SendAppAlerts(alert, buildIds, buildingName, userGroups);
//                        sendAppAlerts.execute();
//                        return;
//                    }

                Log.d("selectedGroups","userGroupIds - " + userGroups);
                Log.d("selectedGroups", "locationgroupIds - " + buildIds);
                if(appsSelected & desktopsSelected) {
                    broadcastProgressDialog.setMessage("Sending your alert to the designated user group(s) of the SmartButton® mobile and desktop app, please wait...");
                    broadcastProgressDialog.setIndeterminate(true);
                    broadcastProgressDialog.setCancelable(false);
                    broadcastProgressDialog.setCanceledOnTouchOutside(false);
                    broadcastProgressDialog.show();
                    broadcastProgressCounter = 2;

                    new SendAppAlerts(alert, buildIds, userGroups).execute();
                    new SendDesktopAlerts(alert, buildIds, userGroups).execute();
                } else if(appsSelected) {
                    broadcastProgressDialog.setMessage("Sending your alert to the designated user group(s) of the SmartButton® mobile app, please wait...");
                    broadcastProgressDialog.setIndeterminate(true);
                    broadcastProgressDialog.setCancelable(false);
                    broadcastProgressDialog.setCanceledOnTouchOutside(false);
                    broadcastProgressDialog.show();
                    broadcastProgressCounter = 1;
                    //send push to apps
                    new SendAppAlerts(alert, buildIds, userGroups).execute();
                } else {
                    broadcastProgressDialog.setMessage("Sending your alert to the designated user group(s) of the SmartButton® desktop app, please wait...");
                    broadcastProgressDialog.setIndeterminate(true);
                    broadcastProgressDialog.setCancelable(false);
                    broadcastProgressDialog.setCanceledOnTouchOutside(false);
                    broadcastProgressDialog.show();
                    broadcastProgressCounter = 1;
                    //send alert to desktops
                    new SendDesktopAlerts(alert, buildIds, userGroups).execute();
                }
            }
        });

        /*CLEAR BUTTON CLICK*/
        btnClearAlert.setOnClickListener(v -> {
            if(selectedBuildingIds.size()>1){
                for(int i=0; i<selectedBuildingIds.size();i++){
                    ClearRSSFeed clearRSSFeed = new ClearRSSFeed(selectedBuildingIds.get(i));
                    clearRSSFeed.execute();
                }
            }
            else if(selectedBuildingIds.size() == 0){
                ClearRSSFeed clearRSSFeed = new ClearRSSFeed("0");
                clearRSSFeed.execute();
            }
            else {
                ClearRSSFeed clearRSSFeed = new ClearRSSFeed(selectedBuildingIds.get(0));
                clearRSSFeed.execute();
            }

            resetContent();
        });

        btnSelectBuildings.setOnClickListener(view -> selectBuildings());

        /*Select Groups Click*/
        btnSelectUserGroups.setOnClickListener(v -> selectUserGroups());
    }

    private void dismissActivityProgressDialog() {
        broadcastSettingsDataCallCounter--;

        switch(broadcastSettingsDataCallCounter) {
            case 2:
                activityProgressDialog.setMessage("Loading your alert settings, nearly there...");
                break;
            case 1:
                activityProgressDialog.setMessage("Loading your alert settings, almost done...");
                break;
            default:
                break;
        }

        Log.d("SendBroadcastsActivity","The value of the counter is: "+broadcastSettingsDataCallCounter);
        if(broadcastSettingsDataCallCounter == 0) {
            resetContent();
            activityProgressDialog.dismiss();
        }
    }

    private void selectBuildings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Group(s) for Notifications");

        builder.setMultiChoiceItems(buildingNames.toArray(new CharSequence[buildingNames.size()]), defaultBuildingArrangement, (dialog, selectedIndex, isChecked) -> {
            // user checked or unchecked a box
            //Log.d("onClick","selectedIndex: "+selectedIndex);
            if(isChecked) {
                if(selectedIndex == 0) {
                    selectedBuildingIds.add(0,allBuildingsStr);
                    selectedBuildingNames.add(0,allBuildingsStr);
                    defaultBuildingArrangement[0] = true;

                    for(int i=1;i<buildingNames.size();i++) {
                        //((AlertDialog) dialog).getListView().getChildAt(i).setBackgroundColor(Color.BLUE);
                        if(!selectedBuildingIds.contains(buildingIds.get(i))) {
                            selectedBuildingIds.add(buildingIds.get(i));
                            selectedBuildingNames.add(buildingNames.get(i));
                            defaultBuildingArrangement[i] = true;
                        }

                        ((AlertDialog) dialog).getListView().setItemChecked(i, true);
                    }
                } else {
                    String locationGroupId = buildingIds.get(selectedIndex);
                    String locationGroupName = buildingNames.get(selectedIndex);
                    selectedBuildingIds.add(locationGroupId);
                    selectedBuildingNames.add(locationGroupName);
                    defaultBuildingArrangement[selectedIndex] = true;

                    boolean allLocationsSelected = selectedBuildingIds.size() == buildingIds.size() - 1;
                    if(allLocationsSelected) {
                        Log.d("locGroup","all locations were selected manually: "+allLocationsSelected);
                        defaultBuildingArrangement[0] = true;
                        ((AlertDialog) dialog).getListView().setItemChecked(0, true);

                        selectedBuildingIds.add(0,allBuildingsStr);
                        selectedBuildingNames.add(0,allBuildingsStr);
                    }
                }
            } else {
                if(selectedIndex == 0) {
                    defaultBuildingArrangement[0] = false;
                    for(int i=1;i<buildingNames.size();i++) {
                        defaultBuildingArrangement[i] = false;
                        ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                    }
                    selectedBuildingIds.clear();
                    selectedBuildingNames.clear();
                }
                else {
                    int indexOfLocationGroupId = selectedBuildingIds.indexOf(buildingIds.get(selectedIndex));
                    int indexOfLocationGroupName = selectedBuildingNames.indexOf( buildingNames.get(selectedIndex));
                    selectedBuildingIds.remove(indexOfLocationGroupId);
                    selectedBuildingNames.remove(indexOfLocationGroupName);
                    defaultBuildingArrangement[selectedIndex] = false;

                    boolean allLocationsSelected = selectedBuildingIds.contains(allBuildingsStr);
                    Log.d("locGroup","top option is selected: "+allLocationsSelected);
                    if(allLocationsSelected) {
                        defaultBuildingArrangement[0] = false;
                        ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                        selectedBuildingIds.remove(0);
                        selectedBuildingNames.remove(0);
                    }
                }
            }
            Log.d("locGroup","selectedLocationGroupIds: "+selectedBuildingIds.toString());
            Log.d("locGroup","selectedLocationGroupNames: "+selectedBuildingNames.toString());

            String displayText;
            if(selectedBuildingIds.size() == 0) {
                displayText = buildingButtonDefaultText;
                selectedBuildingName = displayText;
            }
            else if(selectedBuildingIds.size() == buildingIds.size()){
                displayText = allBuildingsStr;
                selectedBuildingName = displayText;
            }
            else if(selectedBuildingIds.size()==1){
                displayText = selectedBuildingNames.get(0);
                selectedBuildingName = displayText;
            }
            else {
                displayText = "Multiple Location Groups";
                selectedBuildingName = displayText;
            }

            btnSelectBuildings.setText(displayText);
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("Done", (dialog, which) -> {
            Log.d("locationGroup","selectedLocationGroupIds: "+selectedBuildingIds);
            Log.d("locationGroup","selectedLocationGroupNames: "+selectedBuildingNames);
        });

        //builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("Clear Selection", (dialog, which) -> {});

        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            for(int i=0; i<buildingNames.size();i++) {
                defaultBuildingArrangement[i] = false;
                dialog.getListView().setItemChecked(i,false);
            }
            selectedBuildingIds.clear();
            selectedBuildingNames.clear();
            Log.d("clearSelection","locationGroupIds: "+selectedBuildingIds.toString());

            btnSelectBuildings.setText(buildingButtonDefaultText);
        });
    }

    private void selectUserGroups() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(userGroupButtonDefaultText);

        // add a checkbox list
        builder.setMultiChoiceItems(userGroupNames.toArray(new CharSequence[userGroupNames.size()]), defaultUserGroupArrangement, (dialog, selectedIndex, isChecked) -> {
            // user checked or unchecked a box
            if(selectedIndex == 0) {
                ((AlertDialog) dialog).getListView().setItemChecked(selectedIndex, true);
                return;
            }

            if(isChecked) {
                String userGroupId = userGroupIds.get(selectedIndex);
                String userGroupName = userGroupNames.get(selectedIndex);
                //Log.d("onClick","id at index: "+userGroupId);
                selectedUserGroupIds.add(userGroupId);
                selectedUserGroupNames.add(userGroupName);
                defaultUserGroupArrangement[selectedIndex] = true;
            } else {
                int indexOfUserGroupId = selectedUserGroupIds.indexOf(userGroupIds.get(selectedIndex));
                int indexOfUserGroupName = selectedUserGroupNames.indexOf(userGroupNames.get(selectedIndex));
                selectedUserGroupIds.remove(indexOfUserGroupId);
                selectedUserGroupNames.remove(indexOfUserGroupName);
                defaultUserGroupArrangement[selectedIndex] = false;
            }
            Log.d("onClick","selectedUsrTypeIds: "+selectedUserGroupIds.toString());
            Log.d("onClick","selectedUsrTypenames: "+selectedUserGroupNames.toString());

            String displayText;
//                if(selectedUsrTypeIds.size() == 1){
//                    displayText = "(Optional) Select User Type(s)";
//                }
            /*else*/ if(selectedUserGroupIds.size()==1){
                displayText = selectedUserGroupNames.get(0);
            }
            else {
                displayText = "Multiple User Type Groups Selected";
            }

            btnSelectUserGroups.setText(displayText);
            Log.d("empty","allowing selection?");
            //return; // allow selection
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("Done", (dialog, which) -> {
            Log.d("onClick","selectedUsrTypeIds: "+selectedUserGroupIds.toString());
            Log.d("onClick","selectedUsrTypenames: "+selectedUserGroupNames.toString());
        });

        builder.setNeutralButton("Clear Selection", (dialog, which) -> {});

        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> {
            //((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(Color.BLUE);
            ((AlertDialog) dialog1).getListView().setItemChecked(0, true);
            ((AlertDialog) dialog1).getListView().getChildAt(0).setBackgroundColor(Color.rgb(125,210,125));
        });
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            for(int i=1; i<defaultUserGroupArrangement.length;i++) {
                defaultUserGroupArrangement[i] = false;
                dialog.getListView().setItemChecked(i,false);
            }
            selectedUserGroupIds = selectedUserGroupIds.subList(0,1);
            selectedUserGroupNames = selectedUserGroupNames.subList(0,1);
        });
    }

    private Boolean validateAlert(String alertMessage) {
        //check if message is empty
        boolean validAlert = !FunctionHelper.isNullOrEmpty(alertMessage);
        /*--1) Check if alert is entered--*/
        if(validAlert) {
            //alert is valid, check if an option has been selected
            boolean optionSelected = checkboxSendToDesks.isChecked() || checkboxSendToApps.isChecked();
//            if(pushToAppOnly){
//                optionSelected = true;
//            }
            /*--2) Check if option is selected--*/
            if(optionSelected) {
                boolean isBuildingValid = selectedBuildingIds.size() > 0;
                /*--3) Check if group is selected--*/
                if(isBuildingValid) {
                    return true;
                } else {
                    displayMessageToUser("Please select a location group(s) that should receive your SmartButton® alert.", Toast.LENGTH_SHORT);
                    return false;
                }

            } else {
                //display a message to user about no option selected
                displayMessageToUser("Please select at least one delivery option for your alert.", Toast.LENGTH_SHORT);
                return false;
            }
        }
        else {
            //display a message to user about an empty message
            displayMessageToUser("Please enter a message to send.", Toast.LENGTH_SHORT);
            return false;
        }
    }

    private void resetContent(){
        etAlertMessage.setText("");

        checkboxSendToApps.setChecked(false);
        checkboxSendToDesks.setChecked(false);

        btnSelectBuildings.setText(buildingButtonDefaultText);
        btnSelectUserGroups.setText("Default Emergency Contact");

        //Rest selected values
        selectedBuildingIds = new ArrayList<>();
        selectedBuildingNames = new ArrayList<>();
//        selectedIndices = new Integer[]{};

        if(buildingIds.size() > 0) {
            defaultBuildingArrangement = new boolean[buildingIds.size()];
            Arrays.fill(defaultBuildingArrangement, false);
        } else {
            defaultBuildingArrangement = new boolean[]{};
        }

        selectedUserGroupNames = new ArrayList<>();
        if(userGroupNames.size() > 0)
            selectedUserGroupNames.add(userGroupNames.get(0));

        selectedUserGroupIds = new ArrayList<>();
        selectedUserGroupIds.add("1");

        if(userGroupIds.size() > 0) {
            defaultUserGroupArrangement = new boolean[userGroupIds.size()];
            for(int i=0; i<defaultUserGroupArrangement.length; i++) {
                defaultUserGroupArrangement[i] = i == 0;
            }
        } else
            defaultUserGroupArrangement = new boolean[]{};

        spinnerIncidents.setSelection(0);

        //Single Selection reset (early version)
        selectedBuildingName = "";
//        selectedBuildingId = "";
    }

    /**==================API CALLS==================**/
    /**
     * Get Buildings
     */
    private class GetBuildingsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {}

        @Override
        protected Boolean doInBackground(Void... voidParams) {
            HashMap<String, String> params = new HashMap<>();
            params.put("controller", "Report");
            params.put("action", "GetBuildingsSb");
            params.put("accountId", user.getData().getAcctId());
            params.put("typeId", "0");

            String jsonData = FunctionHelper.apiCaller(params);

            Log.d("SendSBAlertsActivity","getbuildingstask() --> response: "+jsonData);

            boolean success = false;
            try {
                JSONObject jsonobject = new JSONObject(jsonData);
                success = jsonobject.getBoolean("success");
                if (success) {
                    ArrayList<SpinnerItem_Building> groups = new ArrayList<>();
                    ArrayList<String> nameList = new ArrayList<>();
                    ArrayList<String> idList = new ArrayList<>();

                    JSONArray jsonarray = jsonobject.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobject = jsonarray.getJSONObject(i);

                        String name = jsonobject.getString("value");
                        String id = jsonobject.getString("id");

                        groups.add(new SpinnerItem_Building(name,id));
                        buildingIds.add(id);
                        buildingNames.add(name);

                        // Populate spinner
                        nameList.add(jsonobject.optString("value"));
                        idList.add(jsonobject.optString("id"));
                    }

                    nameList.add(0,allBuildingsStr);
                    idList.add(0, "0");

                    //spinner items
                    groups.add(0, new SpinnerItem_Building(nameList.get(0), idList.get(0)));
                    buildingIds.add(0,allBuildingsStr);
                    buildingNames.add(0,allBuildingsStr);
                    defaultBuildingArrangement = new boolean[buildingIds.size()];
                    Arrays.fill(defaultBuildingArrangement,false);

                    //give the group list the groups items
//                    groupList = groups;

                    Log.d("fromBackend","locationGroupIds: "+buildingIds);
                    Log.d("fromBackend","locationGroupNames: "+buildingNames);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            dismissActivityProgressDialog();
        }
    }

    /**
     * Get Buildings
     */
    private class GetIncidentsTask extends AsyncTask<Void, Void, Boolean> {
        ArrayList<SpinnerItem_Incidents> spinnerItems = new ArrayList<>();
        ArrayList<String> incidentsList = new ArrayList<>();

        ArrayList<String> messageList = new ArrayList<>();
        // ArrayList<String> idList = new ArrayList<>();

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {}

        @Override
        protected Boolean doInBackground(Void... voidParams) {
            HashMap<String, String> params = new HashMap<>();
            params.put("controller", "Locator");
            params.put("action", "GetLocatorMessagesSB");
            params.put("accountId", user.getData().getAcctId());

            String jsonData = FunctionHelper.apiCaller(params);

            Log.d("SendSBAlertsActivity","getIncidentsTask() --> response: "+jsonData);

            boolean success = false;
            try {
                JSONObject jsonobject = new JSONObject(jsonData);
                success = jsonobject.getBoolean("success");
                if (success) {
                    JSONArray jsonarray = jsonobject.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobject = jsonarray.getJSONObject(i);

                        String iMessage = jsonobject.getString("message");

                        spinnerItems.add(new SpinnerItem_Incidents(iMessage));
                        messageList.add(jsonobject.optString("message"));
                    }

                    messageList.add(0, "-Choose a Canned Message-");
                    spinnerItems.add(0, new SpinnerItem_Incidents("-Choose a Canned Message-"));
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if(success) {
                spinnerIncidents.setAdapter(new CustomSpinnerAdapter_SelectIncidents(context, messageList));
                spinnerIncidents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        String messageName = spinnerItems.get(position).getMessage();

                        if(!messageName.equals("-Choose a Canned Message-")){
                            etAlertMessage.setText(messageName);
                            //spinnerIncidents.setSelection(0);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {}
                });
            }

            dismissActivityProgressDialog();
        }
    }

    private class GetUserGroupsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HashMap<String, String> params = new HashMap<>();
            params.put("controller", "Report");
            params.put("action", "GetContactTypeSB");
            params.put("accountId", user.getData().getAccountId());

            String jsonData = FunctionHelper.apiCaller(params);

            Log.d("SendSBAlertsActivity","getusergrouptasks --> response: "+jsonData);
            boolean success = false;
            try {
                JSONObject jsonobject = new JSONObject(jsonData);
                success = jsonobject.getBoolean("success");
                if (success) {
                    JSONArray jsonarray = jsonobject.getJSONArray("data");

                    ArrayList<UserGroup> userGroups = new ArrayList<>();
                    ArrayList<String> nameList = new ArrayList<>();
                    ArrayList<String> idList = new ArrayList<>();

                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobject = jsonarray.getJSONObject(i);

                        String name = jsonobject.getString("value");
                        String id = jsonobject.getString("id");

                        userGroups.add(new UserGroup(name,id));
                        // Populate spinner
                        userGroupIds.add(jsonobject.optString("id"));
                        userGroupNames.add(jsonobject.optString("value"));

                        nameList.add(jsonobject.optString("value"));
                        idList.add(jsonobject.optString("id"));
                    }

                    nameList.add(0, userGroupButtonDefaultText);
                    idList.add(0, "");

                    //give the group list the groups items
                    Collections.swap(userGroupIds, 0 , userGroupIds.indexOf("1"));
                    Collections.swap(userGroupNames, 0 , userGroupNames.indexOf("Default Emergency Contact"));
                    selectedUserGroupNames.add(userGroupNames.get(0));

                    defaultUserGroupArrangement = new boolean[userGroupIds.size()];
                    for(int i=0; i<defaultUserGroupArrangement.length; i++) {
                        defaultUserGroupArrangement[i] = i == 0;
                    }

                    Log.d("fromBackend","userGroupnames: " + userGroupNames.toString());
                    Log.d("fromBackend","userGroupIds: " + userGroupIds.toString());
                    Log.d("fromBackend","defaultUserGroupArrangement: "+ Arrays.toString(defaultUserGroupArrangement));
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            dismissActivityProgressDialog();
        }
    }

    private void manageBroadcastProgressIndicator() {
        broadcastProgressCounter--;

        if(broadcastProgressCounter == 0) {
            resetContent();
            broadcastProgressDialog.dismiss();
        }
    }

    /**
     * Send to Desktops
     */
    private class SendDesktopAlerts extends AsyncTask<Void, Void, Boolean> {
        String alertMessage;
        String buildingId;
        String selectedUserTypes;

        private SendDesktopAlerts(String alert, String buildId, String usrTypes){
            alertMessage = alert;
            buildingId = buildId;
            selectedUserTypes = usrTypes;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected Boolean doInBackground(Void... voidParams) {
            HashMap<String, String> params = new HashMap<>();
            params.put("controller", "Locator");
            params.put("action", "SendSmartButtonDeskAlert2");
            params.put("accountId", user.getData().getAcctId());
            params.put("pin_id", user.getData().getPinId());
            params.put("image_url", "");
            params.put("building_id", buildingId);
            params.put("message", alertMessage);
            params.put("link", "");
            params.put("title", "");

            Log.d("SendSBAlertsActivity", "inside sendDesktopAlerts()");
            Log.d("SendSBAlertsActivity", "sendDesktopAlerts() --> params:" + params);
//            String jsonData = FunctionHelper.apiCaller(params);
//            Log.e("JSONDATA: ","SENDDESKTOPALERTS: "+jsonData);
//            boolean success = false;
//            try {
//                JSONObject jsonobject = new JSONObject(jsonData);
//                success = jsonobject.getBoolean("success");
//            } catch (JSONException e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//
//            return success;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success) {
                displayMessageToUser("Your alert was sent successfully to the designated user group(s) of the SmartButton® desktop app.", Toast.LENGTH_SHORT);
            } else {
                displayMessageToUser("Something went wrong while sending your alert to the designated user group(s) of the SmartButton® desktop app, please try again.", Toast.LENGTH_SHORT);
            }

            manageBroadcastProgressIndicator();
        }
    }

    /**
     * Send to Apps
     */
    private class SendAppAlerts extends AsyncTask<Void, Void, Boolean> {
        String alertMessage;
        String buildingId;
        String userGroups;

        private SendAppAlerts(String alert, String build_id, String userGroups){
            this.alertMessage = alert;
            this.buildingId = build_id;
            this.userGroups = userGroups;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected Boolean doInBackground(Void... voidParams) {

            //[Apps] pushing to ALL buildings: BuildingId = "" (empty string)
            if(buildingId.equals("0")){
                buildingId = "";
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("controller", "Push");
            params.put("action", "SendSBAlertWithGroups");
            params.put("accountId", user.getData().getAcctId());
            params.put("subject", "Administrator Alert");
            params.put("message", alertMessage);
            params.put("schoolId", buildingId);
            params.put("alertType", userGroups);

            Log.d("SendSBAlertsActivity","inside sendAppAlerts()");
            Log.d("SendSBAlertsActivity","sendAppAlerts() --> params: "+params);

//            String jsonData = FunctionHelper.apiCaller(params);
//
//            Log.d("SendSBAlertsActivity","sendAppAlerts() --> response: "+jsonData);
//
//            boolean success = false;
//            try {
//                JSONObject jsonobject = new JSONObject(jsonData);
//                success = jsonobject.getBoolean("success");
//            } catch (JSONException e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//
//            return success;
            return  true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success)
                displayMessageToUser("Your alert was sent successfully to the designated user group(s) of the SmartButton® mobile app.", Toast.LENGTH_SHORT);
            else
                displayMessageToUser("Something went wrong while sending your alert to the designated user group(s) of the SmartButton® mobile app, please try again.", Toast.LENGTH_SHORT);

            manageBroadcastProgressIndicator();
        }
    }

    /**
     * Clear RSS
     */
    private class ClearRSSFeed extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog mDialog;
        private String buildingIds;

        private ClearRSSFeed(String mBuildingIds){
            buildingIds = mBuildingIds;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        private void showProgressDialog() {
            if (mDialog == null) {
                mDialog = new ProgressDialog(context);
                mDialog.setMessage("Clearing your RSS feed, please wait...");
                mDialog.setIndeterminate(true);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
            }
            mDialog.show();
        }

        private void dismissProgressDialog() {
            try {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.cancel();
                    mDialog.dismiss();
                }
            } catch (IllegalArgumentException ex) {
                //do nothing
            } finally {
                mDialog = null;
            }
        }

        @Override
        protected Boolean doInBackground(Void... voidParams) {
            HashMap<String, String> params = new HashMap<>();
            params.put("controller", "Locator");
            params.put("action", "ClearSmartButtonRSS");
            params.put("accountId", user.getData().getAcctId());
            params.put("pinID", user.getData().getPinId());

            String jsonData = FunctionHelper.apiCaller(params);

            boolean success = false;
            try {
                JSONObject jsonobject = new JSONObject(jsonData);
                success = jsonobject.getBoolean("success");
            } catch (Exception e) {
                Log.d("SendSBAlertsActivity","encountered exception: "+e);
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                displayMessageToUser("Your SmartButton® alert has been cleared from desktops.", Toast.LENGTH_SHORT);
            } else {
                displayMessageToUser("Something went wrong while clearing your SmartButton® alert from desktops, please try again.", Toast.LENGTH_SHORT);
            }
            dismissProgressDialog();
        }
    }

    /**
     * Utility
     */
    private void displayMessageToUser(final String message, final int toastLength){
        this.runOnUiThread(() -> Toast.makeText(context.getApplicationContext(), message, toastLength).show());
    }
}
