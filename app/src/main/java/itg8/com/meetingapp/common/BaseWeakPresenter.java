package itg8.com.meetingapp.common;

import java.lang.ref.WeakReference;


public class BaseWeakPresenter<T> {
    private WeakReference<T> weakReference;

    public BaseWeakPresenter(T t) {
        this.weakReference=new WeakReference<T>(t);
    }

    public boolean hasView(){
        return weakReference!=null && weakReference.get() != null;
    }

    public T getView(){
        return weakReference.get();
    }


    protected void detactView(){
        weakReference.clear();
    }

}
