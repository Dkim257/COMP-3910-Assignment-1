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
     * Adds total hours for this timesheet row.
     * @return the weekly hours
     */
    public BigDecimal getSum() {
         return row.getMonHours()
                 .add(row.getTueHours())
                 .add(row.getWedHours())
                 .add(row.getThuHours())
                 .add(row.getFriHours())
                 .add(row.getSatHours())
                 .add(row.getSunHours());
    }
    
    /**
     * Displays a row edited message to the user.
     * @param event the row edit event 
     */
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Success", "Row Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    /**
     * Displays a row edit cancelled message to the user.
     * @param event the row cancelled event
     */
    
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Cancel", "Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }   
}
