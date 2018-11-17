package controller;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import models.TimesheetRow;

public class EditableRow implements Serializable {

    public static final BigDecimal HOURS_IN_DAY = new BigDecimal(24);
    public static final int DAYS_IN_WEEK = 7;
    
    private boolean editable;
    
    /** Holds timesheet row to be displayed, edited or deleted.*/
    private TimesheetRow row;
    
    public EditableRow (TimesheetRow model){
        this.setRow(model);
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return the row
     */
    public TimesheetRow getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(TimesheetRow row) {
        this.row = row;
    }
    
    
    public String toggleEditable() {
        editable = !editable;
        return null;
    }
    
    /**
     * Checks if hour value is out of the valid
     * bounds of 0.0 to 24.0, or has more than one decimal digit.
     *
     *@param hour the value to check
     */
    private void checkHour(final BigDecimal hour) {
        if (hour != null) {
            if (hour.compareTo(HOURS_IN_DAY) > 0.0
                    || hour.compareTo(BigDecimal.ZERO) < 0.0) {
                throw new IllegalArgumentException(
                       "out of range: should be between 0 and 24");
            }
            if (hour.scale() > 1) {
                throw new IllegalArgumentException(
                        "too many decimal digits: should be at most 1");
            }
        }
    }
    
    /**
     * Checks if any hour value in any day of the week is out of the valid
     * bounds of 0.0 to 24.0, or has more than one decimal digit.
     *
     * @param hours array of hours charged for each day in a week
     */
    private void checkHoursForWeek(final BigDecimal[] hours) {
        if (hours.length != DAYS_IN_WEEK) {
            throw new IllegalArgumentException(
                    "wrong week length: should be 7");
        }
        for (BigDecimal next : hours) {
            checkHour(next);
        }
    }
    
    /**
     * adds total hours for this timesheet row.
     * @return the weekly hours
     */
    public BigDecimal getSum() {
         return row.getMon_hours()
                 .add(row.getTue_hours())
                 .add(row.getWed_hours())
                 .add(row.getThu_hours())
                 .add(row.getFri_hours())
                 .add(row.getSat_hours())
                 .add(row.getSun_hours());
    }
    
    /**
     * sets the hours for each day in the week when the row edit event occurs
     * also displays a row edited message to the user.
     * @param event the row edit event 
     */
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Row Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        /*
        setHour(SAT, satHrs);
        setHour(SUN, sunHrs);
        setHour(MON, monHrs);
        setHour(TUE, tueHrs);
        setHour(WED, wedHrs);
        setHour(THU, thuHrs);
        setHour(FRI, friHrs);
        */
    }
    
    /**
     * displays a row edit cancelled message to the user.
     * @param event the row cancelled event
     */
    
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
}
