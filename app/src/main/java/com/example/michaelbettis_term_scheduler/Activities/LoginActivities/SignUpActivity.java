package com.example.michaelbettis_term_scheduler.Activities.LoginActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.example.michaelbettis_term_scheduler.ViewModel.UserViewModel;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    int userId;

    private EditText editTextUserFName;
    private EditText editTextUserMName;
    private EditText editTextUserLName;
    private EditText editTextUserAddress;
    private EditText editTextUserPhone;
    private EditText editTextUserEmail;
    private EditText editTextUserMinor;
    private EditText editTextUserSATScore;
    private EditText editTextUserPassword;
    private EditText editTextUserPasswordChk;
    private Spinner spinnerStudentType;
    private Spinner spinnerCollegeType;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextUserFName = findViewById(R.id.user_first_name);
        editTextUserMName = findViewById(R.id.user_middle_name);
        editTextUserLName = findViewById(R.id.user_last_name);
        editTextUserAddress = findViewById(R.id.user_address);
        editTextUserPhone = findViewById(R.id.user_Phone);
        editTextUserEmail = findViewById(R.id.user_email);
        editTextUserMinor = findViewById(R.id.user_minor);
        editTextUserSATScore = findViewById(R.id.user_sat_score);
        editTextUserPassword = findViewById(R.id.user_password);
        editTextUserPasswordChk = findViewById(R.id.user_password_check);
        spinnerStudentType = findViewById(R.id.student_type);
        spinnerCollegeType = findViewById(R.id.college_type);
        Button create_account = findViewById(R.id.create_account);

        //sets the values for the parent spinners
        final ArrayAdapter<String> studentAdapter = new ArrayAdapter<>(SignUpActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.student_type));
        studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudentType.setAdapter(studentAdapter);


        final ArrayAdapter<String> collegeAdapter = new ArrayAdapter<>(SignUpActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.college_type));
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCollegeType.setAdapter(collegeAdapter);

        //checks the value of the spinner and sets the values of the subclass specific fields
        spinnerStudentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinnerStudentType.getSelectedItem().equals("Graduate")) {
                    editTextUserMinor.setVisibility(View.VISIBLE);
                    editTextUserSATScore.setVisibility(View.GONE);
                } else {
                    editTextUserSATScore.setVisibility(View.VISIBLE);
                    editTextUserMinor.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Intent intent = getIntent();
        userId = getIntent().getIntExtra(MainActivity.USER_ID, -1);
        if (intent.hasExtra(MainActivity.USER_ID)) {
            getSupportActionBar().setTitle("Edit User Info");
            create_account.setText("Update User");
            editTextUserFName.setText(intent.getStringExtra(MainActivity.USER_F_NAME));
            editTextUserMName.setText(intent.getStringExtra(MainActivity.USER_M_NAME));
            editTextUserLName.setText(intent.getStringExtra(MainActivity.USER_L_NAME));
            editTextUserAddress.setText(intent.getStringExtra(MainActivity.USER_ADDRESS));
            editTextUserPhone.setText(intent.getStringExtra(MainActivity.USER_PHONE));
            editTextUserEmail.setText(intent.getStringExtra(MainActivity.USER_EMAIL));

            //gets and sets spinner value
            int sSelectionPosition = studentAdapter.getPosition(intent.getStringExtra(MainActivity.STUDENT_TYPE));
            spinnerStudentType.setSelection(sSelectionPosition);
            int cSelectionPosition = collegeAdapter.getPosition(intent.getStringExtra(MainActivity.COLLEGE_TYPE));
            spinnerCollegeType.setSelection(cSelectionPosition);


            if (spinnerStudentType.getSelectedItem().equals("Graduate")) {
                editTextUserMinor.setText(intent.getStringExtra(MainActivity.USER_MINOR));
            } else {

                double sat = intent.getDoubleExtra(MainActivity.USER_SAT_SCORE, -1);
                editTextUserSATScore.setText(Double.toString(sat));
            }

        }

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        //creates or provides a view model instance
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);

    }

    @SuppressWarnings("SpellCheckingInspection")
    public void createAccount() {
        String fName = editTextUserFName.getText().toString();
        String mName = editTextUserMName.getText().toString();
        String lName = editTextUserLName.getText().toString();
        String address = editTextUserAddress.getText().toString();
        String phone = editTextUserPhone.getText().toString();
        String email = editTextUserEmail.getText().toString().toUpperCase();
        String password = editTextUserPassword.getText().toString();
        String passwordChk = editTextUserPasswordChk.getText().toString();
        String type = spinnerStudentType.getSelectedItem().toString();
        String college = spinnerCollegeType.getSelectedItem().toString();
        String program = type + " " + college;

        //check to see if all fields have a value and are not blank spaces
        if (fName.trim().isEmpty() || mName.trim().isEmpty() || lName.trim().isEmpty() ||
                address.trim().isEmpty() || phone.trim().isEmpty() || email.trim().isEmpty() ||
                password.trim().isEmpty() || passwordChk.trim().isEmpty()) {

            Toast.makeText(this, "Please enter a value in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //checks to see if the password matches the password checker
        if (!password.equals(passwordChk)) {

            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            return;

        }

        if (userId == -1) {

            if (isCurrentUser(email)) {
                Toast.makeText(this, "This email address is already associated with an account.", Toast.LENGTH_SHORT).show();
                return;
            }

            //checks to see if the student is an Undergraduate or not, if so
            //gets the SAT score
            if (spinnerStudentType.getSelectedItem().equals("Undergraduate")) {
                String satScore = editTextUserSATScore.getText().toString();

                //checks to see if the SAT score has a value and if that value is less than
                //or equal to 4.0, if so inserts a new Undergraduate User
                if (!satScore.trim().isEmpty() && Double.parseDouble(satScore) <= 4.0) {

                    UserEntity undergrad = new UserEntity(fName, mName, lName, address, phone, email, password, type, college, program, Double.parseDouble(satScore));
                    userViewModel.insert(undergrad);
                    Toast.makeText(this, "New user created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(this, "Please enter a valid value for you SAT score", Toast.LENGTH_SHORT).show();
                    return;

                }
            }

            //checks to see if the student is an Graduate or not, if so
            //gets the Users college minor
            if (spinnerStudentType.getSelectedItem().equals("Graduate")) {
                String minor = editTextUserMinor.getText().toString();

                //checks to see if the minor textbox has a value, if so inserts a new Graduate User
                if (!minor.trim().isEmpty()) {

                    UserEntity graduate = new UserEntity(fName, mName, lName, address, phone, email, password, type, college, program, minor);
                    userViewModel.insert(graduate);
                    Toast.makeText(this, "New user created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(this, "Please enter a value in the Minor field", Toast.LENGTH_SHORT).show();

                }
            }

        } else {

            //checks to see if the student is an Undergraduate or not, if so
            //gets the SAT score
            if (spinnerStudentType.getSelectedItem().equals("Undergraduate")) {
                String satScore = editTextUserSATScore.getText().toString();

                //checks to see if the SAT score has a value and if that value is less than
                //or equal to 4.0, if so inserts a new Undergraduate User
                if (!satScore.trim().isEmpty() && Double.parseDouble(satScore) <= 4.0) {

                    UserEntity undergrad = new UserEntity(fName, mName, lName, address, phone, email, password, type, college, program, Double.parseDouble(satScore));
                    undergrad.setUser_id(userId);
                    userViewModel.update(undergrad);
                    Toast.makeText(this, "User updated created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(this, "Please enter a valid value for you SAT score", Toast.LENGTH_SHORT).show();
                    return;

                }
            }

            //checks to see if the student is an Graduate or not, if so
            //gets the Users college minor
            if (spinnerStudentType.getSelectedItem().equals("Graduate")) {
                String minor = editTextUserMinor.getText().toString();

                //checks to see if the minor textbox has a value, if so inserts a new Graduate User
                if (!minor.trim().isEmpty()) {

                    UserEntity graduate = new UserEntity(fName, mName, lName, address, phone, email, password, type, college, program, minor);
                    graduate.setUser_id(userId);
                    userViewModel.update(graduate);
                    Toast.makeText(this, "User updated created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(this, "Please enter a value in the Minor field", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    //checks the users email against the emails in the db to see if the provided email
    //has an account already associated with it
    private boolean isCurrentUser(String username) {
        SchedulerDatabase db = SchedulerDatabase.getInstance(getApplicationContext());
        UserEntity currentUser = db.userDao().validateUserByEmail(username);

        return currentUser != null;
    }
}