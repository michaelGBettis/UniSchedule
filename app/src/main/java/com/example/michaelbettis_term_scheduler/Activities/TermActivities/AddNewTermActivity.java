package com.example.michaelbettis_term_scheduler.Activities.TermActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.DatePickerFragment;
import com.example.michaelbettis_term_scheduler.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import com.example.michaelbettis_term_scheduler.Entities.TermEntity;
import com.example.michaelbettis_term_scheduler.utils.SchedulerDatabase;
import com.example.michaelbettis_term_scheduler.ViewModel.TermViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewTermActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private int termId;
    private int userId;
    private int viewId;
    private TextInputLayout termNameTextInput;
    private TextInputLayout termStartTextInput;
    private TextInputLayout termEndTextInput;
    private TextInputEditText termStartEditText;
    private TermViewModel termViewModel;
    private long lastTermEndDate = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_term);

        //======================================Hooks=============================================//

        termNameTextInput = findViewById(R.id.term_name);
        termStartTextInput = findViewById(R.id.term_start);
        termEndTextInput = findViewById(R.id.term_end);
        termStartEditText = findViewById(R.id.term_start_text);
        TextInputEditText termEndEditText = findViewById(R.id.term_end_text);
        Button saveTermBtn = findViewById(R.id.save_term);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //======================================Tool Bar==========================================//

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Term");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //===============================Setting Intent Values====================================//

        Intent intent = getIntent();
        userId = getIntent().getIntExtra(Helper.USER_ID, -1);
        termId = getIntent().getIntExtra(Helper.TERM_ID, -1);

        if (intent.hasExtra(Helper.TERM_ID)) {
            getSupportActionBar().setTitle("Edit Term");
            SchedulerDatabase db = SchedulerDatabase.getInstance(getApplicationContext());
            lastTermEndDate = db.termDao().getLastEndDate(userId);
            termNameTextInput.getEditText().setText(intent.getStringExtra(Helper.TERM_NAME));
            termStartEditText.setText(Helper.sdf(intent.getStringExtra(Helper.TERM_START)));
            termEndEditText.setText(Helper.sdf(intent.getStringExtra(Helper.TERM_END)));
            try {
                lastTermEndDate = Helper.shortStringToLong(termEndEditText.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //====================================Buttons=============================================//

         termStartEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTermDateRange(view, lastTermEndDate + Helper.DAY_IN_MILLIS, -1);

            }
        });

        termEndEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setTermDateRange(view,
                            Helper.shortStringToLong(termStartEditText.getText().toString()) + (Helper.DAY_IN_MILLIS)
                            , Helper.shortStringToLong(termStartEditText.getText().toString()) + (90 * Helper.DAY_IN_MILLIS));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        saveTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTerm();
            }
        });

        //creates or provides a view model instance
        termViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TermViewModel.class);

    }

    //method for the save button

    private void saveTerm() {
        termId = getIntent().getIntExtra(Helper.TERM_ID, -1);
        String name = termNameTextInput.getEditText().getText().toString();
        String startDate = termStartTextInput.getEditText().getText().toString();
        String endDate = termEndTextInput.getEditText().getText().toString();
        TermEntity term = null;

        //check to see if all fields have a value and are not blank spaces
        if (Helper.isInputEmpty(termNameTextInput) | Helper.isInputEmpty(termStartTextInput) | Helper.isInputEmpty(termEndTextInput)) {
            return;
        }

        //checks to see if there is a term id or not,
        // if no term id is found, insert the new term...
        if (termId == -1) {

            try {
                term = new TermEntity(name, Helper.stringToDate(startDate), Helper.stringToDate(endDate), userId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            termViewModel.insert(term);
            Toast.makeText(this, "Term Added", Toast.LENGTH_SHORT).show();

            //If a term id is found, update the term with the new info.
        } else {

            try {
                term = new TermEntity(name, Helper.stringToDate(startDate), Helper.stringToDate(endDate), userId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(term).setTerm_id(termId);
            termViewModel.update(term);
            Toast.makeText(this, "Term updated", Toast.LENGTH_SHORT).show();

        }

        Helper.goToTerms(userId, this);
    }


    //sets the text view text to the selected date
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String selectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        TextView textView = findViewById(viewId);
        textView.setText(selectedDate);

    }

    private void setTermDateRange(View view, long startOfDateRange, long endOfDateRange) {
        Bundle currentDate = new Bundle();
        DialogFragment datePicker = new DatePickerFragment();

        currentDate.putLong("setStartDate", startOfDateRange);
        currentDate.putLong("setEndDate", endOfDateRange);
        datePicker.setArguments(currentDate);
        datePicker.show(getSupportFragmentManager(), "Date Picker");
        viewId = view.getId();
    }

    //sets the action for the back button and displays a message
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Toast.makeText(this, "Term not Added", Toast.LENGTH_SHORT).show();
        return true;
    }
}