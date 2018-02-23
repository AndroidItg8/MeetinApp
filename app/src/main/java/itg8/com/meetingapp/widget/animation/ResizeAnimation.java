package itg8.com.meetingapp.widget.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import itg8.com.meetingapp.common.CommonMethod;

/**
 * Created by Android itg 8 on 2/21/2018.
 */

public class ResizeAnimation extends Animation {
    private static final String TAG = "ResizeAnimation";
    final int targetHeight;
    View view;
    int startHeight;
    private int from;

    public ResizeAnimation(View view, int startHeight, int targetHeight, int from) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.startHeight = startHeight;
        this.from = from;
    }

//    @Override
//    protected void applyTransformation(float interpolatedTime, Transformation t) {
//        int newHeight = (int) (startHeight + targetHeight * interpolatedTime);
//        Log.d(TAG, "applyTransformation:newHeight - "+newHeight+" startHeight:"+startHeight+" targetHeight:"+targetHeight +" interPlo"+interpolatedTime);
//        //to support decent animation, change new heigt as Nico S. recommended in comments
//        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
//        view.getLayoutParams().height = newHeight>0?newHeight:-1*newHeight;
//        view.requestLayout();
//    }
 @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
     int newHeight=0;
         if(from== CommonMethod.FROM_ARROW_DOWN)
             newHeight = (int) ( targetHeight+startHeight * interpolatedTime);
         else
             newHeight = (int) ( targetHeight- (targetHeight- startHeight)* interpolatedTime);

     Log.d(TAG, "applyTransformation:"+from+"newHeight - "+newHeight+" startHeight:"+startHeight+" targetHeight:"+targetHeight +" interPlo"+interpolatedTime);
        //to support decent animation, change new heigt as Nico S. recommended in comments
        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height = newHeight>0?newHeight:-1*newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}