package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("timesheettable")
@SessionScoped
public class TimesheetTable implements Serializable {
    
    private static List<Timesheet> timesheets;
    static {
        timesheets = new ArrayList<>();
    }

    private Timesheet viewedTimesheet;
    
    public List<Timesheet> getTimesheets() {
        return timesheets;
    }

    public List<Timesheet> getTimesheets(Employee e) {
        if(e == EmployeeTable.getAdmin())
            return getTimesheets();
        List<Timesheet> returnList = new ArrayList<>();
        for(Timesheet ts : timesheets)
            if(ts.getEmployee() == e)
                returnList.add(ts);
        return returnList;
    }

    public Timesheet getCurrentTimesheet(Employee e) {
        if(getTimesheets(e).isEmpty())
            return null;
        return getTimesheets(e).get(getTimesheets(e).size()-1);
    }
    
    public Timesheet getViewedTimesheet(Employee e) {
        if(viewedTimesheet == null)
            viewedTimesheet = getCurrentTimesheet(e);
        return viewedTimesheet;
    }
    
    public void setViewedTimesheet(Timesheet ts) {
        viewedTimesheet = ts;
    }

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
    
    public String saveChanges() {
        if(!timesheets.contains(viewedTimesheet)) 
            timesheets.add(viewedTimesheet);
        return "timesheet.xhtml";
    }
    
    public String viewTimesheet(Timesheet ts) {
        viewedTimesheet = ts;
        return "timesheet.xhtml";
    }
}
