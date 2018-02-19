package itg8.com.meetingapp.db;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Android itg 8 on 2/8/2018.
 */

public class DaoContactInteractor {
    DBHelper helper;
    public DaoContactInteractor(Context context){
        DBManager.init(context);
        helper=DBManager.getInstance().getHelper();
    }

    public void insert(TblContact contact) throws SQLException {
            helper.getContactDao().create(contact);
    }

    public void update(TblContact contact) throws SQLException {
            helper.getContactDao().update(contact);
    }

    public void delete(TblContact contact) throws SQLException {
            helper.getContactDao().delete(contact);
    }

//    public List<TblContact> getTAGByDate(Date date){
//        try {
//            return helper.getContactDao().queryForEq(TblContact.DATE,date);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public List<TblContact> getContacts() throws SQLException {
            return helper.getContactDao().queryForAll();
    }

    public List<TblContact> getContactsByMeetingId(long pkid) throws SQLException {
       return helper.getContactDao().queryBuilder().where().eq(TblContact.FIELD_MEETING_ID,pkid).query();
    }
}
