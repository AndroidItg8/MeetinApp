package itg8.com.meetingapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Prefs;

public class NotificationService extends Service {


    public NotificationService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction()!=null && intent.getAction().equalsIgnoreCase(CommonMethod.NOTIFICATION_CHANGE_MEETING)) {
            Prefs.putBoolean(CommonMethod.PREF_MEETING_MODE, intent.getBooleanExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING, false));
        }
        Intent intentNotification=new Intent(CommonMethod.ACTION_START_STATIC_NOTIFICATION);
        sendBroadcast(intentNotification);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
