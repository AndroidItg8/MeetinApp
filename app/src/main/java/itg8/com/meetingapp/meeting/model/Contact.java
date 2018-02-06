package itg8.com.meetingapp.meeting.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by swapnilmeshram on 06/02/18.
 */

public class Contact implements Parcelable {
    private String name;
    private String number;
    private String email;
    private Bitmap picture;
    private boolean isSelected = false;

    public Contact(String name, String email, Bitmap picture,String number){


        this.name = name;
        this.email = email;
        this.picture = picture;
        this.number=number;

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public Bitmap getPicture(){
        return picture;
    }

    public boolean isSelected(){
        return isSelected ;
    }
    public void setSelected(boolean b){
        isSelected = b;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.number);
        dest.writeString(this.email);
        dest.writeParcelable(this.picture, flags);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected Contact(Parcel in) {
        this.name = in.readString();
        this.number = in.readString();
        this.email = in.readString();
        this.picture = in.readParcelable(Bitmap.class.getClassLoader());
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
