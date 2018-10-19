package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;

@Named("tabledata")
@ApplicationScoped
public class TimesheetTableData implements TimesheetCollection, Serializable {
    
    private static List<Timesheet> timesheets;
    
    public TimesheetTableData() {
        
    }
    
    
    @Override
    public List<Timesheet> getTimesheets() {
        return timesheets;
    }

    @Override
    public List<Timesheet> getTimesheets(Employee e) {
        return null;
    }

    @Override
    public Timesheet getCurrentTimesheet(Employee e) {
        return null;
    }

    @Override
    public String addTimesheet() {
        Timesheet sheet = new Timesheet();
        timesheets.add(sheet);
        return "timesheet.xhtml";
    }

}
