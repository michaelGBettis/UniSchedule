package com.example.michaelbettis_term_scheduler.Activities.NoteActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.michaelbettis_term_scheduler.R;

import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.NoteViewModel;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddNewNoteActivity extends AppCompatActivity {


    private int userId;
    private int termId;
    private int courseId;
    private int noteId;
    private TextInputLayout noteNameTextInput;
    private TextInputLayout noteDescriptionTextInput;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        //======================================Hooks=============================================//

        //sets the values of the input data to variables
        noteNameTextInput = findViewById(R.id.note_name);
        noteDescriptionTextInput = findViewById(R.id.note_description);
        Button saveNoteBtn = findViewById(R.id.save_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);

        //=====================================Tool Bar===========================================//

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //===============================Setting Intent Values====================================//

        Intent intent = getIntent();
        userId = intent.getIntExtra(Helper.USER_ID, -1);
        termId = getIntent().getIntExtra(Helper.TERM_ID, -1);
        courseId = intent.getIntExtra(Helper.COURSE_ID, -1);
        noteId = getIntent().getIntExtra(Helper.NOTE_ID, -1);
        if (intent.hasExtra(Helper.NOTE_ID)) {
            getSupportActionBar().setTitle("Edit Note");
            noteNameTextInput.getEditText().setText(intent.getStringExtra(Helper.NOTE_NAME));
            noteDescriptionTextInput.getEditText().setText(intent.getStringExtra(Helper.NOTE_DESCRIPTION));

        }

        //====================================Buttons=============================================//

        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    //method for the save button
    private void saveNote() {
        String name = noteNameTextInput.getEditText().getText().toString();
        String description = noteDescriptionTextInput.getEditText().getText().toString();
        NoteEntity note;

        //check to see if all fields have a value and are not blank spaces
        if (Helper.isInputEmpty(noteDescriptionTextInput) | Helper.isInputEmpty(noteDescriptionTextInput)) {
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
