package itg8.com.meetingapp.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import static itg8.com.meetingapp.db.TblDocument.FIELD_MEETING_ID;


/**
 * Created by Android itg 8 on 2/6/2018.
 */
@DatabaseTable(tableName = TblContact.TBL_CONTACT)
public class TblContact implements Parcelable {
    static final String TBL_CONTACT="TBL_CONTACT";
    public static final String FIELD_ID="pkid";
    public static final String CONTACT_NAME="name";
    public static final String CONTACT_NUMBER="number";
    public static final String FIELD_MEETING_ID="meeting_id";

    @DatabaseField(columnName = FIELD_ID,generatedId = true)
    private long pkid;

    @DatabaseField(columnName = FIELD_MEETING_ID,foreign = true,foreignAutoRefresh = true)
    private TblMeeting meeting;

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

    public TblContact() {
    }

    public TblMeeting getMeeting() {
        return meeting;
    }

    public void setMeeting(TblMeeting meeting) {
        this.meeting = meeting;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.pkid);
        dest.writeParcelable(this.meeting, flags);
        dest.writeString(this.name);
        dest.writeString(this.number);
    }

    protected TblContact(Parcel in) {
        this.pkid = in.readLong();
        this.meeting = in.readParcelable(TblMeeting.class.getClassLoader());
        this.name = in.readString();
        this.number = in.readString();
    }

    public static final Creator<TblContact> CREATOR = new Creator<TblContact>() {
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
