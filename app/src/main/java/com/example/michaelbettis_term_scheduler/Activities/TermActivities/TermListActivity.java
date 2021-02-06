package com.example.michaelbettis_term_scheduler.Activities.TermActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.michaelbettis_term_scheduler.Activities.LoginActivities.SignUpActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Converters;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.Adapters.TermAdapter;

import com.example.michaelbettis_term_scheduler.SchedulerDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Entities.TermEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.TermViewModel;

@TypeConverters(Converters.class)
public class TermListActivity extends AppCompatActivity {

    private TermViewModel termViewModel;
    int userID;
    private TermAdapter adapter;
    SchedulerDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Term List");

        //assigning intent values
        Intent intent = getIntent();
        userID = intent.getIntExtra(MainActivity.USER_ID, -1);

        //sets the recycle view
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
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
                    if (t.getUser_id() == userID)
                        filteredTerms.add(t);
                adapter.setTerms(filteredTerms);
                adapter.notifyDataSetChanged();

            }
        });


        //Used to implement swipe to delete terms from term list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                db = SchedulerDatabase.getInstance(getApplicationContext());
                final int countNum = db.courseDao().getCourseCount(adapter.getTermAt(viewHolder.getAdapterPosition()).getTerm_id());

                if (countNum > 0) {

                    Toast.makeText(TermListActivity.this, "Can not delete a Term with associated Courses", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();

                } else {

                    termViewModel.delete(adapter.getTermAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(TermListActivity.this, "Term Deleted", Toast.LENGTH_SHORT).show();
                }


            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TermAdapter.onItemClickListener() {
            @Override
            public void onItemClick(TermEntity term) {
                Intent intent = new Intent(TermListActivity.this, TermDetailActivity.class);
                intent.putExtra(AddNewTermActivity.TERM_ID, term.getTerm_id());
                intent.putExtra(AddNewTermActivity.TERM_NAME, term.getTerm_name());
                intent.putExtra(AddNewTermActivity.TERM_START, term.getStart_date().toString());
                intent.putExtra(AddNewTermActivity.TERM_END, term.getEnd_date().toString());
                intent.putExtra(MainActivity.USER_ID, userID);
                startActivity(intent);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermListActivity.this, AddNewTermActivity.class);
                intent.putExtra(MainActivity.USER_ID, userID);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.term_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_terms);
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
            case R.id.reports:
                generateReports();
                return true;
            case R.id.user_info:
                editUser();
                return true;
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Intent intent = new Intent(TermListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Sign out successful!", Toast.LENGTH_SHORT).show();

    }

    private void editUser() {
        db = SchedulerDatabase.getInstance(getApplicationContext());
        UserEntity currentUser = db.userDao().getCurrentUser(userID);

        Intent intent = new Intent(TermListActivity.this, SignUpActivity.class);
        intent.putExtra(MainActivity.USER_ID, userID);
        intent.putExtra(MainActivity.USER_F_NAME, currentUser.getUser_fname());
        intent.putExtra(MainActivity.USER_M_NAME, currentUser.getUser_mname());
        intent.putExtra(MainActivity.USER_L_NAME, currentUser.getUser_lname());
        intent.putExtra(MainActivity.USER_ADDRESS, currentUser.getUser_address());
        intent.putExtra(MainActivity.USER_PHONE, currentUser.getUser_phone());
        intent.putExtra(MainActivity.USER_EMAIL, currentUser.getUser_email());
        intent.putExtra(MainActivity.STUDENT_TYPE, currentUser.getUser_type());
        intent.putExtra(MainActivity.COLLEGE_TYPE, currentUser.getCollege_type());
        intent.putExtra(MainActivity.USER_MINOR, currentUser.getMinor());
        intent.putExtra(MainActivity.USER_SAT_SCORE, currentUser.getSat_score());
        startActivity(intent);
    }

    //returns to main menu
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void generateReports() {
        db = SchedulerDatabase.getInstance(getApplicationContext());
        Cursor result = db.userDao().getReport(userID);
        StringBuilder buffer = new StringBuilder();
        Date today = new Date();

        while (result.moveToNext()) {
            buffer.append("Term   " + "Course   " + "Assessment   \n");
            buffer.append(result.getString(2)).append(" ").append(result.getString(3)).append(" ").append(result.getString(4)).append("\n");

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        })
                .setCancelable(true)
                .setTitle("Student Report for: " + Converters.formatter(today))
                .setMessage(buffer.toString())
                .show();

    }
}