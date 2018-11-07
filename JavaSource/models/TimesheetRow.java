package models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="timesheet_row")
public class TimesheetRow implements Serializable {
    
    
    @Column(name="project_id")
    private int project_id;
    
    @Column(name="work_package")
    private String work_package;
    
    @Column(name="notes")
    private String notes;
    
    @Column(name="sun_hours")
    private int sun_hours;
    
    @Column(name="mon_hours")
    private int mon_hours;
    
    @Column(name="tue_hours")
    private int tue_hours;    
    
    @Column(name="wed_hours")
    private int wed_hours;    
    
    @Column(name="thu_hours")
    private int thu_hours;    
    
    @Column(name="fri_hours")
    private int fri_hours;    
    
    @Column(name="sat_hours")
    private int sat_hours;   
    
    @Column(name="timesheet_id")
    private int timesheet_id;
    
    @Id
    @Column(name="timesheet_row_id")
    private int timesheet_row_id;

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getWork_package() {
        return work_package;
    }

    public void setWork_package(String work_package) {
        this.work_package = work_package;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getSun_hours() {
        return sun_hours;
    }

    public void setSun_hours(int sun_hours) {
        this.sun_hours = sun_hours;
    }

    public int getMon_hours() {
        return mon_hours;
    }

    public void setMon_hours(int mon_hours) {
        this.mon_hours = mon_hours;
    }

    public int getTue_hours() {
        return tue_hours;
    }

    public void setTue_hours(int tue_hours) {
        this.tue_hours = tue_hours;
    }

    public int getWed_hours() {
        return wed_hours;
    }

    public void setWed_hours(int wed_hours) {
        this.wed_hours = wed_hours;
    }

    public int getThu_hours() {
        return thu_hours;
    }

    public void setThu_hours(int thu_hours) {
        this.thu_hours = thu_hours;
    }

    public int getFri_hours() {
        return fri_hours;
    }

    public void setFri_hours(int fri_hours) {
        this.fri_hours = fri_hours;
    }

    public int getSat_hours() {
        return sat_hours;
    }

    public void setSat_hours(int sat_hours) {
        this.sat_hours = sat_hours;
    }

    public int getTimesheet_id() {
        return timesheet_id;
    }

    public void setTimesheet_id(int timesheet_id) {
        this.timesheet_id = timesheet_id;
    }

    public int getTimesheet_row_id() {
        return timesheet_row_id;
    }

    public void setTimesheet_row_id(int timesheet_row_id) {
        this.timesheet_row_id = timesheet_row_id;
    }
    
    
    
}
