package itg8.com.meetingapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.RemoteViews;

import itg8.com.meetingapp.R;

public class MeetingStartNotificationBroadcast extends BroadcastReceiver {

    private CountDownTimer mCTimer;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        RemoteViews meetingStartCustomview=new RemoteViews(context.getPackageName(), R.layout.notification_meeting_started);
        meetingStartCustomview.setImageViewResource(R.id.img_meeting,R.mipmap.ic_launcher);
        meetingStartCustomview.setImageViewResource(R.id.img_finish,R.drawable.ic_stop_black_24dp);
        meetingStartCustomview.setTextViewText(R.id.txtMeetingTitle,"ABC Meeting has started.");
    }
}
