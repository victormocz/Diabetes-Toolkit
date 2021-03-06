package auburn.com.diabetes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import data.ReadingDatabaseHandler;
import model.Readinginfo;

public class AddReading extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private TextView inputDate;
    private TextView inputTime;
    private EditText inputName;
    private EditText inputbg;
    private EditText inputNote;
    private Calendar mycalendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private ReadingDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String name = inputName.getText().toString();
        String glucosestr = inputbg.getText().toString();
        Double glucose = 0.0;
        String note = inputNote.getText().toString();
        Long date = mycalendar.getTimeInMillis();
        if (id == R.id.action_save) {
            if (name == null || name.length() <= 1) {
                Toast.makeText(getApplicationContext(),"Name's length should be greater than 1",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(glucosestr == null || glucosestr.length() <= 0 ) {
                Toast.makeText(getApplicationContext(),"Glucose's length should be greater than 1",Toast.LENGTH_SHORT).show();
                return false;
            }
            try{
                glucose = Double.parseDouble(glucosestr);
                if( glucose > 500 || glucose < 0){
                    Toast.makeText(getApplicationContext(),"Blood Glucose range should between 0 - 500(mg/dL)",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }catch (NumberFormatException ex) {
                Toast.makeText(getApplicationContext(),"Glucose's should be a number",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(note == null) {
                note = "";
            }
            db.addReadings(new Readinginfo(name,glucose,note,date));
        }
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("fragment", "reading");
        startActivity(intent);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.input_date) {
            DatePickerDialog.newInstance(this, mycalendar.get(Calendar.YEAR), mycalendar.get(Calendar.MONTH), mycalendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "Date Picker");
        } else if (id == R.id.input_time) {
            TimePickerDialog.newInstance(this, mycalendar.get(Calendar.HOUR_OF_DAY), mycalendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "Time Picker");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        mycalendar.set(year,monthOfYear,dayOfMonth);
        update();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mycalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mycalendar.set(Calendar.MINUTE, minute);
        update();
    }

    private void update() {
        inputDate.setText(dateFormat.format(mycalendar.getTime()));
        inputTime.setText(timeFormat.format(mycalendar.getTime()));
    }

    private void initializeComponents() {
        inputDate = (TextView) findViewById(R.id.input_date);
        inputTime = (TextView) findViewById(R.id.input_time);
        inputName = (EditText) findViewById(R.id.input_name);
        inputbg = (EditText) findViewById(R.id.input_bg);
        inputNote = (EditText) findViewById(R.id.input_note);

        mycalendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        inputDate.setOnClickListener(this);
        inputTime.setOnClickListener(this);
        db = new ReadingDatabaseHandler(getApplicationContext());
        update();
    }
}
