package com.example.michaelbettis_term_scheduler.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;

@Dao
public interface AssessmentDao {

    @Insert
    void insert(AssessmentEntity assessment);

    @Update
    void update(AssessmentEntity assessment);

    @Delete
    void delete(AssessmentEntity assessment);

    @Query("SELECT * FROM ASSESSMENT_TABLE")
    LiveData<List<AssessmentEntity>> getAllAssessments();

    @Query("SELECT * FROM ASSESSMENT_TABLE WHERE assessment_id = :assessmentId AND course_id = :courseId")
    AssessmentEntity getCurrentAssessments(int courseId, int assessmentId);

    @Query("SELECT COUNT(*) FROM ASSESSMENT_TABLE WHERE course_id = :courseId")
    int getAssessmentCount(int courseId);

}
