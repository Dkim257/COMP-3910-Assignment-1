package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.ArrayList;
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
        List<Timesheet> returnList = new ArrayList<>();
        for(Timesheet ts : timesheets)
            if(ts.getEmployee().equals(e))
                returnList.add(ts);
        return returnList;
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