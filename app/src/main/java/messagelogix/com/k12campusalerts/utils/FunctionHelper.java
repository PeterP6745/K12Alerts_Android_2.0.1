package messagelogix.com.k12campusalerts.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import messagelogix.com.k12campusalerts.BuildConfig;
import messagelogix.com.k12campusalerts.activities.LoginActivity;


public class FunctionHelper {

    private static final String LOG_TAG = FunctionHelper.class.getSimpleName();

    private Context context;

    public FunctionHelper(Context context) {
        this.context = context;
    }


    public static String apiCaller(HashMap<String, String> postDataParams) {

        postDataParams.put("api_key", ServiceGenerator.API_KEY);
        postDataParams.put("app_id", ServiceGenerator.APP_ID);

        return httpPost(ServiceGenerator.API_BASE_URL, postDataParams);
    }

    public static String httpPost(String urlString, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(urlString);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";
                //throw new HttpException(responseCode+"");
            }
        } catch (Exception e) {
            Log.d("FunctionHelper", "httpPost() --> encountered exception: " + e);
        }
        if (BuildConfig.DEBUG){
            Log.d("FunctionHelper", "httpPost() --> response = " + response);
        }

        return response;
    }

    public static String getPostDataString(HashMap<String, String> params){
        StringBuilder result = new StringBuilder();

        try {
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public void logOut(){
        SharedPreferencesManager sharedPreferencesManager;
        sharedPreferencesManager = new SharedPreferencesManager(context);
        sharedPreferencesManager.setBool(Config.REMEMBER_ME, false);
        Intent loginScreen =new Intent(this.context,LoginActivity.class);
        loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.context.startActivity(loginScreen);
        ((Activity) this.context).finish();
    }

    public void saveObject(Object object) {
        String key = object.getClass().getSimpleName();
        Gson gson = new Gson() ;
        String objectJSON = gson.toJson(object);
        SharedPreferencesManager sharedPreferencesManager;
        sharedPreferencesManager = new SharedPreferencesManager(context);
        sharedPreferencesManager.setString(key, objectJSON);
    }

    public Object retrieveObject(Class objectClass){
        String key = objectClass.getSimpleName();
        Gson gson = new Gson();
        SharedPreferencesManager sharedPreferencesManager;
        sharedPreferencesManager = new SharedPreferencesManager(context);
        String objectJSON = sharedPreferencesManager.getString(key);
        Object object  = gson.fromJson(objectJSON, objectClass);
        return object;
    }
    
    /***
     * Checks if a string is null or empty
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty() || str.trim().length() == 0;
    }

    public void showToast(final String message) {
        final Activity activity = (Activity)context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isAlphanumeric(String s){
        if(s != null) {
            //Check that string contains at least one letter
            //boolean minOneLetter = s.matches(".*[a-zA-Z]+.*");
            //Check that string contains a combination of alphanumeric characters and the characters '.,-()/:;
            boolean hasCombo = s.matches("^[a-zA-Z0-9'.,\\-()/:;\\s]*$");

            return /*minOneLetter &&*/ hasCombo;
        }

        return false;
    }

    public static String convertToASCII(String str) {
        String subjectString = Normalizer.normalize(str, Normalizer.Form.NFD);
        return subjectString.replaceAll("[^\\x00-\\x7F]", "?");
    }

    public static String getDynamicCopyRightNotice() {
        return "Copyright © 2002–"+ Calendar.getInstance().get(Calendar.YEAR)+" Message Logix, Inc. All rights reserved.\n" +
                "        Patented (U.S. Patent No. 8,180,274) additional patents pending";
    }

    public static void showErrorPrompt(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}

