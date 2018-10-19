package ca.bcit.comp3910;

import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named("row")
@ApplicationScoped
public class TimesheetRowBean extends TimesheetRow implements Serializable {
    private int id;
    private Timesheet timesheet;

    public TimesheetRowBean() {
        super();
    }
    
    public TimesheetRowBean(final Timesheet ts) {
        super();
        this.timesheet = ts;
    }
    
    public TimesheetRowBean(final int i, final String wp,
            final BigDecimal[] hours, final String comments) {
        super(i, wp, hours, comments);
    }
    
    @PostConstruct 
    public void init() {

    }
    
    public Timesheet getTimesheet() {
        return timesheet;
    }
    
    public void setTimesheet(final Timesheet t) {
        timesheet = t;
    }
    
    public BigDecimal getHourMon() {
        return getHour(TimesheetRow.MON);
    }

    public BigDecimal getHourTue() {
        return getHour(TimesheetRow.TUE);
    }

    public BigDecimal getHourWed() {
        return getHour(TimesheetRow.WED);
    }

    public BigDecimal getHourThur() {
        return getHour(TimesheetRow.THU);
    }

    public BigDecimal getHourFri() {
        return getHour(TimesheetRow.FRI);
    }

    public BigDecimal getHourSat() {
        return getHour(TimesheetRow.SAT);
    }

    public BigDecimal getHourSun() {
        return getHour(TimesheetRow.SUN);
    }
    
    public void setHourMon(final BigDecimal hour) {
        setHour(TimesheetRow.MON, hour);
    }

    public void setHourTue(final BigDecimal hour) {
        setHour(TimesheetRow.TUE, hour);
    }

    public void setHourWed(final BigDecimal hour) {
        setHour(TimesheetRow.WED, hour);
    }

    public void setHourThur(final BigDecimal hour) {
        setHour(TimesheetRow.THU, hour);
    }

    public void setHourFri(final BigDecimal hour) {
        setHour(TimesheetRow.FRI, hour);
    }

    public void setHourSat(final BigDecimal hour) {
        setHour(TimesheetRow.SAT, hour);
    }

    public void setHourSun(final BigDecimal hour) {
        setHour(TimesheetRow.SUN, hour);
    }
    
    
    
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Row Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
}
