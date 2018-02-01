package itg8.com.meetingapp.meeting.mvp;

import java.util.Date;

import io.reactivex.Observable;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Helper;
import itg8.com.meetingapp.db.TblMeeting;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class MeetingModule implements MeetingMVP.MeetingModule {

    private MeetingMVP.MeetingListener listener;

    public MeetingModule(MeetingMVP.MeetingListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSaveMeeting(TblMeeting meeting) {
        storeToDb(meeting);
        setAlarmForMeeting(meeting);
    }

    private void storeToDb(TblMeeting meeting) {

    }

    private void setAlarmForMeeting(TblMeeting meeting) {
        long timeDifference=Helper.getTimeDifferenceFromCurrent(meeting.getStartTime());
        if(timeDifference>1){
            long diffByPriority= CommonMethod.getDifferenceFromPriority(meeting.getPriority(),timeDifference);
            if(diffByPriority>0){
                sendBroadcastForAlarmScheduling(diffByPriority,meeting);
            }
        }
    }

    /**
     *
     * @param diffByPriority time divided for notification
     * @param meeting meetingf data
     */
    private void sendBroadcastForAlarmScheduling(long diffByPriority, TblMeeting meeting) {
        listener.startServiceToSetAlarmManager(diffByPriority,meeting);
    }

}
