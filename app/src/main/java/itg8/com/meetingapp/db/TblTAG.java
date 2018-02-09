package itg8.com.meetingapp.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by Android itg 8 on 2/6/2018.
 */
@DatabaseTable(tableName = TblTAG.TBL_TAG)
public class TblTAG implements Parcelable {
    static final String TBL_TAG="TBL_TAG";
    public static final String FIELD_ID="pkid";
    public static final String FIELD_NAME="name";
    public static final String FIELD_SELECT ="value_select";
    private static final String FIELD_MEETING_ID = "meeting_id";



    @DatabaseField(columnName = FIELD_ID,generatedId = true)
    private long pkid;

    @DatabaseField(columnName = FIELD_MEETING_ID,foreign = true,foreignAutoRefresh = true)
    private TblMeeting meeting;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @DatabaseField(columnName = FIELD_SELECT)
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



    @DatabaseField(columnName = FIELD_NAME)
    private String name;


    public TblTAG() {
    }

    public boolean isSelected() {
        return isSelected;
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
        dest.writeParcelable(this.meeting, flags);
        dest.writeLong(this.pkid);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
    }

    protected TblTAG(Parcel in) {
        this.meeting = in.readParcelable(TblMeeting.class.getClassLoader());
        this.pkid = in.readLong();
        this.isSelected = in.readByte() != 0;
        this.name = in.readString();
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
}
