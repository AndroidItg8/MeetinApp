package itg8.com.meetingapp.common;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import itg8.com.meetingapp.db.TblMeeting;

/**
 * Created by swapnilmeshram on 30/01/18.
 */

public class CommonMethod {
    public static final String ACTION_START_STATIC_NOTIFICATION = "itg8.com.meetingapp.ACTION_START_NOTIFICATION";
    public static final String ACTION_MEETING_NOTIFICATION_REMINDER = "itg8.com.meetingapp.ACTION_MEETING_NOTIFICATION_REMINDER";
    public static final String PREF_MEETING_MODE = "MYPREF_MEETING_MODE";
    public static final String PREF_HAS_DOCUMENT = "MYPREF_HAS_DOCUMENT";
    public static final String NOTIFICATION_CHANGE_MEETING = "change_noficationMeeting";
    public static final int STATIC_NOTIFICATION_ID = 1001;
    public static final int MEETING_NOTIFICATION_ID = 1002;
    public static final String SETTING_PREF_MEETING = "SETTING_PREF_MEETING";
    public static final String SETTING_PREF_MEETING_NOT_DISTURB = "SETTING_PREF_MEETING_NOT_DISTURB";
    public static final String SETTING_PREF_MEETING_VIBRATE = "SETTING_PREF_MEETING_VIBRATE";
    public static final String PRIORITY_LOW ="PRIORITY_LOW" ;
    public static final String PRIORITY_MEDIUM ="PRIORITY_MEDIUM" ;
    public static final String PRIORITY_HIGH ="PRIORITY_HIGH" ;
    public static final String SETTING_PREF_NOTIFICATION_VIBRATE = "SETTING_PREF_NOTIFICATION_VIBRATE";
    public static final String SETTING_PREF_NOTIFICATION_TOGGLE = "SETTING_PREF_NOTIFICATION_TOGGLE";
    public static final String NOTIFICATION_RINGTONE = "NOTIFICATION_RINGTONE";
    public static final int PRIORITY_INT_HIGH=1;
    public static final int PRIORITY_INT_MEDIUM=2;
    public static final int PRIORITY_INT_LOW=3;
    public static final String EXTRA_MEETING = "fromMeeting";
    public static final String EXTRA_MEETING_TIME_DIFF = "meeting_time_diff";
    public static final String ACTION_MEETING_NOTIFICATION_START = "actionMeetingStarts";
    public static final String EXTRA_MEETING_STOPPED = "meetingHasBeenStopped";
    public static final String ACTION_MEETING_NOTIFICATION_STOP = "meetingNotificationStopped";
    public static final String EXTRA_STICKY_NOTIFICATION = "stickyNotificationChangeFromSetting";
    public  static final String EXT_PDF ="pdf";
    public  static final String EXT_TXT ="txt";
    public  static final String EXT_JPG ="jpg";
    public  static final String EXT_PNG ="png";
    public  static final String EXT_PPT ="ppt";
    public  static final String EXT_EXL ="xls";
    public  static final String EXT_DOC ="doc";
    public  static final String EXT_ZIP ="zip";
    public static final String FIRST_TIME_WALLET = "FIRST_TIME_WALLET";
    public static final String MEETING = "MEETING";
    public static final String EXTRA_PRE_DOCUMENTS = "EXTRA_PRE_DOCUMENT";
    public static final String EXTRA_POST_DOCUMENTS = "EXTRA_POST_DOCUMENT";
    public static final int TYPE_PRE_MEETING=0;
    public static final int TYPE_POST_MEETING=1;
    public static final String EXTRA_MEETING_CANCELED = "extra_meeting_canceled";
    public static final int NOT_NOTIFIED = 0;
    public static final int NOTIFIED = 1;
    public static final String SELECTED_TAG = "SELECTED_TAG";

    public static long getDifferenceFromPriority(int priority, long timeDifference) {
        int priorityDiff=getPriorityDifferenceFromSetting(priority);

        return timeDifference/priorityDiff;
    }

    public static int getPriorityDifferenceFromSetting(int priority) {
        int priorityDiff=0;
        switch (priority){
            case PRIORITY_INT_HIGH:
                priorityDiff=Prefs.getInt(PRIORITY_HIGH,3);
                break;
            case PRIORITY_INT_MEDIUM:
                priorityDiff=Prefs.getInt(PRIORITY_MEDIUM,2);
                break;
            case PRIORITY_INT_LOW:
                priorityDiff=Prefs.getInt(PRIORITY_LOW,1);
                break;
        }
        return priorityDiff;
    }

    public static String getMimetypeFromUri(Uri uri,ContentResolver cr) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static long diffByPriority(TblMeeting meeting) {
        long timeDifference=Helper.getTimeDifferenceFromCurrent(meeting.getStartTime());
        if(timeDifference>1){
            long diffByPriority= CommonMethod.getDifferenceFromPriority(meeting.getPriority(),timeDifference);
            if(diffByPriority>0){
                return diffByPriority;
            }
        }
        return 0;
    }

    public  interface ItemClickListner{
        void onItemClcikedListener(int position, String item, ImageView img);
    }


    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

}
