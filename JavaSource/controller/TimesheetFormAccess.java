package controller;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import access.EmployeeManager;
import access.TimesheetManager;
import access.TimesheetRowManager;
import models.Employees;
import models.Timesheet;
import models.TimesheetRow;

@Named("timesheettable")
@SessionScoped
public class TimesheetFormAccess implements Serializable {

    private static final long serialVersionUID = 11L;
    @Inject private TimesheetManager mgr;
    @Inject private EmployeeManager empMgr;
    @Inject private TimesheetRowManager tsRowMgr;
    private List<Timesheet> timesheets;
    
    public EmployeeManager getEmpMgr() {
        return empMgr;
    }

    public TimesheetRowManager getTsRowMgr() {
        return tsRowMgr;
    }

    /**
     * The timesheet which is to be displayed to the user.
     */
    private Timesheet viewedTimesheet;

    public List<Timesheet> getTimesheets() {
        if(timesheets == null)
            refreshList();
        return timesheets;
    }
    
    /**
     * TODO: make this work with our db and mgrs
     * get all timesheets for an employee, or all timesheets 
     * if user is the administrator.
     * @param e the employee whose timesheets are returned
     * @return all of the timesheets for an employee.
     */
    public List<Timesheet> getTimesheets(Employees e) {
        if (true /*is admin*/) {
            return timesheets;
        }
        List<Timesheet> returnList = new ArrayList<>();
        for (Timesheet ts : timesheets) {
            if (ts.getEmp_number() == e.getEmp_number()) {
                returnList.add(ts);
            }
        }
        return returnList;
    }
    
    /**
     * get current timesheet for an employee.
     * @param e the employee whose current timesheet is returned
     * @return the current timesheet for an employee.
     */
    public Timesheet getCurrentTimesheet(Employees e) {
        if (getTimesheets(e).isEmpty()) {
            return null;
        }
        return getTimesheets(e).get(getTimesheets(e).size() - 1);
    }
    
    /**
     * Getter for viewedTimesheet.
     * @param e the employee currently signed in
     * @return viewedTimesheet
     */
    public Timesheet getViewedTimesheet(Employees e) {
        if (viewedTimesheet == null) {
            viewedTimesheet = getCurrentTimesheet(e);
        }
        return viewedTimesheet;
    }
    
    /**
     * Setter for viewedTimesheet.
     * @param ts viewedTimesheet
     */
    public void setViewedTimesheet(Timesheet ts) {
        viewedTimesheet = ts;
    }
    
    /**
     * TODO: work with mgr
     * Creates a Timesheet object and sets it to viewedTimesheet.
     * @param e the currently logged in user, needed to assign the timesheet
     * @return a String representing navigation to the newTimesheet page.
     */
    public String addTimesheet(Employees e) {
        Timesheet sheet = new Timesheet();
//        sheet.setEmployee(e);
//        sheet.addRow();
//        sheet.addRow();
//        sheet.addRow();
//        sheet.addRow();
//        sheet.addRow();
        viewedTimesheet = sheet;
        return "timesheet.xhtml";
    }
    
    /**
     * TODO: ya know
     * Validates a timesheet being editing, and if validation passes
     * adds the timesheet to the list of timesheets (represents writing
     * it to a database).
     * @return the page to navigate to after validation
     */
    public String saveChanges() {
//        if(!viewedTimesheet.isValid()) {
//            FacesMessage msg = 
//                  new FacesMessage("Invalid timesheet hours: "
//                          + "Maximum 40 per week, "
//                          + "overtime must be added in Overtime column.");
//            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//            FacesContext.getCurrentInstance().addMessage("", msg);
//            return "";
//        }
//        if (!viewedTimesheet.hasAllUniqueIds()) {
//            for (TimesheetRow row : viewedTimesheet.getDetails()) {
//                row.setWorkPackage(null);
//            }
//            FacesMessage msg = new FacesMessage(
//                    "Project and WP combination for each row must be unique.");
//            msg.setSeverity(FacesMessage.SEVERITY_WARN);
//            FacesContext.getCurrentInstance().addMessage("", msg);
//            return "";
//        }
//        if (!timesheets.contains(viewedTimesheet)) {
//            timesheets.add(viewedTimesheet);
//        }
        return "timesheet.xhtml";
    }
    
    /**
     * Sets the timesheet to view and then returns a string to navigate to 
     * the timesheet viewing page in order to display the timehseet to the user.
     * @param ts the timesheet to display
     * @return "timesheet.xhtml" the timesheet viewing page
     */
    public String viewTimesheet(Timesheet ts) {
        viewedTimesheet = ts;
        return "timesheet.xhtml";
    }
    
    public void setList(List<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }
    
    private void refreshList() {
        timesheets = mgr.getAll();
    }
    
    //TODO: This properly
    public int getTimesheetEmpNumber() {
        return viewedTimesheet.getEmp_number();
    }
    
    public Date getTimesheetWeekNumber() {
        return viewedTimesheet.getEnd_week();
    }
    
    public String getTimesheetEmployeeName() {
        int empNum = viewedTimesheet.getEmp_number();
        List<Employees> emps = empMgr.getAll();
        for(Employees e : emps) {
            if(e.getEmp_number() == empNum) {
                return e.getName();
            }
        }
        return null;
    }
    
    //TODO: This properly
    public Date getTimesheetDateInCustomizedFormat() {
        return getTimesheetWeekNumber();
    }
    
    public int getTimesheetID() {
        return viewedTimesheet.getTimesheet_id();
    }
    
    public List<TimesheetRow> getViewedSheetRows(){
        return tsRowMgr.getAllForTimesheet(viewedTimesheet.getTimesheet_id());
    }
    
}
