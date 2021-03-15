package com.example.michaelbettis_term_scheduler.Activities.CourseActivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities.AssessmentListActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Activities.NoteActivities.NoteListActivity;
import com.example.michaelbettis_term_scheduler.Activities.TermActivities.AddNewTermActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Adapters.CourseAdapter;
import com.example.michaelbettis_term_scheduler.Entities.CourseEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.CourseViewModel;

public class CourseListActivity extends AppCompatActivity {

    private CourseViewModel courseViewModel;
    private int termId;
    private String termStart;
    private String termEnd;
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Course List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //assigning intent values
        Intent intent = getIntent();
        termId = intent.getIntExtra(AddNewTermActivity.TERM_ID, -1);
        termStart = intent.getStringExtra(AddNewTermActivity.TERM_START);
        termEnd = intent.getStringExtra(AddNewTermActivity.TERM_END);

        //sets the recycle view
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //connects the recycler view and the adapter which updates the recycler view any time a change
        //happens in the adapter
        adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);
        courseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courses) {
                List<CourseEntity> filteredCourses = new ArrayList<>();
                for (CourseEntity c : courses)
                    if (c.getTerm_id() == termId)
                        filteredCourses.add(c);
                adapter.setCourses(filteredCourses);
                adapter.notifyDataSetChanged();
            }
        });

        //Used to implement swipe to delete courses from course list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                courseViewModel.delete(adapter.getCourseAt(viewHolder.getAdapterPosition()));
                Toast.makeText(CourseListActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new CourseAdapter.onItemClickListener() {
            @Override
            public void onCourseClick(CourseEntity course) {

            }

            @Override
            public void onAssessmentsClick(CourseEntity course) {
                Intent intent = new Intent(CourseListActivity.this, AssessmentListActivity.class);
                intent.putExtra(AddNewCourseActivity.COURSE_ID, course.getCourse_id());
                intent.putExtra(AddNewCourseActivity.COURSE_START, course.getStart_date());
                intent.putExtra(AddNewCourseActivity.COURSE_END, course.getEnd_date());
                startActivity(intent);
            }

            @Override
            public void onNotesClick(CourseEntity course) {
                Intent intent = new Intent(CourseListActivity.this, NoteListActivity.class);
                intent.putExtra(AddNewCourseActivity.COURSE_ID, course.getCourse_id());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseListActivity.this, AddNewCourseActivity.class);
                intent.putExtra(AddNewTermActivity.TERM_ID, termId);
                intent.putExtra(AddNewTermActivity.TERM_START, termStart);
                intent.putExtra(AddNewTermActivity.TERM_END, termEnd);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.course_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_courses);
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
            case R.id.delete_all_courses:
                delete_all_courses();
                return true;
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Intent intent = new Intent(CourseListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Sign out successful!", Toast.LENGTH_SHORT).show();
    }

    private void delete_all_courses() {

        courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courses) {
                for (CourseEntity c : courses) {
                    if (c.getTerm_id() == getIntent().getIntExtra(AddNewTermActivity.TERM_ID, -1)) {

                        courseViewModel.delete(c);
                        Toast.makeText(CourseListActivity.this, "Courses Deleted", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    //returns to main menu
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}