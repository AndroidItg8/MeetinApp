package itg8.com.meetingapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME="meetingDb";
    private static final int DB_VERSION=3;

    private Dao<TblDocument,Long> documentDao=null;
    private Dao<TblMeeting,Long> meetingDao=null;
    private Dao<TblTAG,Long> tagDao=null;
    private Dao<TblMeetingTag,Long> meetingTagDao=null;
    private Dao<TblContact,Long> contactDao=null;



    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource,TblMeeting.class);
            TableUtils.createTableIfNotExists(connectionSource,TblDocument.class);
            TableUtils.createTableIfNotExists(connectionSource,TblTAG.class);
            TableUtils.createTableIfNotExists(connectionSource,TblContact.class);
            TableUtils.createTableIfNotExists(connectionSource,TblMeetingTag.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
            try {
                Log.i(DBManager.class.getName(), "onUpgrade");
                dropingTable(connectionSource);
                // after we drop the old databases, we create the new ones
                onCreate(database, connectionSource);
            } catch (SQLException e) {
                Log.e(DBManager.class.getName(), "Can't drop databases", e);
                throw new RuntimeException(e);
            }

    }

    private void dropingTable(ConnectionSource connectionSource) throws SQLException {
        TableUtils.dropTable(connectionSource,TblMeeting.class,true);
        TableUtils.dropTable(connectionSource,TblDocument.class,true);
        TableUtils.dropTable(connectionSource,TblTAG.class,true);
        TableUtils.dropTable(connectionSource,TblContact.class,true);
        TableUtils.dropTable(connectionSource,TblMeetingTag.class,true);
    }

    /**
     * Table_meeting
     */
    public Dao<TblMeeting,Long> getMeetingDao() throws SQLException{
        if(meetingDao==null)
            meetingDao=getDao(TblMeeting.class);
        return meetingDao;
    }

    /**
     * table_document
     */
    public Dao<TblDocument,Long> getDocumentDao() throws SQLException{
        if(documentDao==null)
            documentDao=getDao(TblDocument.class);
        return documentDao;
    }

    /**
     * table_tag
     */

    public Dao<TblTAG,Long> getTagDao()throws SQLException{
        if(tagDao==null)
            tagDao=getDao(TblTAG.class);
        return tagDao;

    } /**
     * table_contact
     */

    public Dao<TblContact,Long> getContactDao()throws SQLException{
        if(contactDao==null)
            contactDao=getDao(TblContact.class);
        return contactDao;

    }

    public Dao<TblMeetingTag,Long>  getMeetingTagDao() throws SQLException{
        if(meetingTagDao==null)
            meetingTagDao=getDao(TblMeetingTag.class);
        return meetingTagDao;
    }

    @Override
    public void close() {
        documentDao = null;
        contactDao = null;
        tagDao = null;
        meetingDao = null;
        super.close();
    }

}
