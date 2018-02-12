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




}
