package com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.michaelbettis_term_scheduler.utils.DatePickerFragment;
import com.example.michaelbettis_term_scheduler.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.AssessmentViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewAssessmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private int userId;
    private int termId;
    private int viewId;
    private int courseId;
    private int assessId;
    private String courseEnd;
    private String courseStart;
    private TextInputLayout assessNameTextInput;
    private TextInputLayout assessDescTextInput;
    private TextInputLayout assessDueDateTextInput;
    private TextInputLayout assessTypeTextInput;
    private TextInputEditText assessDueDateEditText;
    private AutoCompleteTextView assessTypeDropdown;
    private CheckBox checkBoxDue;
    private AssessmentViewModel assessViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_assessment);

        //======================================Hooks=============================================//

        assessNameTextInput = findViewById(R.id.assessment_name);
        assessDescTextInput = findViewById(R.id.assessment_description);
        assessDueDateTextInput = findViewById(R.id.assessment_due_date);
        assessTypeTextInput = findViewById(R.id.assessment_type);
        assessTypeDropdown = findViewById(R.id.assessment_type_text);
        assessDueDateEditText = findViewById(R.id.assessment_due_date_text);
        checkBoxDue = findViewById(R.id.due_date_notification);
        Button saveAssessBtn = findViewById(R.id.save_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //=====================================Tool Bar===========================================//

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Assessment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //=====================================Adapters===========================================//

        ArrayAdapter<String> assessTypeAdapter = new ArrayAdapter<>(AddNewAssessmentActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.assessment_type));
        assessTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessTypeDropdown.setAdapter(assessTypeAdapter);

        //creates or provides a view model instance
        assessViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AssessmentViewModel.class);

        //===============================Setting Intent Values====================================//

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
            assessNameTextInput.getEditText().setText(intent.getStringExtra(Helper.ASSESS_NAME));
            assessDescTextInput.getEditText().setText(intent.getStringExtra(Helper.ASSESS_DESC));
            assessDueDateTextInput.getEditText().setText(Helper.sdf(intent.getStringExtra(Helper.ASSESS_DUE_DATE)));
            assessTypeDropdown.setText(intent.getStringExtra(Helper.ASSESS_TYPE));

        }

        //====================================Buttons=============================================//

        assessDueDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    setAssessmentDateRange(view, Helper.stringToLong(courseStart), Helper.stringToLong(courseEnd));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

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

    }

    //sets the text view text to the selected date
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String selectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        TextView textView = findViewById(viewId);
        textView.setText(selectedDate);

    }

    private void setAssessmentDateRange(View view, long startOfDateRange, long endOfDateRange) {
        Bundle currentDate = new Bundle();
        DialogFragment datePicker = new DatePickerFragment();

        currentDate.putLong("setStartDate", startOfDateRange);
        currentDate.putLong("setEndDate", endOfDateRange);
        datePicker.setArguments(currentDate);
        datePicker.show(getSupportFragmentManager(), "Date Picker");
        viewId = view.getId();
    }

    //method for the save button
    private void saveAssess() throws ParseException {
        String name = assessNameTextInput.getEditText().getText().toString();
        String description = assessDescTextInput.getEditText().getText().toString();
        String dueDate = assessDueDateEditText.getText().toString();
        String type = assessTypeDropdown.getEditableText().toString();
        AssessmentEntity assess;

        //check to see if all fields have a value and are not blank spaces
        if (Helper.isInputEmpty(assessNameTextInput) | Helper.isInputEmpty(assessDescTextInput) | Helper.isInputEmpty(assessDueDateTextInput) | Helper.isInputEmpty(assessTypeTextInput)) {
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
