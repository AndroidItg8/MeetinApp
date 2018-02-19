package itg8.com.meetingapp.common;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import itg8.com.meetingapp.R;

/**
 * Created by swapnilmeshram on 01/02/18.
 */

public class Helper {
    private static final String TAG = "Helper";
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

    public static String getPriorityFromType(int priority) {
        switch (priority){
            case CommonMethod.PRIORITY_INT_HIGH:
                return "High";
            case CommonMethod.PRIORITY_INT_MEDIUM:
                return "Medium";
            case CommonMethod.PRIORITY_INT_LOW:
                return "Low";
        }
        return "";
    }

    public static String getDateFromDate(Date startTime) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(startTime);
        return simpleTimeFormatDate.format(calendar.getTime());
    }

    public static String getDateFromMilliseconds(long millies){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(millies);
        return simpleTimeFormatDate.format(calendar.getTime());
    }


    public static String getFileExtFromName(String fileName) {
        int i = fileName.lastIndexOf('.');
        String ext="";
        if (i > 0) {
            ext= fileName.substring(i+1);
            Log.d(TAG,"Ext: "+ext);
        }
        return ext;
    }

    public static Date getDateFromString(Date dateOnly) {
        return null;
    }

    public static int getColorFromPriority(Context context, int priority) {
        switch (priority){
            case CommonMethod.PRIORITY_INT_HIGH:
                return ContextCompat.getColor(context, R.color.colorGoogle);
            case CommonMethod.PRIORITY_INT_MEDIUM:
                return ContextCompat.getColor(context, R.color.colorFire);
            case CommonMethod.PRIORITY_INT_LOW:
                return ContextCompat.getColor(context, R.color.colorGreen);
        }
        return 0;
    }

    public static Date parseDateFromString(String s) throws ParseException {
            return simpleTimeFormatDate.parse(s);
    }

    public static Date parseTimeFromString(String s) throws ParseException {
            return simpleTimeFormatTime.parse(s);
    }
}
