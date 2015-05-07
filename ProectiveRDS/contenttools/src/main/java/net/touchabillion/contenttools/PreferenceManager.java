package net.touchabillion.contenttools;

import android.content.Context;
import android.content.SharedPreferences;

import net.touchabillion.contenttools.Constants.App;

/**
 * Created by den4ik on 5/7/15.
 */
public class PreferenceManager {

    private Context context;
    private SharedPreferences prefs;

    private static PreferenceManager instance;

    public static PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }
        return instance;
    }

    private PreferenceManager(Context context) {
        instance = this;
        this.context = context;
        prefs = context.getSharedPreferences(App.Pref.NAME, Context.MODE_PRIVATE);
    }

    public boolean isAuthorized() {
        return prefs.getBoolean(App.Pref.USER_IS_LOGGED, false);
    }

    public void setAuthorized(boolean authorized) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(App.Pref.USER_IS_LOGGED, authorized);
        editor.apply();
    }

    public void saveCookies(String cookies) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(App.Pref.COOKIES, cookies);
        editor.apply();
    }

    public String getCookies() {
        return prefs.getString(App.Pref.COOKIES, null);
    }
}
