package itg8.com.meetingapp.common;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Calendar;

import itg8.com.meetingapp.R;
import itg8.com.meetingapp.meeting.MeetingActivity;
import itg8.com.meetingapp.service.NotificationBroadcast;
import itg8.com.meetingapp.wallet_document.WalletActivity;

import static itg8.com.meetingapp.service.NotificationBroadcast.MY_STATIC_CHANNEL_ID;
import static itg8.com.meetingapp.service.NotificationBroadcast.NOTIFICATION_CHANNEL_NAME;

/**
 * Created by swapnilmeshram on 31/01/18.
 */
@ReportsCrashes(formUri = "", mailTo = "app.itechgalaxy@gmail.com", mode = ReportingInteractionMode.SILENT)
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static final String MY_PREF = "MeetingApp";
    private static MyApplication mInstance;
    private NotificationManager mManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ACRA.init(this);
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Intent intent = new Intent(CommonMethod.ACTION_START_STATIC_NOTIFICATION);
            intent.setClass(getApplicationContext(), NotificationBroadcast.class);
            Log.d(TAG, "sendBroadcastForUpdation: Sending Broadcast");
            sendBroadcast(intent);
        }else {
//            Intent intent = new Intent(getApplicationContext(), NotificationBroadcast.class);
//            intent.setAction(CommonMethod.ACTION_START_STATIC_NOTIFICATION);
//            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
//
////            Log.d(TAG, "sendBroadcastForUpdation: Sending Broadcast ORIO");
////            sendBroadcast(intent);
//
//            AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
//            Calendar calendarOld=Calendar.getInstance();
////        Calendar calendar=Calendar.getInstance();
////        calendar.set(Calendar.HOUR_OF_DAY,calendarOld.get(Calendar.HOUR_OF_DAY));
////        calendar.set(Calendar.MINUTE,calendarOld.get(Calendar.MINUTE));
////        calendar.set(Calendar.SECOND,calendarOld.get(Calendar.SECOND));
//            if (manager != null) {
//                manager.set(AlarmManager.RTC_WAKEUP,calendarOld.getTimeInMillis(),pendingIntent);
//            }



            sendNotificationDirectly();
        }
    }

    private void sendNotificationDirectly() {
        Context context=this;
        RemoteViews staticNotificationView =new RemoteViews(context.getPackageName(), R.layout.notification_static_menu);
        staticNotificationView.setImageViewResource(R.id.img_add,R.drawable.ic_add_circle_black_24dp);
        staticNotificationView.setImageViewResource(R.id.img_import,R.drawable.ic_import);
        boolean isInMeeting=Prefs.getBoolean(CommonMethod.PREF_MEETING_MODE,false);
        staticNotificationView.setImageViewResource(R.id.img_meeting, isInMeeting ?R.drawable.ic_in_meeting:R.drawable.ic_no_in_meeting);
        staticNotificationView.setImageViewResource(R.id.img_wallet,Prefs.getBoolean(CommonMethod.PREF_HAS_DOCUMENT,false)?R.drawable.ic_folder_fullfill:R.drawable.ic_folder_empty);
        staticNotificationView.setImageViewResource(R.id.img_more_menu,R.drawable.ic_keyboard_arrow_right_black_24dp);

        Intent notificationMeetingIntent= new Intent(CommonMethod.ACTION_START_STATIC_NOTIFICATION);
        Log.d(TAG,"IsInMeeting: "+isInMeeting);
        notificationMeetingIntent.putExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING,isInMeeting);
        PendingIntent notificationMeetingPendingIntent=PendingIntent.getBroadcast(context,0,notificationMeetingIntent,0);
        staticNotificationView.setOnClickPendingIntent(R.id.img_meeting,notificationMeetingPendingIntent);


        Intent notificationWalletIntent= new Intent(context, WalletActivity.class);
        Log.d(TAG,"IsInMeeting: "+isInMeeting);
//        notificationMeetingIntent.putExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING,isInMeeting);
        PendingIntent notificationWalletPendingIntent=PendingIntent.getActivity(context,0,notificationWalletIntent,0);
        staticNotificationView.setOnClickPendingIntent(R.id.img_wallet,notificationWalletPendingIntent);


        Intent notificationAddMeetingIntent= new Intent(context, MeetingActivity.class);
        Log.d(TAG,"IsInMeeting: "+isInMeeting);
//        notificationMeetingIntent.putExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING,isInMeeting);
        PendingIntent notificationAddMeetingPendingIntent=PendingIntent.getActivity(context,0,notificationAddMeetingIntent,0);
        staticNotificationView.setOnClickPendingIntent(R.id.img_add,notificationAddMeetingPendingIntent);



        //notification for orio
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel androidChannel = null;

            androidChannel = new NotificationChannel(MY_STATIC_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager(context).createNotificationChannel(androidChannel);

            // create ios channel
//        NotificationChannel iosChannel = new NotificationChannel(IOS_CHANNEL_ID,
//                IOS_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//        iosChannel.enableLights(true);
//        iosChannel.enableVibration(true);
//        iosChannel.setLightColor(Color.GRAY);
//        iosChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//        getManager().createNotificationChannel(iosChannel);

        }


        NotificationCompat.Builder builder= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder = new NotificationCompat.Builder(context,MY_STATIC_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
//                    .setCustomHeadsUpContentView(staticNotificationView)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCustomContentView(staticNotificationView);
        }
        if(getManager(context)!=null && builder!=null)
            getManager(context).notify(CommonMethod.STATIC_NOTIFICATION_ID,builder.build());
        else
            throw new NullPointerException("NotificationService not available.");

    }
    private NotificationManager getManager(Context context) {
        if (mManager == null) {
            mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
}
