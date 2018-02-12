package itg8.com.meetingapp.db;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Android itg 8 on 2/8/2018.
 */

public class DaoTagInteractor {

    DBHelper helper;
    public static final String TAG ="DaoTagInteractor";
    public DaoTagInteractor(Context context){
        DBManager.init(context);
        helper=DBManager.getInstance().getHelper();
    }

    public void insert(TblTAG tag) throws SQLException {
            helper.getTagDao().create(tag);
            Log.d(TAG,"InsertData InDb:"+new Gson().toJson(tag));
    }

    public void update(TblTAG meeting) throws SQLException {
            helper.getTagDao().update(meeting);
    }

    public void delete(TblTAG meeting) throws SQLException {
            helper.getTagDao().delete(meeting);
    }

//    public List<TblTAG> getTAGByDate(Date date){
//        try {
//            return helper.getTagDao().queryForEq(TblTAG.DATE,date);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public List<TblTAG> getTags() throws SQLException {
            return helper.getTagDao().queryForAll();
    }

}
