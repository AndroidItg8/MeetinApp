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

    public void insert(TblTAG tag){
        try {
            helper.getTagDao().create(tag);
            Log.d(TAG,"InsertData InDb:"+new Gson().toJson(tag));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(TblTAG meeting){
        try {
            helper.getTagDao().update(meeting);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(TblTAG meeting){
        try {
            helper.getTagDao().delete(meeting);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

//    public List<TblTAG> getTAGByDate(Date date){
//        try {
//            return helper.getTagDao().queryForEq(TblTAG.DATE,date);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public List<TblTAG> getTags(){
        try {
            return helper.getTagDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
