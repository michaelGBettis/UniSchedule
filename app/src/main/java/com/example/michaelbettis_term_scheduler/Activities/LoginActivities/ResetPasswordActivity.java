package com.example.michaelbettis_term_scheduler.Activities.LoginActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.Activities.JavaMailAPI;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.SchedulerDatabase;
import com.example.michaelbettis_term_scheduler.ViewModel.UserViewModel;

import java.util.UUID;

@SuppressWarnings("ALL")
public class ResetPasswordActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPhone;
    SchedulerDatabase db;
    private UserViewModel userViewModel;
    UserEntity currentUser;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //initializes the values of the EditText fields
        editTextUsername = (EditText) findViewById(R.id.reset_email);
        editTextPhone = (EditText) findViewById(R.id.reset_phone);
        db = SchedulerDatabase.getInstance(getApplicationContext());

        //creates or provides a view model instance
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);

        Button resetPW = findViewById(R.id.reset);
        resetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString().toUpperCase();
                String phone = editTextPhone.getText().toString();

                //check to see if all fields have a value and are not blank spaces
                if (username.trim().isEmpty() || phone.trim().isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter a value in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    if (validateReset(username, phone)) {
                        String fName = currentUser.getUser_fname();
                        String mName = currentUser.getUser_mname();
                        String lName = currentUser.getUser_lname();
                        String address = currentUser.getUser_address();
                        String random = UUID.randomUUID().toString().substring(0, 8);
                        String type = currentUser.getUser_type();
                        String college = currentUser.getCollege_type();
                        String program = currentUser.getProgram_type();

                        if (currentUser.getUser_type().equals("Graduate")) {
                            UserEntity user = new UserEntity(fName, mName, lName, address, phone, username, random, type, college, program, currentUser.getMinor());
                            user.setUser_id(currentUser.getUser_id());
                            userViewModel.update(user);
                        } else {
                            UserEntity user = new UserEntity(fName, mName, lName, address, phone, username, random, type, college, program, currentUser.getSat_score());
                            user.setUser_id(currentUser.getUser_id());
                            userViewModel.update(user);
                        }

                        resetEmail(username, random, fName);
                        Toast.makeText(ResetPasswordActivity.this, "Password reset. Please check your email.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Username and/or phone do not match our records. Please try again.", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

    }

    //method for the login button
    private boolean validateReset(String username, String phone) {
        currentUser = db.userDao().validateCurrentUser(username, phone);

        if (currentUser == null) {
            Toast.makeText(ResetPasswordActivity.this, "Username and/or phone do not match our records. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    //method for sending email with new password
    public void resetEmail(String email, String password, String fname) {

        String message = "Hello " + fname + ",\n" +
                "It seems you have forgotten your password, not to worry! Below you will find your\n " +
                "new password. While this is a secure password it is recommended that you change you\n " +
                "password for your protection and ease of use in the future.\n" +
                "Temporary password: " + password + "\n" +
                "Thank you,\n" +
                "TT Team";

        JavaMailAPI javaMailAPI = new JavaMailAPI(email, "Password Reset", message);
        javaMailAPI.execute();

    }

}