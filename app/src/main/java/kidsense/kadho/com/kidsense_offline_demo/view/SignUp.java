package kidsense.kadho.com.kidsense_offline_demo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import kidsense.kadho.com.kidsense_offline_demo.R;
import kidsense.kadho.com.kidsense_offline_demo.view.MainActivity;

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
        this.isValid = true;

        this.firstName = findViewById(R.id.firstName);
        this.lastName = findViewById(R.id.lastName);
        this.username = findViewById(R.id.username);
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);

        if( isEmpty(this.firstName) ){
            this.firstName.setError("Field can't be empty.");
            this.isValid = false;
        }

        if( isEmpty(this.lastName) ){
            this.lastName.setError("Field can't be empty.");
            this.isValid = false;
        }

        if( isEmpty(this.username) ){
            this.username.setError("Field can't be empty.");
            this.isValid = false;
        }

        if( isEmpty(this.email) ){
            this.email.setError("Field can't be empty.");
            this.isValid = false;
        }

        String emailAddress = this.email.getText().toString();
        if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            this.email.setError("Invalid Email.");
            this.isValid = false;
        }

        if( isEmpty(this.password) ){
            this.password.setError("Field can't be empty.");
            this.isValid = false;
        }

    }

    private boolean isEmpty(EditText input){
        String text = input.getText().toString();
        return TextUtils.isEmpty(text);
    }

    public void registerUser(View view) throws Exception {
        validateFields();

        if(this.isValid)
            new Database(SignUp.this).execute(firstName.getText().toString(),
                                                        lastName.getText().toString(),
                                                        username.getText().toString(),
                                                        email.getText().toString(),
                                                        password.getText().toString());
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
}