package com.example.michaelbettis_term_scheduler.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "course_table", foreignKeys = @ForeignKey(entity = TermEntity.class,
        parentColumns = "term_id",
        childColumns = "term_id",
        onDelete = CASCADE,
        onUpdate = CASCADE))
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    private int course_id;
    private final String course_name;
    private final Date start_date;
    private final Date end_date;
    @ColumnInfo(defaultValue = "Plan To Take")
    private String course_status;
    private final String course_mentor;
    private final String mentor_phone;
    private final String mentor_email;
    private final int term_id;

    public CourseEntity(String course_name, Date start_date, Date end_date, String course_status, String course_mentor, String mentor_phone, String mentor_email, int term_id) {
        this.course_name = course_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.course_status = course_status;
        this.course_mentor = course_mentor;
        this.mentor_phone = mentor_phone;
        this.mentor_email = mentor_email;
        this.term_id = term_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public String getCourse_status() {
        return course_status;
    }

    public String getCourse_mentor() {
        return course_mentor;
    }

    public String getMentor_phone() {
        return mentor_phone;
    }

    public String getMentor_email() {
        return mentor_email;
    }

    public int getTerm_id() {
        return term_id;
    }
}
