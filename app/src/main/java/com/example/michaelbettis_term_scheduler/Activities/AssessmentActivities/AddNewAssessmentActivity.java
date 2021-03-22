package com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.AddNewCourseActivity;
import com.example.michaelbettis_term_scheduler.utils.DatePickerFragment;
import com.example.michaelbettis_term_scheduler.utils.MyReceiver;
import com.example.michaelbettis_term_scheduler.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.AssessmentViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;

public class AddNewAssessmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private int userId;
    private int termId;
    private int courseId;
    private int assessId;
    private Button saveAssessBtn;
    private String courseStart;
    private String courseEnd;
    private EditText etAssessName;
    private EditText etAssessDesc;
    private TextView tvAssessDueDate;
    private Spinner spinnerType;
    private CheckBox checkBoxDue;
    private AssessmentViewModel assessViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Assessment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //sets the values of the input data to variables
        etAssessName = findViewById(R.id.assessment_name);
        etAssessDesc = findViewById(R.id.assessment_description);
        tvAssessDueDate = findViewById(R.id.assessment_due_date);
        spinnerType = findViewById(R.id.assessment_type);
        checkBoxDue = findViewById(R.id.due_date_notification);
        saveAssessBtn = findViewById(R.id.save_assessment);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNewAssessmentActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.assessment_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);


        //Gets intent data and checks if it has an id value or not, if it does, it changes the title
        //text to edit assessment, otherwise it stays as edit assessment
        Intent intent = getIntent();
        userId = intent.getIntExtra(Helper.USER_ID, -1);
        termId = getIntent().getIntExtra(Helper.TERM_ID, -1);
        courseId = intent.getIntExtra(Helper.COURSE_ID, -1);
        assessId = getIntent().getIntExtra(Helper.ASSESS_ID, -1);
        courseStart = intent.getStringExtra(Helper.COURSE_START);
        courseEnd = intent.getStringExtra(Helper.COURSE_END);
        if (intent.hasExtra(Helper.ASSESS_ID)) {
            getSupportActionBar().setTitle("Edit Assessment");
            etAssessName.setText(intent.getStringExtra(Helper.ASSESS_NAME));
            etAssessDesc.setText(intent.getStringExtra(Helper.ASSESS_DESC));
            tvAssessDueDate.setText(Helper.sdf(intent.getStringExtra(Helper.ASSESS_DUE_DATE)));

            //gets and sets spinner value
            int selectionPosition = adapter.getPosition(intent.getStringExtra(Helper.ASSESS_TYPE));
            spinnerType.setSelection(selectionPosition);

        }

        saveAssessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveAssess();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //sets the onClickListener for the date textViews
        tvAssessDueDate.setOnClickListener(this);

        //creates or provides a view model instance
        assessViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AssessmentViewModel.class);

    }

    //sets the text view text to the selected date
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        tvAssessDueDate.setText(currentDateString);

    }

    //creates a pop-up date picker when the date text views are clicked
    @Override
    public void onClick(View view) {
        Bundle currentDate = new Bundle();
        try {
            currentDate.putLong("setEndDate", Helper.stringToLong(courseEnd));
            currentDate.putLong("setStartDate", Helper.stringToLong(courseStart));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DialogFragment datePicker = new DatePickerFragment();
        datePicker.setArguments(currentDate);
        datePicker.show(getSupportFragmentManager(), "date picker");

    }

    //method for the save button
    private void saveAssess() throws ParseException {
        String name = etAssessName.getText().toString();
        String description = etAssessDesc.getText().toString();
        String dueDate = tvAssessDueDate.getText().toString();
        String type = spinnerType.getSelectedItem().toString();
        AssessmentEntity assess;

        //check to see if all fields have a value and are not blank spaces
        if (name.trim().isEmpty() || description.trim().isEmpty() || dueDate.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a value in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (assessId == -1) {
            assess = new AssessmentEntity(name, Helper.stringToDate(dueDate), description, type, courseId);
            assessViewModel.insert(assess);
            Toast.makeText(this, "Assessment Added", Toast.LENGTH_SHORT).show();
        } else {
            assess = new AssessmentEntity(name, Helper.stringToDate(dueDate), description, type, courseId);
            assess.setAssessment_id(assessId);
            assessViewModel.update(assess);
            Toast.makeText(this, "Assessment updated", Toast.LENGTH_SHORT).show();

        }

        if (checkBoxDue.isChecked()) {
            Helper.sendNotification(dueDate, "This is a reminder that your Assessment, " + name + " is due today!", 2, AddNewAssessmentActivity.this);
        }

        Helper.goToAssessments(userId, termId, courseId, courseStart, courseEnd, AddNewAssessmentActivity.this);

    }

    //sets the action for the back button and displays a message
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Toast.makeText(this, "Assessment not Added", Toast.LENGTH_SHORT).show();
        return true;
    }
}
