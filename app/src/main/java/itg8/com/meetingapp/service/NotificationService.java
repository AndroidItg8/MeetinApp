package itg8.com.meetingapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Parcelables;
import itg8.com.meetingapp.common.Prefs;
import itg8.com.meetingapp.db.TblMeeting;


public class NotificationService extends Service {


    private static final String TAG = NotificationService.class.getSimpleName();

    public NotificationService() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(CommonMethod.EXTRA_MEETING)) {
            TblMeeting meeting = intent.getParcelableExtra(CommonMethod.EXTRA_MEETING);
            if (intent.hasExtra(CommonMethod.EXTRA_MEETING_CANCELED)) {
                cancelMeeting(meeting);
                scheduleMeetingNotification(meeting, true);
                return START_NOT_STICKY;
            }
            Log.d(TAG, "Meeting Model: " + meeting.toString());
            scheduleMeetingNotification(meeting, false);
            long timeDiff = intent.getLongExtra(CommonMethod.EXTRA_MEETING_TIME_DIFF, 0);

            //When android restarts all alarm manager get removed by system. For that reason we can create a simple
//            method which stores meeting id and difference calculated at storing meeting. And we can user
//            those data to re initialise alarms when its done.

            for (int i = 1; i < CommonMethod.getPriorityDifferenceFromSetting(meeting.getPriority()); i++) {
                long newTimeDiff = meeting.getStartTime().getTime() - (timeDiff * i);
                if (newTimeDiff > Calendar.getInstance().getTimeInMillis()) {
                    scheduleReminderNotification(i, meeting, newTimeDiff);

                }
            }
            return START_NOT_STICKY;
        }
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(CommonMethod.NOTIFICATION_CHANGE_MEETING)) {
            Prefs.putBoolean(CommonMethod.PREF_MEETING_MODE, intent.getBooleanExtra(CommonMethod.NOTIFICATION_CHANGE_MEETING, false));
        }
        Intent intentNotification = new Intent(CommonMethod.ACTION_START_STATIC_NOTIFICATION);
        sendBroadcast(intentNotification);
        return START_NOT_STICKY;
    }

    private void cancelMeeting(TblMeeting meeting) {
        for (int i = 1; i <= CommonMethod.getPriorityDifferenceFromSetting(meeting.getPriority()); i++) {
            Intent intent = new Intent(this, NotificationReminderBroadcast.class);
            intent.setAction(CommonMethod.ACTION_MEETING_NOTIFICATION_REMINDER);
            intent.putExtra(CommonMethod.EXTRA_MEETING, Parcelables.toByteArray(meeting));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ((int) meeting.getPkid()) * i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
        }
    }

    private void scheduleReminderNotification(int i, TblMeeting meeting, long newTimeDiff) {
        Intent intent = new Intent(this, NotificationReminderBroadcast.class);
        intent.setAction(CommonMethod.ACTION_MEETING_NOTIFICATION_REMINDER);
        intent.putExtra(CommonMethod.EXTRA_MEETING, Parcelables.toByteArray(meeting));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ((int) meeting.getPkid()) * i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendarOld = Calendar.getInstance();
        calendarOld.setTimeInMillis(newTimeDiff);

        if (manager != null) {
            manager.set(AlarmManager.RTC_WAKEUP, calendarOld.getTimeInMillis(), pendingIntent);
        }
    }

    private void scheduleMeetingNotification(TblMeeting meeting, boolean isCancel) {
        scheduleMeetingStartNotification(meeting.getStartTime(), meeting, isCancel);
        scheduleMeetingStopNotification(meeting.getEndTime(), meeting, isCancel);
    }

    private void scheduleMeetingStopNotification(Date endTime, TblMeeting meeting, boolean isCancel) {
        if(endTime.getTime()<System.currentTimeMillis())
            return;

        Intent intent = new Intent(this, MeetingStartNotificationBroadcast.class);
        intent.setClass(this, MeetingStartNotificationBroadcast.class);
        intent.putExtra(CommonMethod.EXTRA_MEETING, Parcelables.toByteArray(meeting));
        intent.setAction(CommonMethod.ACTION_MEETING_NOTIFICATION_STOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ((int) meeting.getPkid()) * 3, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (isCancel) {
            pendingIntent.cancel();
            return;
        }
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendarOld = Calendar.getInstance();
        calendarOld.setTime(endTime);

        if (manager != null) {
            manager.set(AlarmManager.RTC_WAKEUP, calendarOld.getTimeInMillis(), pendingIntent);
        }
    }

    private void scheduleMeetingStartNotification(Date startTime, TblMeeting meeting, boolean isCancel) {
        if(startTime.getTime()<System.currentTimeMillis())
            return;
        Intent intent = new Intent(this, MeetingStartNotificationBroadcast.class);
        intent.setClass(this, MeetingStartNotificationBroadcast.class);
        intent.putExtra(CommonMethod.EXTRA_MEETING, Parcelables.toByteArray(meeting));
        intent.setAction(CommonMethod.ACTION_MEETING_NOTIFICATION_START);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ((int) meeting.getPkid()) * 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (isCancel) {
            pendingIntent.cancel();
            return;
        }
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendarOld = Calendar.getInstance();
        calendarOld.setTime(startTime);
//        Calendar calendar=Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY,calendarOld.get(Calendar.HOUR_OF_DAY));
//        calendar.set(Calendar.MINUTE,calendarOld.get(Calendar.MINUTE));
//        calendar.set(Calendar.SECOND,calendarOld.get(Calendar.SECOND));
        if (manager != null) {
            manager.set(AlarmManager.RTC_WAKEUP, calendarOld.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
