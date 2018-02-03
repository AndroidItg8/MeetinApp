package itg8.com.meetingapp.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import itg8.com.meetingapp.common.Helper;

import static itg8.com.meetingapp.db.TblMeeting.TBL_MEETING;

/**
 * Created by swapnilmeshram on 01/02/18.
 */
@DatabaseTable(tableName = TBL_MEETING)
public class TblMeeting implements Parcelable {
    static final String TBL_MEETING="TBL_MEETING";
    public static final String FIELD_ID="pkid";
    public static final String TITLE="title";
    public static final String START_TIME="start_time";
    public static final String END_TIME="end_time";
    public static final String PRIORITY="priority";
    public static final String DATE="dateonly";
    public static final String CREATED="created";

    @DatabaseField(columnName = FIELD_ID,generatedId = true)
    private long pkid;

    @DatabaseField(columnName = TITLE)
    private String title;

    @DatabaseField(columnName = START_TIME, dataType = DataType.DATE_LONG)
    private Date startTime;

    @DatabaseField(columnName = END_TIME, dataType = DataType.DATE_LONG)
    private Date endTime;

    @DatabaseField(columnName = PRIORITY)
    private int priority;

    @DatabaseField(columnName = DATE, dataType = DataType.DATE_STRING, format = Helper.DATE_FORMAT)
    private Date date;

    @DatabaseField(columnName = CREATED, dataType = DataType.DATE_LONG)
    private Date created;

    @ForeignCollectionField
    private ForeignCollection<TblDocument> documents;


    public TblMeeting() {
    }

    public ForeignCollection<TblDocument> getDocuments() {
        return documents;
    }



    public void setDocuments(ForeignCollection<TblDocument> documents) {
        this.documents = documents;
    }

    public long getPkid() {
        return pkid;
    }

    public void setPkid(long pkid) {
        this.pkid = pkid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.pkid);
        dest.writeString(this.title);
        dest.writeLong(this.startTime != null ? this.startTime.getTime() : -1);
        dest.writeLong(this.endTime != null ? this.endTime.getTime() : -1);
        dest.writeInt(this.priority);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeLong(this.created != null ? this.created.getTime() : -1);
    }

    protected TblMeeting(Parcel in) {
        this.pkid = in.readLong();
        this.title = in.readString();
        long tmpStartTime = in.readLong();
        this.startTime = tmpStartTime == -1 ? null : new Date(tmpStartTime);
        long tmpEndTime = in.readLong();
        this.endTime = tmpEndTime == -1 ? null : new Date(tmpEndTime);
        this.priority = in.readInt();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
    }

    public static final Creator<TblMeeting> CREATOR = new Creator<TblMeeting>() {
        @Override
        public TblMeeting createFromParcel(Parcel source) {
            return new TblMeeting(source);
        }

        @Override
        public TblMeeting[] newArray(int size) {
            return new TblMeeting[size];
        }
    };
}
