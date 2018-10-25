package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("timesheettable")
@SessionScoped
public class TimesheetTable implements Serializable {
    
    List<Timesheet> timesheets;
    
    public List<Timesheet> getTimesheets() {
        return timesheets;
    }

    public List<Timesheet> getTimesheets(Employee e) {
        return null;
    }

    public Timesheet getCurrentTimesheet(Employee e) {
        return null;
    }

    public String addTimesheet() {
        Timesheet sheet = new Timesheet();
        timesheets.add(sheet);
        return "timesheet.xhtml";
    }
}
