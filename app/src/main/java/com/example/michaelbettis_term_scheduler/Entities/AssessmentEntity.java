package com.example.michaelbettis_term_scheduler.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;


import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "assessment_table", foreignKeys = @ForeignKey(entity = CourseEntity.class,
        parentColumns = "course_id",
        childColumns = "course_id",
        onDelete = CASCADE,
        onUpdate = CASCADE))
public class AssessmentEntity {

    @PrimaryKey(autoGenerate = true)
    private int assessment_id;

    private final String assessment_name;

    private final Date due_date;

    private final String assessment_info;

    private final String assessment_type;

    private final int course_id;

    public AssessmentEntity(String assessment_name, Date due_date, String assessment_info, String assessment_type, int course_id) {
        this.assessment_name = assessment_name;
        this.due_date = due_date;
        this.assessment_info = assessment_info;
        this.assessment_type = assessment_type;
        this.course_id = course_id;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public int getAssessment_id() {
        return assessment_id;
    }

    public String getAssessment_name() {
        return assessment_name;
    }

    public Date getDue_date() {
        return due_date;
    }

    public String getAssessment_info() {
        return assessment_info;
    }

    public String getAssessment_type() {
        return assessment_type;
    }

    public int getCourse_id() {
        return course_id;
    }
}
