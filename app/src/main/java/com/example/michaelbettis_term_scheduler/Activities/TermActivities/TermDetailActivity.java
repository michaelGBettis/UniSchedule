package com.example.michaelbettis_term_scheduler.Activities.TermActivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.CourseListActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Converters;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.SchedulerDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.Entities.TermEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.TermViewModel;

import java.util.Objects;

public class TermDetailActivity extends AppCompatActivity {

    private int termId;
    private int userId;
    private String name;
    private String start;
    private String end;
    private TermViewModel termViewModel;
    SchedulerDatabase db;
    TermEntity currentTerm;
    int countNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Term Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //creates or provides a view model instance
        termViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TermViewModel.class);

        Button allCourses = findViewById(R.id.all_courses);
        allCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermDetailActivity.this, CourseListActivity.class);
                intent.putExtra(AddNewTermActivity.TERM_ID, termId);
                intent.putExtra(AddNewTermActivity.TERM_START, start);
                intent.putExtra(AddNewTermActivity.TERM_END, end);
                startActivity(intent);

            }
        });

        //initializing views
        TextView textViewTermName = findViewById(R.id.term_name);
        TextView textViewStartDate = findViewById(R.id.term_start_date);
        TextView textViewEndDate = findViewById(R.id.term_end_date);


        //assigning intent values
        Intent intent = getIntent();
        termId = intent.getIntExtra(AddNewTermActivity.TERM_ID, -1);
        userId = intent.getIntExtra(MainActivity.USER_ID, -1);
        name = intent.getStringExtra(AddNewTermActivity.TERM_NAME);
        start = intent.getStringExtra(AddNewTermActivity.TERM_START);
        end = intent.getStringExtra(AddNewTermActivity.TERM_END);
        db = SchedulerDatabase.getInstance(getApplicationContext());
        countNum = db.courseDao().getCourseCount(termId);
        currentTerm = db.termDao().getCurrentTerm(termId, userId);

        //setting view text
        textViewTermName.setText(name);
        textViewStartDate.setText(Converters.sdf(start));
        textViewEndDate.setText(Converters.sdf(end));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.term_detail_menu, menu);
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
            case R.id.delete_term:
                delete_term();
                return true;
            case R.id.edit_term:
                edit_term();
                return true;
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Intent intent = new Intent(TermDetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Sign out successful!", Toast.LENGTH_SHORT).show();
    }

    //sends data to the edit term screen
    private void edit_term() {

        Intent intent = new Intent(TermDetailActivity.this, AddNewTermActivity.class);
        intent.putExtra(AddNewTermActivity.TERM_NAME, name);
        intent.putExtra(AddNewTermActivity.TERM_START, start);
        intent.putExtra(AddNewTermActivity.TERM_END, end);
        intent.putExtra(AddNewTermActivity.TERM_ID, termId);
        intent.putExtra(MainActivity.USER_ID, userId);
        startActivity(intent);

    }

    //deletes the term from the db
    private void delete_term() {

        if (countNum == 0) {

            termViewModel.delete(currentTerm);
            Toast.makeText(this, "Term deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TermDetailActivity.this, TermListActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Can not delete a Term with associated Courses", Toast.LENGTH_SHORT).show();
        }
    }
}


