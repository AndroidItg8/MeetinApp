package itg8.com.meetingapp.common;

import android.app.Application;
import android.content.Intent;

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
        Prefs.putBoolean(CommonMethod.SETTING_PREF_NOTIFICATION_TOGGLE,true);
        Prefs.putInt(CommonMethod.PRIORITY_LOW,1);
        Prefs.putInt(CommonMethod.PRIORITY_MEDIUM,2);
        Prefs.putInt(CommonMethod.PRIORITY_HIGH,3);
        sendBroadcastForUpdation();
    }

    private void initPreference() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(MODE_PRIVATE)
                .setPrefsName(MY_PREF)
                .setUseDefaultSharedPreference(false)
                .build();
    }

    private void sendBroadcastForUpdation() {
        Intent intent=new Intent(CommonMethod.ACTION_START_STATIC_NOTIFICATION);
        sendBroadcast(intent);
    }

}
