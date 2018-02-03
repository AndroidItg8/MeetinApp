package itg8.com.meetingapp.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class Helper {
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "hh:mm a";
    private static SimpleDateFormat simpleTimeFormatTime=new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
    private static SimpleDateFormat simpleTimeFormatDate=new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public static Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }

    public static long getTimeDifferenceFromCurrent(Date endTime) {
        long diff = endTime.getTime() - Calendar.getInstance().getTimeInMillis();
        return diff;
    }

    /**
     * Function to convert milliseconds time to Timer Format
     * Hours:Minutes:Seconds
     * */
    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static String getStringTimeFromDate(Date startTime) {

        try {
            return simpleTimeFormatTime.format(startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static DocType getDocumentExtention(String fileName) {

        return null;
    }

    public static String getStringDateFromCalander(Calendar selectedDate) {

        return simpleTimeFormatDate.format(selectedDate.getTime());
    }

    public static int getTypeFromPriorityText(String priority){
        if(priority.equalsIgnoreCase("High")){
            return CommonMethod.PRIORITY_INT_HIGH;
        }else if(priority.equalsIgnoreCase("Medium")){
            return CommonMethod.PRIORITY_INT_MEDIUM;
        }else if(priority.equalsIgnoreCase("Low")){
            return CommonMethod.PRIORITY_INT_LOW;
        }
        return 0;
    }
}
