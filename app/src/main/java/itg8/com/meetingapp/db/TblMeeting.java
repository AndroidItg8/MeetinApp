package itg8.com.meetingapp.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import itg8.com.meetingapp.common.Helper;

import static itg8.com.meetingapp.db.TblMeeting.TBL_MEETING;

/**
 * Created by swapnilmeshram on 01/02/18.
 */
@DatabaseTable(tableName = TBL_MEETING)
public class TblMeeting implements Parcelable,Cloneable {
    static final String TBL_MEETING="TBL_MEETING";
    public static final String FIELD_ID="pkid";
    public static final String TITLE="title";
    public static final String START_TIME="start_time";
    public static final String END_TIME="end_time";
    public static final String PRIORITY="priority";
    public static final String DATE="dateonly";
    public static final String CREATED="created";
    public static final String LATITUDE="latitude";
    public static final String LONGITUDE="longitude";
    public static final String ADDRESS="address";
    public static final String NOTIFIED="notified";

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
    private Date dateOnly;

    @DatabaseField(columnName = CREATED, dataType = DataType.DATE_LONG)
    private Date created;

    @DatabaseField(columnName = LATITUDE)
    private double latitude;

    @DatabaseField(columnName = LONGITUDE)
    private double longitude;

    @DatabaseField(columnName = ADDRESS)
    private String address;

    @DatabaseField(columnName = NOTIFIED)
    private int notified;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNotified() {
        return notified;
    }

    public void setNotified(int notified) {
        this.notified = notified;
    }

    @ForeignCollectionField
    private ForeignCollection<TblDocument> documents;


    @ForeignCollectionField
    private ForeignCollection<TblTAG> tags;

    @ForeignCollectionField
    private ForeignCollection<TblContact> contacts;


    public TblMeeting() {
    }

    public List<TblDocument> getDocuments() {
        return new ArrayList<>(documents);
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

    public Date getDateOnly() {
        return dateOnly;
    }

    public void setDateOnly(Date dateOnly) {
        this.dateOnly = dateOnly;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<TblTAG> getTags() {
        return new ArrayList<>(tags);
    }

    public void setTags(ForeignCollection<TblTAG> tags) {
        this.tags = tags;
    }

    public List<TblContact> getContacts() {
        return new ArrayList<>(contacts);
    }

    public void setContacts(ForeignCollection<TblContact> contacts) {
        this.contacts = contacts;
    }


    @Override
    public String toString() {
        return "TblMeeting{" +
                "pkid=" + pkid +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", priority=" + priority +
                ", dateOnly=" + dateOnly +
                ", created=" + created +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                '}';
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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
        dest.writeLong(this.dateOnly != null ? this.dateOnly.getTime() : -1);
        dest.writeLong(this.created != null ? this.created.getTime() : -1);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.address);
        dest.writeInt(this.notified);
    }

    protected TblMeeting(Parcel in) {
        this.pkid = in.readLong();
        this.title = in.readString();
        long tmpStartTime = in.readLong();
        this.startTime = tmpStartTime == -1 ? null : new Date(tmpStartTime);
        long tmpEndTime = in.readLong();
        this.endTime = tmpEndTime == -1 ? null : new Date(tmpEndTime);
        this.priority = in.readInt();
        long tmpDateOnly = in.readLong();
        this.dateOnly = tmpDateOnly == -1 ? null : new Date(tmpDateOnly);
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.address = in.readString();
        this.notified = in.readInt();
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
