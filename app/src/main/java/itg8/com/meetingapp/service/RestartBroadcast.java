package itg8.com.meetingapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.sql.SQLException;
import java.util.List;

import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.db.DaoMeetingInteractor;
import itg8.com.meetingapp.db.TblMeeting;

/**
 * Created by swapnilmeshram on 19/02/18.
 */

public class RestartBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationForAllPendingMeetings(context);
    }

    private void createNotificationForAllPendingMeetings(Context context) {
        try {
            DaoMeetingInteractor daoMeetingInteractor=new DaoMeetingInteractor(context);
            List<TblMeeting> meetings=daoMeetingInteractor.getMeetingNotNotified(CommonMethod.NOT_NOTIFIED);
            if(meetings.size()>0){
                for (TblMeeting meeting :
                        meetings) {
                    startServiceToSetAlarm(CommonMethod.diffByPriority(meeting),meeting,context);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startServiceToSetAlarm(long diffByPriority, TblMeeting meeting, Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra(CommonMethod.EXTRA_MEETING, meeting);
        long timeDifference = Helper.getTimeDifferenceFromCurrent(meeting.getStartTime());
        if (timeDifference > 1) {
            if (diffByPriority > 0) {
                intent.putExtra(CommonMethod.EXTRA_MEETING_TIME_DIFF, diffByPriority);
            }
        }
        context.startService(intent);
    }

}
