package itg8.com.meetingapp.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = TblMeetingTag.TABLE_NAME)
public class TblMeetingTag implements Parcelable {
    public  static final String TABLE_NAME="TBL_MEETING_TAG";

    public static final String FIELD_ID="pkid";
    public static final String FIELD_MEETING_ID="meeting_pkid";
    public static final String FIELD_TAG_NAME="tag_name";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private long pkid;

    @DatabaseField(columnName = FIELD_MEETING_ID, foreignAutoRefresh = true, foreign = true)
    private TblMeeting meeting;

    @DatabaseField(columnName = FIELD_TAG_NAME)
    private String  tag;

    public TblMeetingTag() {
    }


    public long getPkid() {
        return pkid;
    }

    public void setPkid(long pkid) {
        this.pkid = pkid;
    }

    public TblMeeting getMeeting() {
        return meeting;
    }

    public void setMeeting(TblMeeting meeting) {
        this.meeting = meeting;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.pkid);
        dest.writeParcelable(this.meeting, flags);
        dest.writeString(this.tag);
    }

    protected TblMeetingTag(Parcel in) {
        this.pkid = in.readLong();
        this.meeting = in.readParcelable(TblMeeting.class.getClassLoader());
        this.tag = in.readString();
    }

    public static final Creator<TblMeetingTag> CREATOR = new Creator<TblMeetingTag>() {
        @Override
        public TblMeetingTag createFromParcel(Parcel source) {
            return new TblMeetingTag(source);
        }

        @Override
        public TblMeetingTag[] newArray(int size) {
            return new TblMeetingTag[size];
        }
    };
}
