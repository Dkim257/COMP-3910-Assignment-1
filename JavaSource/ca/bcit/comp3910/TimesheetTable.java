package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Class representing a database table of Timesheets.
 * @author Tony Pacheco + Danny DiIorio
 * @version 1.0
 */
@Named("timesheettable")
@SessionScoped
public class TimesheetTable implements Serializable {
    
    /**
     * The Timesheet data list containing all of the Timesheets
     * in the system.
     */
    private static List<Timesheet> timesheets;
    static {
        timesheets = new ArrayList<>();
    }

    /**
     * The timesheet which is to be displayed to the user.
     */
    private Timesheet viewedTimesheet;
    
    /**
     * timesheets getter.
     * @return all of the timesheets.
     */
    public List<Timesheet> getTimesheets() {
        return timesheets;
    }

    /**
     * get all timesheets for an employee, or all timesheets 
     * if user is the administrator
     * @param e the employee whose timesheets are returned
     * @return all of the timesheets for an employee.
     */
    public List<Timesheet> getTimesheets(Employee e) {
        if(e == EmployeeTable.getAdmin())
            return getTimesheets();
        List<Timesheet> returnList = new ArrayList<>();
        for(Timesheet ts : timesheets)
            if(ts.getEmployee() == e)
                returnList.add(ts);
        return returnList;
    }

    /**
     * get current timesheet for an employee.
     * @param e the employee whose current timesheet is returned
     * @return the current timesheet for an employee.
     */
    public Timesheet getCurrentTimesheet(Employee e) {
        if(getTimesheets(e).isEmpty())
            return null;
        return getTimesheets(e).get(getTimesheets(e).size()-1);
    }
    
    /**
     * Getter for viewedTimesheet
     * @param e the employee currently signed in
     * @return viewedTimesheet
     */
    public Timesheet getViewedTimesheet(Employee e) {
        if(viewedTimesheet == null)
            viewedTimesheet = getCurrentTimesheet(e);
        return viewedTimesheet;
    }
    
    /**
     * Setter for viewedTimesheet
     * @param ts viewedTimesheet
     */
    public void setViewedTimesheet(Timesheet ts) {
        viewedTimesheet = ts;
    }
    
    /**
     * Creates a Timesheet object and adds it to the collection.
     *
     * @return a String representing navigation to the newTimesheet page.
     */
    public String addTimesheet(Employee e) {
        Timesheet sheet = new Timesheet();
        sheet.setEmployee(e);
        sheet.addRow();
        sheet.addRow();
        sheet.addRow();
        sheet.addRow();
        sheet.addRow();
        viewedTimesheet = sheet;
        return "timesheet.xhtml";
    }
    
    /**
     * Validates a timesheet being editing, and if validation passes
     * adds the timesheet to the list of timesheets (represents writing
     * it to a database)
     * @return the page to navigate to after validation
     */
    public String saveChanges() {
//        if(!viewedTimesheet.isValid()) {
//            FacesMessage msg = new FacesMessage("Invalid timesheet hours: Maximum 40 per week, overtime must be added in Overtime column.");
//            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//            FacesContext.getCurrentInstance().addMessage("", msg);
//            return "";
//        }
        if(!viewedTimesheet.hasAllUniqueIds()) {
            for(TimesheetRow row : viewedTimesheet.getDetails()){
                row.setWorkPackage(null);
            }
            FacesMessage msg = new FacesMessage("Project ID and WP combination for each row must be unique.");
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage("", msg);
            return "";
        }
        if(!timesheets.contains(viewedTimesheet)) 
            timesheets.add(viewedTimesheet);
        return "timesheet.xhtml";
    }
    
    /**
     * Sets the timesheet to view and then returns a string to navigate to 
     * the timesheet viewing page in order to display the timehseet to the user
     * @param ts the timesheet to display
     * @return "timesheet.xhtml" the timesheet viewing page
     */
    public String viewTimesheet(Timesheet ts) {
        viewedTimesheet = ts;
        return "timesheet.xhtml";
    }
}
