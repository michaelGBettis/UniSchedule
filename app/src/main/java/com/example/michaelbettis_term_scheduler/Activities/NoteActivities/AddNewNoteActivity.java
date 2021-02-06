package com.example.michaelbettis_term_scheduler.Activities.NoteActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.Objects;

public class AddNewNoteActivity extends AppCompatActivity {
    public static final String NOTE_ID = "com.example.michaelbettis_term_scheduler.Activities.NOTE_ID";
    public static final String NOTE_NAME = "com.example.michaelbettis_term_scheduler.Activities.NOTE_NAME";
    public static final String NOTE_DESCRIPTION = "com.example.michaelbettis_term_scheduler.Activities.NOTE_DESCRIPTION";


    private int courseId;
    private int noteId;
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

        //Gets intent data and checks if it has an id value or not, if it does, it changes the title
        //text to edit note, otherwise it stays as edit note
        Intent intent = getIntent();
        courseId = intent.getIntExtra(AddNewCourseActivity.COURSE_ID, -1);
        noteId = getIntent().getIntExtra(NOTE_ID, -1);
        if (intent.hasExtra(NOTE_ID)) {
            getSupportActionBar().setTitle("Edit Note");
            editTextNoteName.setText(intent.getStringExtra(NOTE_NAME));
            editTextNoteDescription.setText(intent.getStringExtra(NOTE_DESCRIPTION));

        }

        //creates or provides a view model instance
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
    }

    //sets the menu for the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_course_menu, menu);
        return true;
    }

    //sets the methods for the menu buttons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_course) {
            saveCourse();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //method for the save button
    private void saveCourse() {
        String name = editTextNoteName.getText().toString();
        String description = editTextNoteDescription.getText().toString();

        //check to see if all fields have a value and are not blank spaces
        if (name.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a value in all fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            NoteEntity note;
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
        }
        Intent intent = new Intent(AddNewNoteActivity.this, NoteListActivity.class);
        intent.putExtra(AddNewNoteActivity.NOTE_NAME, name);
        intent.putExtra(AddNewNoteActivity.NOTE_DESCRIPTION, description);
        intent.putExtra(AddNewNoteActivity.NOTE_ID, noteId);
        intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
        startActivity(intent);

    }

    //sets the action for the back button and displays a message
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Toast.makeText(this, "Note not Added", Toast.LENGTH_SHORT).show();
        return true;
    }

}
