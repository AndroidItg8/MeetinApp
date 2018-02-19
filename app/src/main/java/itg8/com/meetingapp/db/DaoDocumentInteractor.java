package itg8.com.meetingapp.db;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import itg8.com.meetingapp.common.CommonMethod;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class DaoDocumentInteractor {

    private final DBHelper helper;

    public DaoDocumentInteractor(Context context) {
        DBManager.init(context);
        helper=DBManager.getInstance().getHelper();
    }

    public void insert(TblDocument document) throws SQLException {
            helper.getDocumentDao().create(document);
    }

    public void update(TblDocument document) throws SQLException {
            helper.getDocumentDao().update(document);
    }

    public void delete(TblDocument document) throws SQLException {
            helper.getDocumentDao().delete(document);
    }
    public List<TblDocument> getAll() throws SQLException {
           return helper.getDocumentDao().queryForAll();
    }


    public List<TblDocument> getDocumentsByMeetingId(long pkid) throws SQLException{
        return helper.getDocumentDao().queryBuilder().where().eq(TblDocument.FIELD_MEETING_ID,pkid).query();

    }

    public List<TblDocument> getPreDocumentsByMeetingId(long pkid) throws SQLException {
        return helper.getDocumentDao().queryBuilder().where().eq(TblDocument.FIELD_MEETING_ID,pkid).and().eq(TblDocument.FIELD_MEETING_TYPE, CommonMethod.TYPE_PRE_MEETING).query();
    }
    public List<TblDocument> getPostDocumentsByMeetingId(long pkid) throws SQLException {
        return helper.getDocumentDao().queryBuilder().where().eq(TblDocument.FIELD_MEETING_ID,pkid).and().eq(TblDocument.FIELD_MEETING_TYPE, CommonMethod.TYPE_POST_MEETING).query();
    }
}
