package ca.bcit.comp3910;

import ca.bcit.infosys.timesheet.TimesheetRow;
import javax.inject.Named;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

@Named("row")
@ApplicationScoped
public class TimesheetRowBean extends TimesheetRow implements Serializable {

}
