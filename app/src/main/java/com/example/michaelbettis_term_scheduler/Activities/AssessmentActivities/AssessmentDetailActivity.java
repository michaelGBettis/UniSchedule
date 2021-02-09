package com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;


import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.AddNewCourseActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Activities.NoteActivities.NoteListActivity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;

import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.AssessmentViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;

import java.util.Objects;

public class AssessmentDetailActivity extends AppCompatActivity {

    private int courseId;
    private int assessId;
    private String courseStart;
    private String courseEnd;
    private String name;
    private String description;
    private String dueDate;
    private String type;
    private AssessmentViewModel assessViewModel;
    SchedulerDatabase db;
    AssessmentEntity currentAssess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Assessment Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //creates or provides a view model instance
        assessViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AssessmentViewModel.class);

        //initializing views
        TextView tvAssessName = findViewById(R.id.assessment_name);
        TextView tvAssessDesc = findViewById(R.id.assessment_description);
        TextView tvAssessDueDate = findViewById(R.id.assessment_due_date);

        //assigning intent values
        Intent intent = getIntent();
        name = intent.getStringExtra(AddNewAssessmentActivity.ASSESS_NAME);
        description = intent.getStringExtra(AddNewAssessmentActivity.ASSESS_DESC);
        dueDate = intent.getStringExtra(AddNewAssessmentActivity.ASSESS_DUE_DATE);
        type = intent.getStringExtra(AddNewAssessmentActivity.ASSESS_TYPE);
        assessId = intent.getIntExtra(AddNewAssessmentActivity.ASSESS_ID, -1);
        courseId = intent.getIntExtra(AddNewCourseActivity.COURSE_ID, -1);
        courseStart = intent.getStringExtra(AddNewCourseActivity.COURSE_START);
        courseEnd = intent.getStringExtra(AddNewCourseActivity.COURSE_END);
        db = SchedulerDatabase.getInstance(getApplicationContext());
        currentAssess = db.assessmentDao().getCurrentAssessments(courseId, assessId);

        //setting view text
        tvAssessName.setText(name);
        tvAssessDesc.setText(description);
        tvAssessDueDate.setText(Helper.sdf(dueDate));

        Button noteButton = findViewById(R.id.all_notes);
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentDetailActivity.this, NoteListActivity.class);
                intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.assessment_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_assessment:
                deleteAssessment();
                return true;
            case R.id.edit_assessment:
                editAssessment();
                return true;
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Intent intent = new Intent(AssessmentDetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Sign out successful!", Toast.LENGTH_SHORT).show();
    }

    private void deleteAssessment() {
        assessViewModel.delete(currentAssess);
        Toast.makeText(this, "Assessment deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AssessmentDetailActivity.this, AssessmentListActivity.class);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_ID, assessId);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_NAME, name);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_DESC, description);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_DUE_DATE, dueDate);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_TYPE, type);
        intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
        startActivity(intent);

    }

    private void editAssessment() {
        Intent intent = new Intent(AssessmentDetailActivity.this, AddNewAssessmentActivity.class);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_ID, assessId);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_NAME, name);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_DESC, description);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_DUE_DATE, dueDate);
        intent.putExtra(AddNewAssessmentActivity.ASSESS_TYPE, type);
        intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
        intent.putExtra(AddNewCourseActivity.COURSE_START, courseStart);
        intent.putExtra(AddNewCourseActivity.COURSE_END, courseEnd);
        startActivity(intent);
    }
}
