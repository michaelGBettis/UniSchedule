package com.example.michaelbettis_term_scheduler.Activities.CourseActivities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.michaelbettis_term_scheduler.Activities.TermActivities.AddNewTermActivity;
import com.example.michaelbettis_term_scheduler.utils.DatePickerFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
    public static final String COURSE_ID = "com.example.michaelbettis_term_scheduler.Activities.COURSE_ID";
    public static final String COURSE_NAME = "com.example.michaelbettis_term_scheduler.Activities.COURSE_NAME";
    public static final String COURSE_START = "com.example.michaelbettis_term_scheduler.Activities.COURSE_START";
    public static final String COURSE_END = "com.example.michaelbettis_term_scheduler.Activities.COURSE_END";
    public static final String COURSE_STATUS = "com.example.michaelbettis_term_scheduler.Activities.COURSE_STATUS";
    public static final String MENTOR_NAME = "com.example.michaelbettis_term_scheduler.Activities.MENTOR_NAME";
    public static final String MENTOR_PHONE = "com.example.michaelbettis_term_scheduler.Activities.MENTOR_PHONE";
    public static final String MENTOR_EMAIL = "com.example.michaelbettis_term_scheduler.Activities.MENTOR_EMAIL";

    private int courseId;
    private int termId;
    private String termStart;
    private String termEnd;
    private int viewId;
    private String courseStatus;
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

        //sets the values for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNewCourseActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.course_status));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseStatus.setAdapter(adapter);


        //Gets intent data and checks if it has an id value or not, if it does, it changes the title
        //text to edit course, otherwise it stays as edit course
        Intent intent = getIntent();
        termId = intent.getIntExtra(AddNewTermActivity.TERM_ID, -1);
        termStart = intent.getStringExtra(AddNewTermActivity.TERM_START);
        termEnd = intent.getStringExtra(AddNewTermActivity.TERM_END);
        courseId = getIntent().getIntExtra(COURSE_ID, -1);
        if (intent.hasExtra(COURSE_ID)) {
            getSupportActionBar().setTitle("Edit Course");
            courseStatus = intent.getStringExtra(AddNewCourseActivity.COURSE_STATUS);
            editTextCourseName.setText(intent.getStringExtra(COURSE_NAME));
            textViewCourseStart.setText(Helper.sdf(intent.getStringExtra(COURSE_START)));
            textViewCourseEnd.setText(Helper.sdf(intent.getStringExtra(COURSE_END)));
            editTextMentorName.setText(intent.getStringExtra(MENTOR_NAME));
            editTextMentorPhone.setText(intent.getStringExtra(MENTOR_PHONE));
            editTextMentorEmail.setText(intent.getStringExtra(MENTOR_EMAIL));

            //gets and sets spinner value
            int selectionPosition = adapter.getPosition(intent.getStringExtra(COURSE_STATUS));
            spinnerCourseStatus.setSelection(selectionPosition);

        }

        //sets the onClickListener for the date textViews and check boxes
        textViewCourseStart.setOnClickListener(this);
        textViewCourseEnd.setOnClickListener(this);

        //creates or provides a view model instance
        courseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CourseViewModel.class);

    }

    //sets the menu for the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_course_menu, menu);
        return true;
    }

    //sets the methods for the menu buttons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_course) {
            try {
                saveCourse();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        //check to see if all fields have a value and are not blank spaces
        if (name.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() ||
                mentorName.trim().isEmpty() || mentorPhone.trim().isEmpty() || mentorEmail.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a value in all fields", Toast.LENGTH_SHORT).show();
        } else {

            //checks to see if there is a course id or not, if so updates a course if not adds a course
            CourseEntity course = null;
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
                Intent intent = new Intent(AddNewCourseActivity.this, MyReceiver.class);
                intent.putExtra("Notification", "This is a reminder that you start course, " + name + ", today!");
                PendingIntent sender = PendingIntent.getBroadcast(AddNewCourseActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, Helper.stringToDate(startDate).getTime(), sender);

            }
            if (checkBoxEnd.isChecked()) {
                Intent intent = new Intent(AddNewCourseActivity.this, MyReceiver.class);
                intent.putExtra("Notification", "This is a reminder that you finish course, " + name + ", today!");
                PendingIntent sender = PendingIntent.getBroadcast(AddNewCourseActivity.this, 1, intent, 0);
                AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, Helper.stringToDate(startDate).getTime(), sender);
            }

            Intent intent = new Intent(AddNewCourseActivity.this, CourseListActivity.class);
            intent.putExtra(AddNewTermActivity.TERM_ID, termId);
            intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
            intent.putExtra(AddNewCourseActivity.COURSE_NAME, name);
            intent.putExtra(AddNewCourseActivity.COURSE_START, startDate);
            intent.putExtra(AddNewCourseActivity.COURSE_END, endDate);
            intent.putExtra(AddNewCourseActivity.COURSE_STATUS, courseStatus);
            intent.putExtra(AddNewCourseActivity.MENTOR_NAME, mentorName);
            intent.putExtra(AddNewCourseActivity.MENTOR_PHONE, mentorPhone);
            intent.putExtra(AddNewCourseActivity.MENTOR_EMAIL, mentorEmail);
            startActivity(intent);

        }
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