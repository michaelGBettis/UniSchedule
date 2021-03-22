package com.example.michaelbettis_term_scheduler.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.Activities.LoginActivities.ResetPasswordActivity;
import com.example.michaelbettis_term_scheduler.Activities.LoginActivities.SignUpActivity;
import com.example.michaelbettis_term_scheduler.Activities.TermActivities.TermListActivity;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity {
    private TextInputLayout usernameTextInput;
    private TextInputLayout passwordTextInput;
    private Button loginBtn;
    private Button signUpBtn;
    private Button resetPasswordBtn;
    private Button testBtn;
    SchedulerDatabase db;
    UserEntity currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GradStudent@test.com
        //UGradStudent@test.com

        //initializes the values of the EditText fields
        usernameTextInput = findViewById(R.id.username);
        passwordTextInput = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.sign_up);
        resetPasswordBtn = findViewById(R.id.reset_password);
        testBtn = findViewById(R.id.test_app_button);
        loginBtn = findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTextInput.getEditText().getText().toString().toUpperCase();
                String password = passwordTextInput.getEditText().getText().toString();

                if (validateLogin(username, password)) {

                    Helper.goToTerms(currentUser.getUser_id(), MainActivity.this);
                    usernameTextInput.getEditText().setText("");
                    passwordTextInput.getEditText().setText("");

                }
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.goToTerms(1, MainActivity.this);
            }
        });

    }

    private boolean validateLogin(String username, String password) {
        db = SchedulerDatabase.getInstance(getApplicationContext());
        currentUser = db.userDao().validateLogin(username, password);


        if (Helper.isInputEmpty(usernameTextInput) | Helper.isInputEmpty(passwordTextInput)) {
            return false;
        }

        if (currentUser == null) {
            Toast.makeText(MainActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}