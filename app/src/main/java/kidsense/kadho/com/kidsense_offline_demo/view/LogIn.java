package kidsense.kadho.com.kidsense_offline_demo.view;

import android.content.Intent;
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

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isEmpty(EditText input){
        String text = input.getText().toString();
        return TextUtils.isEmpty(text);
    }

    public void validateFields(){
        this.isValid = true;

        this.username = findViewById(R.id.loginUsername);
        this.password = findViewById(R.id.loginPassword);

        if( isEmpty(this.username) ){
            this.username.setError("Field can't be empty.");
            this.isValid = false;
        }

        if( isEmpty(this.password) ){
            this.password.setError("Field can't be empty.");
            this.isValid = false;
        }
    }

    public void login(View view) throws Exception{
        validateFields();

        if(this.isValid)
            new UserLogin(LogIn.this).execute(username.getText().toString(), password.getText().toString());
    }




}
