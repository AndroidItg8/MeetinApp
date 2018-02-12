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

    public void insert(TblMeeting meeting) throws SQLException{
            helper.getMeetingDao().create(meeting);
            Log.d("TAG","InsertMeeting");
    }

    public void update(TblMeeting meeting) throws SQLException{
            helper.getMeetingDao().update(meeting);
    }

    public void delete(TblMeeting meeting) throws SQLException{
            helper.getMeetingDao().delete(meeting);
    }

    public List<TblMeeting> getMeetingsByDate(Date date) throws SQLException{
            return helper.getMeetingDao().queryForEq(TblMeeting.DATE,date);
    }

    public List<TblMeeting> getMeetings() throws SQLException{
            return helper.getMeetingDao().queryForAll();
    }

}
