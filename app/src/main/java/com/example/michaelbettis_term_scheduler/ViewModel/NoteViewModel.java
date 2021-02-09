package com.example.michaelbettis_term_scheduler.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.michaelbettis_term_scheduler.utils.SchedulerRepository;

import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;

public class NoteViewModel extends AndroidViewModel {

    private final SchedulerRepository repository;
    private LiveData<List<NoteEntity>> allNotes;
    private final LiveData<List<NoteEntity>> associatedNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allNotes = repository.getAllNotes();
        associatedNotes = repository.getAssociatedNotes();
    }

    public void insert(NoteEntity note) {
        repository.insert(note);
    }

    public void update(NoteEntity note) {
        repository.update(note);
    }

    public void delete(NoteEntity note) {
        repository.delete(note);
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

}
