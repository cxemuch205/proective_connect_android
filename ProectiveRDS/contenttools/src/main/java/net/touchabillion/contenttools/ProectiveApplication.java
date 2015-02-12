package net.touchabillion.contenttools;

import android.app.Application;

/**
 * Created by daniil on 2/12/15.
 */
public class ProectiveApplication extends Application {

    private static ProectiveApplication instance;

    public static ProectiveApplication getInstance() {
        if (instance == null) {
            instance = new ProectiveApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        new ImageCache(R.drawable.icon_empty);
    }
}
