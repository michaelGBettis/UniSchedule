package com.example.michaelbettis_term_scheduler.Activities.CourseActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.michaelbettis_term_scheduler.utils.DatePickerFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Entities.CourseEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.CourseViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewCourseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private int userId;
    private int termId;
    private int courseId;
    private int viewId;
    private String termStart;
    private String termEnd;
    private String courseStatus;
    private TextInputLayout courseNameTextInput;
    private TextInputLayout courseStartTextInput;
    private TextInputLayout courseEndTextInput;
    private TextInputLayout mentorNameTextInput;
    private TextInputLayout mentorPhoneTextInput;
    private TextInputLayout mentorEmailTextInput;
    private TextInputLayout courseStatusTextInput;
    private TextInputEditText courseStartEditText;
    private TextInputEditText courseEndEditText;
    private AutoCompleteTextView courseStatusDropdown;
    private CheckBox checkBoxStart;
    private CheckBox checkBoxEnd;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        //======================================Hooks=============================================//

        courseNameTextInput = findViewById(R.id.course_name);
        courseStartTextInput = findViewById(R.id.course_start);
        courseEndTextInput = findViewById(R.id.course_end);
        mentorNameTextInput = findViewById(R.id.course_mentor);
        mentorPhoneTextInput = findViewById(R.id.mentor_phone);
        mentorEmailTextInput = findViewById(R.id.mentor_email);
        courseStatusTextInput = findViewById(R.id.course_status);
        courseStatusDropdown = findViewById(R.id.course_status_text);
        courseStartEditText = findViewById(R.id.course_start_text);
        courseEndEditText = findViewById(R.id.course_end_text);
        checkBoxStart = findViewById(R.id.start_notification);
        checkBoxEnd = findViewById(R.id.end_notification);
        Button saveCourseBtn = findViewById(R.id.save_course);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //=====================================Tool Bar===========================================//

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //=======================================Adapters=========================================//

        final ArrayAdapter<String> courseStatusAdapter = new ArrayAdapter<>(AddNewCourseActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.course_status));
        courseStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatusDropdown.setAdapter(courseStatusAdapter);

        //creates or provides a view model instance
        courseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CourseViewModel.class);

        //===============================Setting Intent Values====================================//

        Intent intent = getIntent();
        userId = intent.getIntExtra(Helper.USER_ID, -1);
        termId = intent.getIntExtra(Helper.TERM_ID, -1);
        termStart = intent.getStringExtra(Helper.TERM_START);
        termEnd = intent.getStringExtra(Helper.TERM_END);
        courseId = getIntent().getIntExtra(Helper.COURSE_ID, -1);

        if (intent.hasExtra(Helper.COURSE_ID)) {
            getSupportActionBar().setTitle("Edit Course");
            courseStatus = intent.getStringExtra(Helper.COURSE_STATUS);
            courseNameTextInput.getEditText().setText(intent.getStringExtra(Helper.COURSE_NAME));
            courseStartTextInput.getEditText().setText(Helper.sdf(intent.getStringExtra(Helper.COURSE_START)));
            courseEndTextInput.getEditText().setText(Helper.sdf(intent.getStringExtra(Helper.COURSE_END)));
            mentorNameTextInput.getEditText().setText(intent.getStringExtra(Helper.MENTOR_NAME));
            mentorPhoneTextInput.getEditText().setText(intent.getStringExtra(Helper.MENTOR_PHONE));
            mentorEmailTextInput.getEditText().setText(intent.getStringExtra(Helper.MENTOR_EMAIL));
            courseStatusDropdown.setText(intent.getStringExtra(Helper.COURSE_STATUS));

        }

        //====================================Buttons=============================================//

        courseStartEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    setCourseDateRange(view, Helper.stringToLong(termStart), Helper.stringToLong(termEnd));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        courseEndEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setCourseDateRange(view,
                            Helper.shortStringToLong(courseStartEditText.getText().toString())
                            , Helper.stringToLong(termEnd));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        saveCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveCourse();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //method for the save button
    private void saveCourse() throws ParseException {
        String name = courseNameTextInput.getEditText().getText().toString();
        String startDate = courseStartEditText.getText().toString();
        String endDate = courseEndEditText.getText().toString();
        courseStatus = courseStatusDropdown.getEditableText().toString();
        String mentorName = mentorNameTextInput.getEditText().getText().toString();
        String mentorPhone = mentorPhoneTextInput.getEditText().getText().toString();
        String mentorEmail = mentorEmailTextInput.getEditText().getText().toString();
        CourseEntity course = null;

        //check to see if all fields have a value and are not blank spaces
        if (Helper.isInputEmpty(courseNameTextInput) | Helper.isInputEmpty(courseStartTextInput) | Helper.isInputEmpty(courseEndTextInput) | Helper.isInputEmpty(courseStatusTextInput) |
                Helper.isInputEmpty(mentorNameTextInput) | Helper.isInputEmpty(mentorPhoneTextInput) | Helper.isInputEmpty(mentorEmailTextInput)) {
            return;
        }

        //checks to see if there is a course id or not, if so updates a course if not adds a course
        if (courseId == -1) {
            try {
                course = new CourseEntity(name, Helper.stringToDate(startDate), Helper.stringToDate(endDate), courseStatus, mentorName, mentorPhone, mentorEmail, termId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            courseViewModel.insert(course);
            Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();
        } else {
            try {
                course = new CourseEntity(name, Helper.stringToDate(startDate), Helper.stringToDate(endDate), courseStatus, mentorName, mentorPhone, mentorEmail, termId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            course.setCourse_id(courseId);
            courseViewModel.update(course);
            Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show();

        }

        //checks to see if a notification needs to be sent or not
        if (checkBoxStart.isChecked()) {
            Helper.sendNotification(startDate, "This is a reminder that you start course, " + name + " today!", 0, AddNewCourseActivity.this);
        }

        if (checkBoxEnd.isChecked()) {
            Helper.sendNotification(startDate, "This is a reminder that you finish course, " + name + " today!", 1, AddNewCourseActivity.this);
        }

        Helper.goToCourses(userId, termId, AddNewCourseActivity.this);

    }

    //sets the text view text to the selected date
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        TextView textView = findViewById(viewId);
        textView.setText(currentDateString);

    }

    private void setCourseDateRange(View view, long startOfDateRange, long endOfDateRange) {
        Bundle currentDate = new Bundle();
        DialogFragment datePicker = new DatePickerFragment();

        currentDate.putLong("setStartDate", startOfDateRange);
        currentDate.putLong("setEndDate", endOfDateRange);
        datePicker.setArguments(currentDate);
        datePicker.show(getSupportFragmentManager(), "Date Picker");
        viewId = view.getId();
    }


    //sets the action for the back button and displays a message
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Toast.makeText(this, "Course not Added", Toast.LENGTH_SHORT).show();
        return true;
    }

}