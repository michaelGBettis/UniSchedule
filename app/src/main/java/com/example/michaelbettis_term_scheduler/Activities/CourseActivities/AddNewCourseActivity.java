package com.example.michaelbettis_term_scheduler.Activities.CourseActivities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.michaelbettis_term_scheduler.utils.DatePickerFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.utils.MyReceiver;
import com.example.michaelbettis_term_scheduler.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Entities.CourseEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.CourseViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;

public class AddNewCourseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private int userId;
    private int termId;
    private int courseId;
    private int viewId;
    private String termStart;
    private String termEnd;
    private String courseStatus;
    private Button saveCourseBtn;
    private EditText editTextCourseName;
    private TextView textViewCourseStart;
    private TextView textViewCourseEnd;
    private EditText editTextMentorName;
    private EditText editTextMentorPhone;
    private EditText editTextMentorEmail;
    private CheckBox checkBoxStart;
    private CheckBox checkBoxEnd;
    private Spinner spinnerCourseStatus;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //sets the values of the input data to variables
        editTextCourseName = findViewById(R.id.course_name);
        textViewCourseStart = findViewById(R.id.course_start);
        textViewCourseEnd = findViewById(R.id.course_end);
        editTextMentorName = findViewById(R.id.course_mentor);
        editTextMentorPhone = findViewById(R.id.mentor_phone);
        editTextMentorEmail = findViewById(R.id.mentor_email);
        checkBoxStart = findViewById(R.id.start_notification);
        checkBoxEnd = findViewById(R.id.end_notification);
        spinnerCourseStatus = findViewById(R.id.course_status);
        saveCourseBtn = findViewById(R.id.save_course);

        //sets the values for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNewCourseActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.course_status));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseStatus.setAdapter(adapter);


        //Gets intent data and checks if it has an id value or not, if it does, it changes the title
        //text to edit course, otherwise it stays as edit course
        Intent intent = getIntent();
        userId = intent.getIntExtra(Helper.USER_ID, -1);
        termId = intent.getIntExtra(Helper.TERM_ID, -1);
        termStart = intent.getStringExtra(Helper.TERM_START);
        termEnd = intent.getStringExtra(Helper.TERM_END);
        courseId = getIntent().getIntExtra(Helper.COURSE_ID, -1);
        if (intent.hasExtra(Helper.COURSE_ID)) {
            getSupportActionBar().setTitle("Edit Course");
            courseStatus = intent.getStringExtra(Helper.COURSE_STATUS);
            editTextCourseName.setText(intent.getStringExtra(Helper.COURSE_NAME));
            textViewCourseStart.setText(Helper.sdf(intent.getStringExtra(Helper.COURSE_START)));
            textViewCourseEnd.setText(Helper.sdf(intent.getStringExtra(Helper.COURSE_END)));
            editTextMentorName.setText(intent.getStringExtra(Helper.MENTOR_NAME));
            editTextMentorPhone.setText(intent.getStringExtra(Helper.MENTOR_PHONE));
            editTextMentorEmail.setText(intent.getStringExtra(Helper.MENTOR_EMAIL));

            //gets and sets spinner value
            int selectionPosition = adapter.getPosition(intent.getStringExtra(Helper.COURSE_STATUS));
            spinnerCourseStatus.setSelection(selectionPosition);

        }

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

        //sets the onClickListener for the date textViews and check boxes
        textViewCourseStart.setOnClickListener(this);
        textViewCourseEnd.setOnClickListener(this);

        //creates or provides a view model instance
        courseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CourseViewModel.class);

    }

    //method for the save button
    private void saveCourse() throws ParseException {
        String name = editTextCourseName.getText().toString();
        String startDate = textViewCourseStart.getText().toString();
        String endDate = textViewCourseEnd.getText().toString();
        courseStatus = spinnerCourseStatus.getSelectedItem().toString();
        String mentorName = editTextMentorName.getText().toString();
        String mentorPhone = editTextMentorPhone.getText().toString();
        String mentorEmail = editTextMentorEmail.getText().toString();
        CourseEntity course = null;

        //check to see if all fields have a value and are not blank spaces
        if (name.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() ||
                mentorName.trim().isEmpty() || mentorPhone.trim().isEmpty() || mentorEmail.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a value in all fields", Toast.LENGTH_SHORT).show();
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

    //sets the action for the back button and displays a message
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Toast.makeText(this, "Course not Added", Toast.LENGTH_SHORT).show();
        return true;
    }

    //creates a pop-up date picker when the date text views are clicked
    @Override
    public void onClick(View view) {
        Bundle currentDate = new Bundle();
        DialogFragment datePicker = new DatePickerFragment();


        switch (view.getId()) {
            case R.id.course_start:
                try {
                    currentDate.putLong("setEndDate", Helper.stringToLong(termEnd));
                    currentDate.putLong("setStartDate", Helper.stringToLong(termStart));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePicker.setArguments(currentDate);
                datePicker.show(getSupportFragmentManager(), "date picker");
                viewId = view.getId();
                break;
            case R.id.course_end:
                try {
                    currentDate.putLong("setEndDate", Helper.stringToLong(termEnd));
                    currentDate.putLong("setStartDate", Helper.shortStringToLong(textViewCourseStart.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePicker.setArguments(currentDate);
                datePicker.show(getSupportFragmentManager(), "date picker");
                viewId = view.getId();
                break;
        }

    }
}