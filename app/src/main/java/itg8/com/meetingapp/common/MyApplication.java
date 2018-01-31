package itg8.com.meetingapp.common;

import android.app.Application;

/**
 * Created by swapnilmeshram on 31/01/18.
 */

public class MyApplication extends Application {
    private static final String MY_PREF = "MeetingApp";
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        ACRA.init(this);
        mInstance.initPreference();
    }

    private void initPreference() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(MODE_PRIVATE)
                .setPrefsName(MY_PREF)
                .setUseDefaultSharedPreference(false)
                .build();
    }



}
