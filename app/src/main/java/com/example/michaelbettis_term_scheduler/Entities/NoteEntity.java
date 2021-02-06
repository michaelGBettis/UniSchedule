package com.example.michaelbettis_term_scheduler.Entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "note_table", foreignKeys = @ForeignKey(entity = CourseEntity.class,
        parentColumns = "course_id",
        childColumns = "course_id",
        onDelete = CASCADE,
        onUpdate = CASCADE))
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    private int note_id;

    private final String note_name;

    private final String note_info;

    private final int course_id;

    public NoteEntity(String note_name, String note_info, int course_id) {
        this.note_name = note_name;
        this.note_info = note_info;
        this.course_id = course_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public int getNote_id() {
        return note_id;
    }

    public String getNote_name() {
        return note_name;
    }

    public String getNote_info() {
        return note_info;
    }

    public int getCourse_id() {
        return course_id;
    }
}
