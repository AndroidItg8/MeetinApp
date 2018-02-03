package itg8.com.meetingapp.common;

import android.os.Parcel;
import android.os.Parcelable;

// inspired by https://stackoverflow.com/a/18000094/115145

/**
 * Custom Parcelable classes — ones unique to your app,
 * not a part of the Android framework — have had intermittent
 * problems over the years when used as Intent extras. Basically,
 * if a core OS process needs to modify the Intent extras,
 * that process winds up trying to recreate your Parcelable
 * objects as part of setting up the extras Bundle for modification.
 * That process does not have your class and so it gets a runtime exception.

 One area where this can occur is with AlarmManager.
 Code that used custom Parcelable objects with AlarmManager
 that might have worked on older versions of Android will not work on Android N.
 */
public class Parcelables {
    public static byte[] toByteArray(Parcelable parcelable) {
        Parcel parcel=Parcel.obtain();

        parcelable.writeToParcel(parcel, 0);

        byte[] result=parcel.marshall();

        parcel.recycle();

        return(result);
    }

    public static <T> T toParcelable(byte[] bytes,
                                     Parcelable.Creator<T> creator) {
        Parcel parcel=Parcel.obtain();

        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);

        T result=creator.createFromParcel(parcel);

        parcel.recycle();

        return(result);
    }
}
