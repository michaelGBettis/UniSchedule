package com.example.michaelbettis_term_scheduler.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.SchedulerRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final SchedulerRepository repository;
    private final LiveData<List<UserEntity>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allUsers = repository.getAllUsers();
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

    public LiveData<List<UserEntity>> getAllUsers() {
        return allUsers;
    }
}
