package com.example.michaelbettis_term_scheduler.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.CourseEntity;

@Dao
public interface CourseDao {

    @Insert
    void insert(CourseEntity course);

    @Update
    void update(CourseEntity course);

    @Delete
    void delete(CourseEntity course);


    @Query("SELECT * FROM COURSE_TABLE ORDER BY course_id DESC")
    LiveData<List<CourseEntity>> getAllCourses();

    @Query("SELECT COUNT(*) FROM COURSE_TABLE WHERE term_id = :termID")
    int getCourseCount(int termID);

    @Query("SELECT * FROM COURSE_TABLE WHERE term_id = :termId AND course_id = :courseID")
    CourseEntity getCurrentCourse(int termId, int courseID);
}
