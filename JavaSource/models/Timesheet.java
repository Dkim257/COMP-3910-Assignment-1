package models;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="timesheet")
public class Timesheet {
    
    @Column(name="emp_number")
    private int emp_number;
    
    @Column(name="end_week")
    private Date end_week;
    
    @Column(name="overtime")
    private BigDecimal overtime;
    
    @Column(name="flextime")
    private BigDecimal flextime;

    @Id
    @Column(name="timesheet_id")
    private int timesheet_id;

    public int getEmp_number() {
        return emp_number;
    }

    public void setEmp_number(int emp_number) {
        this.emp_number = emp_number;
    }

    public Date getEnd_week() {
        return end_week;
    }

    public void setEnd_week(Date end_week) {
        this.end_week = end_week;
    }

    public BigDecimal getOvertime() {
        return overtime;
    }

    public void setOvertime(BigDecimal overtime) {
        this.overtime = overtime;
    }

    public BigDecimal getFlextime() {
        return flextime;
    }

    public void setFlextime(BigDecimal flextime) {
        this.flextime = flextime;
    }

    public int getTimesheet_id() {
        return timesheet_id;
    }

    public void setTimesheet_id(int timesheet_id) {
        this.timesheet_id = timesheet_id;
    }
    
    
    
}
