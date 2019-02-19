package kidsense.kadho.com.kidsense_offline_demo.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import kidsense.kadho.com.kidsense_offline_demo.R;

public class DisplaySettingsPage extends AppCompatActivity {
    private ArrayList<String> authoizedWebsites = new ArrayList<String>(Arrays.asList("WWW.KIDSESNE.AI", "WWW.KADHO.COM"));


    private int deleteWebsite = -1;
    private ArrayAdapter<String> adapter;

    EditText text;
    Button addButton;
    Button deleteButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings_page);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        mToolbar.setTitle(getString(R.string.settingsPage));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        text = (EditText) findViewById(R.id.enterWebsite);
        addButton = (Button) findViewById(R.id.addWebsiteButton);
        deleteButton = (Button) findViewById(R.id.deleteWebsiteButton);
        listView = (ListView) findViewById(R.id.websites);

        adapter = new ArrayAdapter<>(DisplaySettingsPage.this, android.R.layout.simple_list_item_multiple_choice, authoizedWebsites);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        unMarkItems();
        onBtnClick();
        onItemClick();
    }


    public void unMarkItems(){
        for(int i = 0; i < authoizedWebsites.size(); i++)
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
                String website = text.getText().toString().toUpperCase();
                if(!website.isEmpty() && !authoizedWebsites.contains(website))
                    authoizedWebsites.add(website);

                text.setText("");
                unMarkItems();
                adapter.notifyDataSetChanged();

            }
        });
    }

    public void onDeleteBtnClicked(View view) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteWebsite != -1) {
                    authoizedWebsites.remove(deleteWebsite);
                }

                deleteWebsite = -1;
                unMarkItems();
                adapter.notifyDataSetChanged();
            }
        });
    }
}
