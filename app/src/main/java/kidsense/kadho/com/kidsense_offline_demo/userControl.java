package kidsense.kadho.com.kidsense_offline_demo;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import kidsense.kadho.com.kidsense_offline_demo.view.AddWebsite;
import kidsense.kadho.com.kidsense_offline_demo.view.DeleteWebsite;
import kidsense.kadho.com.kidsense_offline_demo.view.LogIn;
import kidsense.kadho.com.kidsense_offline_demo.view.MainActivity;
import kidsense.kadho.com.kidsense_offline_demo.view.UserLogin;
import kidsense.kadho.com.kidsense_offline_demo.view.UserSettings;

import static android.app.Activity.RESULT_OK;
import static kidsense.kadho.com.kidsense_offline_demo.view.AddWebsite.*;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class userControl extends Fragment {
    private String userID = null;
    private UserSettings settings;

    private int deleteWebsite = -1;
    private ArrayAdapter<String> listAdapter;

    EditText text;
    Button addButton;
    Button deleteButton;
    ListView listView;

    public userControl(String userID, UserSettings settings) {
        this.userID = userID;
        this.settings = settings;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tab_user_control, container, false);

        text = (EditText) rootView.findViewById(R.id.enterWebsite);
        addButton = (Button) rootView.findViewById(R.id.addWebsiteButton);
        deleteButton = (Button) rootView.findViewById(R.id.deleteWebsiteButton);
        listView = (ListView) rootView.findViewById(R.id.allWebsites);

        listView = (ListView)rootView.findViewById(R.id.allWebsites);
        listAdapter = new ArrayAdapter<String>(userControl.this.getActivity(), android.R.layout.simple_list_item_multiple_choice, this.settings.authorizedWebsites){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);

                // Get the Layout Parameters for ListView Current Item View
                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Set the height of the Item View
                params.height = 200;
                view.setLayoutParams(params);

                return view;
            }
        };
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(listAdapter);


        unMarkItems();
        onItemClick();
        onBtnClick();
        onDeleteBtnClicked();

        return rootView;
    }

    public void unMarkItems(){
        for(int i = 0; i < this.settings.authorizedWebsites.size(); i++)
            listView.setItemChecked(i, false);
    }

    public void onItemClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(deleteWebsite == position) {
                    listView.setItemChecked(deleteWebsite, false);
                    deleteWebsite = -1;
                }
                else
                    deleteWebsite = position;
            }
        });
    }

    public void onBtnClick(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String website = text.getText().toString().toUpperCase();
                if(!website.isEmpty()){
                    if(website.length() <= 45) {
                        if (!settings.authorizedWebsites.contains(website)) {
                            new AddWebsite().execute(userID, website);
                            settings.authorizedWebsites.add(website);
                        } else
                            showMessageOK("Website URL already added to account.");
                    } else
                        showMessageOK("Website URL is too long. Must be less than 45 characters.");
                } else
                    showMessageOK("Enter a website URL.");

                text.setText("");
                unMarkItems();
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onDeleteBtnClicked() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteWebsite != -1) {
                    String website = settings.authorizedWebsites.get(deleteWebsite);
                    if(settings.authorizedWebsites.contains(website)) {
                        new DeleteWebsite().execute(userID, website);
                        settings.authorizedWebsites.remove(deleteWebsite);
                    }
                } else{
                    showMessageOK("Select a website URL to delete.");
                }

                deleteWebsite = -1;
                unMarkItems();
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showMessageOK(String message) {
        new AlertDialog.Builder(userControl.this.getActivity())
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }
}
