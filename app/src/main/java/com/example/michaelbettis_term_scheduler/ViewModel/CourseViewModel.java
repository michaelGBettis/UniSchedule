package com.example.michaelbettis_term_scheduler.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.michaelbettis_term_scheduler.utils.SchedulerRepository;

import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.CourseEntity;

public class CourseViewModel extends AndroidViewModel {

    private final SchedulerRepository repository;
    private LiveData<List<CourseEntity>> allCourses;
    private final LiveData<List<CourseEntity>> associatedCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allCourses = repository.getAllCourses();
        associatedCourses = repository.getAssociatedCourses();
    }

    public void insert(CourseEntity course) {
        repository.insert(course);
    }

    public void update(CourseEntity course) {
        repository.update(course);
    }

    public void delete(CourseEntity course) {
        repository.delete(course);
    }

    public LiveData<List<CourseEntity>> getAllCourses() {
        return allCourses;
    }

}
