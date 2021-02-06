package com.example.michaelbettis_term_scheduler.DAO;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.michaelbettis_term_scheduler.Entities.UserEntity;

@Dao
public interface UserDao {

    @Insert
    void insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Delete
    void delete(UserEntity user);

    @Query("SELECT * FROM USER_TABLE WHERE user_email = :user_email AND user_password = :password")
    UserEntity validateUser(String user_email, String password);

    @Query("SELECT * FROM USER_TABLE WHERE user_id = :userId")
    UserEntity getCurrentUser(int userId);

    @Query("SELECT * FROM USER_TABLE WHERE user_email = :user_email AND user_phone = :phone")
    UserEntity validateCurrentUser(String user_email, String phone);

    @Query("SELECT * FROM USER_TABLE WHERE user_email = :user_email")
    UserEntity isAUser(String user_email);

    @Query("SELECT u.user_fname||\" \"||u.user_lname AS USER,u.user_id,t.term_name AS TERM,c.course_name AS COURSE, a.assessment_name AS ASSESSMENT," +
            " n.note_name AS NOTE FROM USER_TABLE AS u, TERM_TABLE AS T,COURSE_TABLE AS c, NOTE_TABLE AS n,ASSESSMENT_TABLE AS a WHERE u.user_id = " +
            ":userID AND u.user_id = t.user_id AND t.term_id = c.term_id AND c.course_id = n.course_id AND c.course_id = a.course_id")
    Cursor getReport(int userID);
}
