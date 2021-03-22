package com.example.michaelbettis_term_scheduler.Activities.TermActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;

import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.CourseListActivity;
import com.example.michaelbettis_term_scheduler.Activities.LoginActivities.SignUpActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.utils.Converters;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.Adapters.TermAdapter;

import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import androidx.room.TypeConverters;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.TermEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.TermViewModel;
import com.google.android.material.navigation.NavigationView;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

@TypeConverters(Converters.class)
public class TermListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int userId;
    private TermViewModel termViewModel;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TermAdapter adapter;
    SchedulerDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);


        //======================================Hooks=============================================//

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        db = SchedulerDatabase.getInstance(getApplicationContext());

        //======================================Tool Bar==========================================//

        setSupportActionBar(toolbar);

        //==============================Navigation Drawer Menu====================================//

        //Hides menu items
        Menu menu =  navigationView.getMenu();
        menu.findItem(R.id.nav_courses).setVisible(false);
        menu.findItem(R.id.nav_assessments).setVisible(false);
        menu.findItem(R.id.nav_notes).setVisible(false);

        //Toggles the navigation drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_terms);

        //===============================Setting Intent Values====================================//
        //assigning intent values
        Intent intent = getIntent();
        userId = intent.getIntExtra(Helper.USER_ID, -1);

        //==================================Recycler View=========================================//

        //sets the recycle view
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //connects the recycler view and the adapter which updates the recycler view any time a change
        //happens in the adapter
        adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);
        termViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> terms) {
                List<TermEntity> filteredTerms = new ArrayList<>();
                for (TermEntity t : terms)
                    if (t.getUser_id() == userId)
                        filteredTerms.add(t);
                adapter.setTerms(filteredTerms);
                adapter.notifyDataSetChanged();

            }
        });


        //Used to implement swipe actions for the term list items
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                TermEntity term = adapter.getTermAt(position);

                switch (direction) {
                    case ItemTouchHelper.LEFT:

                        editTerm(term);
                        adapter.notifyDataSetChanged();

                        break;
                    case ItemTouchHelper.RIGHT:

                        deleteTerm(position);

                        break;

                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(TermListActivity.this, R.color.colorSecondary))
                        .addSwipeLeftActionIcon(R.drawable.ic_edit)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(TermListActivity.this, R.color.colorAccentSecond))
                        .addSwipeRightActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TermAdapter.onItemClickListener() {
            @Override
            public void onItemClick(TermEntity term) {

                Helper.goToCourses(userId, term.getTerm_id(), TermListActivity.this);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermListActivity.this, AddNewTermActivity.class);
                intent.putExtra(Helper.USER_ID, userId);
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
        switch (item.getItemId()){
            case R.id.nav_reports:
                Helper.generateReports(db, userId, TermListActivity.this);
                break;
            case R.id.nav_account_info:
                Helper.editUser(db, userId, TermListActivity.this);
                break;
            case R.id.nav_sign_out:
                Helper.signOut(TermListActivity.this);
                break;
            case R.id.nav_help:
                Toast.makeText(TermListActivity.this, "Help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback:
                Toast.makeText(TermListActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
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

        Helper.signOut(TermListActivity.this);

    }

    private void deleteTerm(int position) {
        final int countNum = db.courseDao().getCourseCount(adapter.getTermAt(position).getTerm_id());

        if (countNum > 0) {

            Toast.makeText(TermListActivity.this, "Can not delete a Term with associated Courses", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            return;
        }

        termViewModel.delete(adapter.getTermAt(position));
        adapter.notifyItemRemoved(position);
        Toast.makeText(TermListActivity.this, "Term Deleted", Toast.LENGTH_SHORT).show();
    }

    private void editTerm(TermEntity term) {
        Intent intent = new Intent(TermListActivity.this, AddNewTermActivity.class);
        intent.putExtra(Helper.TERM_ID, term.getTerm_id());
        intent.putExtra(Helper.TERM_NAME, term.getTerm_name());
        intent.putExtra(Helper.TERM_START, term.getStart_date().toString());
        intent.putExtra(Helper.TERM_END, term.getEnd_date().toString());
        intent.putExtra(Helper.USER_ID, userId);
        startActivity(intent);
    }

}

