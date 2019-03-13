package kidsense.kadho.com.kidsense_offline_demo.view;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserSettings implements Parcelable {
    public ArrayList<String> authorizedWebsites;


    protected UserSettings(Parcel in) {
        authorizedWebsites = in.createStringArrayList();
    }

    public static final Creator<UserSettings> CREATOR = new Creator<UserSettings>() {
        @Override
        public UserSettings createFromParcel(Parcel in) {
            return new UserSettings(in);
        }

        @Override
        public UserSettings[] newArray(int size) {
            return new UserSettings[size];
        }
    };


    UserSettings(ArrayList<String> websites){
        this.authorizedWebsites = new ArrayList<String>();

        if(!websites.isEmpty()) {
            for(int i = 0; i < websites.size(); i++)
                this.authorizedWebsites.add(websites.get(i));
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(authorizedWebsites);
    }
}
