package access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.TimesheetRow;

/**
 * Handle CRUD actions for TimesheetRow class.
 * @author Tony & Danny
 * @version 1
 *
 */
@Dependent
@Stateless
public class TimesheetRowManager implements Serializable {
    
    /** Peristence manager for class. */
    private @PersistenceContext(unitName = "db") EntityManager em;
    
    /**
     * Getter for persistence manager.
     * @return em
     */
    public EntityManager getEm() {
        return em;
    }

    /**
     * Find TimesheetRow record from database.
     * 
     * @param id primary key of tsRow record.
     * @return the TimesheetRow record with key = id, null if not found.
     */
    public TimesheetRow find(int id) {
        return em.find(TimesheetRow.class, id);
    }

    /**
     * Persist TimesheetRow record into database.  id must be unique.
     * @param tsRow the record to be persisted.
     */
    public void persist(TimesheetRow tsRow) {
        em.persist(tsRow);
    }
    
    /**
     * Merge TimesheetRow record fields into existing database record.
     * @param tsRow the record to be merged.
     */
    public void merge(TimesheetRow tsRow) {
        em.merge(tsRow);
    }
    
    /**
     * Remove tsRow from database.
     * @param tsRow record to be removed from database
     */
    public void remove(TimesheetRow tsRow) {
        tsRow = find(tsRow.getTimesheet_row_id());
        em.remove(tsRow);
    }

    /**
     * Return TimesheetRows table as List of TimesheetRow.
     * @return List of all records in TimesheetRows table
     */
    public List<TimesheetRow> getAll() {
        return em.createQuery("select t from TimesheetRow t",
                TimesheetRow.class)
                .getResultList();
    }
    
    /**
     * Return TimesheetRows table as List of TimesheetRow, for
     * a specific timesheet.
     * @param timesheetId selected timesheet
     * @return List of all rows in timesheet
     */
    public List<TimesheetRow> getAllForTimesheet(int timesheetId) {
        return em.createQuery("select t from TimesheetRow t "
                + "where t.timesheet_id = :id", TimesheetRow.class)
                .setParameter("id", timesheetId)
                .getResultList();
    }

}
