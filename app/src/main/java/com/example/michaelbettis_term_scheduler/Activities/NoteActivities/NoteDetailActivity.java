package com.example.michaelbettis_term_scheduler.Activities.NoteActivities;

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

import com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities.AssessmentListActivity;
import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.AddNewCourseActivity;

import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.R;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;

import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.NoteViewModel;

import java.util.Objects;

public class NoteDetailActivity extends AppCompatActivity {

    private int courseId;
    private int noteId;
    private String name;
    private String description;
    private NoteViewModel noteViewModel;
    SchedulerDatabase db;
    NoteEntity currentNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Note Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //creates or provides a view model instance
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);

        //initializing views
        TextView textViewNoteName = findViewById(R.id.note_name);
        TextView textViewNoteDescription = findViewById(R.id.note_description);

        //assigning intent values
        Intent intent = getIntent();
        name = intent.getStringExtra(AddNewNoteActivity.NOTE_NAME);
        description = intent.getStringExtra(AddNewNoteActivity.NOTE_DESCRIPTION);
        noteId = intent.getIntExtra(AddNewNoteActivity.NOTE_ID, -1);
        courseId = intent.getIntExtra(AddNewCourseActivity.COURSE_ID, -1);
        db = SchedulerDatabase.getInstance(getApplicationContext());
        currentNote = db.noteDao().getCurrentNote(courseId, noteId);

        //setting view text
        textViewNoteName.setText(name);
        textViewNoteDescription.setText(description);

        Button assessmentButton = findViewById(R.id.all_assessments);
        assessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteDetailActivity.this, AssessmentListActivity.class);
                intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_detail_menu, menu);
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
            case R.id.delete_note:
                deleteNote();
                return true;
            case R.id.edit_note:
                editNote();
                return true;
            case R.id.share_note:
                shareNote();
                return true;
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Intent intent = new Intent(NoteDetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Sign out successful!", Toast.LENGTH_SHORT).show();
    }

    private void shareNote() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, name + "\n" + description);
        sendIntent.putExtra(Intent.EXTRA_TITLE, "(Username)'s shared note");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void deleteNote() {
        noteViewModel.delete(currentNote);
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NoteDetailActivity.this, NoteListActivity.class);
        intent.putExtra(AddNewNoteActivity.NOTE_ID, noteId);
        intent.putExtra(AddNewNoteActivity.NOTE_NAME, name);
        intent.putExtra(AddNewNoteActivity.NOTE_DESCRIPTION, description);
        intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
        startActivity(intent);

    }

    private void editNote() {
        Intent intent = new Intent(NoteDetailActivity.this, AddNewNoteActivity.class);
        intent.putExtra(AddNewNoteActivity.NOTE_ID, noteId);
        intent.putExtra(AddNewNoteActivity.NOTE_NAME, name);
        intent.putExtra(AddNewNoteActivity.NOTE_DESCRIPTION, description);
        intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
        startActivity(intent);
    }
}
