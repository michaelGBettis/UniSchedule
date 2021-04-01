package com.example.michaelbettis_term_scheduler.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.utils.SchedulerRepository;

public class UserViewModel extends AndroidViewModel {

    private final SchedulerRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);

    }

    public void insert(UserEntity user) {
        repository.insert(user);
    }

    public void update(UserEntity user) {
        repository.update(user);
    }

    public void delete(UserEntity user) {
        repository.delete(user);
    }

}
