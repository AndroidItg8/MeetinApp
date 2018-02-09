package itg8.com.meetingapp.showcase;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Android itg 8 on 2/8/2018.
 */

public interface Target {

    /**
     * Returns center point of target.
     * We can get x and y coordinates using
     * point object
     * @return
     */
   public Point getPoint();

    /**
     * Returns Rectangle points of target view
     * @return
     */
  public   Rect getRect();

    /**
     * return target view
     * @return
     */
  public   View getView();
}
