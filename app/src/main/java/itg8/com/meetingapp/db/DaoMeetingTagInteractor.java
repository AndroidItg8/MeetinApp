package itg8.com.meetingapp.db;

import android.content.Context;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Created by Android itg 8 on 2/8/2018.
 */

public class DaoMeetingTagInteractor {

    DBHelper helper;
    public static final String TAG ="DaoTagInteractor";
    public DaoMeetingTagInteractor(Context context){
        DBManager.init(context);
        helper=DBManager.getInstance().getHelper();
    }

    public void insert(TblMeetingTag tag) throws SQLException {
            helper.getMeetingTagDao().create(tag);
//            Log.d(TAG,"InsertData InDb:"+new Gson().toJson(tag));
    }

    public void update(TblMeetingTag meeting) throws SQLException {
            helper.getMeetingTagDao().update(meeting);
    }

    public void delete(TblMeetingTag meeting) throws SQLException {
            helper.getMeetingTagDao().delete(meeting);
    }

    public void deleteAll(List<TblMeetingTag> meeting) throws SQLException {
            helper.getMeetingTagDao().delete(meeting);
    }

//    public List<TblMeetingTag> getTAGByDate(Date date){
//        try {
//            return helper.getTagDao().queryForEq(TblMeetingTag.DATE,date);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public List<TblMeetingTag> getMeetingTags() throws SQLException {
            return helper.getMeetingTagDao().queryForAll();
    }

    public List<TblMeetingTag> getMeetingTagByMeetingId(long pkid) {
        try {
            return helper.getMeetingTagDao().queryBuilder().where().eq(TblMeetingTag.FIELD_MEETING_ID,pkid).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TblMeetingTag> getDistrictTags() throws SQLException {
        return helper.getMeetingTagDao().queryBuilder().distinct().selectColumns(TblMeetingTag.FIELD_TAG_NAME).query();
    }
}
