package itg8.com.meetingapp.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

@DatabaseTable(tableName = TblDocument.TABLE_NAME)
public class TblDocument implements Parcelable {
    public static final String TABLE_NAME = "TBL_DOCUMENT";
    public static final String FIELD_ID="pkid";
    public static final String FIELD_MEETING_ID="meeting_id";
    public static final String FIELD_ACT_PATH="file_act_path";
    public static final String FIELD_CACHE_PATH="file_cache_path";
    public static final String FIELD_FILE_NAME="file_name";

    @DatabaseField(columnName = FIELD_ID,generatedId = true)
    private long pkId;

    @DatabaseField(columnName = FIELD_MEETING_ID,foreign = true,foreignAutoRefresh = true)
    private TblMeeting meeting;

    @DatabaseField(columnName = FIELD_ACT_PATH)
    private String fileActPath;

    @DatabaseField(columnName = FIELD_CACHE_PATH)
    private String fileCachePath;

    @DatabaseField(columnName = FIELD_FILE_NAME)
    private String fileName;


    public long getPkId() {
        return pkId;
    }

    public void setPkId(long pkId) {
        this.pkId = pkId;
    }

    public TblMeeting getMeeting() {
        return meeting;
    }

    public void setMeeting(TblMeeting meeting) {
        this.meeting = meeting;
    }

    public String getFileActPath() {
        return fileActPath;
    }

    public void setFileActPath(String fileActPath) {
        this.fileActPath = fileActPath;
    }

    public String getFileCachePath() {
        return fileCachePath;
    }

    public void setFileCachePath(String fileCachePath) {
        this.fileCachePath = fileCachePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public TblDocument() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.pkId);
        dest.writeParcelable(this.meeting, flags);
        dest.writeString(this.fileActPath);
        dest.writeString(this.fileCachePath);
        dest.writeString(this.fileName);
    }

    protected TblDocument(Parcel in) {
        this.pkId = in.readLong();
        this.meeting = in.readParcelable(TblMeeting.class.getClassLoader());
        this.fileActPath = in.readString();
        this.fileCachePath = in.readString();
        this.fileName = in.readString();
    }

    public static final Parcelable.Creator<TblDocument> CREATOR = new Parcelable.Creator<TblDocument>() {
        @Override
        public TblDocument createFromParcel(Parcel source) {
            return new TblDocument(source);
        }

        @Override
        public TblDocument[] newArray(int size) {
            return new TblDocument[size];
        }
    };
}
