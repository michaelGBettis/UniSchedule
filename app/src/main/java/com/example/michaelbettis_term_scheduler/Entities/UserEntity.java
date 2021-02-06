package com.example.michaelbettis_term_scheduler.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@SuppressWarnings("ALL")
@Entity(tableName = "user_table")

public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    private int user_id;
    private String user_fname;
    private String user_mname;
    private String user_lname;
    private String user_address;
    private String user_phone;
    private String user_email;
    private final String user_password;
    private String user_type;
    private String college_type;
    private String program_type;
    private String minor;
    private double sat_score;

    @Ignore
    public UserEntity(String user_fname, String user_mname, String user_lname,
                      String user_address, String user_phone, String user_email, String user_password,
                      String user_type, String college_type, String program_type, String minor) {
        this.user_fname = user_fname;
        this.user_mname = user_mname;
        this.user_lname = user_lname;
        this.user_address = user_address;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_type = user_type;
        this.college_type = college_type;
        this.program_type = program_type;
        this.minor = minor;
    }

    public UserEntity(String user_fname, String user_mname, String user_lname,
                      String user_address, String user_phone, String user_email, String user_password,
                      String user_type, String college_type, String program_type, double sat_score) {
        this.user_fname = user_fname;
        this.user_mname = user_mname;
        this.user_lname = user_lname;
        this.user_address = user_address;
        this.user_phone = user_phone;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_type = user_type;
        this.college_type = college_type;
        this.program_type = program_type;
        this.sat_score = sat_score;
    }

    @Ignore
    public UserEntity(String user_password) {
        this.user_password = user_password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public String getUser_mname() {
        return user_mname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public String getUser_address() {
        return user_address;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getCollege_type() {
        return college_type;
    }

    public String getProgram_type() {
        return program_type;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public double getSat_score() {
        return sat_score;
    }

    public void setSat_score(double sat_score) {
        this.sat_score = sat_score;
    }
}

