package itg8.com.meetingapp.service.jobscheduler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Prefs;
import itg8.com.meetingapp.meeting.MeetingActivity;
import itg8.com.meetingapp.wallet_document.WalletActivity;

/**
 * Created by swapnilmeshram on 16/02/18.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {

    private static final String TAG = "NotificationJobService";
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Context context=this;
//        if(intent.hasExtra(CommonMethod.EXTRA_STICKY_NOTIFICATION)){
//            if(!Prefs.getBoolean(CommonMethod.SETTING_PREF_NOTIFICATION_TOGGLE,false)) {
//                NotificationManager managerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                if(managerCompat!=null)
//                    managerCompat.cancel(CommonMethod.STATIC_NOTIFICATION_ID);
//                return true;
//            }
//        }
//        if(intent.hasExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING)) {
//            boolean getBoolean=Prefs.getBoolean(CommonMethod.PREF_MEETING_MODE,false);
//            Log.d(TAG,"getBoolean : "+getBoolean);
//            Prefs.putBoolean(CommonMethod.PREF_MEETING_MODE, !getBoolean);
//        }
//
//
//
//
//
//
//
//        RemoteViews staticNotificationView =new RemoteViews(context.getPackageName(), R.layout.notification_static_menu);
//        staticNotificationView.setImageViewResource(R.id.img_add,R.drawable.ic_add_circle_black_24dp);
//        staticNotificationView.setImageViewResource(R.id.img_import,R.drawable.ic_import);
//        boolean isInMeeting=Prefs.getBoolean(CommonMethod.PREF_MEETING_MODE,false);
//        staticNotificationView.setImageViewResource(R.id.img_meeting, isInMeeting ?R.drawable.ic_in_meeting:R.drawable.ic_no_in_meeting);
//        staticNotificationView.setImageViewResource(R.id.img_wallet,Prefs.getBoolean(CommonMethod.PREF_HAS_DOCUMENT,false)?R.drawable.ic_folder_fullfill:R.drawable.ic_folder_empty);
//        staticNotificationView.setImageViewResource(R.id.img_more_menu,R.drawable.ic_keyboard_arrow_right_black_24dp);
//
//        Intent notificationMeetingIntent= new Intent(CommonMethod.ACTION_START_STATIC_NOTIFICATION);
//        Log.d(TAG,"IsInMeeting: "+isInMeeting);
//        notificationMeetingIntent.putExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING,isInMeeting);
//        PendingIntent notificationMeetingPendingIntent=PendingIntent.getBroadcast(context,0,notificationMeetingIntent,0);
//        staticNotificationView.setOnClickPendingIntent(R.id.img_meeting,notificationMeetingPendingIntent);
//
//
//        Intent notificationWalletIntent= new Intent(context, WalletActivity.class);
//        Log.d(TAG,"IsInMeeting: "+isInMeeting);
////        notificationMeetingIntent.putExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING,isInMeeting);
//        PendingIntent notificationWalletPendingIntent=PendingIntent.getActivity(context,0,notificationWalletIntent,0);
//        staticNotificationView.setOnClickPendingIntent(R.id.img_wallet,notificationWalletPendingIntent);
//
//
//        Intent notificationAddMeetingIntent= new Intent(context, MeetingActivity.class);
//        Log.d(TAG,"IsInMeeting: "+isInMeeting);
////        notificationMeetingIntent.putExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING,isInMeeting);
//        PendingIntent notificationAddMeetingPendingIntent=PendingIntent.getActivity(context,0,notificationAddMeetingIntent,0);
//        staticNotificationView.setOnClickPendingIntent(R.id.img_add,notificationAddMeetingPendingIntent);
//
//
//
//        //notification for orio
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel androidChannel = null;
//
//            androidChannel = new NotificationChannel(MY_STATIC_CHANNEL_ID,
//                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
//            // Sets whether notifications posted to this channel should display notification lights
//            androidChannel.enableLights(true);
//            // Sets whether notification posted to this channel should vibrate.
//            androidChannel.enableVibration(true);
//            // Sets the notification light color for notifications posted to this channel
//            androidChannel.setLightColor(Color.GREEN);
//            // Sets whether notifications posted to this channel appear on the lockscreen or not
//            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//            getManager(context).createNotificationChannel(androidChannel);
//
//            // create ios channel
////        NotificationChannel iosChannel = new NotificationChannel(IOS_CHANNEL_ID,
////                IOS_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
////        iosChannel.enableLights(true);
////        iosChannel.enableVibration(true);
////        iosChannel.setLightColor(Color.GRAY);
////        iosChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
////        getManager().createNotificationChannel(iosChannel);
//
//        }
//
//
//        NotificationCompat.Builder builder= null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            builder = new NotificationCompat.Builder(context,MY_STATIC_CHANNEL_ID)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setOngoing(true)
//                    .setPriority(NotificationCompat.PRIORITY_MAX)
////                    .setCustomHeadsUpContentView(staticNotificationView)
//                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                    .setCustomContentView(staticNotificationView);
//        }
//        if(getManager(context)!=null && builder!=null)
//            getManager(context).notify(CommonMethod.STATIC_NOTIFICATION_ID,builder.build());
//        else
//            throw new NullPointerException("NotificationService not available.");

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
