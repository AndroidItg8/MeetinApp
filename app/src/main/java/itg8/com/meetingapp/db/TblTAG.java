package itg8.com.meetingapp.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by Android itg 8 on 2/6/2018.
 */
@DatabaseTable(tableName = TblTAG.TBL_CONTACT)
public class TblTAG implements Parcelable {
    static final String TBL_CONTACT="TBL_TAG";
    public static final String FIELD_ID="pkid";
    public static final String TAG_NAME="name";

    @DatabaseField(columnName = FIELD_ID,generatedId = true)
    private long pkid;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public long getPkid() {
        return pkid;
    }

    public void setPkid(long pkid) {
        this.pkid = pkid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @DatabaseField(columnName = TAG_NAME)
    private String name;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.pkid);
        dest.writeString(this.name);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public TblTAG() {
    }

    protected TblTAG(Parcel in) {
        this.pkid = in.readLong();
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;


    }

    public static final Creator<TblTAG> CREATOR = new Creator<TblTAG>() {
        @Override
        public TblTAG createFromParcel(Parcel source) {
            return new TblTAG(source);
        }

        @Override
        public TblTAG[] newArray(int size) {
            return new TblTAG[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }
}
