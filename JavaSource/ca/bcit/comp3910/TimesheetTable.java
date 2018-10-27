package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    
    public String viewTimesheet(Timesheet ts) {
        viewedTimesheet = ts;
        return "timesheet.xhtml";
    }
}
