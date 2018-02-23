package itg8.com.meetingapp.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import itg8.com.meetingapp.common.CommonMethod;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class DaoMeetingInteractor {
    private static final String TAG = DaoMeetingInteractor.class.getSimpleName();
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
        if (conditions > 0)
            where.or(conditions);

        PreparedQuery<TblMeeting> preparedQuery = quiryBuilder.prepare();
        return helper.getMeetingDao().query(preparedQuery);
    }

    public TblMeeting getMeetingById(long id) throws SQLException {
        return helper.getMeetingDao().queryForId(id);
    }

    public List<TblMeeting> getMeetingNotNotified(int isNotified) throws SQLException {
        return helper.getMeetingDao().queryForEq(TblMeeting.NOTIFIED, isNotified);
    }

    public List<TblMeeting> getMeetingByTitleLike(String title) throws SQLException {
        QueryBuilder<TblMeeting, Long> qb = helper.getMeetingDao().queryBuilder();
        qb.where().like(TblMeeting.TITLE, "%" + title + "%");
        PreparedQuery<TblMeeting> pq = qb.prepare();
        return helper.getMeetingDao().query(pq);
    }

    public List<TblMeeting> getMeetingByTagsLike(List<TblTAG> tag) throws SQLException {
        QueryBuilder<TblMeetingTag, Long> qbMeetingTAg = helper.getMeetingTagDao().queryBuilder();
        QueryBuilder<TblMeeting, Long> qbMeeting = helper.getMeetingDao().queryBuilder();
        int conditions = 0;
        List<TblMeetingTag> list =new ArrayList<>();
        List<TblMeeting> listMeeting =new ArrayList<>();
        HashMap<Long, TblMeeting> hashMap = new HashMap<>();

        if (tag != null) {

            for (TblTAG t : tag) {

                list.addAll(qbMeetingTAg.where().eq(TblMeetingTag.FIELD_TAG_ID, t.getPkid()).query());
                Log.d(TAG, "getMeetingByTagsLike: Meeting First Condition :"+t.getPkid()+ t.getName());

            }
            conditions++;

        }
        if(list.size()>0){
        for (TblMeetingTag meetingTag:list) {
            if(!hashMap.containsKey(meetingTag.getMeeting().getPkid()))
            {
                hashMap.put(meetingTag.getMeeting().getPkid(),meetingTag.getMeeting());

                listMeeting.addAll(qbMeeting.where().eq(TblMeeting.FIELD_ID, meetingTag.getMeeting().getPkid()).query());
            }

            Log.d(TAG, "getMeetingByTagsLike: Meeting Second  Condition :"+"Meeting "+meetingTag.getMeeting() +"Meeting TAG "+meetingTag.getTag().toString() +"Meeting Id"+meetingTag.getPkid());

        }
        conditions++;
        }

        if(conditions>0) {


//            PreparedQuery<TblMeeting> preparedQuery = qbMeeting.prepare();
//            return helper.getMeetingDao().query(preparedQuery);
            return listMeeting;
        }
        return null;


//
//        return helper.getMeetingDao().query(qb);

    }


}
