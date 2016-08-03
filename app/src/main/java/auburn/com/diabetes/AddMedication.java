package auburn.com.diabetes;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import data.ReadingDatabaseHandler;
import model.Medicationinfo;

public class AddMedication extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{
    //interval hours
    private final int[] intervalValue = {0,1,8,24,168};
    private Spinner intervalspinner;

    private EditText editname;
    private EditText editform;
    private EditText editdosage;
    private EditText editunit;
    private EditText editnote;
    private TextView inputDate;
    private TextView inputTime;

    private Calendar mycalendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private ReadingDatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
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
        inputDate.setOnClickListener(this);
        inputTime.setOnClickListener(this);

        db = new ReadingDatabaseHandler(this);
        mycalendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());


        List<String> categories = new ArrayList<String>();
        categories.add("Don't remind me to take medication");
        categories.add("Take the Medication Every Hour");
        categories.add("Every 8 Hour");
        categories.add("Every Day");
        categories.add("Week");
        categories.add("Month");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalspinner.setAdapter(dataAdapter);

        update();
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
            db.addMedication(new Medicationinfo(name,form,dosage,unit,note,date,interval,0,0));

            //TODO implement alert receiver
            if(intervalspinner.getSelectedItemPosition() == 1){
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle("Medication Reminder").setContentText("It's time to take " + name + "\n Form: "+ form + " \nDosage: " + Double.toString(dosage) + " "+unit )
                        .setTicker("Diabetes Medication")
                        .setSmallIcon(R.drawable.ic_medication);
                notificationBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
                NotificationManager notificationManager;
                notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1,notificationBuilder.build());
            }
        }
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("fragment", "medication");
        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }
}
