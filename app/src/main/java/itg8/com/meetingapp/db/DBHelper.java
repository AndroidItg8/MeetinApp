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



    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,TblMeeting.class);
            TableUtils.createTable(connectionSource,TblDocument.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,TblMeeting.class,true);
            TableUtils.dropTable(connectionSource,TblDocument.class,true);
        }catch (SQLException e){
            e.printStackTrace();
        }
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


}
