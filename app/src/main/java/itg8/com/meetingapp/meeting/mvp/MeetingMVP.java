package itg8.com.meetingapp.meeting.mvp;

import itg8.com.meetingapp.db.TblMeeting;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public interface MeetingMVP {
    public interface MeetingView{
        void startServiceToSetAlarm(long diffByPriority, TblMeeting meeting);
    }

    public interface MeetingModule{
        void onSaveMeeting(TblMeeting meeting);
    }

    public interface MeetingListener{
        void startServiceToSetAlarmManager(long diffByPriority,TblMeeting meeting);
    }

    public interface MeetingPresenter {
        void saveMeeting(TblMeeting meeting);
    }
}
