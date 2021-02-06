package com.example.michaelbettis_term_scheduler.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.michaelbettis_term_scheduler.SchedulerRepository;

import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.TermEntity;

public class TermViewModel extends AndroidViewModel {

    private final SchedulerRepository repository;
    private LiveData<List<TermEntity>> allTerms;
    private final LiveData<List<TermEntity>> associatedTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allTerms = repository.getAllTerms();
        associatedTerms = repository.getAssociatedTerms();
    }

    public void insert(TermEntity term) {
        repository.insert(term);
    }

    public void update(TermEntity term) {
        repository.update(term);
    }

    public void delete(TermEntity term) {
        repository.delete(term);
    }

    public LiveData<List<TermEntity>> getAllTerms() {
        return allTerms;
    }


}
