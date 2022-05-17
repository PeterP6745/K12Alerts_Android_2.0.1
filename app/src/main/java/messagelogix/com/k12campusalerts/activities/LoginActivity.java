package messagelogix.com.k12campusalerts.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;

import messagelogix.com.k12campusalerts.BuildConfig;
import messagelogix.com.k12campusalerts.R;
import messagelogix.com.k12campusalerts.models.User;
import messagelogix.com.k12campusalerts.utils.Config;
import messagelogix.com.k12campusalerts.utils.FunctionHelper;
import messagelogix.com.k12campusalerts.utils.Preferences;
import messagelogix.com.k12campusalerts.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    // UI references.
    private TextView mAccountView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Context context = this;

    public interface LoginService {
        @FormUrlEncoded
        @POST(ServiceGenerator.API_BASE_URL)
        Call<User> request(@FieldMap HashMap<String,String> parameters);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Preferences.init(context);

        TextView copyRightTV = (TextView) findViewById(R.id.copyRightTV);
        copyRightTV.setText(FunctionHelper.getDynamicCopyRightNotice());

        // Set up the login form.
        mAccountView = (TextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        User mUser = (User) new FunctionHelper(context).retrieveObject(User.class);
        if(mUser != null){
            mAccountView.setText(mUser.getData().getAccountId());
            mPasswordView.setText(mUser.getData().getPin());
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!isAccountValid(account)) {
            mAccountView.setError(getString(R.string.error_invalid_account));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            performLogin(account, password);
        }
    }

    private void performLogin(String account, String password){
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);

        LoginService client = ServiceGenerator.createService(LoginService.class);

        HashMap<String, String> params = ServiceGenerator.getApiMap();
        params.put("controller", "User");
        params.put("action", "Authenticate");
        params.put("username", account);
        params.put("password", password);

        Call<User> call = client.request(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                showProgress(false);

                Log.d("LoginActivity","performLogin() --> response: "+response.body().getResponseObj());
                
                if(response.isSuccess()){
                    User user = response.body();
                    if (user.getSuccess()) {
                        Preferences.putBoolean(Config.IS_LOGGED_IN, true);
                        Preferences.putInteger(Config.LAST_VERSION_CODE, BuildConfig.VERSION_CODE);
                        Preferences.putBoolean("shouldReloadHome", true);
                        new FunctionHelper(context).saveObject(user);
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                    } else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                Log.d("LoginActivity","performLogin() --> onFailure: "+t.toString());
                Log.d("LoginActivity","performLogin() --> onFailure: "+t.getCause());
                Log.d("LoginActivity","performLogin() --> onFailure: "+t.getMessage());
                Log.d("LoginActivity","performLogin() --> onFailure: "+t.getLocalizedMessage());
                Log.d("LoginActivity","performLogin() --> onFailure: "+ Arrays.toString(t.getStackTrace()));
                Log.e(LOG_TAG, "onFailure =" + t.getMessage());
            }
        });
    }

    private boolean isAccountValid(String account) {
        return account.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

