package kidsense.kadho.com.kidsense_offline_demo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kidsense.kadho.com.kidsense_offline_demo.R;
import kidsense.kadho.com.kidsense_offline_demo.view.MainActivity;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void goToMain(View view) {

        if (ExecuteCommands.verifyUser(findViewById(R.id.email).toString(), findViewById(R.id.password).toString())) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            System.out.println("Invalid sign in");
        }


    }




}
