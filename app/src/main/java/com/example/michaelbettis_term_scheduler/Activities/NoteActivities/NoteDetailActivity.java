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
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;

import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.NoteViewModel;

import java.util.Objects;

public class NoteDetailActivity extends AppCompatActivity {

    private int userId;
    private int termId;
    private int courseId;
    private int noteId;
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
        userId = intent.getIntExtra(Helper.USER_ID, -1);
        termId = intent.getIntExtra(Helper.TERM_END, -1);
        courseId = intent.getIntExtra(Helper.COURSE_ID, -1);
        noteId = intent.getIntExtra(Helper.NOTE_ID, -1);
        db = SchedulerDatabase.getInstance(getApplicationContext());
        currentNote = db.noteDao().getCurrentNote(courseId, noteId);

        //setting view text
        textViewNoteName.setText(currentNote.getNote_name());
        textViewNoteDescription.setText(currentNote.getNote_info());
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
                Helper.deleteNote(NoteDetailActivity.this, noteViewModel, currentNote);
                Helper.goToNotes(termId, userId, courseId, NoteDetailActivity.this);
                return true;
            case R.id.edit_note:
                Helper.editNote(NoteDetailActivity.this, currentNote);
                return true;
            case R.id.share_note:
                shareNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareNote() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, currentNote.getNote_name() + "\n" + currentNote.getNote_info());
        sendIntent.putExtra(Intent.EXTRA_TITLE, "(Username)'s shared note");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

}
