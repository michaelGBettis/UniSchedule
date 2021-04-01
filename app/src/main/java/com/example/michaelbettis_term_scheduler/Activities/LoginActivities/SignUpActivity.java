package com.example.michaelbettis_term_scheduler.Activities.LoginActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.ViewModel.UserViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    int userId;

    private TextInputLayout userFNameTextInput;
    private TextInputLayout userMNameTextInput;
    private TextInputLayout userLNameTextInput;
    private TextInputLayout userAddressTextInput;
    private TextInputLayout userPhoneTextInput;
    private TextInputLayout userEmailTextInput;
    private TextInputLayout userMinorTextInput;
    private TextInputLayout userGPAScoreTextInput;
    private TextInputLayout userPasswordTextInput;
    private TextInputLayout userPasswordChkTextInput;
    private TextInputLayout studentTypeTextInput;
    private TextInputLayout collegeTypeTextInput;
    private AutoCompleteTextView studentTypeDropdown;
    private AutoCompleteTextView collegeTypeDropdown;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //======================================Hooks=============================================//

        userFNameTextInput = findViewById(R.id.user_first_name);
        userMNameTextInput = findViewById(R.id.user_middle_name);
        userLNameTextInput = findViewById(R.id.user_last_name);
        userAddressTextInput = findViewById(R.id.user_address);
        userPhoneTextInput = findViewById(R.id.user_Phone);
        userEmailTextInput = findViewById(R.id.user_email);
        userMinorTextInput = findViewById(R.id.user_minor);
        userGPAScoreTextInput = findViewById(R.id.user_gpa_score);
        userPasswordTextInput = findViewById(R.id.user_password);
        userPasswordChkTextInput = findViewById(R.id.user_password_check);
        studentTypeTextInput = findViewById(R.id.student_type);
        collegeTypeTextInput = findViewById(R.id.college_type);
        studentTypeDropdown = findViewById(R.id.student_type_text);
        collegeTypeDropdown = findViewById(R.id.college_type_text);
        Button createAccountBtn = findViewById(R.id.create_account);
        TextView titleTextView = findViewById(R.id.create_account_title);
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);

        //=======================================Adapters=========================================//

        //sets the values for the student type menu
        final ArrayAdapter<String> studentTypeAdapter = new ArrayAdapter<>(
                SignUpActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.student_type));
        studentTypeDropdown.setAdapter(studentTypeAdapter);

        //sets the values for the college type menu
        final ArrayAdapter<String> collegeTypeAdapter = new ArrayAdapter<>(
                SignUpActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.college_type));
        collegeTypeDropdown.setAdapter(collegeTypeAdapter);

        //checks the value of the student type and sets the subclass specific field accordingly
        studentTypeDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (studentTypeDropdown.getEditableText().toString().equals("Graduate")) {
                    userMinorTextInput.setVisibility(View.VISIBLE);
                    userGPAScoreTextInput.setVisibility(View.GONE);
                    return;
                }

                userGPAScoreTextInput.setVisibility(View.VISIBLE);
                userMinorTextInput.setVisibility(View.GONE);

            }
        });

        //===============================Setting Intent Values====================================//

        //Gets and sets the passed in values
        Intent intent = getIntent();
        userId = getIntent().getIntExtra(Helper.USER_ID, -1);
        if (intent.hasExtra(Helper.USER_ID)) {
            titleTextView.setText("Account Information");
            createAccountBtn.setText("Update User");
            userFNameTextInput.getEditText().setText(intent.getStringExtra(Helper.USER_F_NAME));
            userMNameTextInput.getEditText().setText(intent.getStringExtra(Helper.USER_M_NAME));
            userLNameTextInput.getEditText().setText(intent.getStringExtra(Helper.USER_L_NAME));
            userAddressTextInput.getEditText().setText(intent.getStringExtra(Helper.USER_ADDRESS));
            userPhoneTextInput.getEditText().setText(intent.getStringExtra(Helper.USER_PHONE));
            userEmailTextInput.getEditText().setText(intent.getStringExtra(Helper.USER_EMAIL));
            studentTypeDropdown.setText(intent.getStringExtra(Helper.STUDENT_TYPE), false);
            collegeTypeDropdown.setText(intent.getStringExtra(Helper.COLLEGE_TYPE), false);

            if (studentTypeDropdown.getEditableText().toString().equals("Graduate")) {
                userMinorTextInput.setVisibility(View.VISIBLE);
                userMinorTextInput.getEditText().setText(intent.getStringExtra(Helper.USER_MINOR));
                return;
            }

            double gpa = intent.getDoubleExtra(Helper.USER_GPA_SCORE, -1);
            userGPAScoreTextInput.setVisibility(View.VISIBLE);
            userGPAScoreTextInput.getEditText().setText(Double.toString(gpa));

        }

        //====================================Buttons=============================================//

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }

    public void createAccount() {
        String fName = userFNameTextInput.getEditText().getText().toString();
        String mName = userMNameTextInput.getEditText().getText().toString();
        String lName = userLNameTextInput.getEditText().getText().toString();
        String address = userAddressTextInput.getEditText().getText().toString();
        String phone = userPhoneTextInput.getEditText().getText().toString();
        String email = userEmailTextInput.getEditText().getText().toString().toUpperCase();
        String password = userPasswordTextInput.getEditText().getText().toString();
        String passwordChk = userPasswordChkTextInput.getEditText().getText().toString();
        String studentType = studentTypeDropdown.getEditableText().toString();
        String collegeType = collegeTypeDropdown.getEditableText().toString();
        String program = studentType + " " + collegeType;

        if (Helper.isInputEmpty(userFNameTextInput) | Helper.isInputEmpty(userMNameTextInput)
                | Helper.isInputEmpty(userLNameTextInput) | Helper.isInputEmpty(userAddressTextInput)
                | Helper.isInputEmpty(userPhoneTextInput) | !isValidEmail(userEmailTextInput)
                | Helper.isInputEmpty(studentTypeTextInput) | Helper.isInputEmpty(collegeTypeTextInput)
                | isExtraStudentInputEmpty(studentType) | !isValidPassword(userPasswordTextInput)
                | !isValidPassword(userPasswordChkTextInput)) {
            return;
        }

        if (!isPasswordMatch(password, passwordChk, userPasswordChkTextInput)) {
            return;
        }

        if (isCurrentUser(userId) && studentType.equals("Graduate")) {

            UserEntity graduate = new UserEntity(fName, mName, lName, address, phone, email, password, studentType, collegeType, program, userMinorTextInput.getEditText().getText().toString());
            graduate.setUser_id(userId);
            userViewModel.update(graduate);
            Toast.makeText(this, "User information updated", Toast.LENGTH_SHORT).show();
            Helper.goToTerms(userId, SignUpActivity.this);

        }

        if (isCurrentUser(userId) && studentType.equals("Undergraduate")) {

            UserEntity undergrad = new UserEntity(fName, mName, lName, address, phone, email, password, studentType, collegeType, program, Double.parseDouble(userGPAScoreTextInput.getEditText().getText().toString()));
            undergrad.setUser_id(userId);
            userViewModel.update(undergrad);
            Toast.makeText(this, "User information updated", Toast.LENGTH_SHORT).show();
            Helper.goToTerms(userId, SignUpActivity.this);

        }

        if (!isCurrentUser(userId) && studentType.equals("Graduate")) {

            UserEntity graduate = new UserEntity(fName, mName, lName, address, phone, email, password, studentType, collegeType, program, userMinorTextInput.getEditText().getText().toString());
            userViewModel.insert(graduate);
            Toast.makeText(this, "New user created", Toast.LENGTH_SHORT).show();
            Helper.signOut(SignUpActivity.this);

        }

        if (!isCurrentUser(userId) && studentType.equals("Undergraduate")) {
            UserEntity undergrad = new UserEntity(fName, mName, lName, address, phone, email, password, studentType, collegeType, program, Double.parseDouble(userGPAScoreTextInput.getEditText().getText().toString()));
            userViewModel.insert(undergrad);
            Toast.makeText(this, "New user created", Toast.LENGTH_SHORT).show();
            Helper.signOut(SignUpActivity.this);
        }
    }

    private boolean isValidEmail(TextInputLayout emailInput) {
        String email = emailInput.getEditText().getText().toString();
        SchedulerDatabase db = SchedulerDatabase.getInstance(getApplicationContext());
        UserEntity isDuplicateEmail = db.userDao().validateUserByEmail(email);

        if (Helper.isInputEmpty(emailInput)) {
            return false;
        }

        if (isDuplicateEmail != null) {
            emailInput.setError("Email is invalid or already taken.");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Email is invalid or already taken.");
            return false;
        }

        emailInput.setError(null);
        return true;

    }

    private boolean isValidPassword(TextInputLayout passwordInput) {
        String password = passwordInput.getEditText().getText().toString();

        if (Helper.isInputEmpty(passwordInput)) {
            return false;
        }

        if (!Helper.PASSWORD_PATTERN.matcher(password).matches()) {
            passwordInput.setError("Password too weak.");
            return false;
        }

        passwordInput.setError(null);
        return true;
    }

    private boolean isPasswordMatch(String password, String passwordChk, TextInputLayout passwordChkInput) {
        if (password.equals(passwordChk)) {
            passwordChkInput.setError(null);
            return true;
        }

        passwordChkInput.setError("Password does not match.");
        return false;

    }

    private boolean isCurrentUser(int userId) {
        if (userId == -1) {
            return false;
        }

        return true;

    }

    private boolean isValidGPAScore(TextInputLayout gpaScoreInput) {
        String gpaScore = gpaScoreInput.getEditText().getText().toString();

        if (Helper.isInputEmpty(gpaScoreInput)) {
            return false;
        }

        if (Double.parseDouble(gpaScore) > 4.0) {
            gpaScoreInput.setError("enter a valid GPA score");
            return false;
        }

        gpaScoreInput.setError(null);
        return true;

    }

    private boolean isExtraStudentInputEmpty(String studentType) {

        if (studentType.equals("Graduate")) {
            return Helper.isInputEmpty(userMinorTextInput);
        }
        if (studentType.equals("Undergraduate")) {
            return isValidGPAScore(userGPAScoreTextInput);
        }

        return true;
    }
}