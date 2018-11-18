package controller;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import models.TimesheetRow;

/**
 * TimesheetRow which has fields indicating whether is is
 * editable or not.
 * @author Tony
 * @version 1
 *
 */
public class EditableRow implements Serializable {

    /** Maximum number accepted for hours in a day. */
    public static final BigDecimal HOURS_IN_DAY = new BigDecimal(24);
    
    /** Number of days in a week, used for validation.*/
    public static final int DAYS_IN_WEEK = 7;

    /** Holds timesheet row to be displayed, edited or deleted.*/
    private TimesheetRow row;
    
    /**
     * Constructor setting Timesheet row variable.
     * @param model row parameter
     */
    public EditableRow(TimesheetRow model) {
        this.setRow(model);
    }

    /**
     * Returns member row.
     * @return the row
     */
    public TimesheetRow getRow() {
        return row;
    }

    /**
     * Sets member row to input row.
     * @param row the row to set
     */
    public void setRow(TimesheetRow row) {
        this.row = row;
    }
    
    /**
     * Checks if hour value is out of the valid
     * bounds of 0.0 to 24.0, or has more than one decimal digit.
     *
     *@param hour the value to check
     */
    @SuppressWarnings("unused")
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
     * Adds total hours for this timesheet row.
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
     * Displays a row edited message to the user.
     * @param event the row edit event 
     */
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Row Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    /**
     * Displays a row edit cancelled message to the user.
     * @param event the row cancelled event
     */
    
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
}
