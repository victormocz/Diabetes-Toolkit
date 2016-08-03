package auburn.com.diabetes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import data.ReadingDatabaseHandler;
import model.Medicationinfo;

public class MedicationDetail extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    private final int[] intervalValue = {0,1,8,24,168};
    private Spinner intervalspinner;

    private EditText editname;
    private EditText editform;
    private EditText editdosage;
    private EditText editunit;
    private EditText editnote;
    private TextView inputDate;
    private TextView inputTime;
    private Button deleteButton;

    private Calendar mycalendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private ReadingDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeComponent();
    }

    private void initializeComponent() {
        editname = (EditText) findViewById(R.id.name);
        editform = (EditText) findViewById(R.id.form);
        editdosage = (EditText) findViewById(R.id.dosage);
        editunit = (EditText) findViewById(R.id.unit);
        editnote = (EditText) findViewById(R.id.note);
        intervalspinner = (Spinner) findViewById(R.id.interval);
        inputDate = (TextView) findViewById(R.id.input_date);
        inputTime = (TextView) findViewById(R.id.input_time);
        deleteButton = (Button) findViewById(R.id.delete);
        inputDate.setOnClickListener(this);
        inputTime.setOnClickListener(this);

        int id = getIntent().getIntExtra("id",0);
        String name = getIntent().getStringExtra("name");
        String form = getIntent().getStringExtra("form");
        double dosage = getIntent().getDoubleExtra("dosage",0.0);
        String units = getIntent().getStringExtra("units");
        String note = getIntent().getStringExtra("note");
        Long startdate = getIntent().getLongExtra("startdate",0);
        int interval = getIntent().getIntExtra("interval",0);

        editname.setText(name);
        editform.setText(form);
        editdosage.setText(Double.toString(dosage));
        editunit.setText(units);
        editnote.setText(note);

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        db = new ReadingDatabaseHandler(this);
        mycalendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        inputDate.setText(dateFormat.format(new Date(startdate)));
        inputTime.setText(timeFormat.format(new Date(startdate)));

        List<String> categories = new ArrayList<String>();
        categories.add("Don't remind me to take medication");
        categories.add("Take the Medication Every Hour");
        categories.add("Take the Medication Every 8 Hour");
        categories.add("Take the Medication Every Day");
        categories.add("Take the Medication Every Week");
        categories.add("Take the Medication Every Month");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteMedication(getIntent().getIntExtra("id", 0));
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("fragment", "medication");
                startActivity(intent);
            }
        });
        intervalspinner.setAdapter(dataAdapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            String name = editname.getText().toString();
            String form = editform.getText().toString();
            double dosage = 0.0;
            String unit = editunit.getText().toString();
            String note = editnote.getText().toString();
            int interval = intervalValue[intervalspinner.getSelectedItemPosition()];

            Long date = mycalendar.getTimeInMillis();

            if (name == null || name.length() <= 1) {
                Toast.makeText(getApplicationContext(),"Name's length should be greater than 1",Toast.LENGTH_SHORT).show();
                return false;
            }
            if (form == null || form.length() <= 1) {
                Toast.makeText(getApplicationContext(),"Form's length should be greater than 1",Toast.LENGTH_SHORT).show();
                return false;
            }
            try{
                dosage = Double.parseDouble(editdosage.getText().toString());
            } catch (NumberFormatException ex) {
                Toast.makeText(getApplicationContext(),"Dosage must be a number",Toast.LENGTH_SHORT).show();
                return false;
            }
            //TODO
            long lastdate = getIntent().getLongExtra("lastdate",0);
            int istaken = getIntent().getIntExtra("istaken",0);
            db.updateMedication(getIntent().getIntExtra("id",0),new Medicationinfo(name,form,dosage,unit,note,date,interval,lastdate,istaken));

        }
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("fragment", "medication");
        startActivity(intent);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
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
}
