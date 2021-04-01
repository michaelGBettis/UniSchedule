package com.example.michaelbettis_term_scheduler.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;

@Dao
public interface NoteDao {

    @Insert
    void insert(NoteEntity note);

    @Update
    void update(NoteEntity note);

    @Delete
    void delete(NoteEntity note);

    @Query("SELECT * FROM NOTE_TABLE")
    LiveData<List<NoteEntity>> getAllNotes();

    @Query("SELECT * FROM NOTE_TABLE WHERE note_id = :noteId AND course_id = :courseId")
    NoteEntity getCurrentNote(int courseId, int noteId);

}
