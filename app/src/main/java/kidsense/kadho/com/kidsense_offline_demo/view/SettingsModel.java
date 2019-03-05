package kidsense.kadho.com.kidsense_offline_demo.view;

public class SettingsModel {

    public boolean FullName = true;

    public boolean Address = true;

    public boolean PhoneNumber = true;

    public boolean Email = true;

    public boolean Social = true;

    public boolean Location = true;

    public SettingsModel() {

    }

    public SettingsModel(boolean fullName, boolean address, boolean phoneNumber, boolean email, boolean social, boolean location) {
        FullName = fullName;
        Address = address;
        PhoneNumber = phoneNumber;
        Email = email;
        Social = social;
        Location = location;
    }
}
