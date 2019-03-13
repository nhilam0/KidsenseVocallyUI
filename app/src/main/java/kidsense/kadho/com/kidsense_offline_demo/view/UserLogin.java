package kidsense.kadho.com.kidsense_offline_demo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import kidsense.kadho.com.kidsense_offline_demo.R;

public class UserLogin extends AsyncTask<String, Void, JSONObject> {

    private static String url = "https://capstone.kidsense.ai/api/server.php?action=LOGIN&username=%s&password=%s";
    private Activity activity;

    protected UserLogin(Activity newActivity) {
        this.activity = newActivity;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        String updatedURL = String.format(url, strings[0], strings[1]);
        JSONObject response = null;

        try {
            URL newUserRequest = new URL(updatedURL);
            HttpsURLConnection conn = (HttpsURLConnection) newUserRequest.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer responseString = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                responseString.append(inputLine);
            }

            in.close();

            response = new JSONObject(responseString.toString());

        } catch (Exception e){
            e.getStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject result){
        TextView errorMessage = activity.findViewById(R.id.errorMessage);
        errorMessage.setText("");
        try {
            if (result.getInt("success") == 1) {
                String userID = result.getString("userid");

                // Get User Authorized Websites
                ArrayList<String> authoizedWebsites = new ArrayList<String>();
                JSONArray websites = result.getJSONArray("websites");

                for(int i = 0; i < websites.length(); i++){
                    authoizedWebsites.add(websites.get(i).toString());
                }

                Intent startApp = new Intent(activity, MainActivity.class);

                startApp.putExtra("websites", authoizedWebsites);
                startApp.putExtra("userID", userID);


                activity.startActivity(startApp);
                activity.finish();
            } else{
                String error = result.getString("message");
                errorMessage.setText(error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
