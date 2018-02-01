package itg8.com.meetingapp.db;

import android.content.Context;

/**
 * Created by swapnilmeshram on 28/09/17.
 */

public class DBManager {
    static private DBManager instance;
    private final DBHelper helper;

    public DBManager(Context context) {
        helper=new DBHelper(context);
    }

    static public void init(Context context){
        if(instance==null)
            instance=new DBManager(context);
    }

    static public DBManager getInstance(){
        return instance;
    }

    public DBHelper getHelper(){
        return helper;
    }


}
