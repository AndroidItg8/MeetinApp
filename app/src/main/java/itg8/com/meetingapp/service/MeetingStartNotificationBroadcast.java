package itg8.com.meetingapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.db.TblMeeting;

public class MeetingStartNotificationBroadcast extends BroadcastReceiver {

    private static final String MY_STATIC_CHANNEL_ID = "itg8.com.meetingapp.NotificationBroadcast.CHANNEL_2";
    private CountDownTimer mCTimer;
    private static final int POST_NOTIFICATION_REQUEST_CODE=31;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        RemoteViews meetingStartCustomview;
        TblMeeting meeting=intent.getParcelableExtra(CommonMethod.EXTRA_MEETING);
        if(intent.getAction()!=null && intent.getAction().equalsIgnoreCase(CommonMethod.ACTION_MEETING_NOTIFICATION_START)) {
            meetingStartCustomview = new RemoteViews(context.getPackageName(), R.layout.notification_meeting_started);
            meetingStartCustomview.setImageViewResource(R.id.img_meeting, R.mipmap.ic_launcher);
            meetingStartCustomview.setImageViewResource(R.id.img_finish, R.drawable.ic_stop_black_24dp);
            meetingStartCustomview.setTextViewText(R.id.txtMeetingTitle, "Meeting started at "+ Helper.getStringTimeFromDate(meeting.getStartTime()));
            meetingStartCustomview.setTextViewText(R.id.txt_description, "Meeting with Agenda: '"+meeting.getTitle()+"' Started");

            Intent intentNew=new Intent(context,PostMeetingService.class);
            intentNew.putExtra(CommonMethod.EXTRA_MEETING_STOPPED,meeting);
            PendingIntent pIntent=PendingIntent.getService(context,POST_NOTIFICATION_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            meetingStartCustomview.setOnClickPendingIntent(R.id.img_finish,pIntent);

        }else{
            meetingStartCustomview = new RemoteViews(context.getPackageName(), R.layout.notification_meeting_started);
            meetingStartCustomview.setImageViewResource(R.id.img_meeting, R.mipmap.ic_launcher);
            meetingStartCustomview.setImageViewResource(R.id.img_finish, R.drawable.ic_stop_black_24dp);
            meetingStartCustomview.setTextViewText(R.id.txtMeetingTitle, "ABC Meeting has started.");
        }

        NotificationCompat.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder = new NotificationCompat.Builder(context,MY_STATIC_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(false)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCustomContentView(meetingStartCustomview);
        }
        NotificationManager managerCompat= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(managerCompat!=null && builder!=null)
            managerCompat.notify(CommonMethod.STATIC_NOTIFICATION_ID,builder.build());
        else
            throw new NullPointerException("NotificationService not available.");


    }
}
