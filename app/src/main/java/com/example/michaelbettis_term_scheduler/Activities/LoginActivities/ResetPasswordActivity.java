package com.example.michaelbettis_term_scheduler.Activities.LoginActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.JavaMailAPI;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.example.michaelbettis_term_scheduler.ViewModel.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.UUID;

@SuppressWarnings("ALL")
public class ResetPasswordActivity extends AppCompatActivity {
    private TextInputLayout textInputUsername;
    private Button resetPasswordBtn;
    private UserViewModel userViewModel;
    SchedulerDatabase db;
    UserEntity currentUser;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        //initializes the values of the EditText fields
        textInputUsername = findViewById(R.id.reset_email);
        resetPasswordBtn = findViewById(R.id.reset_button);
        db = SchedulerDatabase.getInstance(getApplicationContext());

        //creates or provides a view model instance
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textInputUsername.getEditText().getText().toString().toUpperCase();

                if (isValidUserEmail(username)) {
                    updatePassword(username);
                    Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

        });
    }

    private boolean isValidUserEmail(String username) {
        currentUser = db.userDao().validateUserByEmail(username);

        if (Helper.isInputEmpty(textInputUsername)) {
            return false;
        }

        if (currentUser == null) {
            Toast.makeText(ResetPasswordActivity.this, "Email does not match our records. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void updatePassword(String username) {
        String fName = currentUser.getUser_fname();
        String mName = currentUser.getUser_mname();
        String lName = currentUser.getUser_lname();
        String address = currentUser.getUser_address();
        String phone = currentUser.getUser_phone();
        String randomPassword = UUID.randomUUID().toString().substring(0, 8);
        String type = currentUser.getUser_type();
        String college = currentUser.getCollege_type();
        String program = currentUser.getProgram_type();

        if (currentUser.getUser_type().equals("Graduate")) {
            UserEntity user = new UserEntity(fName, mName, lName, address, phone, username, randomPassword, type, college, program, currentUser.getMinor());
            user.setUser_id(currentUser.getUser_id());
            userViewModel.update(user);
        } else {
            UserEntity user = new UserEntity(fName, mName, lName, address, phone, username, randomPassword, type, college, program, currentUser.getSat_score());
            user.setUser_id(currentUser.getUser_id());
            userViewModel.update(user);
        }

        sendResetPasswordEmail(username, randomPassword, fName);

    }

    public void sendResetPasswordEmail(String email, String password, String fname) {

        String message = "Hello " + fname + ",\n" +
                "It seems you have forgotten your password, not to worry! Below you will find your\n " +
                "new password. While this is a secure password, it is recommended that you change this\n " +
                "password for your protection and ease of use in the future!\n" +
                "New password: " + password + "\n" +
                "Thank you,\n" +
                "UniSchedule Team";

        JavaMailAPI javaMailAPI = new JavaMailAPI(email, "Password Reset", message);
        javaMailAPI.execute();

    }

}