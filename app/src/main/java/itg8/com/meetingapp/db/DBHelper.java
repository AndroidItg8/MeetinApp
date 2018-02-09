package itg8.com.meetingapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    private static final int DB_VERSION=1;

    private Dao<TblDocument,Integer> documentDao=null;
    private Dao<TblMeeting,Integer> meetingDao=null;
    private Dao<TblTAG,Integer> tagDao=null;
    private Dao<TblContact,Integer> contactDao=null;



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
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            switch (oldVersion)
            {
                case 1:
                    dropingTable(connectionSource);
                case 2:
                    dropingTable(connectionSource);
                case 3:
                    dropingTable(connectionSource);
                    break;

            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void dropingTable(ConnectionSource connectionSource) throws SQLException {
        TableUtils.dropTable(connectionSource,TblMeeting.class,true);
        TableUtils.dropTable(connectionSource,TblDocument.class,true);
        TableUtils.dropTable(connectionSource,TblTAG.class,true);
        TableUtils.dropTable(connectionSource,TblContact.class,true);
    }

    /**
     * Table_meeting
     */
    public Dao<TblMeeting,Integer> getMeetingDao() throws SQLException{
        if(meetingDao==null)
            meetingDao=getDao(TblMeeting.class);
        return meetingDao;
    }

    /**
     * table_document
     */
    public Dao<TblDocument,Integer> getDocumentDao() throws SQLException{
        if(documentDao==null)
            documentDao=getDao(TblDocument.class);
        return documentDao;
    }

    /**
     * table_tag
     */

    public Dao<TblTAG,Integer> getTagDao()throws SQLException{
        if(tagDao==null)
            tagDao=getDao(TblTAG.class);
        return tagDao;

    } /**
     * table_contact
     */

    public Dao<TblContact,Integer> getContactDao()throws SQLException{
        if(contactDao==null)
            contactDao=getDao(TblContact.class);
        return contactDao;

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
