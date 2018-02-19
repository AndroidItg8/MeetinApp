package itg8.com.meetingapp.widget.search;

import android.graphics.drawable.Drawable;

import itg8.com.meetingapp.db.TblMeeting;

public class SearchResult {
    public TblMeeting getTitle() {
        return title;
    }

    public void setTitle(TblMeeting title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public TblMeeting title;
    public Drawable icon;

    /**
     * Create a search result with text and an icon
     * @param title
     * @param icon
     */
    public SearchResult(TblMeeting title, Drawable icon) {
       this.title = title;
       this.icon = icon;
    }

    public int viewType = 0;

    public SearchResult(TblMeeting title){
        this.title = title;
    }

    public SearchResult(int viewType, TblMeeting title){
        this.viewType = viewType;
        this.title = title;
    }
    
    /**
     * Return the title of the result
     */
    @Override
    public String toString() {
        return title.getTitle();
    }
    
}