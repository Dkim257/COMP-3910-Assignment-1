package ca.bcit.comp3910;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("timesheet")
@ApplicationScoped
public class Timesheet implements Serializable {

    public static final int DAYS_IN_WEEK = 7;
    public static final double HOURS_IN_A_DAY = 24.0;
    public static final BigDecimal HOURS_IN_DAY =
           new BigDecimal(HOURS_IN_A_DAY).setScale(1, BigDecimal.ROUND_HALF_UP);
    public static final double WORK_HOURS = 40.0;
    public static final BigDecimal FULL_WORK_WEEK =
            new BigDecimal(WORK_HOURS).setScale(1, BigDecimal.ROUND_HALF_UP);

    private Employee employee;
    private Date endWeek;
    private List<TimesheetRow> details;
    private BigDecimal overtime;
    private BigDecimal flextime;

    public Timesheet() {
        details = new ArrayList<TimesheetRow>();
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        endWeek = c.getTime();
    }

    public Timesheet(final Employee user, final Date end,
            final List<TimesheetRow> charges) {
        employee = user;
        checkFriday(end);
        endWeek = end;
        details = charges;
    }

    public Employee getEmployee() {
        return employee;
    }
 
    public void setEmployee(final Employee user) {
        employee = user;
    }

    public Date getEndWeek() {
        return endWeek;
    }

    private void checkFriday(final Date end) {
        Calendar c = new GregorianCalendar();
        c.setTime(end);
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        if (currentDay != Calendar.FRIDAY) {
            throw new IllegalArgumentException("EndWeek must be a Friday");
        }

    }
  
    public void setEndWeek(final Date end) {
        checkFriday(end);
        endWeek = end;
    }

    public int getWeekNumber() {
        Calendar c = new GregorianCalendar();
        c.setTime(endWeek);
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    
    public void setWeekNumber(final int weekNo, final int weekYear) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.setTime(endWeek);
        c.setWeekDate(weekYear, weekNo, Calendar.FRIDAY);
        endWeek = c.getTime();
    }
    
    public String getWeekEnding() {
        Calendar c = new GregorianCalendar();
        c.setTime(endWeek);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month += 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return month + "/" + day + "/" + year;
    }

    public List<TimesheetRow> getDetails() {
        return details;
    }

    public void setDetails(final ArrayList<TimesheetRow> newDetails) {
        details = newDetails;
    }

    public BigDecimal getOvertime() {
        return overtime;
    }

    public void setOvertime(final BigDecimal ot) {
        overtime = ot.setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getFlextime() {
        return flextime;
    }

    public void setFlextime(final BigDecimal flex) {
        flextime = flex.setScale(1, BigDecimal.ROUND_HALF_UP);
    }
    
    public BigDecimal getTotalHours() {
        BigDecimal sum = BigDecimal.ZERO;
        for (TimesheetRow row : details) {
            sum = sum.add(row.getSum());
        }
        return sum;
    }
    
    public BigDecimal getTotalDaysHours(int day) {
        BigDecimal sum = BigDecimal.ZERO;
        for (TimesheetRow row : details) {
            if(row.getHour(day) == null)
                sum = sum.add(BigDecimal.ZERO);
            else
                sum = sum.add(row.getHour(day));
        }
        return sum;
    }

    public BigDecimal[] getDailyHours() {
        BigDecimal[] sums = new BigDecimal[DAYS_IN_WEEK];
        for (TimesheetRow day : details) {
            BigDecimal[] hours = day.getHoursForWeek();
            for (int i = 0; i < DAYS_IN_WEEK; i++) {
                if (hours[i] != null) {
                    if (sums[i] == null) {
                        sums[i] = hours[i];
                    } else {
                        sums[i] = sums[i].add(hours[i]);
                    }
                }
            }
        }
        return sums;
    }

    public boolean isValid() {
        BigDecimal net = getTotalHours();
        if (overtime != null) {
            net = net.subtract(overtime);
        }
        if (flextime != null) {
            net = net.subtract(flextime);
        }
        return net.equals(FULL_WORK_WEEK);
    }
    
    public boolean hasAllUniqueIds() {
        List<String> ids = new ArrayList<>();
        for(TimesheetRow row : details) {
            if(row.getWorkPackage() == null)
                continue;
            String id = row.getWorkPackage() + row.getProjectID();
            if(ids.contains(id))
                return false;
            else 
                ids.add(id);
        }
        return true;
    }

    public void deleteRow(final TimesheetRow rowToRemove) {
        details.remove(rowToRemove);
    }

    public void addRow() {
        details.add(new TimesheetRow());
    }

    
}
