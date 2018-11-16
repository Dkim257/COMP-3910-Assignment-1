package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import access.TimesheetManager;
import access.TimesheetRowManager;
import models.Timesheet;
import models.TimesheetRow;

@Named("row")
@SessionScoped
public class TimesheetRowFormAccess implements Serializable {

    /** Timesheet row index for Saturday. */
    public static final int SAT = 0;
    /** Timesheet row index for Sunday. */
    public static final int SUN = 1;
    /** Timesheet row index for Monday. */
    public static final int MON = 2;
    /** Timesheet row index for Tuesday. */
    public static final int TUE = 3;
    /** Timesheet row index for Wednesday. */
    public static final int WED = 4;
    /** Timesheet row index for Thursday. */
    public static final int THU = 5;
    /** Timesheet row index for Friday. */
    public static final int FRI = 6;
    
    public static final BigDecimal HOURS_IN_DAY = new BigDecimal(24);
    public static final int DAYS_IN_WEEK = 7;
    
    private static final long serialVersionUID = 11L;
    @Inject private TimesheetRowManager mgr;
    @Inject private TimesheetManager tsMgr;
    private List<EditableRow> list;
    
    public TimesheetManager getTsMgr() {
        return tsMgr;
    }

    public List<EditableRow> getList() {
        if(list == null)
            refreshList();
        return list;
    }
    
    public void setList(List<EditableRow> rows) {
        this.list = rows;
    }
    
    private void refreshList() {
        List<TimesheetRow> all = mgr.getAll();
        list = new ArrayList<EditableRow>();
        for (TimesheetRow row : all) {
            list.add(new EditableRow(row));
        }
    }
    
    /**
     * sets hour for a given day.
    * @param day The day of week to set the hour
    * @param hour The number of hours worked for that day
    */
//   public void setHour(final int day, final double hour) {
//       BigDecimal bdHour = null;
//       if (hour != 0.0) {
//           bdHour = new BigDecimal(hour).setScale(1, BigDecimal.ROUND_HALF_UP);
//       }
//       checkHour(bdHour);
//       hoursForWeek[day] = bdHour;
//   }

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
//    public BigDecimal getSum() {
//        BigDecimal sum = BigDecimal.ZERO;
//        for (BigDecimal next : hoursForWeek) {
//            if (next != null) {
//                sum = sum.add(next);
//            }
//        }
//        return sum;
//    }
    /**
     * sets the hours for each day in the week when the row edit event occurs
     * also displays a row edited message to the user.
     * @param event the row edit event 
     */
//    public void onRowEdit(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Row Edited");
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//        setHour(SAT, satHrs);
//        setHour(SUN, sunHrs);
//        setHour(MON, monHrs);
//        setHour(TUE, tueHrs);
//        setHour(WED, wedHrs);
//        setHour(THU, thuHrs);
//        setHour(FRI, friHrs);
//    }
    /**
     * displays a row edit cancelled message to the user.
     * @param event the row cancelled event
     */
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<TimesheetRow> getViewedSheetRows(int id){
        return mgr.getAllForTimesheet(id);
    }
    
    public int getTotalHours(TimesheetRow row) {
        return row.getMon_hours() + 
               row.getThu_hours() + 
               row.getWed_hours() + 
               row.getThu_hours() + 
               row.getFri_hours() + 
               row.getSat_hours() + 
               row.getSun_hours();
    }
    
}
