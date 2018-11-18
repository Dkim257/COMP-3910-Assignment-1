package controller;

import java.io.IOException;
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

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import access.EmployeeManager;
import access.TimesheetManager;
import access.TimesheetRowManager;
import models.Employees;
import models.Timesheet;
import models.TimesheetRow;

/**
 * Timesheet helper class providing functionality between user interface and 
 * persistence tier.
 * @author Danny & Tony
 * @version 2
 *
 */
@Named("timesheettable")
@SessionScoped
public class TimesheetFormAccess implements Serializable {

    /** Maximum number accepted for hours in a day. */
    public static final BigDecimal HOURS_IN_DAY = new BigDecimal(24);
    
    /** Number of rows a new timesheet is created with. */
    private static final int ROWS_TO_START_SHEET_WITH = 5;
    
    private static final long serialVersionUID = 11L;
    
    /** Today's date used for calculating new timesheet end week date. */
    private LocalDate today = LocalDate.now();
    
    /** Manager from Timesheet objects. */
    @Inject private TimesheetManager mgr;
    
    /** Manager from Employee objects. */
    @Inject private EmployeeManager empMgr;
    
    /** Manager from TimesheetRow objects. */
    @Inject private TimesheetRowManager tsRowMgr;
    
    /** List holding all timesheets. */
    private List<Timesheet> timesheets;
    
    /** List holding current timesheet rows being edited. */
    private List<EditableRow> currentEditables;
    
    /** The timesheet which is to be displayed to the user. */
    private Timesheet viewedTimesheet;
    
    /**
     * Getter for Employee manager.
     * @return Manager from Employee objects
     */
    public EmployeeManager getEmpMgr() {
        return empMgr;
    }

    /**
     * Getter for TimesheetRow manager.
     * @return Manager from TimesheetRow objects
     */
    public TimesheetRowManager getTsRowMgr() {
        return tsRowMgr;
    }
    
    /**
     * Fills timesheet list with all timesheets in database.
     * @return Timesheet list
     */
    public List<Timesheet> getTimesheets() {
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
        return mgr.getAll(e.getEmpNumber());
    }
    
