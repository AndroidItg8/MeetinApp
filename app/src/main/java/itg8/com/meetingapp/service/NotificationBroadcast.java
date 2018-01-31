package itg8.com.meetingapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Prefs;

public class NotificationBroadcast extends BroadcastReceiver {

    private static final String MY_STATIC_CHANNEL_ID = "itg8.com.meetingapp.NotificationBroadcast.CHANNEL_1";
    private static final String TAG = NotificationBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING)) {
            boolean getBoolean=Prefs.getBoolean(CommonMethod.PREF_MEETING_MODE,false);
            Log.d(TAG,"getBoolean : "+getBoolean);
            Prefs.putBoolean(CommonMethod.PREF_MEETING_MODE, !getBoolean);
        }

        RemoteViews staticNotificationView =new RemoteViews(context.getPackageName(), R.layout.notification_static_menu);
        staticNotificationView.setImageViewResource(R.id.img_add,R.drawable.ic_add_circle_black_24dp);
        staticNotificationView.setImageViewResource(R.id.img_import,R.drawable.ic_import);
        boolean isInMeeting=Prefs.getBoolean(CommonMethod.PREF_MEETING_MODE,false);
        staticNotificationView.setImageViewResource(R.id.img_meeting, isInMeeting ?R.drawable.ic_in_meeting:R.drawable.ic_no_meeting);
        staticNotificationView.setImageViewResource(R.id.img_wallet,Prefs.getBoolean(CommonMethod.PREF_HAS_DOCUMENT,false)?R.drawable.ic_folder_fullfill:R.drawable.ic_folder_empty);
        staticNotificationView.setImageViewResource(R.id.img_more_menu,R.drawable.ic_keyboard_arrow_right_black_24dp);

        Intent notificationMeetingIntent= new Intent(CommonMethod.ACTION_START_STATIC_NOTIFICATION);
        Log.d(TAG,"IsInMeeting: "+isInMeeting);
        notificationMeetingIntent.putExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING,isInMeeting);
        PendingIntent notificationMeetingPendingIntent=PendingIntent.getBroadcast(context,0,notificationMeetingIntent,0);
        staticNotificationView.setOnClickPendingIntent(R.id.img_meeting,notificationMeetingPendingIntent);


        NotificationCompat.Builder builder= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder = new NotificationCompat.Builder(context,MY_STATIC_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
//                    .setCustomHeadsUpContentView(staticNotificationView)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCustomContentView(staticNotificationView);
        }
        NotificationManager managerCompat= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(managerCompat!=null && builder!=null)
            managerCompat.notify(CommonMethod.STATIC_NOTIFICATION_ID,builder.build());
        else
            throw new NullPointerException("NotificationService not available.");
    }
}
