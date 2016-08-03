package model;

/**
 * Created by victorm on 4/18/16.
 */
public class Readinginfo {
    private int id;
    private String name;
    private double glucose;
    private long date;
    private String note;

    public Readinginfo(String name, double glucose, String note, long date) {
        this.name = name;
        this.glucose = glucose;
        this.note = note;
        this.date = date;
    }

    public Readinginfo(){

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getGlucose() {
        return glucose;
    }

    public long getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGlucose(double glucose) {
        this.glucose = glucose;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
