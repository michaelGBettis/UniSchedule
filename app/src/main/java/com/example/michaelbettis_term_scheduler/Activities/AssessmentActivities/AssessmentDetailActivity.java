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

    private int userId;
    private int termId;
    private int courseId;
    private int assessId;
    private String courseStart;
    private String courseEnd;
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
        userId = intent.getIntExtra(Helper.USER_ID, -1);
        termId = intent.getIntExtra(Helper.TERM_END, -1);
        courseId = intent.getIntExtra(Helper.COURSE_ID, -1);
        assessId = intent.getIntExtra(Helper.ASSESS_ID, -1);
        courseStart = intent.getStringExtra(Helper.COURSE_START);
        courseEnd = intent.getStringExtra(Helper.COURSE_END);
        db = SchedulerDatabase.getInstance(getApplicationContext());
        currentAssess = db.assessmentDao().getCurrentAssessments(courseId, assessId);

        //setting view text
        tvAssessName.setText(currentAssess.getAssessment_name());
        tvAssessDesc.setText(currentAssess.getAssessment_info());
        tvAssessDueDate.setText(Helper.sdf(currentAssess.getDue_date().toString()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.assessment_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_assessment:
                Helper.deleteAssessment(AssessmentDetailActivity.this, assessViewModel, currentAssess);
                Helper.goToAssessments(userId, termId, courseId, courseStart, courseEnd, this);
                return true;
            case R.id.edit_assessment:
                Helper.editAssessment(AssessmentDetailActivity.this, currentAssess, courseStart, courseEnd);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
