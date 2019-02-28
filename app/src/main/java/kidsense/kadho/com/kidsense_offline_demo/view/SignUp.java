package kidsense.kadho.com.kidsense_offline_demo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import java.sql.Connection;
import java.util.regex.Pattern;

import java.sql.SQLException;

import kidsense.kadho.com.kidsense_offline_demo.R;
import kidsense.kadho.com.kidsense_offline_demo.view.MainActivity;

public class SignUp extends AppCompatActivity {
    private boolean isValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.loginLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin(v);
            }
        });
    }

    private void validateFields(){
        this.isValid = true;

        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText username = findViewById(R.id.username);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        if( isEmpty(firstName) ){
            firstName.setError("Field can't be empty.");
            this.isValid = false;
        }

        if( isEmpty(lastName) ){
            lastName.setError("Field can't be empty.");
            this.isValid = false;
        }

        if( isEmpty(username) ){
            username.setError("Field can't be empty.");
            this.isValid = false;
        }

        if( isEmpty(email) ){
            email.setError("Field can't be empty.");
            this.isValid = false;
        }

        String emailAddress = email.getText().toString();
        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            email.setError("Invalid Email.");
            this.isValid = false;
        }

        if( isEmpty(password) ){
            password.setError("Field can't be empty.");
            this.isValid = false;
        }

    }

    private boolean isEmpty(EditText input){
        String text = input.getText().toString();
        return TextUtils.isEmpty(text);
    }

    public void goToMain(View view) {
        validateFields();

        Connection conn = Database.establishConnection();

        if(this.isValid) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
}