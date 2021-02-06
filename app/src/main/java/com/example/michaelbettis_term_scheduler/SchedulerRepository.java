package com.example.michaelbettis_term_scheduler;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

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

public class SchedulerRepository {

    public final UserDao userDao;
    public final TermDao termDao;
    public final CourseDao courseDao;
    public final NoteDao noteDao;
    public final AssessmentDao assessmentDao;
    private LiveData<List<UserEntity>> allUsers;
    private final LiveData<List<TermEntity>> allTerms;
    private final LiveData<List<TermEntity>> associatedTerms;
    private final LiveData<List<CourseEntity>> allCourses;
    private final LiveData<List<CourseEntity>> associatedCourses;
    private final LiveData<List<NoteEntity>> allNotes;
    private final LiveData<List<NoteEntity>> associatedNotes;
    private final LiveData<List<AssessmentEntity>> allAssessments;
    private final LiveData<List<AssessmentEntity>> associatedAssessments;
    private int termID;
    private int courseID;
    private int userID;

    public SchedulerRepository(Application application) {
        SchedulerDatabase database = SchedulerDatabase.getInstance(application);
        userDao = database.userDao();
        termDao = database.termDao();
        courseDao = database.courseDao();
        noteDao = database.noteDao();
        assessmentDao = database.assessmentDao();
        allTerms = termDao.getAllTerms();
        associatedTerms = termDao.getAllAssociatedTerms(userID);
        allCourses = courseDao.getAllCourses();
        associatedCourses = courseDao.getAllAssociatedCourses(termID);
        allNotes = noteDao.getAllNotes();
        associatedNotes = noteDao.getAllAssociatedNotes(courseID);
        allAssessments = assessmentDao.getAllAssessments();
        associatedAssessments = assessmentDao.getAllAssociatedAssessments(courseID);

    }

    //========================================USERS===============================================//
    public void insert(UserEntity user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void update(UserEntity user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    public void delete(UserEntity user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        return allUsers;
    }

    private static class InsertUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private final UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            userDao.insert(userEntities[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private final UserDao userDao;

        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            userDao.update(userEntities[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private final UserDao userDao;

        private DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            userDao.delete(userEntities[0]);
            return null;
        }
    }

    //========================================TERMS===============================================//

    public void insert(TermEntity term) {
        new InsertTermAsyncTask(termDao).execute(term);
    }

    public void update(TermEntity term) {
        new UpdateTermAsyncTask(termDao).execute(term);
    }

    public void delete(TermEntity term) {
        new DeleteTermAsyncTask(termDao).execute(term);
    }

    public void deleteAllTerms() {
        new DeleteAllTermAsyncTask(termDao).execute();
    }

    public LiveData<List<TermEntity>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<TermEntity>> getAssociatedTerms() {
        return associatedTerms;
    }

    private static class InsertTermAsyncTask extends AsyncTask<TermEntity, Void, Void> {
        private final TermDao termDao;

        private InsertTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(TermEntity... termEntities) {
            termDao.insert(termEntities[0]);
            return null;
        }
    }

    private static class UpdateTermAsyncTask extends AsyncTask<TermEntity, Void, Void> {
        private final TermDao termDao;

        private UpdateTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(TermEntity... termEntities) {
            termDao.update(termEntities[0]);
            return null;
        }
    }

    private static class DeleteTermAsyncTask extends AsyncTask<TermEntity, Void, Void> {
        private final TermDao termDao;

        private DeleteTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(TermEntity... termEntities) {
            termDao.delete(termEntities[0]);
            return null;
        }
    }

    private static class DeleteAllTermAsyncTask extends AsyncTask<Void, Void, Void> {
        private final TermDao termDao;

        private DeleteAllTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.deleteAllTerms();
            return null;
        }
    }

    //=======================================COURSES==============================================//

    public void insert(CourseEntity course) {
        new InsertCourseAsyncTask(courseDao).execute(course);
    }

    public void update(CourseEntity course) {
        new UpdateCourseAsyncTask(courseDao).execute(course);
    }

    public void delete(CourseEntity course) {
        new DeleteCourseAsyncTask(courseDao).execute(course);
    }

    public LiveData<List<CourseEntity>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<CourseEntity>> getAssociatedCourses() {
        return associatedCourses;
    }

    private static class InsertCourseAsyncTask extends AsyncTask<CourseEntity, Void, Void> {
        private final CourseDao courseDao;

        private InsertCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(CourseEntity... courseEntities) {
            courseDao.insert(courseEntities[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<CourseEntity, Void, Void> {
        private final CourseDao courseDao;

        private UpdateCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(CourseEntity... courseEntities) {
            courseDao.update(courseEntities[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<CourseEntity, Void, Void> {
        private final CourseDao courseDao;

        private DeleteCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(CourseEntity... courseEntities) {
            courseDao.delete(courseEntities[0]);
            return null;
        }
    }

    //=======================================NOTES================================================//

    public void insert(NoteEntity note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(NoteEntity note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(NoteEntity note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<NoteEntity>> getAssociatedNotes() {
        return associatedNotes;
    }


    private static class InsertNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private final NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... NoteEntities) {
            noteDao.insert(NoteEntities[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private final NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... NoteEntities) {
            noteDao.update(NoteEntities[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private final NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... NoteEntities) {
            noteDao.delete(NoteEntities[0]);
            return null;
        }
    }

    //===================================Assessments==============================================//

    public void insert(AssessmentEntity assessment) {
        new InsertAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void update(AssessmentEntity assessment) {
        new UpdateAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void delete(AssessmentEntity assessment) {
        new DeleteAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public LiveData<List<AssessmentEntity>> getAllAssessments() {
        return allAssessments;
    }

    public LiveData<List<AssessmentEntity>> getAssociatedAssessments() {
        return associatedAssessments;
    }


    private static class InsertAssessmentAsyncTask extends AsyncTask<AssessmentEntity, Void, Void> {
        private final AssessmentDao assessmentDao;

        private InsertAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(AssessmentEntity... AssessmentEntities) {
            assessmentDao.insert(AssessmentEntities[0]);
            return null;
        }
    }

    private static class UpdateAssessmentAsyncTask extends AsyncTask<AssessmentEntity, Void, Void> {
        private final AssessmentDao assessmentDao;

        private UpdateAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(AssessmentEntity... AssessmentEntities) {
            assessmentDao.update(AssessmentEntities[0]);
            return null;
        }
    }

    private static class DeleteAssessmentAsyncTask extends AsyncTask<AssessmentEntity, Void, Void> {
        private final AssessmentDao assessmentDao;

        private DeleteAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(AssessmentEntity... AssessmentEntities) {
            assessmentDao.delete(AssessmentEntities[0]);
            return null;
        }
    }

}


