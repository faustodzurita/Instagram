package com.faustodzurita.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private EditText emailInput;
    private EditText phoneInput;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.activity_signup_username_textfield);
        passwordInput = findViewById(R.id.activity_signup_password_textfield);
        passwordConfirmInput = findViewById(R.id.activity_signup_password_confirm_textfield);
        emailInput = findViewById(R.id.activity_signup_email_textfield);
        phoneInput = findViewById(R.id.activity_signup_phone_textfield);
        signupButton = findViewById(R.id.activity_signup_signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String passwordConfirm = passwordConfirmInput.getText().toString();
                final String email = emailInput.getText().toString();
                final String phone = phoneInput.getText().toString();

                if (password.equals(passwordConfirm)) {
                    signup(username, password, email, phone);
                } else {
                    Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signup(String username, String password, String email, String phone) {
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("phone", phone);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("SignupActivity","Signup successful.");

                    final Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("SignupActivity", "Signup failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}
