package itg8.com.meetingapp.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


/**
 * Created by Android itg 8 on 2/6/2018.
 */
@DatabaseTable(tableName = TblContact.TBL_CONTACT)
public class TblContact implements Parcelable {
    static final String TBL_CONTACT="TBL_CONTACT";
    public static final String FIELD_ID="pkid";
    public static final String CONTACT_NAME="name";
    public static final String CONTACT_NUMBER="number";

    @DatabaseField(columnName = FIELD_ID,generatedId = true)
    private long pkid;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @DatabaseField(columnName = CONTACT_NAME)
    private String name;

    @DatabaseField(columnName = CONTACT_NUMBER)
    private String number;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.pkid);
        dest.writeString(this.name);
        dest.writeString(this.number);
    }

    public TblContact() {
    }

    protected TblContact(Parcel in) {
        this.pkid = in.readLong();
        this.name = in.readString();
        this.number = in.readString();
    }

    public static final Parcelable.Creator<TblContact> CREATOR = new Parcelable.Creator<TblContact>() {
        @Override
        public TblContact createFromParcel(Parcel source) {
            return new TblContact(source);
        }

        @Override
        public TblContact[] newArray(int size) {
            return new TblContact[size];
        }
    };
}
