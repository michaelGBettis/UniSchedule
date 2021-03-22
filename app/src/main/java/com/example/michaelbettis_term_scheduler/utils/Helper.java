package com.example.michaelbettis_term_scheduler.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities.AddNewAssessmentActivity;
import com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities.AssessmentListActivity;
import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.AddNewCourseActivity;
import com.example.michaelbettis_term_scheduler.Activities.CourseActivities.CourseListActivity;
import com.example.michaelbettis_term_scheduler.Activities.LoginActivities.SignUpActivity;
import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.Activities.NoteActivities.AddNewNoteActivity;
import com.example.michaelbettis_term_scheduler.Activities.NoteActivities.NoteListActivity;
import com.example.michaelbettis_term_scheduler.Activities.TermActivities.TermListActivity;
import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;
import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;
import com.example.michaelbettis_term_scheduler.ViewModel.AssessmentViewModel;
import com.example.michaelbettis_term_scheduler.ViewModel.NoteViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class Helper {
    //===================================Constants================================================//
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?.@#$%^&+=])(?=\\S+$).{8,}$");
    public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
    public static final String USER_ID = "com.example.michaelbettis_term_scheduler.Activities.USER_ID";
    public static final String USER_F_NAME = "com.example.michaelbettis_term_scheduler.Activities.USER_F_NAME";
    public static final String USER_M_NAME = "com.example.michaelbettis_term_scheduler.Activities.USER_M_NAME";
    public static final String USER_L_NAME = "com.example.michaelbettis_term_scheduler.Activities.USER_L_NAME";
    public static final String USER_ADDRESS = "com.example.michaelbettis_term_scheduler.Activities.USER_ADDRESS";
    public static final String USER_PHONE = "com.example.michaelbettis_term_scheduler.Activities.USER_PHONE";
    public static final String USER_MINOR = "com.example.michaelbettis_term_scheduler.Activities.USER_MINOR";
    public static final String USER_GPA_SCORE = "com.example.michaelbettis_term_scheduler.Activities.USER_GPA_SCORE";
    public static final String USER_EMAIL = "com.example.michaelbettis_term_scheduler.Activities.USER_EMAIL";
    public static final String STUDENT_TYPE = "com.example.michaelbettis_term_scheduler.Activities.STUDENT_TYPE";
    public static final String COLLEGE_TYPE = "com.example.michaelbettis_term_scheduler.Activities.COLLEGE_TYPE";
    public static final String TERM_ID = "com.example.michaelbettis_term_scheduler.Activities.TERM_ID";
    public static final String TERM_NAME = "com.example.michaelbettis_term_scheduler.Activities.TERM_NAME";
    public static final String TERM_START = "com.example.michaelbettis_term_scheduler.Activities.TERM_START";
    public static final String TERM_END = "com.example.michaelbettis_term_scheduler.Activities.TERM_END";
    public static final String COURSE_ID = "com.example.michaelbettis_term_scheduler.Activities.COURSE_ID";
    public static final String COURSE_NAME = "com.example.michaelbettis_term_scheduler.Activities.COURSE_NAME";
    public static final String COURSE_START = "com.example.michaelbettis_term_scheduler.Activities.COURSE_START";
    public static final String COURSE_END = "com.example.michaelbettis_term_scheduler.Activities.COURSE_END";
    public static final String COURSE_STATUS = "com.example.michaelbettis_term_scheduler.Activities.COURSE_STATUS";
    public static final String MENTOR_NAME = "com.example.michaelbettis_term_scheduler.Activities.MENTOR_NAME";
    public static final String MENTOR_PHONE = "com.example.michaelbettis_term_scheduler.Activities.MENTOR_PHONE";
    public static final String MENTOR_EMAIL = "com.example.michaelbettis_term_scheduler.Activities.MENTOR_EMAIL";
    public static final String ASSESS_ID = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_ID";
    public static final String ASSESS_NAME = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_NAME";
    public static final String ASSESS_DESC = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_DESC";
    public static final String ASSESS_DUE_DATE = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_DUE_DATE";
    public static final String ASSESS_TYPE = "com.example.michaelbettis_term_scheduler.Activities.ASSESS_TYPE";
    public static final String NOTE_ID = "com.example.michaelbettis_term_scheduler.Activities.NOTE_ID";
    public static final String NOTE_NAME = "com.example.michaelbettis_term_scheduler.Activities.NOTE_NAME";
    public static final String NOTE_DESCRIPTION = "com.example.michaelbettis_term_scheduler.Activities.NOTE_DESCRIPTION";

    //=================================Date converters============================================//
    public static Date stringToDate(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        return sdf.parse(string);
    }

    public static long stringToLong(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date date = sdf.parse(string);
        return Objects.requireNonNull(date).getTime();
    }

    public static long shortStringToLong(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        Date date = sdf.parse(string);
        return Objects.requireNonNull(date).getTime();
    }

    public static String formatter(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String sdf(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);

        return sdf2.format(Objects.requireNonNull(parsedDate));
    }

    //===================================Java mail helper=========================================//
    public static final String EMAIL = "27NlUuV1qTRpqvUVAs9/VA==";
    public static final String PASSWORD = "0kKnejGHJYM0s2agxwrNpg==";

    public static String decrypt(String key, String encryptedMsg) {

        String messageAfterDecrypt = null;
        try {
            messageAfterDecrypt = AESCrypt.decrypt(key, encryptedMsg);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return messageAfterDecrypt;

    }

    //===================================Java mail helper=========================================//
    public static void sendNotification(String startDate, String s, int i, Context context) throws ParseException {
        Intent intent = new Intent(context, MyReceiver.class);
        intent.putExtra("Notification", s );
        PendingIntent sender = PendingIntent.getBroadcast(context, i, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Helper.stringToDate(startDate).getTime(), sender);
    }

    //===================================Form Validation==========================================//
    public static boolean isInputEmpty(TextInputLayout textInput) {

        if (textInput.getEditText().getText().toString().trim().isEmpty()) {
            textInput.setError("Field cannot be empty");
            return true;
        } else {
            textInput.setError(null);
            return false;
        }

    }

    //=====================================Navigation=============================================//

    public static void goToTerms(int userId, Context context) {
        Intent intent = new Intent(context, TermListActivity.class);
        intent.putExtra(Helper.USER_ID, userId);
        context.startActivity(intent);
    }

    public static void goToCourses(int userId, int termId, Context context) {
        Intent intent = new Intent(context, CourseListActivity.class);
        intent.putExtra(Helper.USER_ID, userId);
        intent.putExtra(Helper.TERM_ID, termId);
        context.startActivity(intent);
    }

    public static void goToAssessments(int userId, int termId, int courseId, String courseStart, String courseEnd, Context context) {
        Intent intent = new Intent(context, AssessmentListActivity.class);
        intent.putExtra(Helper.USER_ID, userId);
        intent.putExtra(Helper.TERM_ID, termId);
        intent.putExtra(Helper.COURSE_ID, courseId);
        intent.putExtra(Helper.COURSE_START, courseStart);
        intent.putExtra(Helper.COURSE_END, courseEnd);
        context.startActivity(intent);
    }

    public static void goToNotes(int termId,  int userId, int courseId, Context context) {
        Intent intent = new Intent(context, NoteListActivity.class);
        intent.putExtra(Helper.TERM_ID, termId);
        intent.putExtra(Helper.USER_ID, userId);
        intent.putExtra(Helper.COURSE_ID, courseId);
        context.startActivity(intent);
    }

    public static void editUser(SchedulerDatabase db, int userID, Context context) {
        UserEntity currentUser = db.userDao().getCurrentUser(userID);

        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(Helper.USER_ID, userID);
        intent.putExtra(Helper.USER_F_NAME, currentUser.getUser_fname());
        intent.putExtra(Helper.USER_M_NAME, currentUser.getUser_mname());
        intent.putExtra(Helper.USER_L_NAME, currentUser.getUser_lname());
        intent.putExtra(Helper.USER_ADDRESS, currentUser.getUser_address());
        intent.putExtra(Helper.USER_PHONE, currentUser.getUser_phone());
        intent.putExtra(Helper.USER_EMAIL, currentUser.getUser_email());
        intent.putExtra(Helper.STUDENT_TYPE, currentUser.getUser_type());
        intent.putExtra(Helper.COLLEGE_TYPE, currentUser.getCollege_type());
        intent.putExtra(Helper.USER_MINOR, currentUser.getMinor());
        intent.putExtra(Helper.USER_GPA_SCORE, currentUser.getSat_score());
        context.startActivity(intent);
    }

    public static void generateReports(SchedulerDatabase db, int userID, Context context) {
        Cursor result = db.userDao().getReport(userID);
        StringBuilder buffer = new StringBuilder();
        Date today = new Date();

        while (result.moveToNext()) {
            buffer.append("Term   " + "Course   " + "Assessment   \n");
            buffer.append(result.getString(2)).append(" ").append(result.getString(3)).append(" ").append(result.getString(4)).append("\n");

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        })
                .setCancelable(true)
                .setTitle("Student Report for: " + Helper.formatter(today))
                .setMessage(buffer.toString())
                .show();

    }

    public static void signOut(Activity activity) {

        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
        Toast.makeText(activity, "Sign out successful!", Toast.LENGTH_SHORT).show();

    }

    //=====================================Assessments============================================//

    public static void deleteAssessment(Activity activity, AssessmentViewModel view, AssessmentEntity assessment) {
        view.delete(assessment);
        Toast.makeText(activity, "Assessment deleted", Toast.LENGTH_SHORT).show();

    }

    public static void editAssessment(Context context, AssessmentEntity assess, String courseStart, String courseEnd) {

        Intent intent = new Intent(context, AddNewAssessmentActivity.class);
        intent.putExtra(Helper.ASSESS_ID, assess.getAssessment_id());
        intent.putExtra(Helper.ASSESS_NAME, assess.getAssessment_name());
        intent.putExtra(Helper.ASSESS_DESC, assess.getAssessment_info());
        intent.putExtra(Helper.ASSESS_DUE_DATE, assess.getDue_date().toString());
        intent.putExtra(Helper.ASSESS_TYPE, assess.getAssessment_type());
        intent.putExtra(Helper.COURSE_ID, assess.getCourse_id());
        intent.putExtra(Helper.COURSE_START, courseStart);
        intent.putExtra(Helper.COURSE_END, courseEnd);
        context.startActivity(intent);

    }

    //========================================Notes===============================================//

    public static void deleteNote(Activity activity, NoteViewModel view, NoteEntity note) {
        view.delete(note);
        Toast.makeText(activity, "Assessment deleted", Toast.LENGTH_SHORT).show();

    }

    public static void editNote(Context context, NoteEntity note) {

        Intent intent = new Intent(context, AddNewNoteActivity.class);
        intent.putExtra(Helper.NOTE_ID, note.getNote_id());
        intent.putExtra(Helper.NOTE_NAME, note.getNote_name());
        intent.putExtra(Helper.NOTE_DESCRIPTION, note.getNote_info());
        intent.putExtra(Helper.COURSE_ID, note.getCourse_id());
        context.startActivity(intent);

    }
}
