package itg8.com.meetingapp.custom_tag;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Android itg 8 on 2/14/2018.
 */

public class MaxHeightScrollView extends NestedScrollView {

    private int maxHeight;
    private final int defaultHeight = 200;

    public MaxHeightScrollView(Context context) {
        super(context);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }



    private void init(Context context, AttributeSet attrs) {
        maxHeight=defaultHeight;
//        if (attrs != null) {
//            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
//            //200 is a defualt value
//            maxHeight = styledAttrs.getDimensionPixelSize(R.styleable.MaxHeightScrollView_maxHeight, defaultHeight);
//
//            styledAttrs.recycle();
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}