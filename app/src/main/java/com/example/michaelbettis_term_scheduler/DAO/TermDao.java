package com.example.michaelbettis_term_scheduler.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.TermEntity;

@Dao
public interface TermDao {

    @Insert
    void insert(TermEntity term);

    @Update
    void update(TermEntity term);

    @Delete
    void delete(TermEntity term);

    @Query("DELETE FROM TERM_TABLE")
    void deleteAllTerms();

    @Query("SELECT * FROM TERM_TABLE ORDER BY START_DATE DESC")
    LiveData<List<TermEntity>> getAllTerms();

    @Query("SELECT * FROM TERM_TABLE WHERE user_id = :userID ORDER BY start_date DESC")
    LiveData<List<TermEntity>> getAllAssociatedTerms(int userID);

    @Query("SELECT * FROM TERM_TABLE WHERE term_id = :termID AND user_id = :userID")
    TermEntity getCurrentTerm(int termID, int userID);

    @Query("SELECT end_date from term_table where user_id = :user_id order by end_date DESC LIMIT 1")
    Long getLastEndDate(int user_id);

}
