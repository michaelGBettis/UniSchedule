package com.example.michaelbettis_term_scheduler.Activities.NoteActivities;

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
import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.AddNewCourseActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Adapters.NoteAdapter;
import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.NoteViewModel;
import com.google.android.material.navigation.NavigationView;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class NoteListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int userId;
    private int termId;
    private int courseId;
    private Toolbar toolbar;
    private NoteAdapter adapter;
    private DrawerLayout drawer;
    private NoteViewModel noteViewModel;
    private NavigationView navigationView;
    SchedulerDatabase db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        db = SchedulerDatabase.getInstance(getApplicationContext());

        //======================================Hooks=============================================//

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        //======================================Tool Bar==========================================//

        setSupportActionBar(toolbar);

        //==============================Navigation Drawer Menu====================================//

        //Toggles the navigation drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_notes);

        //===============================Setting Intent Values====================================//


        //assigning intent values
        Intent intent = getIntent();
        userId = intent.getIntExtra(Helper.USER_ID, -1);
        termId = intent.getIntExtra(Helper.TERM_END, -1);
        courseId = intent.getIntExtra(Helper.COURSE_ID, -1);

        //==================================Recycler View=========================================//

        //sets the recycle view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //connects the recycler view and the adapter which updates the recycler view any time a change
        //happens in the adapter
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> notes) {
                List<NoteEntity> filteredNotes = new ArrayList<>();
                for (NoteEntity n : notes)
                    if (n.getCourse_id() == courseId)
                        filteredNotes.add(n);
                adapter.setNotes(filteredNotes);
                adapter.notifyDataSetChanged();

            }
        });

        //Used to implement swipe to delete notes from note list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                NoteEntity note = adapter.getNoteAt(position);

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        Helper.editNote(NoteListActivity.this, note);
                        adapter.notifyDataSetChanged();
                        break;
                    case ItemTouchHelper.RIGHT:
                        Helper.deleteNote(NoteListActivity.this, noteViewModel, note);
                        adapter.notifyItemRemoved(position);

                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(NoteListActivity.this, R.color.colorSecondary))
                        .addSwipeLeftActionIcon(R.drawable.ic_edit)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(NoteListActivity.this, R.color.colorAccentSecond))
                        .addSwipeRightActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NoteEntity note) {
                Intent intent = new Intent(NoteListActivity.this, NoteDetailActivity.class);
                intent.putExtra(Helper.USER_ID, userId);
                intent.putExtra(Helper.TERM_END, termId);
                intent.putExtra(Helper.COURSE_ID, courseId);
                intent.putExtra(Helper.NOTE_ID, note.getNote_id());
                startActivity(intent);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this, AddNewNoteActivity.class);
                intent.putExtra(Helper.USER_ID, userId);
                intent.putExtra(Helper.TERM_END, termId);
                intent.putExtra(Helper.COURSE_ID, courseId);
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
                Helper.goToTerms(userId, NoteListActivity.this);
                break;
            case R.id.nav_courses:
                Helper.goToCourses(userId, termId, NoteListActivity.this);
                break;
            case R.id.nav_account_info:
                Helper.editUser(db, userId, NoteListActivity.this);
                break;
            case R.id.nav_reports:
                Helper.generateReports(db, userId, NoteListActivity.this);
                break;
            case R.id.nav_sign_out:
                Helper.signOut(NoteListActivity.this);
                break;
            case R.id.nav_help:
                Toast.makeText(NoteListActivity.this, "Help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback:
                Toast.makeText(NoteListActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
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
}
