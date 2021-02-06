package com.example.michaelbettis_term_scheduler.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.Activities.LoginActivities.ResetPasswordActivity;
import com.example.michaelbettis_term_scheduler.Activities.LoginActivities.SignUpActivity;
import com.example.michaelbettis_term_scheduler.Activities.TermActivities.TermListActivity;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.SchedulerDatabase;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    public static final String USER_ID = "com.example.michaelbettis_term_scheduler.Activities.USER_ID";
    public static final String USER_F_NAME = "com.example.michaelbettis_term_scheduler.Activities.USER_F_NAME";
    public static final String USER_M_NAME = "com.example.michaelbettis_term_scheduler.Activities.USER_M_NAME";
    public static final String USER_L_NAME = "com.example.michaelbettis_term_scheduler.Activities.USER_L_NAME";
    public static final String USER_ADDRESS = "com.example.michaelbettis_term_scheduler.Activities.USER_ADDRESS";
    public static final String USER_PHONE = "com.example.michaelbettis_term_scheduler.Activities.USER_PHONE";
    public static final String USER_MINOR = "com.example.michaelbettis_term_scheduler.Activities.USER_MINOR";
    public static final String USER_SAT_SCORE = "com.example.michaelbettis_term_scheduler.Activities.USER_SAT_SCORE";
    public static final String USER_EMAIL = "com.example.michaelbettis_term_scheduler.Activities.USER_EMAIL";
    public static final String STUDENT_TYPE = "com.example.michaelbettis_term_scheduler.Activities.STUDENT_TYPE";
    public static final String COLLEGE_TYPE = "com.example.michaelbettis_term_scheduler.Activities.COLLEGE_TYPE";
    private EditText editTextUsername;
    private EditText editTextPassword;
    SchedulerDatabase db;
    UserEntity currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Uni Schedule");

        //initializes the values of the EditText fields
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString().toUpperCase();
                String password = editTextPassword.getText().toString();

                //check to see if all fields have a value and are not blank spaces
                if (username.trim().isEmpty() || password.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a value in all fields.", Toast.LENGTH_SHORT).show();
                } else {

                    if (validateLogin(username, password)) {

                        Intent intent = new Intent(MainActivity.this, TermListActivity.class);
                        intent.putExtra(MainActivity.USER_ID, currentUser.getUser_id());
                        startActivity(intent);
                        editTextUsername.setText("");
                        editTextPassword.setText("");

                    } else {
                        Toast.makeText(MainActivity.this, "Username and/or password do not match. Please try again.", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });

        Button sign_up = findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

        Button resetPW = findViewById(R.id.reset_password);
        resetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    //method for the login button
    private boolean validateLogin(String username, String password) {
        db = SchedulerDatabase.getInstance(getApplicationContext());
        currentUser = db.userDao().validateUser(username, password);

        if (currentUser == null) {
            Toast.makeText(MainActivity.this, "Username and/or password do not match. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}