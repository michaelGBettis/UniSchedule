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

import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Activities.TermActivities.TermListActivity;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.ViewModel.UserViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.SQLOutput;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?.@#$%^&+=])(?=\\S+$).{8,}$");
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
    private TextView titleTextView;
    private Button createAccountBtn;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        createAccountBtn = findViewById(R.id.create_account);
        titleTextView = findViewById(R.id.create_account_title);

        //creates or provides a view model instance
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);

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
                } else {
                    userGPAScoreTextInput.setVisibility(View.VISIBLE);
                    userMinorTextInput.setVisibility(View.GONE);
                }

            }
        });
        //Gets and sets the passed in values
        Intent intent = getIntent();
        userId = getIntent().getIntExtra(MainActivity.USER_ID, -1);
        if (intent.hasExtra(MainActivity.USER_ID)) {
            titleTextView.setText("Account Information");
            createAccountBtn.setText("Update User");
            userFNameTextInput.getEditText().setText(intent.getStringExtra(MainActivity.USER_F_NAME));
            userMNameTextInput.getEditText().setText(intent.getStringExtra(MainActivity.USER_M_NAME));
            userLNameTextInput.getEditText().setText(intent.getStringExtra(MainActivity.USER_L_NAME));
            userAddressTextInput.getEditText().setText(intent.getStringExtra(MainActivity.USER_ADDRESS));
            userPhoneTextInput.getEditText().setText(intent.getStringExtra(MainActivity.USER_PHONE));
            userEmailTextInput.getEditText().setText(intent.getStringExtra(MainActivity.USER_EMAIL));
            studentTypeDropdown.setText(intent.getStringExtra(MainActivity.STUDENT_TYPE), false);
            collegeTypeDropdown.setText(intent.getStringExtra(MainActivity.COLLEGE_TYPE), false);

            if (studentTypeDropdown.getEditableText().toString().equals("Graduate")) {
                userMinorTextInput.setVisibility(View.VISIBLE);
                userMinorTextInput.getEditText().setText(intent.getStringExtra(MainActivity.USER_MINOR));
            } else {
                double gpa = intent.getDoubleExtra(MainActivity.USER_GPA_SCORE, -1);
                userGPAScoreTextInput.setVisibility(View.VISIBLE);
                userGPAScoreTextInput.getEditText().setText(Double.toString(gpa));
            }

        }

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


        if (Helper.isInputEmpty(userFNameTextInput, fName) | Helper.isInputEmpty(userMNameTextInput, mName)
                | Helper.isInputEmpty(userLNameTextInput, lName) | Helper.isInputEmpty(userAddressTextInput, address)
                | Helper.isInputEmpty(userPhoneTextInput, phone) | !isValidEmail(userEmailTextInput, email)
                | Helper.isInputEmpty(studentTypeTextInput, studentType) | Helper.isInputEmpty(collegeTypeTextInput, collegeType)
                | isExtraStudentInputEmpty(studentType) | !isValidPassword(userPasswordTextInput, password)
                | !isValidPassword(userPasswordChkTextInput, passwordChk)) {
            return;
        }

        if (!isPasswordMatch(password, passwordChk, userPasswordChkTextInput)) {
            return;
        }

        if (isCurrentUser(userId)) {

            if (studentType.equals("Graduate")) {

                UserEntity graduate = new UserEntity(fName, mName, lName, address, phone, email, password, studentType, collegeType, program, userMinorTextInput.getEditText().getText().toString());
                graduate.setUser_id(userId);
                userViewModel.update(graduate);

            } else {

                UserEntity undergrad = new UserEntity(fName, mName, lName, address, phone, email, password, studentType, collegeType, program, Double.parseDouble(userGPAScoreTextInput.getEditText().getText().toString()));
                undergrad.setUser_id(userId);
                userViewModel.update(undergrad);

            }
            Toast.makeText(this, "User information updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, TermListActivity.class);
            startActivity(intent);

        } else {

            if (studentType.equals("Graduate")) {

                 UserEntity graduate = new UserEntity(fName, mName, lName, address, phone, email, password, studentType, collegeType, program, userMinorTextInput.getEditText().getText().toString());
                userViewModel.insert(graduate);

            } else {
                UserEntity undergrad = new UserEntity(fName, mName, lName, address, phone, email, password, studentType, collegeType, program, Double.parseDouble(userGPAScoreTextInput.getEditText().getText().toString()));
                userViewModel.insert(undergrad);
            }

            Toast.makeText(this, "New user created", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
        }


    }

    private boolean isValidEmail(TextInputLayout emailInput, String email) {
        SchedulerDatabase db = SchedulerDatabase.getInstance(getApplicationContext());
        UserEntity isDuplicateEmail = db.userDao().validateUserByEmail(email);

        if (Helper.isInputEmpty(emailInput, email)) {
            return false;
        } else if (isDuplicateEmail != null) {
            emailInput.setError("Email is invalid or already taken.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Email is invalid or already taken.");
            return false;
        } else {
            emailInput.setError(null);
            return true;
        }

    }

    private boolean isValidPassword(TextInputLayout passwordInput, String password) {

        if (Helper.isInputEmpty(passwordInput, password)) {
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passwordInput.setError("Password too weak.");
            return false;
        } else {
            passwordInput.setError(null);
            return true;
        }
    }

    private boolean isPasswordMatch(String password, String passwordChk, TextInputLayout passwordChkInput) {
        if (password.equals(passwordChk)) {
            passwordChkInput.setError(null);
            return true;
        } else {
            passwordChkInput.setError("Password does not match.");
            return false;
        }

    }

    private boolean isCurrentUser(int userId) {
        if (userId == -1) {
            return false;
        }
        return true;

    }

    private boolean isValidGPAScore(TextInputLayout gpaScoreInput, String gpaScore) {

        if (Helper.isInputEmpty(gpaScoreInput, gpaScore)) {
            return false;
        } else if (Double.parseDouble(gpaScore) > 4.0) {
            gpaScoreInput.setError("enter a valid GPA score");
            return false;
        } else {
            gpaScoreInput.setError(null);
            return true;
        }

    }

    private boolean isExtraStudentInputEmpty(String studentType) {

        if (studentType.equals("Graduate")) {
            System.out.println(userMinorTextInput.getEditText().getText().toString());
            return Helper.isInputEmpty(userMinorTextInput, userMinorTextInput.getEditText().getText().toString());
        } else if (studentType.equals("Undergraduate")) {
            System.out.println(userGPAScoreTextInput.getEditText().getText().toString());
            return Helper.isInputEmpty(userGPAScoreTextInput, userGPAScoreTextInput.getEditText().getText().toString());
        } else {
            return true;
        }
    }

}