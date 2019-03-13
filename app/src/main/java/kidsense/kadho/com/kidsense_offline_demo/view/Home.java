package kidsense.kadho.com.kidsense_offline_demo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kidsense.kadho.com.kidsense_offline_demo.R;

public class Home extends AppCompatActivity {
    public static Home Instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Instance = this;
    }

    public void goToLogin(View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Home.Instance, LogIn.class);
                startActivity(intent);
            }
        });
    }

    public void goToSignup(View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Home.Instance, SignUp.class);
                startActivity(intent) ;
            }
        });
    }
}
