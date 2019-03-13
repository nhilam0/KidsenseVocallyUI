package kidsense.kadho.com.kidsense_offline_demo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import kidsense.kadho.com.kidsense_offline_demo.R;

public class SignUp extends AppCompatActivity {
    private boolean isValid = true;
    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    private void validateFields(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isValid = true;

                firstName = findViewById(R.id.firstName);
                lastName = findViewById(R.id.lastName);
                username = findViewById(R.id.username);
                email = findViewById(R.id.emailAddress);
                password = findViewById(R.id.password);

                if( isEmpty(firstName) ){
                    firstName.setError("Field can't be empty.");
                    isValid = false;
                }

                if( isEmpty(lastName) ){
                    lastName.setError("Field can't be empty.");
                    isValid = false;
                }

                if( isEmpty(username) ){
                    username.setError("Field can't be empty.");
                    isValid = false;
                }

                if( isEmpty(email) ){
                    email.setError("Field can't be empty.");
                    isValid = false;
                }

                String emailAddress = email.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                    email.setError("Invalid email address.");
                    isValid = false;
                }

                if( isEmpty(password) ){
                    password.setError("Field can't be empty.");
                    isValid = false;
                }
            }
        });
    }

    private boolean isEmpty(EditText input){
        String text = input.getText().toString();
        return TextUtils.isEmpty(text);
    }

    public void registerUser(View view) throws Exception {
        validateFields();

        if(this.isValid)
            new RegisterUser(SignUp.this).execute(this.firstName.getText().toString(),
                                                            this.lastName.getText().toString(),
                                                            this.username.getText().toString(),
                                                            this.email.getText().toString(),
                                                            this.password.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}