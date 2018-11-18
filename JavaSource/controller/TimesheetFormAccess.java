package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    private static final int ROWS_TO_START_SHEET_WITH = 5;
    
    private static final long serialVersionUID = 11L;
    LocalDate today = LocalDate.now();
    
    @Inject private TimesheetManager mgr;
    @Inject private EmployeeManager empMgr;
    @Inject private TimesheetRowManager tsRowMgr;
    
    private List<Timesheet> timesheets;
    
    List<EditableRow> currentEditables;
    
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
     * Get all timesheets for an employee, or all timesheets 
     * if user is the administrator.
     * @param e the employee whose timesheets are returned
     * @return all of the timesheets for an employee.
     */
    public List<Timesheet> getTimesheets(Employees e) {
        if (e.getIsAdmin()) {
            return getTimesheets();
        }
        return (timesheets = mgr.getAll(e.getEmp_number()));
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
        int id = (int) mgr.getCount()+1;
        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
          sunday = sunday.plusDays(1);
        }
        Timesheet sheet = new Timesheet(id, e.getEmp_number(), java.sql.Date.valueOf(sunday));
        mgr.persist(sheet);
        for(int i = 0; i < ROWS_TO_START_SHEET_WITH; ++i) {
            addRowToSheet(id);
        }
        getTimesheets(e);
        return viewTimesheet(sheet);
    }
    
    public String deleteCurrentTimesheet() {
        for (EditableRow row : currentEditables) {
            tsRowMgr.remove(row.getRow());
        }
        mgr.remove(viewedTimesheet);
        timesheets.remove(viewedTimesheet);
        currentEditables = null;
        viewedTimesheet = null;
        return "timesheetSelect.xhtml";
    }
    
    public String deleteTimesheet(Timesheet sheet, Employees e) {
        List<TimesheetRow> rowsToDelete = tsRowMgr.getAllForTimesheet(sheet.getTimesheet_id());
        for (TimesheetRow row : rowsToDelete) {
            tsRowMgr.remove(row);
        }
        mgr.remove(sheet);
        timesheets.remove(sheet);
        getTimesheets(e);
        return "timesheetSelect.xhtml";
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
        if (!timesheetHasAllUniqueIds()) {
            for (EditableRow row : currentEditables) {
                row.getRow().setWork_package(null);
            }
            FacesMessage msg = new FacesMessage(
                    "Project and WP combination for each row must be unique.");
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage("", msg);
            return "";
        }
        for(EditableRow editable : currentEditables) {
            tsRowMgr.merge(editable.getRow());
        }
        return "timesheet.xhtml";
    }
    
    /**
     * Checks if all rows in the timesheet have a unique combination
     * of ProjectID and WorkPackage.
     * @return true if all combinations are unique
     */
    public boolean timesheetHasAllUniqueIds() {
        HashSet<String> ids = new HashSet<>();
        for (EditableRow row : currentEditables) {
            if (row.getRow().getWork_package().isEmpty()) {
                continue;
            }
            String id = row.getRow().getWork_package() + row.getRow().getProject_id();
            if (ids.contains(id)) {
                return false;
            } else {
                ids.add(id);
            }
        }
        return true;
    }
    
    /**
     * Sets the timesheet to view and then returns a string to navigate to 
     * the timesheet viewing page in order to display the timehseet to the user.
     * @param ts the timesheet to display
     * @return "timesheet.xhtml" the timesheet viewing page
     */
    public String viewTimesheet(Timesheet ts) {
        viewedTimesheet = ts;
        getViewedSheetRows();
        return "timesheet.xhtml";
    }
    
    public List<EditableRow> getViewedSheetRows(){
        currentEditables = new ArrayList<>();
        List<TimesheetRow> rows = tsRowMgr.getAllForTimesheet(viewedTimesheet.getTimesheet_id());
        for(TimesheetRow row : rows) {
            currentEditables.add(new EditableRow(row));
        }
        return currentEditables;
    }
    
    public void setList(List<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }
    
    private void refreshList() {
        timesheets = mgr.getAll();
    }
    
    public int getTimesheetEmpNumber() {
        return viewedTimesheet.getEmp_number();
    }
    
    /**
     * Calculate the week number of the timesheet.
     * @return the calculated week number
     */
    public int getTimesheetWeekNumber() {
        Calendar c = new GregorianCalendar();
        c.setTime(viewedTimesheet.getEnd_week());
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        return c.get(Calendar.WEEK_OF_YEAR);
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
    
    public Date getTimesheetDate() {
        return viewedTimesheet.getEnd_week();
    }
    
    public int getTimesheetID() {
        return viewedTimesheet.getTimesheet_id();
    }
    
    public BigDecimal getTimesheetTotalHours() {
        BigDecimal total = BigDecimal.ZERO;
        for(EditableRow row : currentEditables) {
            if (row.getSum() != null)
                total = total.add(row.getSum());
        }
        return total;
    }
    
    public void addRowToCurrentSheet() {
        TimesheetRow newRow = new TimesheetRow(viewedTimesheet.getTimesheet_id());
        tsRowMgr.persist(newRow);
        currentEditables.add(new EditableRow(newRow));
    }
    
    public void addRowToSheet(int timesheetid) {
        TimesheetRow newRow = new TimesheetRow(timesheetid);
        tsRowMgr.persist(newRow);
    }
    
    public BigDecimal getTimesheetTotalSatHours() {
        BigDecimal total = BigDecimal.ZERO;
        for(EditableRow row : currentEditables) {
            total = total.add(row.getRow().getSat_hours());
        }
        return total;
    }
    public BigDecimal getTimesheetTotalSunHours() {
        BigDecimal total = BigDecimal.ZERO;
        for(EditableRow row : currentEditables) {
            total = total.add(row.getRow().getSun_hours());
        }
        return total;
    }
    public BigDecimal getTimesheetTotalMonHours() {
        BigDecimal total = BigDecimal.ZERO;
        for(EditableRow row : currentEditables) {
            total = total.add(row.getRow().getMon_hours());
        }
        return total;
    }
    public BigDecimal getTimesheetTotalTueHours() {
        BigDecimal total = BigDecimal.ZERO;
        for(EditableRow row : currentEditables) {
            total = total.add(row.getRow().getTue_hours());
        }
        return total;
    }
    public BigDecimal getTimesheetTotalWedHours() {
        BigDecimal total = BigDecimal.ZERO;
        for(EditableRow row : currentEditables) {
            total = total.add(row.getRow().getWed_hours());
        }
        return total;
    }
    public BigDecimal getTimesheetTotalThuHours() {
        BigDecimal total = BigDecimal.ZERO;
        for(EditableRow row : currentEditables) {
            total = total.add(row.getRow().getThu_hours());
        }
        return total;
    }
    public BigDecimal getTimesheetTotalFriHours() {
        BigDecimal total = BigDecimal.ZERO;
        for(EditableRow row : currentEditables) {
            total = total.add(row.getRow().getFri_hours());
        }
        return total;
    }
    
}
