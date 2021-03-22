package com.example.michaelbettis_term_scheduler.Activities.CourseActivities;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities.AssessmentListActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Activities.NoteActivities.NoteListActivity;
import com.example.michaelbettis_term_scheduler.Activities.TermActivities.AddNewTermActivity;
import com.example.michaelbettis_term_scheduler.Activities.TermActivities.TermListActivity;
import com.example.michaelbettis_term_scheduler.Adapters.CourseAdapter;
import com.example.michaelbettis_term_scheduler.Entities.CourseEntity;
import com.example.michaelbettis_term_scheduler.Entities.TermEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.ViewModel.CourseViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CourseListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int termId;
    private int userId;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CourseAdapter adapter;
    private CourseViewModel courseViewModel;
    TermEntity selectedTerm;
    SchedulerDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        //======================================Hooks=============================================//

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        db = SchedulerDatabase.getInstance(getApplicationContext());


        //======================================Tool Bar==========================================//

        setSupportActionBar(toolbar);

        //==============================Navigation Drawer Menu====================================//

        //Hides menu items
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_assessments).setVisible(false);
        menu.findItem(R.id.nav_notes).setVisible(false);

        //Toggles the navigation drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_courses);

        //===============================Setting Intent Values====================================//

        //assigning intent values
        Intent intent = getIntent();
        termId = intent.getIntExtra(Helper.TERM_ID, -1);
        userId = intent.getIntExtra(Helper.USER_ID, -1);
        selectedTerm = db.termDao().getCurrentTerm(termId, userId);

        //==================================Recycler View=========================================//

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

                int position = viewHolder.getAdapterPosition();
                CourseEntity course = adapter.getCourseAt(position);

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        editCourse(course);
                        adapter.notifyDataSetChanged();
                        break;
                    case ItemTouchHelper.RIGHT:
                        courseViewModel.delete(course);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(CourseListActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(CourseListActivity.this, R.color.colorSecondary))
                        .addSwipeLeftActionIcon(R.drawable.ic_edit)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(CourseListActivity.this, R.color.colorAccentSecond))
                        .addSwipeRightActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new CourseAdapter.onItemClickListener() {
            @Override
            public void onCourseClick(CourseEntity course) {

            }

            @Override
            public void onAssessmentsClick(CourseEntity course) {
                Helper.goToAssessments(userId, termId, course.getCourse_id(), course.getStart_date().toString(), course.getEnd_date().toString(), CourseListActivity.this);
            }

            @Override
            public void onNotesClick(CourseEntity course) {
                Helper.goToNotes(userId, termId, course.getCourse_id(), CourseListActivity.this);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseListActivity.this, AddNewCourseActivity.class);
                intent.putExtra(Helper.TERM_ID, termId);
                intent.putExtra(Helper.TERM_START, selectedTerm.getStart_date().toString());
                intent.putExtra(Helper.TERM_END, selectedTerm.getEnd_date().toString());
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_bar_manu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_items);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_terms:
                Helper.goToTerms(userId, CourseListActivity.this);
                break;
            case R.id.nav_account_info:
                Helper.editUser(db, userId, CourseListActivity.this);
                break;
            case R.id.nav_reports:
                Helper.generateReports(db, userId, CourseListActivity.this);
                break;
            case R.id.nav_sign_out:
                Helper.signOut(CourseListActivity.this);
                break;
            case R.id.nav_help:
                Toast.makeText(CourseListActivity.this, "Help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback:
                Toast.makeText(CourseListActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void editCourse(CourseEntity course) {

        Intent intent = new Intent(CourseListActivity.this, AddNewCourseActivity.class);
        intent.putExtra(Helper.USER_ID, userId);
        intent.putExtra(Helper.TERM_ID, termId);
        intent.putExtra(Helper.TERM_START, selectedTerm.getStart_date().toString());
        intent.putExtra(Helper.TERM_END, selectedTerm.getEnd_date().toString());
        intent.putExtra(Helper.COURSE_ID, course.getCourse_id());
        intent.putExtra(Helper.COURSE_NAME, course.getCourse_name());
        intent.putExtra(Helper.COURSE_START, course.getStart_date().toString());
        intent.putExtra(Helper.COURSE_END, course.getEnd_date().toString());
        intent.putExtra(Helper.COURSE_STATUS, course.getCourse_status());
        intent.putExtra(Helper.MENTOR_NAME, course.getCourse_mentor());
        intent.putExtra(Helper.MENTOR_PHONE, course.getMentor_phone());
        intent.putExtra(Helper.MENTOR_EMAIL, course.getMentor_email());
        startActivity(intent);
    }

}