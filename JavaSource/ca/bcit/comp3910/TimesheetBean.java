package ca.bcit.comp3910;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.infosys.timesheet.Timesheet;

@Named("timesheet")
@ApplicationScoped
public class TimesheetBean extends Timesheet implements Serializable{
    
    public TimesheetBean() {
        
    }
    
}
