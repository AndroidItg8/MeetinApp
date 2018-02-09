package itg8.com.meetingapp.db;

import android.content.Context;
import android.util.Log;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class DaoMeetingInteractor {
    DBHelper helper;
    public DaoMeetingInteractor(Context context){
        DBManager.init(context);
        helper=DBManager.getInstance().getHelper();
    }

    public void insert(TblMeeting meeting){
        try {
            helper.getMeetingDao().create(meeting);
            Log.d("TAG","InsertMeeting");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(TblMeeting meeting){
        try {
            helper.getMeetingDao().update(meeting);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(TblMeeting meeting){
        try {
            helper.getMeetingDao().delete(meeting);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<TblMeeting> getMeetingsByDate(Date date){
        try {
            return helper.getMeetingDao().queryForEq(TblMeeting.DATE,date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TblMeeting> getMeetings(){
        try {
            return helper.getMeetingDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
