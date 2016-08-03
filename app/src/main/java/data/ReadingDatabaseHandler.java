package data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import auburn.com.diabetes.Medication;
import model.Medicationinfo;
import model.Question;
import model.Readinginfo;

/**
 * Created by paulodichone on 2/16/15.
 */
public class ReadingDatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<Readinginfo> readingList = new ArrayList<>();
    public ReadingDatabaseHandler(Context context) {
        super(context, ReadingConstants.DATABASE_NAME, null, ReadingConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create our table
        String CREATE_READING_TABLE = "CREATE TABLE " + ReadingConstants.TABLE_NAME + "("
                + ReadingConstants.KEY_ID + " INTEGER PRIMARY KEY, " + ReadingConstants.TITLE_NAME +
                " TEXT, " + ReadingConstants.GLOCUSE_NUM + " DOUBLE, " + ReadingConstants.DATE_NAME + " LONG, " +
                ReadingConstants.NOTE_CONTENT + " TEXT);";

        String CREATE_MED_TABLE = "CREATE TABLE " + MedicationConstants.TABLE_NAME + "("
                + MedicationConstants.MED_ID + " INTEGER PRIMARY KEY, " + MedicationConstants.MED_NAME +
                " TEXT, " + MedicationConstants.MED_FORM + " TEXT, " + MedicationConstants.MED_DOSAGE + " DOUBLE, " +
                MedicationConstants.MED_UNITS + " TEXT, " +  MedicationConstants.MED_NOTES + " TEXT, " +
                MedicationConstants.MED_STARTDATE + " LONG, " +  MedicationConstants.MED_LASTTAKENDATE + " LONG, " +
                MedicationConstants.MED_INTERVAL + " INTEGER, " + MedicationConstants.MED_ISTAKEN + " INTEGER);";

        db.execSQL(CREATE_READING_TABLE);
        db.execSQL(CREATE_MED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReadingConstants.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MedicationConstants.TABLE_NAME);
        Log.v("ONUPGRADE", "DROPING TABLES AND CREATING NEW ONE!");
        //create a new one
        onCreate(db);
    }



    public void deleteReading(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ReadingConstants.TABLE_NAME, ReadingConstants.KEY_ID + " = ? ",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteMedication(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MedicationConstants.TABLE_NAME, MedicationConstants.MED_ID + " = ? ",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateReading(int id, Readinginfo info) {
        SQLiteDatabase db = this.getWritableDatabase();
        String UPDATE = "UPDATE " + ReadingConstants.TABLE_NAME + " SET " + ReadingConstants.TITLE_NAME + " = '" + info.getName() +"',"+
                ReadingConstants.GLOCUSE_NUM + " = " + info.getGlucose() + ", " + ReadingConstants.DATE_NAME + " = " + info.getDate() +
                ", " + ReadingConstants.NOTE_CONTENT + " = '" + info.getNote() + "' WHERE " + ReadingConstants.KEY_ID + " = " + id;
        db.execSQL(UPDATE);
        db.close();
    }

    public void updateMedication(int id, Medicationinfo info) {
        SQLiteDatabase db = this.getWritableDatabase();
        String UPDATE = "UPDATE " + MedicationConstants.TABLE_NAME + " SET " + MedicationConstants.MED_NAME + " = '" + info.getName() +"',"+
                MedicationConstants.MED_FORM + " = '" + info.getForm() + "', " + MedicationConstants.MED_DOSAGE + " = '" + info.getDosage() +
                "', " + MedicationConstants.MED_UNITS + " = '" + info.getUnits() +
                "', " + MedicationConstants.MED_NOTES + " = '" + info.getNote() +
                "', " + MedicationConstants.MED_STARTDATE + " = " + info.getStartdate() +
                ", " + MedicationConstants.MED_INTERVAL + " = " + info.getInterval() +
                ", "+ MedicationConstants.MED_LASTTAKENDATE + " = " + info.getLasttakendate() +
                ", " + MedicationConstants.MED_ISTAKEN + " = " + info.getIsTaken() + " WHERE " + MedicationConstants.MED_ID + " = " + id;

        db.execSQL(UPDATE);
        db.close();
    }


    public void addReadings(Readinginfo info) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReadingConstants.TITLE_NAME, info.getName());
        values.put(ReadingConstants.GLOCUSE_NUM, info.getGlucose());
        values.put(ReadingConstants.DATE_NAME, info.getDate());
        values.put(ReadingConstants.NOTE_CONTENT, info.getNote());
        db.insert(ReadingConstants.TABLE_NAME, null, values);
        db.close();
    }

    public void addMedication(Medicationinfo info) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MedicationConstants.MED_NAME, info.getName());
        values.put(MedicationConstants.MED_FORM, info.getForm());
        values.put(MedicationConstants.MED_DOSAGE, info.getDosage());
        values.put(MedicationConstants.MED_UNITS, info.getUnits());
        values.put(MedicationConstants.MED_NOTES, info.getNote());
        values.put(MedicationConstants.MED_STARTDATE, info.getStartdate());
        values.put(MedicationConstants.MED_INTERVAL, info.getInterval());
        values.put(MedicationConstants.MED_LASTTAKENDATE, info.getLasttakendate());
        values.put(MedicationConstants.MED_ISTAKEN, info.getIsTaken());

        db.insert(MedicationConstants.TABLE_NAME, null, values);
        db.close();
    }

    public double getAvg() {
        String selectQuery = "SELECT avg(glocuse) FROM " + ReadingConstants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        double average = 0.0;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()) {
            average = cursor.getDouble(cursor.getColumnIndex("avg(glocuse)"));
        }
        return average;
    }

    public ArrayList<Readinginfo> getReadingList() {

        readingList.clear();

        String selectQuery = "SELECT * FROM " + ReadingConstants.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
       // Cursor cursor = db.rawQuery(selectQuery, null);

        Cursor cursor = db.query(ReadingConstants.TABLE_NAME, new String[]{ReadingConstants.KEY_ID, ReadingConstants.TITLE_NAME, ReadingConstants.GLOCUSE_NUM, ReadingConstants.DATE_NAME, ReadingConstants.NOTE_CONTENT},null,null, null,null, ReadingConstants.DATE_NAME+ " ASC");

        if (cursor.moveToFirst()) {

            do {
                Readinginfo row = new Readinginfo();
                row.setName(cursor.getString(cursor.getColumnIndex(ReadingConstants.TITLE_NAME)));
                row.setGlucose(cursor.getDouble(cursor.getColumnIndex(ReadingConstants.GLOCUSE_NUM)));
                row.setId(cursor.getInt(cursor.getColumnIndex(ReadingConstants.KEY_ID)));
                row.setNote(cursor.getString(cursor.getColumnIndex(ReadingConstants.NOTE_CONTENT)));

                row.setDate(cursor.getLong(cursor.getColumnIndex(ReadingConstants.DATE_NAME)));

                readingList.add(row);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return readingList;

    }

    //TODO
    public ArrayList<Medicationinfo> getMedicationList() {

        ArrayList<Medicationinfo> medicationList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + MedicationConstants.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor cursor = db.rawQuery(selectQuery, null);

        Cursor cursor = db.query(MedicationConstants.TABLE_NAME, new String[]{MedicationConstants.MED_ID, MedicationConstants.MED_NAME, MedicationConstants.MED_FORM, MedicationConstants.MED_DOSAGE, MedicationConstants.MED_UNITS,
                MedicationConstants.MED_NOTES,MedicationConstants.MED_STARTDATE,MedicationConstants.MED_INTERVAL,MedicationConstants.MED_LASTTAKENDATE,MedicationConstants.MED_ISTAKEN},null,null, null,null, MedicationConstants.MED_STARTDATE+ " DESC");

        if (cursor.moveToFirst()) {

            do {
                Medicationinfo row = new Medicationinfo();
                row.setId(cursor.getInt(cursor.getColumnIndex(MedicationConstants.MED_ID)));
                row.setName(cursor.getString(cursor.getColumnIndex(MedicationConstants.MED_NAME)));
                row.setForm(cursor.getString(cursor.getColumnIndex(MedicationConstants.MED_FORM)));
                row.setDosage(cursor.getDouble(cursor.getColumnIndex(MedicationConstants.MED_DOSAGE)));
                row.setUnits(cursor.getString(cursor.getColumnIndex(MedicationConstants.MED_UNITS)));
                row.setNote(cursor.getString(cursor.getColumnIndex(MedicationConstants.MED_NOTES)));
                row.setStartdate(cursor.getLong(cursor.getColumnIndex(MedicationConstants.MED_STARTDATE)));
                row.setInterval(cursor.getInt(cursor.getColumnIndex(MedicationConstants.MED_INTERVAL)));
                row.setLasttakendate(cursor.getLong(cursor.getColumnIndex(MedicationConstants.MED_LASTTAKENDATE)));
                row.setIsTaken(cursor.getInt(cursor.getColumnIndex(MedicationConstants.MED_ISTAKEN)));

                medicationList.add(row);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return medicationList;

    }


}
