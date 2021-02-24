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
    public static final String USER_ID = "com.example.michaelbettis_term_scheduler.Activities.USER_ID";
    public static final String USER_F_NAME = "com.example.michaelbettis_term_scheduler.Activities.USER_F_NAME";
    public static final String USER_M_NAME = "com.example.michaelbettis_term_scheduler.Activities.USER_M_NAME";
    public static final String USER_L_NAME = "com.example.michaelbettis_term_scheduler.Activities.USER_L_NAME";
    public static final String USER_ADDRESS = "com.example.michaelbettis_term_scheduler.Activities.USER_ADDRESS";
    public static final String USER_PHONE = "com.example.michaelbettis_term_scheduler.Activities.USER_PHONE";
    public static final String USER_MINOR = "com.example.michaelbettis_term_scheduler.Activities.USER_MINOR";
    public static final String USER_GPA_SCORE = "com.example.michaelbettis_term_scheduler.Activities.USER_GPA_SCORE";
    public static final String USER_EMAIL = "com.example.michaelbettis_term_scheduler.Activities.USER_EMAIL";
    public static final String STUDENT_TYPE = "com.example.michaelbettis_term_scheduler.Activities.STUDENT_TYPE";
    public static final String COLLEGE_TYPE = "com.example.michaelbettis_term_scheduler.Activities.COLLEGE_TYPE";
    private TextInputLayout usernameTextInput;
    private TextInputLayout passwordTextInput;
    private Button loginBtn;
    private Button signUpBtn;
    private Button resetPasswordBtn;
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
        loginBtn = findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTextInput.getEditText().getText().toString().toUpperCase();
                String password = passwordTextInput.getEditText().getText().toString();

                if (validateLogin(username, password)) {

                    Intent intent = new Intent(MainActivity.this, TermListActivity.class);
                    intent.putExtra(MainActivity.USER_ID, currentUser.getUser_id());
                    startActivity(intent);
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

    }

    private boolean validateLogin(String username, String password) {
        db = SchedulerDatabase.getInstance(getApplicationContext());
        currentUser = db.userDao().validateLogin(username, password);


        if (Helper.isInputEmpty(usernameTextInput, password) | Helper.isInputEmpty(passwordTextInput, username)) {
            return false;
        }

        if (currentUser == null) {
            Toast.makeText(MainActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}