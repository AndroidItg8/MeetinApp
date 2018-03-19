package itg8.com.meetingapp.service;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.sql.SQLException;

import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.common.Parcelables;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblMeeting;
import itg8.com.meetingapp.import_meeting.MeetingDetailActivity;

import static itg8.com.meetingapp.service.NotificationBroadcast.MY_STATIC_CHANNEL_ID;
import static itg8.com.meetingapp.service.NotificationBroadcast.NOTIFICATION_CHANNEL_NAME;

public class MeetingStartNotificationBroadcast extends BroadcastReceiver {

    private static final String MY_STATIC_CHANNEL_ID = "itg8.com.meetingapp.NotificationBroadcast.CHANNEL_2";
    private CountDownTimer mCTimer;
    private static final int POST_NOTIFICATION_REQUEST_CODE = 31;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        RemoteViews meetingCustomview;

        TblMeeting meeting = Parcelables.toParcelable(intent.getByteArrayExtra(CommonMethod.EXTRA_MEETING), TblMeeting.CREATOR);
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(CommonMethod.ACTION_MEETING_NOTIFICATION_START)) {
            meetingCustomview = new RemoteViews(context.getPackageName(), R.layout.notification_meeting_started);
            meetingCustomview.setImageViewResource(R.id.img_meeting, R.mipmap.ic_launcher);
            meetingCustomview.setImageViewResource(R.id.img_finish, R.drawable.ic_stop_black_24dp);
            meetingCustomview.setTextViewText(R.id.txtMeetingTitle, "Meeting started at " + Helper.getStringTimeFromDate(meeting.getStartTime()));
            meetingCustomview.setTextViewText(R.id.txt_description, "Meeting with agenda: '" + meeting.getTitle() + "' has been started");

            Intent intentNew = new Intent(context, MeetingStartNotificationBroadcast.class);
            intentNew.setAction(CommonMethod.ACTION_MEETING_NOTIFICATION_STOP);
            intentNew.putExtra(CommonMethod.EXTRA_MEETING, Parcelables.toByteArray(meeting));
            PendingIntent pIntent = PendingIntent.getBroadcast(context, POST_NOTIFICATION_REQUEST_CODE, intentNew, PendingIntent.FLAG_UPDATE_CURRENT);
            meetingCustomview.setOnClickPendingIntent(R.id.img_finish, pIntent);
        } else {
            meetingCustomview = new RemoteViews(context.getPackageName(), R.layout.notification_meeting_stopped);
            meetingCustomview.setImageViewResource(R.id.img_meeting, R.mipmap.ic_launcher);
            meetingCustomview.setTextViewText(R.id.txtMeetingTitle, "Meeting Finished?");
            meetingCustomview.setTextViewText(R.id.txt_description, "Meeting with agenda: '" + meeting.getTitle() + "' seems like finished. Would you like to add documents related to meeting?");
            Intent intentNew = new Intent(context, PostMeetingService.class);
            intentNew.putExtra(CommonMethod.EXTRA_MEETING_STOPPED, Parcelables.toByteArray(meeting));
            PendingIntent pIntent = PendingIntent.getService(context, POST_NOTIFICATION_REQUEST_CODE, intentNew, PendingIntent.FLAG_UPDATE_CURRENT);
            meetingCustomview.setOnClickPendingIntent(R.id.img_finish, pIntent);
            setMeetingStopped(new DaoMeetingInteractor(context), meeting);
        }

        Intent intent2 = new Intent(context, MeetingDetailActivity.class);
        intent2.putExtra(CommonMethod.EXTRA_MEETING, meeting);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent2, 0);


        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            builder = new NotificationCompat.Builder(context, MY_STATIC_CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setSmallIcon(R.drawable.bg_item_cross);
        builder .setOngoing(false);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder  .setCustomContentView(meetingCustomview);
        builder.setContentIntent(pIntent);



        NotificationManager managerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (managerCompat != null && builder != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setNotificationChannel(managerCompat);
            }
            managerCompat.notify(CommonMethod.MEETING_NOTIFICATION_ID, builder.build());
        } else
            throw new NullPointerException("NotificationService not available.");


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void setNotificationChannel(NotificationManager managerCompat) {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(MY_STATIC_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        managerCompat.createNotificationChannel(notificationChannel);
    }

    private void setMeetingStopped(DaoMeetingInteractor daoMeetingInteractor, TblMeeting meeting) {
        try {
            TblMeeting meeting1 = daoMeetingInteractor.getMeetingById(meeting.getPkid());
            meeting1.setNotified(CommonMethod.NOTIFIED);
            daoMeetingInteractor.update(meeting1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