    /**
     * Get current timesheet for an employee.
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
     * Creates a Timesheet object and sets it to viewedTimesheet.
     * @param e the currently logged in user, needed to assign the timesheet
     * @return a String representing navigation to the newTimesheet page.
     */
    public String addTimesheet(Employees e) {
        int id = (int) mgr.getCount() + 1;
        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
          sunday = sunday.plusDays(1);
        }
        Timesheet sheet = new Timesheet(id, e.getEmpNumber(),
                java.sql.Date.valueOf(sunday));
        mgr.persist(sheet);
        for (int i = 0; i < ROWS_TO_START_SHEET_WITH; ++i) {
            addRowToSheet(id);
        }
        getTimesheets(e);
        return viewTimesheet(sheet);
    }
    
    /**
     * Deletes a timesheet while it is being viewed.
     * @return Redirects user to timesheet history page
     */
    public String deleteCurrentTimesheet() {
        for (EditableRow row : currentEditables) {
            tsRowMgr.remove(row.getRow());
        }
        mgr.remove(viewedTimesheet);
        if (timesheets.contains(viewedTimesheet)) {
            timesheets.remove(viewedTimesheet);
        }
        currentEditables = null;
        viewedTimesheet = null;
        return "timesheetSelect.xhtml";
    }
    
    /**
     * Deletes a timesheet from the history page.
     * @param sheet Timesheet being deleted
     * @param e Owner of the timesheet
     * @throws IOException on reload
     */
    public void deleteTimesheet(Timesheet sheet, Employees e)
            throws IOException {
        List<TimesheetRow> rowsToDelete = tsRowMgr
                .getAllForTimesheet(sheet.getTimesheetId());
        for (TimesheetRow row : rowsToDelete) {
            tsRowMgr.remove(row);
        }
        mgr.remove(sheet);
        if (timesheets.contains(sheet)) {
            timesheets.remove(sheet);
        }
        getTimesheets(e);
        reload();
    }
    
    /**
     * Conducts hard page reload in browser.
     * @throws IOException on reload
     */
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    /**
     * Validates a timesheet being editing, and if validation passes
     * adds the timesheet to the list of timesheets (represents writing
     * it to a database).
     * @return the page to navigate to after validation
     */
    public String saveChanges() {
        if (!timesheetHasAllUniqueIds()) {
            for (EditableRow row : currentEditables) {
                row.getRow().setWorkPackage(null);
            }
            FacesMessage msg = new FacesMessage("Warning",
                    "Project and WP combination for each row must be unique.");
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        }
        if (!daysAllUnder24Hours()) {
            FacesMessage msg = new FacesMessage("Warning",
                    "Cannot work more than 24 hours in a day.");
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        }
        for (EditableRow editable : currentEditables) {
            tsRowMgr.merge(editable.getRow());
        }
        FacesMessage msg = new FacesMessage("Success",
                "Timesheet Saved");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "timesheet.xhtml";
    }
    
    /**
     *  Verifies that all days have a valid total number of hours.
     * @return boolean true if all days have valid total hours
     */
    private boolean daysAllUnder24Hours() {
        return hoursValid(getTimesheetTotalMonHours()) 
            && hoursValid(getTimesheetTotalTueHours())
            && hoursValid(getTimesheetTotalWedHours()) 
            && hoursValid(getTimesheetTotalThuHours())
            && hoursValid(getTimesheetTotalFriHours())
            && hoursValid(getTimesheetTotalSatHours())
            && hoursValid(getTimesheetTotalSunHours()); 
    }
    
    /**
     * Checks if hour value is out of the valid
     * bounds of 0.0 to 24.0, or has more than one decimal digit.
     *
     *@param hour the value to check
     *@return boolean true if hours are valid
     */
    public boolean hoursValid(final BigDecimal hour) {
        if (hour != null) {
            if (hour.compareTo(HOURS_IN_DAY) > 0.0
                    || hour.compareTo(BigDecimal.ZERO) < 0.0) {
                return false;
            }
            if (hour.scale() > 1) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if all rows in the timesheet have a unique combination
     * of ProjectID and WorkPackage.
     * @return true if all combinations are unique
     */
    public boolean timesheetHasAllUniqueIds() {
        HashSet<String> ids = new HashSet<>();
        for (EditableRow row : currentEditables) {
            if (row.getRow().getWorkPackage().isEmpty()) {
                continue;
            }
            String id = row.getRow().getWorkPackage() 
                      + row.getRow().getProjectId();
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
        List<TimesheetRow> rows = tsRowMgr
                .getAllForTimesheet(ts.getTimesheetId());
        currentEditables = new ArrayList<>();
        for (TimesheetRow row : rows) {
            currentEditables.add(new EditableRow(row));
        }
        return "timesheet.xhtml";
    }
    
    /**
     * Returns all editable rows in timesheet.
     * @return list of rows
     */
    public List<EditableRow> getViewedSheetRows() {
        return currentEditables;
    }
    
    /**
     * Setter for list of timesheets.
     * @param ts list of timesheets
     */
    public void setList(List<Timesheet> ts) {
        this.timesheets = ts;
    }
    
    /**
     * Fills timesheet list with all timesheets from database.
     */
    private void refreshList() {
        timesheets = mgr.getAll();
    }
    
    /**
     * Returns employee number for the timesheet selected.
     * @return employee number
     */
    public int getTimesheetEmpNumber() {
        return viewedTimesheet.getEmpNumber();
    }
    
    /**
     * Calculate the week number of the timesheet.
     * @return the calculated week number
     */
    public int getTimesheetWeekNumber() {
        Calendar c = new GregorianCalendar();
        c.setTime(viewedTimesheet.getEndWeek());
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Returns name of employee owning selected timesheet.
     * @return employee name
     */
    public String getTimesheetEmployeeName() {
        int empNum = viewedTimesheet.getEmpNumber();
        List<Employees> emps = empMgr.getAll();
        for (Employees e : emps) {
            if (e.getEmpNumber() == empNum) {
                return e.getName();
            }
        }
        return null;
    }
    
    /**
     * Returns end week date for a timesheet.
     * @return date
     */
    public Date getTimesheetDate() {
        return viewedTimesheet.getEndWeek();
    }
    
    /**
     * Returns id for a timesheet.
     * @return id
     */
    public int getTimesheetID() {
        return viewedTimesheet.getTimesheetId();
    }
    
    /**
     * Returns total hours in a timesheet row.
     * @return hours
     */
    public BigDecimal getTimesheetTotalHours() {
        BigDecimal total = BigDecimal.ZERO;
        for (EditableRow row : currentEditables) {
            if (row.getSum() != null) {
                total = total.add(row.getSum());
            }
        }
        return total;
    }
    
    /**
     * Adds a new row to the bottom of a timesheet being viewed.
     */
    public void addRowToCurrentSheet() {
        TimesheetRow newRow = new TimesheetRow(viewedTimesheet
                .getTimesheetId());
        currentEditables.add(new EditableRow(newRow));
    }
    
    /**
     * Adds new row to timesheet in database.
     * @param timesheetid id
     */
    public void addRowToSheet(int timesheetid) {
        TimesheetRow newRow = new TimesheetRow(timesheetid);
        tsRowMgr.persist(newRow);
    }
    
    /**
     * Sums and returns total hours for Saturday column.
     * @return sat hours
     */
    public BigDecimal getTimesheetTotalSatHours() {
        BigDecimal total = BigDecimal.ZERO;
        for (EditableRow row : currentEditables) {
            total = total.add(row.getRow().getSatHours());
        }
        return total;
    }
    
    /**
     * Sums and returns total hours for Sunday column.
     * @return sun hours
     */
    public BigDecimal getTimesheetTotalSunHours() {
        BigDecimal total = BigDecimal.ZERO;
        for (EditableRow row : currentEditables) {
            total = total.add(row.getRow().getSunHours());
        }
        return total;
    }
    
    /**
     * Sums and returns total hours for Monday column.
     * @return mon hours
     */
    public BigDecimal getTimesheetTotalMonHours() {
        BigDecimal total = BigDecimal.ZERO;
        for (EditableRow row : currentEditables) {
            total = total.add(row.getRow().getMonHours());
        }
        return total;
    }
    
    /**
     * Sums and returns total hours for Tuesday column.
     * @return tue hours
     */
    public BigDecimal getTimesheetTotalTueHours() {
        BigDecimal total = BigDecimal.ZERO;
        for (EditableRow row : currentEditables) {
            total = total.add(row.getRow().getTueHours());
        }
        return total;
    }
    
    /**
     * Sums and returns total hours for Wednesday column.
     * @return wed hours
     */
    public BigDecimal getTimesheetTotalWedHours() {
        BigDecimal total = BigDecimal.ZERO;
        for (EditableRow row : currentEditables) {
            total = total.add(row.getRow().getWedHours());
        }
        return total;
    }
    
    /**
     * Sums and returns total hours for Thursday column.
     * @return thu hours
     */
    public BigDecimal getTimesheetTotalThuHours() {
        BigDecimal total = BigDecimal.ZERO;
        for (EditableRow row : currentEditables) {
            total = total.add(row.getRow().getThuHours());
        }
        return total;
    }
    
    /**
     * Sums and returns total hours for Friday column.
     * @return fri hours
     */
    public BigDecimal getTimesheetTotalFriHours() {
        BigDecimal total = BigDecimal.ZERO;
        for (EditableRow row : currentEditables) {
            total = total.add(row.getRow().getFriHours());
        }
        return total;
    }
    
}
