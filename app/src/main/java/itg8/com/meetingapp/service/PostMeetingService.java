package itg8.com.meetingapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;

import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Parcelables;
import itg8.com.meetingapp.db.TblMeeting;

public class PostMeetingService extends Service {
    public PostMeetingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction()!=null && intent.getAction().equalsIgnoreCase(CommonMethod.ACTION_MEETING_NOTIFICATION_STOP)){
//            TblMeeting meeting= Parcelables.toParcelable(intent.getByteArrayExtra(CommonMethod.EXTRA_MEETING_STOPPED),TblMeeting.CREATOR);
//            if(meeting!=null){
//                send
//            }
            TblMeeting meeting=Parcelables.toParcelable(intent.getByteArrayExtra(CommonMethod.EXTRA_MEETING),TblMeeting.CREATOR);
            createNotificationToStopMeeting(meeting);

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createNotificationToStopMeeting(TblMeeting meeting) {
        Intent intent=new Intent(this,MeetingStartNotificationBroadcast.class);
        intent.putExtra(CommonMethod.EXTRA_MEETING,Parcelables.toByteArray(meeting));
        intent.setAction(CommonMethod.ACTION_MEETING_NOTIFICATION_STOP);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,((int)meeting.getPkid()),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendarOld=Calendar.getInstance();
//        calendarOld.setTime(meeting.getEndTime());
        manager.set(AlarmManager.RTC_WAKEUP,calendarOld.getTimeInMillis(),pendingIntent);
    }
}
