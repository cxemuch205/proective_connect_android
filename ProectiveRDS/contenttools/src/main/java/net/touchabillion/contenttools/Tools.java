package net.touchabillion.contenttools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by daniil on 2/12/15.
 */
public class Tools {

    public static final String TAG = "Tools";

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
        }
    }

    public static boolean checkInternet(Context ctx) {
        try {
            ConnectivityManager nInfo = (ConnectivityManager) ctx.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();
            Log.d(TAG, "Net avail:"
                    + nInfo.getActiveNetworkInfo().isConnectedOrConnecting());
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Log.d(TAG, "Network available:true");
                return true;
            } else {
                Log.d(TAG, "Network available:false");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidPassword(String password) {
        if (password.contains("%")
                || password.contains("#")
                || password.contains("@")
                || password.contains("!")
                || password.contains("^")
                || password.contains("&")
                || password.contains("*")
                || password.contains("+")
                || password.contains("=")
                || password.contains("/")
                || password.contains("\\")
                || password.contains("|")
                || password.contains(",")
                || password.contains(":")
                || password.contains(";")
                || password.contains(" "))
            return false;
        return true;
    }

    public static boolean isValidEmail(String email) {
        if (email.contains("%")
                || email.contains("#")
                || email.contains("!")
                || email.contains("^")
                || email.contains("&")
                || email.contains("*")
                || email.contains("{")
                || email.contains("}")
                || email.contains("(")
                || email.contains(")")
                || email.contains("+")
                || email.contains("=")
                || email.contains("/")
                || email.contains("\\")
                || email.contains("|")
                || email.contains(",")
                || email.contains("[")
                || email.contains("]")
                || email.contains(":")
                || email.contains(";")
                || email.contains(" "))
            return false;
        return true;
    }

    public static void showToastCenter(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastCenter(Context context, String msg, int colorBackground) {
        Toast toast = Toast.makeText(context, msg,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.getView().setBackgroundColor(colorBackground);
        toast.show();
    }

    public static void showToastCenterErr(Context context, String msg) {
        showToastCenter(context, msg, Color.parseColor("#e9e92300"));
    }

    public static void saveCookies(Context context, Map<String, String> headers) {
        if (headers != null) {
            Log.d(TAG, "HEADERS: " + headers);
            for (Map.Entry<String, String> map : headers.entrySet()) {
                String key = map.getKey();
                String value = map.getValue();
                if (key.equals("Set-Cookie")) {
                    if (value.contains(";")) {
                        int indexCookie = value.indexOf(";");
                        String cookies = value.substring(0, indexCookie);

                        Log.i(TAG, "### SAVE Cookies: " + cookies);

                        PreferenceManager.getInstance(context).saveCookies(cookies);
                    } else {
                        Log.e(TAG, "NO HAVE COOKIES");
                    }
                    break;
                }
            }
        }
    }
}
