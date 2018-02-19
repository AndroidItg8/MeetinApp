package itg8.com.meetingapp.meeting.mvp;

import itg8.com.meetingapp.common.BaseWeakPresenter;
import itg8.com.meetingapp.db.TblMeeting;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class MeetingPresenterImp extends BaseWeakPresenter<MeetingMVP.MeetingView> implements MeetingMVP.MeetingPresenter,MeetingMVP.MeetingListener{

    MeetingMVP.MeetingModule module;

    public MeetingPresenterImp(MeetingMVP.MeetingView meetingView) {
        super(meetingView);
        module=new MeetingModule(this);
    }

    @Override
    public void saveMeeting(TblMeeting meeting) {
        module.onSaveMeeting(meeting);
    }

    @Override
    public void startServiceToSetAlarmManager(long diffByPriority, TblMeeting meeting) {
        if(hasView()){
            getView().startServiceToSetAlarm(diffByPriority,meeting);
        }
    }
}
