package model;

/**
 * Created by victormo on 7/17/2016.
 */
public class Medicationinfo {
    private int id;
    private String name;
    private String form;
    private double dosage;
    private String units;
    private String note;
    private long startdate;
    private int interval;
    private long lasttakendate;
    private int isTaken;

    public Medicationinfo(String name, String form, double dosage, String units, String note, long startdate, int interval, long lasttakendate, int isTaken) {
        this.name = name;
        this.form = form;
        this.dosage = dosage;
        this.units = units;
        this.note = note;
        this.startdate = startdate;
        this.interval = interval;
        this.lasttakendate = lasttakendate;
        this.isTaken = isTaken;
    }

    public Medicationinfo() {
        ;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setLasttakendate(long lasttakendate) {
        this.lasttakendate = lasttakendate;
    }

    public void setIsTaken(int isTaken) {
        this.isTaken = isTaken;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getForm() {
        return form;
    }

    public double getDosage() {
        return dosage;
    }

    public String getUnits() {
        return units;
    }

    public String getNote() {
        return note;
    }

    public long getStartdate() {
        return startdate;
    }

    public int getInterval() {
        return interval;
    }

    public long getLasttakendate() {
        return lasttakendate;
    }

    public int getIsTaken() {
        return isTaken;
    }
}
