package itg8.com.meetingapp.showcase;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Android itg 8 on 2/8/2018.
 */

public class ViewTarget implements Target {

    private View view;

    public ViewTarget(View view) {
        this.view = view;
    }

    @Override
    public Point getPoint() {

        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0] + (view.getWidth() / 2), location[1] + (view.getHeight() / 2));
    }

    @Override
    public Rect getRect() {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Rect(
                location[0],
                location[1],
                location[0] + view.getWidth(),
                location[1] + view.getHeight()
        );
    }

    @Override
    public View getView() {
        return view;
    }
}
