package kidsense.kadho.com.kidsense_offline_demo.view;

import android.app.Activity;
import android.os.AsyncTask;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class AuthorizedWebsites extends AsyncTask<String, Void, JSONObject> {

    private static String url = "https://capstone.kidsense.ai/api/server.php?action=LOGIN&username=%s&password=%s";
    private Activity activity;

    protected AuthorizedWebsites(Activity newActivity) {
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
}
