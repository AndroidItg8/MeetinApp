package itg8.com.meetingapp.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;

import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.common.Parcelables;
import itg8.com.meetingapp.common.Prefs;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.import_meeting.MeetingDetailActivity;
import itg8.com.meetingapp.meeting.MeetingActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static itg8.com.meetingapp.service.NotificationBroadcast.MY_STATIC_CHANNEL_ID;
import static itg8.com.meetingapp.service.NotificationBroadcast.NOTIFICATION_CHANNEL_NAME;

public class NotificationReminderBroadcast extends BroadcastReceiver {
    private static final int REMINDER_NOTIFICATION=2;

    CountDownTimer timer;
    private long counter;

    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.hasExtra(CommonMethod.EXTRA_MEETING))
        {
            TblMeeting meeting= Parcelables.toParcelable(intent.getByteArrayExtra(CommonMethod.EXTRA_MEETING),TblMeeting.CREATOR);
            if(meeting!=null)
            {
                String title="Meeting Alert : meeting on "+Helper.getStringTimeFromDate(meeting.getStartTime())+".";
                String description="You have a meeting about "+meeting.getTitle();



//                //count to end the game
//                timer= new CountDownTimer(30000, 1000){
//
//                    public void onTick(long millisUntilFinished){
//                        counter = millisUntilFinished / 1000;
//                    }
//
//                    public void onFinish(){
//                        counter = 0;
//
//
//                    }
//
//                };

                Intent intent2 = new Intent(context, MeetingDetailActivity.class);
                intent2.putExtra(CommonMethod.EXTRA_MEETING,meeting);
                PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent2, 0);

                // Build notification
                // Actions are just fake
                Notification.Builder builder = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    builder = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContentText(description)
                            .setSmallIcon(R.drawable.ic_meeting_white)
                            .setContentIntent(pIntent);
    //                        .addAction(R.mipmap.ic_launcher, "Call", pIntent)
    //                        .addAction(R.mipmap.ic_launcher, "More", pIntent)
    //                        .addAction(R.drawable.icon, "And more", pIntent)

                    Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    if(!Prefs.getBoolean(CommonMethod.PREF_MEETING_MODE,true)){
                        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                        builder.setSound(uri);
                    }else {
                        if(Prefs.getBoolean(CommonMethod.SETTING_PREF_MEETING_VIBRATE,false)){
                            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                        }
                    }
//                }
                Notification noti= null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    noti = builder.build();
                }
                else
                    return;
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                if (notificationManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        setNotificationChannel(notificationManager);
                    }
                    notificationManager.notify((int) (REMINDER_NOTIFICATION*meeting.getPkid()), noti);
                }

            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void setNotificationChannel(NotificationManager managerCompat) {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(MY_STATIC_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        managerCompat.createNotificationChannel(notificationChannel);
    }
}
