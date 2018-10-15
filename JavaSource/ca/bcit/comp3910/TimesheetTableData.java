package ca.bcit.comp3910;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;

public class TimesheetTableData implements TimesheetCollection {

    ArrayList<Timesheet> timesheets;
    
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
