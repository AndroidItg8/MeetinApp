package itg8.com.meetingapp.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import itg8.com.meetingapp.common.CommonMethod;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class DaoMeetingInteractor {
    DBHelper helper;

    public DaoMeetingInteractor(Context context) {
        DBManager.init(context);
        helper = DBManager.getInstance().getHelper();
    }

    public void insert(TblMeeting meeting) throws SQLException {
        helper.getMeetingDao().create(meeting);
        Log.d("TAG", "InsertMeeting");
    }

    public void update(TblMeeting meeting) throws SQLException {
        helper.getMeetingDao().update(meeting);
    }

    public void delete(TblMeeting meeting) throws SQLException {
        helper.getMeetingDao().delete(meeting);
    }

    public List<TblMeeting> getMeetingsByDate(Date date) throws SQLException {
        return helper.getMeetingDao().queryForEq(TblMeeting.DATE, date);
    }

    public List<TblMeeting> getMeetings() throws SQLException {
        return helper.getMeetingDao().queryForAll();
    }

    public List<TblMeeting> getMeetingsSortBy(String column) throws SQLException {
        return helper.getMeetingDao().queryBuilder().orderBy(column, true).query();
    }

    public List<TblMeeting> getMeetingSortByWithPriority(String column, HashMap<Integer, Boolean> priority) throws SQLException {
        QueryBuilder<TblMeeting, Long> quiryBuilder = helper.getMeetingDao().queryBuilder();
        quiryBuilder.orderBy(column, true);
        Where<TblMeeting, Long> where = quiryBuilder.where();
        int conditions = 0;
        if (priority.get(CommonMethod.PRIORITY_INT_HIGH)) {
            where.eq(TblMeeting.PRIORITY, CommonMethod.PRIORITY_INT_HIGH);
            conditions++;
        }
        if (priority.get(CommonMethod.PRIORITY_INT_MEDIUM)) {
            where.eq(TblMeeting.PRIORITY, CommonMethod.PRIORITY_INT_MEDIUM);
            conditions++;
        }
        if (priority.get(CommonMethod.PRIORITY_INT_LOW)) {
            where.eq(TblMeeting.PRIORITY, CommonMethod.PRIORITY_INT_LOW);
            conditions++;
        }
        if(conditions>0)
           where.or(conditions);
        PreparedQuery<TblMeeting> preparedQuery=quiryBuilder.prepare();
        return helper.getMeetingDao().query(preparedQuery);
    }

    public TblMeeting getMeetingById(long id) throws SQLException {
        return helper.getMeetingDao().queryForId(id);
    }

    public List<TblMeeting> getMeetingNotNotified(int isNotified) throws SQLException {
        return helper.getMeetingDao().queryForEq(TblMeeting.NOTIFIED, isNotified);
    }
}
