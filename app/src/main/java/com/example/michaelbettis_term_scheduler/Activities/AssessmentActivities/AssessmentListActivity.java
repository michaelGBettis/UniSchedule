package com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.AddNewCourseActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Adapters.AssessmentAdapter;
import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.AssessmentViewModel;


public class AssessmentListActivity extends AppCompatActivity {
    private AssessmentViewModel assessViewModel;
    SchedulerDatabase db;
    private AssessmentAdapter adapter;
    private int courseId;
    private String courseStart;
    private String courseEnd;
    private int assessmentCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assessment_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Assessment List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //sets the recycle view
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //assigning intent values
        Intent intent = getIntent();
        courseId = intent.getIntExtra(AddNewCourseActivity.COURSE_ID, -1);
        courseStart = intent.getStringExtra(AddNewCourseActivity.COURSE_START);
        courseEnd = intent.getStringExtra(AddNewCourseActivity.COURSE_END);
        db = SchedulerDatabase.getInstance(getApplicationContext());

        //connects the recycler view and the adapter which updates the recycler view any time a change
        //happens in the adapter
        adapter = new AssessmentAdapter();
        recyclerView.setAdapter(adapter);
        assessViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AssessmentViewModel.class);
        assessViewModel.getAllAssessments().observe(this, new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessments) {
                List<AssessmentEntity> filteredAssessments = new ArrayList<>();
                for (AssessmentEntity a : assessments)
                    if (a.getCourse_id() == courseId)
                        filteredAssessments.add(a);
                adapter.setAssessment(filteredAssessments);
                adapter.notifyDataSetChanged();

            }
        });

        //Used to implement swipe to delete Assessments from Assessment list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                assessViewModel.delete(adapter.getAssessmentAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AssessmentListActivity.this, "Assessment Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new AssessmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(AssessmentEntity assessment) {
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentDetailActivity.class);
                intent.putExtra(AddNewAssessmentActivity.ASSESS_NAME, assessment.getAssessment_name());
                intent.putExtra(AddNewAssessmentActivity.ASSESS_DESC, assessment.getAssessment_info());
                intent.putExtra(AddNewAssessmentActivity.ASSESS_DUE_DATE, assessment.getDue_date().toString());
                intent.putExtra(AddNewAssessmentActivity.ASSESS_ID, assessment.getAssessment_id());
                intent.putExtra(AddNewAssessmentActivity.ASSESS_TYPE, assessment.getAssessment_type());
                intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
                intent.putExtra(AddNewCourseActivity.COURSE_START, courseStart);
                intent.putExtra(AddNewCourseActivity.COURSE_END, courseEnd);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assessmentCount = db.assessmentDao().getAssessmentCount(courseId);
                if (assessmentCount <= 4) {
                    Intent intent = new Intent(AssessmentListActivity.this, AddNewAssessmentActivity.class);
                    intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
                    intent.putExtra(AddNewCourseActivity.COURSE_START, courseStart);
                    intent.putExtra(AddNewCourseActivity.COURSE_END, courseEnd);
                    startActivity(intent);
                } else
                    Toast.makeText(AssessmentListActivity.this, "Error: The max number of Assessments per Course is 5", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.assessment_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_assessments);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_assessments:
                deleteAllAssessments();
                return true;
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Intent intent = new Intent(AssessmentListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Sign out successful!", Toast.LENGTH_SHORT).show();
    }

    private void deleteAllAssessments() {

        assessViewModel.getAllAssessments().observe(this, new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessments) {
                for (AssessmentEntity a : assessments) {
                    if (a.getCourse_id() == courseId) {

                        assessViewModel.delete(a);
                        Toast.makeText(AssessmentListActivity.this, "Assessments Deleted", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
