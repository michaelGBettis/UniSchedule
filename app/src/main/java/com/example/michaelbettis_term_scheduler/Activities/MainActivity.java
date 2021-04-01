package com.example.michaelbettis_term_scheduler.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.Activities.LoginActivities.ResetPasswordActivity;
import com.example.michaelbettis_term_scheduler.Activities.LoginActivities.SignUpActivity;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity {

    private TextInputLayout passwordTextInput;
    private TextInputLayout usernameTextInput;
    UserEntity currentUser;
    SchedulerDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //======================================Hooks=============================================//

        usernameTextInput = findViewById(R.id.username);
        passwordTextInput = findViewById(R.id.password);
        Button loginBtn = findViewById(R.id.login);
        Button signUpBtn = findViewById(R.id.sign_up);
        Button testBtn = findViewById(R.id.test_app_button);
        Button resetPasswordBtn = findViewById(R.id.reset_password);

        //====================================Buttons=============================================//

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTextInput.getEditText().getText().toString().toUpperCase();
                String password = passwordTextInput.getEditText().getText().toString();

                //Username: GradStudent@test.com Password: GradStudent
                //Username: UGradStudent@test.com Password: UGradStudent


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