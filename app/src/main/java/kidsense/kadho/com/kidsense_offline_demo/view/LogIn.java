package kidsense.kadho.com.kidsense_offline_demo.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import kidsense.kadho.com.kidsense_offline_demo.R;
import kidsense.kadho.com.kidsense_offline_demo.view.MainActivity;

public class LogIn extends AppCompatActivity {
    private boolean isValid = true;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    private boolean isEmpty(EditText input){
        String text = input.getText().toString();
        return TextUtils.isEmpty(text);
    }

    public void validateFields(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isValid = true;

                username = findViewById(R.id.loginUsername);
                password = findViewById(R.id.loginPassword);

                if( isEmpty(username) ){
                    username.setError("Field can't be empty.");
                    isValid = false;
                }

                if( isEmpty(password) ){
                    password.setError("Field can't be empty.");
                    isValid = false;
                }
            }
        });
    }

    public void login(View view){
        validateFields();

        if(this.isValid)
            new UserLogin(LogIn.this).execute(username.getText().toString(), password.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
