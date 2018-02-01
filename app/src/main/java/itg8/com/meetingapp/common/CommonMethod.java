package itg8.com.meetingapp.common;

/**
 * Created by swapnilmeshram on 30/01/18.
 */

public class CommonMethod {
    public static final String ACTION_START_STATIC_NOTIFICATION = "itg8.com.meetingapp.ACTION_START_NOTIFICATION";
    public static final String PREF_MEETING_MODE = "MYPREF_MEETING_MODE";
    public static final String PREF_HAS_DOCUMENT = "MYPREF_HAS_DOCUMENT";
    public static final String NOTIFICATION_CHANGE_MEETING = "change_noficationMeeting";
    public static final int STATIC_NOTIFICATION_ID = 1001;
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

    public static long getDifferenceFromPriority(int priority, long timeDifference) {
        int priorityDiff=getPriorityDifferenceFromSetting(priority);

        return priorityDiff/timeDifference;
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

}
