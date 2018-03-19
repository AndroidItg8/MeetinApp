package com.github.tibolte.agendacalendarview.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class BusProvider
{
    private static final String TAG = "BusProvider";

    public static BusProvider mInstance;

    private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

    // region Constructors

    public static BusProvider getInstance() {
        if (mInstance == null) {
            mInstance = new BusProvider();
        }
        return mInstance;
    }

    // endregion

    // region Public methods

    public void send(Object object) {
        Log.d(TAG, "send:Object "+object.getClass().getSimpleName());
        mBus.onNext(object);
    }

    public Observable<Object> toObserverable() {
        return mBus;
    }
    public  void destoryBusProvider()
    {
        mInstance=null ;

    }

    // endregion


}
