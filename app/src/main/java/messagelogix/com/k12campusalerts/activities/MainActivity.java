package messagelogix.com.k12campusalerts.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import java.util.Calendar;

import messagelogix.com.k12campusalerts.BuildConfig;
import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.utils.Config;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.Preferences;


public class MainActivity extends Activity {
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Preferences.init(context);

        TextView copyRightTV = (TextView) findViewById(R.id.textView);
        copyRightTV.setText(FunctionHelper.getDynamicCopyRightNotice());

        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                    Intent intent;
                    if (userIsAuthenticated() && !appIsUpdated()) {
                        intent = new Intent(context, HomeActivity.class);
                    } else {
                        intent = new Intent(context, LoginActivity.class);
                    }
                    startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }

        };
        logoTimer.start();
    }


    protected boolean userIsAuthenticated() {
        return Preferences.getBoolean(Config.IS_LOGGED_IN);
    }

    protected boolean appIsUpdated() {
        int lastVersionCode = Preferences.getInteger(Config.LAST_VERSION_CODE);
        return lastVersionCode < BuildConfig.VERSION_CODE;
    }


}
