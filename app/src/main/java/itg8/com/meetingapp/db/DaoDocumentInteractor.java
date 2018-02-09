package itg8.com.meetingapp.db;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class DaoDocumentInteractor {

    private final DBHelper helper;

    public DaoDocumentInteractor(Context context) {
        DBManager.init(context);
        helper=DBManager.getInstance().getHelper();
    }

    public void insert(TblDocument document){
        try {
            helper.getDocumentDao().create(document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(TblDocument document){
        try {
            helper.getDocumentDao().update(document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(TblDocument document){
        try {
            helper.getDocumentDao().delete(document);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }  public List<TblDocument> getAll(){
        try {
           return helper.getDocumentDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }




}
