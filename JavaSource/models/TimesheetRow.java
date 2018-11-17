package models;

import java.io.Serializable;
import java.math.BigDecimal;

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
    private BigDecimal sun_hours;
    
    @Column(name="mon_hours")
    private BigDecimal mon_hours;
    
    @Column(name="tue_hours")
    private BigDecimal tue_hours;    
    
    @Column(name="wed_hours")
    private BigDecimal wed_hours;    
    
    @Column(name="thu_hours")
    private BigDecimal thu_hours;    
    
    @Column(name="fri_hours")
    private BigDecimal fri_hours;    
    
    @Column(name="sat_hours")
    private BigDecimal sat_hours;   
    
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

    public BigDecimal getSun_hours() {
        return sun_hours != null ? sun_hours : new BigDecimal(0);
    }

    public void setSun_hours(BigDecimal sun_hours) {
        this.sun_hours = sun_hours;
    }

    public BigDecimal getMon_hours() {
        return mon_hours != null ? mon_hours : new BigDecimal(0);
    }

    public void setMon_hours(BigDecimal mon_hours) {
        this.mon_hours = mon_hours;
    }

    public BigDecimal getTue_hours() {
        return tue_hours != null ? tue_hours : new BigDecimal(0);
    }

    public void setTue_hours(BigDecimal tue_hours) {
        this.tue_hours = tue_hours;
    }

    public BigDecimal getWed_hours() {
        return wed_hours != null ? wed_hours : new BigDecimal(0);
    }

    public void setWed_hours(BigDecimal wed_hours) {
        this.wed_hours = wed_hours;
    }

    public BigDecimal getThu_hours() {
        return thu_hours != null ? thu_hours : new BigDecimal(0);
    }

    public void setThu_hours(BigDecimal thu_hours) {
        this.thu_hours = thu_hours;
    }

    public BigDecimal getFri_hours() {
        return fri_hours != null ? fri_hours : new BigDecimal(0);
    }

    public void setFri_hours(BigDecimal fri_hours) {
        this.fri_hours = fri_hours;
    }

    public BigDecimal getSat_hours() {
        return sat_hours != null ? sat_hours : new BigDecimal(0);
    }

    public void setSat_hours(BigDecimal sat_hours) {
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
