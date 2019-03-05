package kidsense.kadho.com.kidsense_offline_demo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import kidsense.kadho.com.kidsense_offline_demo.view.ExecuteCommands;
import kidsense.kadho.com.kidsense_offline_demo.view.SettingsModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class security extends Fragment {


    public security() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_security, container, false);
        // Inflate the layout for this fragment

        SettingsModel settingsModel = ExecuteCommands.getSettings();

        CheckBox fullNameCheckBox = (CheckBox) rootView.findViewById(R.id.fullNameCheckBox);
        CheckBox homeAddressCheckBox = (CheckBox) rootView.findViewById(R.id.homeAddressCheckBox);
        CheckBox telephoneCheckBox = (CheckBox) rootView.findViewById(R.id.telephoneCheckBox);
        CheckBox emailAddressCheckBox = (CheckBox) rootView.findViewById(R.id.emailAddressCheckBox);
        CheckBox socialSecurityCheckBox = (CheckBox) rootView.findViewById(R.id.socialSecurityCheckBox);
        CheckBox locationCheckBox = (CheckBox) rootView.findViewById(R.id.locationCheckBox);
//        CheckBox videoAudioFilesCheckBox = (CheckBox) findViewById(R.id.videoAudioFilesCheckBox);

        fullNameCheckBox.setChecked(settingsModel.FullName);
        homeAddressCheckBox.setChecked(settingsModel.Address);
        telephoneCheckBox.setChecked(settingsModel.PhoneNumber);
        emailAddressCheckBox.setChecked(settingsModel.Email);
        socialSecurityCheckBox.setChecked(settingsModel.Social);
        locationCheckBox.setChecked(settingsModel.Location);
//        videoAudioFilesCheckBox.setChecked(settingsModel.);


        return inflater.inflate(R.layout.tab_security, container, false);
    }

}
