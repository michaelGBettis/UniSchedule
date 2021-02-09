package com.example.michaelbettis_term_scheduler.utils;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;

import com.example.michaelbettis_term_scheduler.DAO.AssessmentDao;
import com.example.michaelbettis_term_scheduler.DAO.CourseDao;
import com.example.michaelbettis_term_scheduler.DAO.NoteDao;
import com.example.michaelbettis_term_scheduler.DAO.TermDao;
import com.example.michaelbettis_term_scheduler.DAO.UserDao;
import com.example.michaelbettis_term_scheduler.Entities.AssessmentEntity;
import com.example.michaelbettis_term_scheduler.Entities.CourseEntity;
import com.example.michaelbettis_term_scheduler.Entities.NoteEntity;
import com.example.michaelbettis_term_scheduler.Entities.TermEntity;
import com.example.michaelbettis_term_scheduler.Entities.UserEntity;


@Database(entities = {UserEntity.class, TermEntity.class,
        CourseEntity.class, AssessmentEntity.class, NoteEntity.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class SchedulerDatabase extends RoomDatabase {

    private static SchedulerDatabase instance;

    public abstract UserDao userDao();

    public abstract TermDao termDao();

    public abstract CourseDao courseDao();

    public abstract AssessmentDao assessmentDao();

    public abstract NoteDao noteDao();

    public static synchronized SchedulerDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SchedulerDatabase.class, "scheduler_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private final UserDao userDao;
        private final TermDao termDao;
        private final CourseDao courseDao;
        private final AssessmentDao assessmentDao;
        private final NoteDao noteDao;
        final Date today = new Date();


        private PopulateDbAsyncTask(SchedulerDatabase db) {
            userDao = db.userDao();
            termDao = db.termDao();
            courseDao = db.courseDao();
            assessmentDao = db.assessmentDao();
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            userDao.insert(new UserEntity("Undergrad", "Student", "One", "123 ABC Street", "1234567890", "UGRADSTUDENT@TEST.COM", "UGradStudent", "Undergraduate", "Business", "Undergraduate Business", 3.2));
            userDao.insert(new UserEntity("Graduate", "Student", "One", "321 CBA Street", "0987654321", "GRADSTUDENT@TEST.COM", "GradStudent", "Graduate", "Information Technology", "Graduate Information Technology", "Film"));
            termDao.insert(new TermEntity("Undergrad Test Term", today, today, 1));
            termDao.insert(new TermEntity("Empty Undergrad Term", today, today, 1));
            termDao.insert(new TermEntity("Grad Test Term", today, today, 2));
            termDao.insert(new TermEntity("Empty Grad Term", today, today, 2));
            courseDao.insert(new CourseEntity("Current Course", today, today, "In Progress", "Test Teacher",
                    "0123456789", "TestTeacher@test.com", 1));
            courseDao.insert(new CourseEntity("Upcoming Course", today, today, "Plan To Take", "Test Teacher",
                    "0123456789", "TestTeacher@test.com", 1));
            courseDao.insert(new CourseEntity("Completed Course", today, today, "Completed", "Test Teacher",
                    "0123456789", "TestTeacher@test.com", 1));
            courseDao.insert(new CourseEntity("Current Course", today, today, "In Progress", "Test Teacher",
                    "0123456789", "TestTeacher@test.com", 3));
            courseDao.insert(new CourseEntity("Upcoming Course", today, today, "Plan To Take", "Test Teacher",
                    "0123456789", "TestTeacher@test.com", 3));
            courseDao.insert(new CourseEntity("Completed Course", today, today, "Completed", "Test Teacher",
                    "0123456789", "TestTeacher@test.com", 3));
            noteDao.insert(new NoteEntity("Test Note 1", "This is the a test note.", 1));
            noteDao.insert(new NoteEntity("Test Note 2", "This is a second test note", 1));
            noteDao.insert(new NoteEntity("Test Note 1", "This is the a test note.", 4));
            noteDao.insert(new NoteEntity("Test Note 2", "This is a second test note", 4));
            assessmentDao.insert(new AssessmentEntity("Assessment 1", today, "This is the first Assessment", "Objective Assessment", 1));
            assessmentDao.insert(new AssessmentEntity("Assessment 2", today, "This is the second Assessment", "Performance Assessment", 1));
            assessmentDao.insert(new AssessmentEntity("Assessment 3", today, "This is the third Assessment", "Performance Assessment", 1));
            assessmentDao.insert(new AssessmentEntity("Assessment 4", today, "This is the fourth Assessment", "Objective Assessment", 1));
            assessmentDao.insert(new AssessmentEntity("Assessment 5", today, "This is the fifth Assessment", "Objective Assessment", 1));
            assessmentDao.insert(new AssessmentEntity("Assessment 1", today, "This is the first Assessment", "Objective Assessment", 3));
            assessmentDao.insert(new AssessmentEntity("Assessment 2", today, "This is the second Assessment", "Performance Assessment", 3));
            assessmentDao.insert(new AssessmentEntity("Assessment 3", today, "This is the third Assessment", "Performance Assessment", 3));
            assessmentDao.insert(new AssessmentEntity("Assessment 4", today, "This is the fourth Assessment", "Objective Assessment", 3));
            assessmentDao.insert(new AssessmentEntity("Assessment 5", today, "This is the fifth Assessment", "Objective Assessment", 3));
            assessmentDao.insert(new AssessmentEntity("Assessment 1", today, "This is the first Assessment", "Objective Assessment", 4));
            assessmentDao.insert(new AssessmentEntity("Assessment 2", today, "This is the second Assessment", "Performance Assessment", 4));
            assessmentDao.insert(new AssessmentEntity("Assessment 3", today, "This is the third Assessment", "Performance Assessment", 4));
            assessmentDao.insert(new AssessmentEntity("Assessment 4", today, "This is the fourth Assessment", "Objective Assessment", 4));
            assessmentDao.insert(new AssessmentEntity("Assessment 5", today, "This is the fifth Assessment", "Objective Assessment", 4));
            assessmentDao.insert(new AssessmentEntity("Assessment 1", today, "This is the first Assessment", "Objective Assessment", 6));
            assessmentDao.insert(new AssessmentEntity("Assessment 2", today, "This is the second Assessment", "Performance Assessment", 6));
            assessmentDao.insert(new AssessmentEntity("Assessment 3", today, "This is the third Assessment", "Performance Assessment", 6));
            assessmentDao.insert(new AssessmentEntity("Assessment 4", today, "This is the fourth Assessment", "Objective Assessment", 6));
            assessmentDao.insert(new AssessmentEntity("Assessment 5", today, "This is the fifth Assessment", "Objective Assessment", 6));
            return null;
        }
    }
}