package itg8.com.meetingapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.common.Prefs;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.meeting.MeetingActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReminderBroadcast extends BroadcastReceiver {
    private static final int REMINDER_NOTIFICATION=2;

    CountDownTimer timer;
    private long counter;

    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.hasExtra(CommonMethod.EXTRA_MEETING))
        {
            TblMeeting meeting=intent.getParcelableExtra(CommonMethod.EXTRA_MEETING);
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

                Intent intent2 = new Intent(context, MeetingActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent2, 0);

                // Build notification
                // Actions are just fake
                Notification noti = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    noti = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContentText(description)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pIntent)
    //                        .addAction(R.mipmap.ic_launcher, "Call", pIntent)
    //                        .addAction(R.mipmap.ic_launcher, "More", pIntent)
    //                        .addAction(R.drawable.icon, "And more", pIntent)
                            .build();
                }
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify((int) (REMINDER_NOTIFICATION*meeting.getPkid()), noti);

            }
        }
    }
}
