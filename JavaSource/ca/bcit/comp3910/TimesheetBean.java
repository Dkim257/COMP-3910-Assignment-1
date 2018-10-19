package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

@Named("timesheet")
@ApplicationScoped
public class TimesheetBean extends Timesheet implements Serializable{
    
    public TimesheetBean() {
        super();

        ArrayList<TimesheetRow> newDetails = new ArrayList<TimesheetRow>();
        newDetails.add(new TimesheetRowBean(this));
        newDetails.add(new TimesheetRowBean(this));
        newDetails.add(new TimesheetRowBean(this));
        newDetails.add(new TimesheetRowBean(this));
        newDetails.add(new TimesheetRowBean(this));

        setDetails(newDetails);
    }
    
}
