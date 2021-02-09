package com.example.michaelbettis_term_scheduler.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.michaelbettis_term_scheduler.utils.SchedulerRepository;

import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;

public class AssessmentViewModel extends AndroidViewModel {

    private final SchedulerRepository repository;
    private LiveData<List<AssessmentEntity>> allAssessments;
    private final LiveData<List<AssessmentEntity>> associatedAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allAssessments = repository.getAllAssessments();
        associatedAssessments = repository.getAssociatedAssessments();
    }

    public void insert(AssessmentEntity assessment) {
        repository.insert(assessment);
    }

    public void update(AssessmentEntity assessment) {
        repository.update(assessment);
    }

    public void delete(AssessmentEntity assessment) {
        repository.delete(assessment);
    }

    public LiveData<List<AssessmentEntity>> getAllAssessments() {
        return allAssessments;
    }

}
