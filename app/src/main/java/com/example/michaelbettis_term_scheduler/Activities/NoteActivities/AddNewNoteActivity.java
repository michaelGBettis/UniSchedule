package com.example.michaelbettis_term_scheduler.Activities.NoteActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.AddNewCourseActivity;
import com.example.michaelbettis_term_scheduler.R;

import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.NoteViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;

import java.util.Objects;

public class AddNewNoteActivity extends AppCompatActivity {


    private int userId;
    private int termId;
    private int courseId;
    private int noteId;
    private Button saveNoteBtn;
    private EditText editTextNoteName;
    private EditText editTextNoteDescription;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //sets the values of the input data to variables
        editTextNoteName = findViewById(R.id.note_name);
        editTextNoteDescription = findViewById(R.id.note_description);
        saveNoteBtn = findViewById(R.id.save_note);

        //Gets intent data and checks if it has an id value or not, if it does, it changes the title
        //text to edit note, otherwise it stays as edit note
        Intent intent = getIntent();
        userId = intent.getIntExtra(Helper.USER_ID, -1);
        termId = getIntent().getIntExtra(Helper.TERM_ID, -1);
        courseId = intent.getIntExtra(Helper.COURSE_ID, -1);
        noteId = getIntent().getIntExtra(Helper.NOTE_ID, -1);
        if (intent.hasExtra(Helper.NOTE_ID)) {
            getSupportActionBar().setTitle("Edit Note");
            editTextNoteName.setText(intent.getStringExtra(Helper.NOTE_NAME));
            editTextNoteDescription.setText(intent.getStringExtra(Helper.NOTE_DESCRIPTION));

        }

        //creates or provides a view model instance
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);

        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    //method for the save button
    private void saveNote() {
        String name = editTextNoteName.getText().toString();
        String description = editTextNoteDescription.getText().toString();
        NoteEntity note;

        //check to see if all fields have a value and are not blank spaces
        if (name.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a value in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noteId == -1) {
            note = new NoteEntity(name, description, courseId);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show();

        } else {
            note = new NoteEntity(name, description, courseId);
            note.setNote_id(noteId);
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

        }

        Helper.goToNotes(termId, userId, courseId, AddNewNoteActivity.this);

    }

    //sets the action for the back button and displays a message
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Toast.makeText(this, "Note not Added", Toast.LENGTH_SHORT).show();
        return true;
    }

}
