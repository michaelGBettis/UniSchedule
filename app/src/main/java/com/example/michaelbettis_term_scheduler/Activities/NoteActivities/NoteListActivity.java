package com.example.michaelbettis_term_scheduler.Activities.NoteActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.AddNewCourseActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Adapters.NoteAdapter;
import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.NoteViewModel;


public class NoteListActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    private int courseId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Note List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //sets the recycle view
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //assigning intent values
        Intent intent = getIntent();
        courseId = intent.getIntExtra(AddNewCourseActivity.COURSE_ID, -1);

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
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(NoteListActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NoteEntity note) {
                Intent intent = new Intent(NoteListActivity.this, NoteDetailActivity.class);
                intent.putExtra(AddNewNoteActivity.NOTE_NAME, note.getNote_name());
                intent.putExtra(AddNewNoteActivity.NOTE_DESCRIPTION, note.getNote_info());
                intent.putExtra(AddNewNoteActivity.NOTE_ID, note.getNote_id());
                intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
                startActivity(intent);

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this, AddNewNoteActivity.class);
                intent.putExtra(AddNewCourseActivity.COURSE_ID, courseId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_notes);
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
            case R.id.delete_all_notes:
                deleteAllNotes();
                return true;
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Intent intent = new Intent(NoteListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Sign out successful!", Toast.LENGTH_SHORT).show();
    }

    private void deleteAllNotes() {

        noteViewModel.getAllNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> notes) {
                for (NoteEntity n : notes) {
                    if (n.getCourse_id() == courseId) {

                        noteViewModel.delete(n);
                        Toast.makeText(NoteListActivity.this, "Notes Deleted", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
