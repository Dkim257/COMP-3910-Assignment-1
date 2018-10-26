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

    private Timesheet currentSheet;
    
    public List<Timesheet> getTimesheets() {
        if(EmployeeTable.getInstance().isAdmin())
            return timesheets;
        else
            return getTimesheets(EmployeeTable.getInstance().getCurrentUser());
    }

    public List<Timesheet> getTimesheets(Employee e) {
        List<Timesheet> returnList = new ArrayList<>();
        for(Timesheet ts : timesheets)
            if(ts.getEmployee() == e)
                returnList.add(ts);
        return returnList;
    }

    public Timesheet getCurrentTimesheet(Employee e) {
        return getTimesheets(e).get(getTimesheets(e).size()-1);
    }

    public String addTimesheet() {
        Timesheet sheet = new Timesheet();
        timesheets.add(sheet);
        return "timesheet.xhtml";
    }
}
