package kidsense.kadho.com.kidsense_offline_demo;


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
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class featureControl extends Fragment {
    private ArrayList<String> authoizedWebsites = new ArrayList<String>(Arrays.asList("WWW.KIDSESNE.AI", "WWW.KADHO.COM"));

    private int deleteWebsite = -1;
    private ArrayAdapter<String> listAdapter;

    EditText text;
    Button addButton;
    Button deleteButton;
    ListView listView;

    public featureControl() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.tab_feature_control, container, false);

        text = (EditText) rootView.findViewById(R.id.enterWebsite);
        addButton = (Button) rootView.findViewById(R.id.addWebsiteButton);
        deleteButton = (Button) rootView.findViewById(R.id.deleteWebsiteButton);
        listView = (ListView) rootView.findViewById(R.id.allWebsites);

        listView = (ListView)rootView.findViewById(R.id.allWebsites);
        listAdapter = new ArrayAdapter<String>(featureControl.this.getActivity(), android.R.layout.simple_list_item_multiple_choice, authoizedWebsites);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(listAdapter);


        unMarkItems();
        onItemClick();
        onBtnClick();
        onDeleteBtnClicked();

        return rootView;
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
                listAdapter.notifyDataSetChanged();

            }
        });
    }

    public void onDeleteBtnClicked() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteWebsite != -1) {
                    authoizedWebsites.remove(deleteWebsite);
                }

                deleteWebsite = -1;
                unMarkItems();
                listAdapter.notifyDataSetChanged();
            }
        });
    }

}
