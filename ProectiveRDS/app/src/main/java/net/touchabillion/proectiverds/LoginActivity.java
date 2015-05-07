package net.touchabillion.proectiverds;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.touchabillion.contenttools.Api.Api;
import net.touchabillion.contenttools.Constants.App;
import net.touchabillion.contenttools.PreferenceManager;
import net.touchabillion.contenttools.Tools;

import java.util.HashMap;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity{

    public static final String TAG = "LoginActivity";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Api api;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferenceManager = PreferenceManager.getInstance(this);
        api = new Api(this);

        initUI();

        if (preferenceManager.isAuthorized()) {
            openHome();
        }
    }

    private void initUI() {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
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
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            enableProgress(true);
            prepareLogin(email, password);
        }
    }

    private void prepareLogin(String email, String password) {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(Api.Fields.USERNAME, email);
        userData.put(Api.Fields.PASSWORD, password);
        userData.put(Api.Fields.SUBMIT, Uri.encode(App.Api.SUBMIT_LOGIN));

        api.requestLogin(userData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                executeResponse(response, SecurityLevel.FIRST);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                executeError(error);
            }
        });
    }

    private void executeError(VolleyError error) {
        error.printStackTrace();
        enableProgress(false);
    }

    private enum SecurityLevel{
        FIRST, LANDING, COOKIES, USER
    }

    private void executeResponse(final String response, SecurityLevel level) {

        if (response == null && level == SecurityLevel.FIRST) {
            api.requestLanding(new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    executeResponse(response, SecurityLevel.LANDING);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    executeError(error);
                }
            });
        }

        if (response == null && level == SecurityLevel.LANDING) {
            api.requestCookies(new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    executeResponse(response, SecurityLevel.COOKIES);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    executeError(error);
                }
            });
        }

        if (level == SecurityLevel.COOKIES) {
            api.requestUser(new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    executeResponse(response, SecurityLevel.USER);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    executeError(error);
                }
            });
        }

        if (response != null && level == SecurityLevel.USER) {

        }

        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                enableProgress(false);
                Log.d(TAG, "RESPONSE: " + response);

                if (false) {
                    preferenceManager.setAuthorized(true);
                    openHome();
                } else {
                    preferenceManager.setAuthorized(false);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }
        });*/
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && Tools.isValidEmail(email);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4 && Tools.isValidPassword(password);
    }

    public void enableProgress(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    private void openHome() {
        Tools.hideKeyboard(this);
        finish();
        Intent openHome = new Intent(this, HomeActivity.class);
        startActivity(openHome);
    }
}



