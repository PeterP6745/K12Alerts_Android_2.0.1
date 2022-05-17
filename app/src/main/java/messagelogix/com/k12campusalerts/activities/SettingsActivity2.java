package messagelogix.com.k12campusalerts.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;

/**
 * Created by Program on 4/6/2017.
 */
public class SettingsActivity2 extends BaseActivity {

    private TextView tvFName;
    private TextView tvLastName;
    private TextView tvTitle;
    private TextView tvAccount;

    private TextView tvPhone;
    private TextView tvEmail;

    private User mUser;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings_2, frameLayout);

        mUser = (User)new FunctionHelper(context).retrieveObject(User.class);

        initWidgets();
    }

    private void initWidgets(){

        tvFName = (TextView) findViewById(R.id.settings_fname);
        try{
            tvFName.setText(mUser.getData().getFname());
        }
        catch (NullPointerException e){
            tvFName.setText("");
        }

        tvLastName = (TextView) findViewById(R.id.settings_last_name);
        try{
            tvLastName.setText(mUser.getData().getLname());
        }
        catch (NullPointerException e){
            tvLastName.setText("");
        }

        tvTitle = (TextView) findViewById(R.id.settings_title);
        try{
            tvTitle.setText(mUser.getData().getTitle());
        }
        catch (NullPointerException e){
            tvTitle.setText("");
        }


        tvAccount = (TextView) findViewById(R.id.settings_account);
        try{
            tvAccount.setText(mUser.getData().getAccountName());
        }
        catch (NullPointerException e){
            tvAccount.setText("");
        }


        tvPhone = (TextView) findViewById(R.id.settings_phone);
        try{
            tvPhone.setText(mUser.getData().getPinSmsPhoneNo());
        }
        catch (NullPointerException e){
            tvPhone.setText("");
        }

        tvEmail = (TextView) findViewById(R.id.settings_email);
        try{
            tvEmail.setText(mUser.getData().getPinEmail1());
        }
        catch (NullPointerException e){
            tvEmail.setText("");
        }

    }
}
