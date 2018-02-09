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

    public void insert(TblContact contact){
        try {
            helper.getContactDao().create(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(TblContact contact){
        try {
            helper.getContactDao().update(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(TblContact contact){
        try {
            helper.getContactDao().delete(contact);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

//    public List<TblContact> getTAGByDate(Date date){
//        try {
//            return helper.getContactDao().queryForEq(TblContact.DATE,date);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public List<TblContact> getContacts(){
        try {
            return helper.getContactDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
