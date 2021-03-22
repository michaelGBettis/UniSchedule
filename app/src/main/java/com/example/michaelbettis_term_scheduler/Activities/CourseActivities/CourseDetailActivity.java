/*
//package com.example.michaelbettis_term_scheduler.Activities.CourseActivities;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.example.michaelbettis_term_scheduler.Activities.AssessmentActivities.AssessmentListActivity;
//import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
//import com.example.michaelbettis_term_scheduler.Activities.NoteActivities.NoteListActivity;
//import com.example.michaelbettis_term_scheduler.Activities.TermActivities.AddNewTermActivity;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.lifecycle.ViewModelProvider;
//
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.michaelbettis_term_scheduler.R;
//import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
//
//import java.text.ParseException;
//import java.util.Objects;
//
//import com.example.michaelbettis_term_scheduler.Entities.CourseEntity;
//import com.example.michaelbettis_term_scheduler.ViewModel.CourseViewModel;
//import com.example.michaelbettis_term_scheduler.utils.Helper;
//
//public class CourseDetailActivity extends AppCompatActivity {
//
//    private int courseId;
//    private String courseStart;
//    private String courseEnd;
//    private int termId;
//    private String termStart;
//    private String termEnd;
//    private String name;
//    private String courseStatus;
//    private String mentorName;
//    private String mentorPhone;
//    private String mentorEmail;
//    private TextView textViewStartDate;
//    private TextView textViewEndDate;
//    private TextView textViewCourseStatus;
//    private CourseViewModel courseViewModel;
//    SchedulerDatabase db;
//    CourseEntity currentCourse;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_course_detail);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setTitle("Course Details");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        //creates or provides a view model instance
//        courseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CourseViewModel.class);
//
//        //initializing views
//        TextView textViewCourseName = findViewById(R.id.course_name);
//        textViewStartDate = findViewById(R.id.course_start_date);
//        textViewEndDate = findViewById(R.id.course_end_date);
//        textViewCourseStatus = findViewById(R.id.course_status_indicator);
//        TextView textViewMentorName = findViewById(R.id.course_mentor_indicator);
//        TextView textViewMentorPhone = findViewById(R.id.mentor_phone_indicator);
//        TextView textViewMentorEmail = findViewById(R.id.mentor_email_indicator);
//
//        //assigning intent values
//        Intent intent = getIntent();
//        courseId = intent.getIntExtra(Helper.COURSE_ID, -1);
//        name = intent.getStringExtra(Helper.COURSE_NAME);
//        courseStart = intent.getStringExtra(Helper.COURSE_START);
//        courseEnd = intent.getStringExtra(Helper.COURSE_END);
//        courseStatus = intent.getStringExtra(Helper.COURSE_STATUS);
//        mentorName = intent.getStringExtra(Helper.MENTOR_NAME);
//        mentorPhone = intent.getStringExtra(Helper.MENTOR_PHONE);
//        mentorEmail = intent.getStringExtra(Helper.MENTOR_EMAIL);
//        termId = intent.getIntExtra(Helper.TERM_ID, -1);
//        termStart = intent.getStringExtra(Helper.TERM_START);
//        termEnd = intent.getStringExtra(Helper.TERM_END);
//        db = SchedulerDatabase.getInstance(getApplicationContext());
//        currentCourse = db.courseDao().getCurrentCourse(termId, courseId);
//
//
//        //setting view text
//        textViewCourseName.setText(name);
//        textViewStartDate.setText(Helper.sdf(courseStart));
//        textViewEndDate.setText(Helper.sdf(courseEnd));
//        textViewCourseStatus.setText(courseStatus);
//        textViewMentorName.setText(mentorName);
//        textViewMentorPhone.setText(mentorPhone);
//        textViewMentorEmail.setText(mentorEmail);
//
//        Button noteButton = findViewById(R.id.all_notes);
//        noteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CourseDetailActivity.this, NoteListActivity.class);
//                intent.putExtra(Helper.COURSE_ID, courseId);
//                startActivity(intent);
//            }
//        });
//
//        Button assessmentButton = findViewById(R.id.all_assessments);
//        assessmentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CourseDetailActivity.this, AssessmentListActivity.class);
//                intent.putExtra(Helper.COURSE_ID, courseId);
//                intent.putExtra(Helper.COURSE_START, courseStart);
//                intent.putExtra(Helper.COURSE_END, courseEnd);
//                startActivity(intent);
//            }
//        });
//
//
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void signOut() {
//        Intent intent = new Intent(CourseDetailActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//        Toast.makeText(this, "Sign out successful!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void courseDropped() {
//        String startDate = textViewStartDate.getText().toString();
//        String endDate = textViewEndDate.getText().toString();
//
//        try {
//
//            currentCourse = new CourseEntity(name, Helper.stringToDate(startDate), Helper.stringToDate(endDate), "Dropped", mentorName, mentorPhone, mentorEmail, termId);
//            currentCourse.setCourse_id(courseId);
//            courseViewModel.update(currentCourse);
//            textViewCourseStatus.setText("Dropped");
//            courseStatus = "Dropped";
//            invalidateOptionsMenu();
//            Toast.makeText(this, "Course Dropped", Toast.LENGTH_SHORT).show();
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void courseStatus() {
//        String startDate = textViewStartDate.getText().toString();
//        String endDate = textViewEndDate.getText().toString();
//
//        if (courseStatus.equals("Plan To Take")) {
//
//            try {
//
//                currentCourse = new CourseEntity(name, Helper.stringToDate(startDate), Helper.stringToDate(endDate), "In Progress", mentorName, mentorPhone, mentorEmail, termId);
//                currentCourse.setCourse_id(courseId);
//                courseViewModel.update(currentCourse);
//                textViewCourseStatus.setText("In Progress");
//                courseStatus = "In Progress";
//                invalidateOptionsMenu();
//                Toast.makeText(this, "Course Started, Good Luck!", Toast.LENGTH_SHORT).show();
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//        } else if (courseStatus.equals("In Progress")) {
//
//            try {
//
//                currentCourse = new CourseEntity(name, Helper.stringToDate(textViewStartDate.getText().toString()), Helper.stringToDate(textViewStartDate.getText().toString()), "Completed", mentorName, mentorPhone, mentorEmail, termId);
//                currentCourse.setCourse_id(courseId);
//                courseViewModel.update(currentCourse);
//                textViewCourseStatus.setText("Completed");
//                courseStatus = "Completed";
//                invalidateOptionsMenu();
//                Toast.makeText(this, "Course Completed, Congratulations!", Toast.LENGTH_SHORT).show();
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        } else
//            Toast.makeText(this, "This Course has been " + courseStatus, Toast.LENGTH_SHORT).show();
//    }
//
//
//    private void editCourse() {
//        db = SchedulerDatabase.getInstance(getApplicationContext());
//        int assessCount = db.assessmentDao().getAssessmentCount(courseId);
//
//        if (assessCount == 0) {
//
//            Intent intent = new Intent(CourseDetailActivity.this, AddNewCourseActivity.class);
//            intent.putExtra(Helper.TERM_ID, termId);
//            intent.putExtra(Helper.TERM_START, termStart);
//            intent.putExtra(Helper.TERM_END, termEnd);
//            intent.putExtra(Helper.COURSE_ID, courseId);
//            intent.putExtra(Helper.COURSE_NAME, name);
//            intent.putExtra(Helper.COURSE_START, courseStart);
//            intent.putExtra(Helper.COURSE_END, courseEnd);
//            intent.putExtra(Helper.COURSE_STATUS, courseStatus);
//            intent.putExtra(Helper.MENTOR_NAME, mentorName);
//            intent.putExtra(Helper.MENTOR_PHONE, mentorPhone);
//            intent.putExtra(Helper.MENTOR_EMAIL, mentorEmail);
//            startActivity(intent);
//
//        } else {
//            Toast.makeText(this, "Can not edit a Course with associated Notes or Assignments", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
//
//    private void deleteCourse() {
//
//        courseViewModel.delete(currentCourse);
//        Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(CourseDetailActivity.this, CourseListActivity.class);
//        intent.putExtra(Helper.TERM_ID, termId);
//        intent.putExtra(Helper.COURSE_ID, courseId);
//        intent.putExtra(Helper.COURSE_NAME, name);
//        intent.putExtra(Helper.COURSE_START, courseStart);
//        intent.putExtra(Helper.COURSE_END, courseEnd);
//        intent.putExtra(Helper.COURSE_STATUS, courseStatus);
//        intent.putExtra(Helper.MENTOR_NAME, mentorName);
//        intent.putExtra(Helper.MENTOR_PHONE, mentorPhone);
//        intent.putExtra(Helper.MENTOR_EMAIL, mentorEmail);
//        startActivity(intent);
//    }
////
////    @Override
////    public boolean onPrepareOptionsMenu(Menu menu) {
////
////        if (courseStatus.equals("Plan To Take")) {
////            menu.findItem(R.id.course_status_button).setTitle("Start Course");
////
////        } else if (courseStatus.equals("In Progress")) {
////            menu.findItem(R.id.course_status_button).setTitle("Complete Course");
////            menu.findItem(R.id.course_dropped_button).setVisible(true);
////        } else {
////            menu.findItem(R.id.course_status_button).setVisible(false);
////            menu.findItem(R.id.course_dropped_button).setVisible(false);
////        }
////
////        return super.onPrepareOptionsMenu(menu);
////    }
//}
*/
