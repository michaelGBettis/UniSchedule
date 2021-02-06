package com.example.michaelbettis_term_scheduler.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "term_table", foreignKeys = @ForeignKey(entity = UserEntity.class,
        parentColumns = "user_id",
        childColumns = "user_id",
        onDelete = CASCADE,
        onUpdate = CASCADE))
public class TermEntity {

    @PrimaryKey(autoGenerate = true)
    private int term_id;
    private final String term_name;
    private final Date start_date;
    private final Date end_date;
    private final int user_id;


    public TermEntity(String term_name, Date start_date, Date end_date, int user_id) {
        this.term_name = term_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.user_id = user_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    public int getTerm_id() {
        return term_id;
    }

    public String getTerm_name() {
        return term_name;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public int getUser_id() {
        return user_id;
    }
}
