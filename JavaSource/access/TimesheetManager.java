package access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import models.Timesheet;

/**
 * Handle CRUD actions for Timesheet class.
 * @author Tony & Danny
 * @version 1
 *
 */
@Dependent
@Stateless
public class TimesheetManager implements Serializable {
    
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
     * Find Timesheet record from database.
     * 
     * @param id primary key of timesheet record.
     * @return the Timesheet record with key = id, null if not found.
     */
    public Timesheet find(int id) {
        return em.find(Timesheet.class, id);
    }

    /**
     * Persist Timesheet record into database.  id must be unique.
     * @param timesheet the record to be persisted.
     */
    public void persist(Timesheet timesheet) {
        em.persist(timesheet);
    }
    
    /**
     * Merge Timesheet record fields into existing database record.
     * @param timesheet the record to be merged.
     */
    public void merge(Timesheet timesheet) {
        em.merge(timesheet);
    }
    
    /**
     * Remove timesheet from database.
     * @param timesheet record to be removed from database
     */
    public void remove(Timesheet timesheet) {
        timesheet = find(timesheet.getTimesheetId());
        em.remove(timesheet);
    }

    /**
     * Return Timesheets table as List of Timesheet.
     * @return List of all records in Timesheets table
     */
    public List<Timesheet> getAll() {
        return em.createQuery("select t from Timesheet t", Timesheet.class)
                .getResultList();
    }
    
    /**
     * Return Timesheets table as List of Timesheets for a specific user.
     * @return List of all records in Timesheets table
     * @param empId employee id
     */
    public List<Timesheet> getAll(int empId) {
        TypedQuery<Timesheet> query 
            = em.createQuery("select t from Timesheet t where "
                    + "t.empNumber = :emp_id",
            Timesheet.class);
        query.setParameter("emp_id", empId);
        return query.getResultList();
    }
    
    /**
     * Gets the max timesheet id from database.
     * @return max id
     */
    public Integer getCount() {
        return (Integer) em.createQuery("select max(t.id) from Timesheet t")
                .getSingleResult();
    }
    
}
