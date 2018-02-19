package itg8.com.meetingapp.test;

import android.databinding.BaseObservable;

/**
 * Created by swapnilmeshram on 13/02/18.
 */

public class TestViewModel extends BaseObservable {
    private String text;

    public TestViewModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
