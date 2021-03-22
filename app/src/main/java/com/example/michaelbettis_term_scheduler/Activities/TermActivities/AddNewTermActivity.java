package com.example.michaelbettis_term_scheduler.Activities.TermActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelbettis_term_scheduler.Activities.MainActivity;
import com.example.michaelbettis_term_scheduler.utils.Helper;
import com.example.michaelbettis_term_scheduler.utils.DatePickerFragment;
import com.example.michaelbettis_term_scheduler.R;

import androidx.annotation.NonNull;
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

public class AddNewTermActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private int termId;
    private int userId;
    private int viewId;
    private EditText editTextTermName;
    private TextView textViewTermStart;
    private TextView textViewTermEnd;
    private TermViewModel termViewModel;
    private Button saveTermBtn;
    private long lastTermEndDate = System.currentTimeMillis();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Term");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //sets the values of the input data to variables
        editTextTermName = findViewById(R.id.term_name);
        textViewTermStart = findViewById(R.id.term_start);
        textViewTermEnd = findViewById(R.id.term_end);
        saveTermBtn = findViewById(R.id.save_term);

        //Gets intent data and checks if it has an id value or not, if it does, it changes the title
        //text to edit term, otherwise it stays as edit term
        Intent intent = getIntent();
        userId = getIntent().getIntExtra(Helper.USER_ID, -1);
        termId = getIntent().getIntExtra(Helper.TERM_ID, -1);

        if (intent.hasExtra(Helper.TERM_ID)) {
            getSupportActionBar().setTitle("Edit Term");
            SchedulerDatabase db = SchedulerDatabase.getInstance(getApplicationContext());
            lastTermEndDate = db.termDao().getLastEndDate(userId);
            editTextTermName.setText(intent.getStringExtra(Helper.TERM_NAME));
            textViewTermStart.setText(Helper.sdf(intent.getStringExtra(Helper.TERM_START)));
            textViewTermEnd.setText(Helper.sdf(intent.getStringExtra(Helper.TERM_END)));
            try {
                lastTermEndDate = Helper.shortStringToLong(textViewTermStart.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //sets the onClickListener for the date textViews
        textViewTermStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTermDateRange(view, lastTermEndDate + Helper.DAY_IN_MILLIS, -1);

            }
        });
        textViewTermEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setTermDateRange(view,
                            Helper.shortStringToLong(textViewTermStart.getText().toString()) + (Helper.DAY_IN_MILLIS)
                            , Helper.shortStringToLong(textViewTermStart.getText().toString()) + (90 * Helper.DAY_IN_MILLIS));
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
        String name = editTextTermName.getText().toString();
        String startDate = textViewTermStart.getText().toString();
        String endDate = textViewTermEnd.getText().toString();

        //check to see if all fields have a value and are not blank spaces
        if (name.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a value in all fields", Toast.LENGTH_SHORT).show();
        } else {

            //checks to see if there is a term id or not, if so updates a term if not adds a term
            TermEntity term = null;
            if (termId == -1) {
                try {
                    term = new TermEntity(name, Helper.stringToDate(startDate), Helper.stringToDate(endDate), userId);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                termViewModel.insert(term);
                Toast.makeText(this, "Term Added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddNewTermActivity.this, TermListActivity.class);
                startActivity(intent);
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

            Intent intent = new Intent(AddNewTermActivity.this, TermListActivity.class);
            intent.putExtra(Helper.TERM_ID, termId);
            intent.putExtra(Helper.USER_ID, userId);
            intent.putExtra(Helper.TERM_NAME, name);
            intent.putExtra(Helper.TERM_START, startDate);
            intent.putExtra(Helper.TERM_END, endDate);
            startActivity(intent);
        }

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