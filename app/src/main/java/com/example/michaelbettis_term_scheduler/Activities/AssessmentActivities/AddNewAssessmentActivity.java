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
import com.example.michaelbettis_term_scheduler.Converters;
import com.example.michaelbettis_term_scheduler.DatePickerFragment;
import com.example.michaelbettis_term_scheduler.MyReceiver;
import com.example.michaelbettis_term_scheduler.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.AssessmentViewModel;

public class AddNewAssessmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    public static final String ASSESS_ID = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_ID";
    public static final String ASSESS_NAME = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_NAME";
    public static final String ASSESS_DESC = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_DESC";
    public static final String ASSESS_DUE_DATE = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_DUE_DATE";
    public static final String ASSESS_TYPE = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_TYPE";

    private int courseId;
    private String courseStart;
    private String courseEnd;
    private int assessId;
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNewAssessmentActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.assessment_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);


        //Gets intent data and checks if it has an id value or not, if it does, it changes the title
        //text to edit assessment, otherwise it stays as edit assessment
        Intent intent = getIntent();
        courseId = intent.getIntExtra(AddNewCourseActivity.COURSE_ID, -1);
        courseStart = intent.getStringExtra(AddNewCourseActivity.COURSE_START);
        courseEnd = intent.getStringExtra(AddNewCourseActivity.COURSE_END);
        assessId = getIntent().getIntExtra(ASSESS_ID, -1);
        if (intent.hasExtra(ASSESS_ID)) {
            getSupportActionBar().setTitle("Edit Assessment");
            etAssessName.setText(intent.getStringExtra(ASSESS_NAME));
            etAssessDesc.setText(intent.getStringExtra(ASSESS_DESC));
            tvAssessDueDate.setText(Converters.sdf(intent.getStringExtra(ASSESS_DUE_DATE)));

            //gets and sets spinner value
            int selectionPosition = adapter.getPosition(intent.getStringExtra(ASSESS_TYPE));
            spinnerType.setSelection(selectionPosition);

        }

        //sets the onClickListener for the date textViews
        tvAssessDueDate.setOnClickListener(this);

        //creates or provides a view model instance
        assessViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AssessmentViewModel.class);

    }

    //sets the menu for the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_assessment_menu, menu);
        return true;
    }

    //sets the methods for the menu buttons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_assessment) {
            try {
                saveAssess();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            currentDate.putLong("setEndDate", Converters.stringToLong(courseEnd));
            currentDate.putLong("setStartDate", Converters.stringToLong(courseStart));
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

        //check to see if all fields have a value and are not blank spaces
        if (name.trim().isEmpty() || description.trim().isEmpty() || dueDate.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a value in all fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            AssessmentEntity assess;
            if (assessId == -1) {
                assess = new AssessmentEntity(name, Converters.stringToDate(dueDate), description, type, courseId);
                assessViewModel.insert(assess);
                Toast.makeText(this, "Assessment Added", Toast.LENGTH_SHORT).show();
            } else {
                assess = new AssessmentEntity(name, Converters.stringToDate(dueDate), description, type, courseId);
                assess.setAssessment_id(assessId);
                assessViewModel.update(assess);
                Toast.makeText(this, "Assessment updated", Toast.LENGTH_SHORT).show();

            }
        }

        if (checkBoxDue.isChecked()) {
            Intent intent = new Intent(AddNewAssessmentActivity.this, MyReceiver.class);
            intent.putExtra("Notification", "This is a reminder that Assessment, " + name + ", is due today!");
            PendingIntent sender = PendingIntent.getBroadcast(AddNewAssessmentActivity.this, 2, intent, 0);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, Converters.stringToDate(dueDate).getTime(), sender);
        }

        Intent intent = new Intent(AddNewAssessmentActivity.this, AssessmentListActivity.class);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_NAME, name);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_DESC, description);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_DUE_DATE, dueDate);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_ID, assessId);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_TYPE, type);
        intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
        startActivity(intent);

    }

    //sets the action for the back button and displays a message
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Toast.makeText(this, "Assessment not Added", Toast.LENGTH_SHORT).show();
        return true;
    }
}
