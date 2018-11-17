package access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import models.Timesheet;

@Dependent
@Stateless
public class TimesheetManager implements Serializable {
    
    @PersistenceContext(unitName="db") EntityManager em;
    
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
     * merge Timesheet record fields into existing database record.
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
        timesheet = find(timesheet.getTimesheet_id());
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
     */
    public List<Timesheet> getAll(int emp_id) {
        TypedQuery<Timesheet> query 
            = em.createQuery("select t from Timesheet t where t.emp_number = :emp_id",
            Timesheet.class);
        query.setParameter("emp_id", emp_id);
        return query.getResultList();
    }
    
    public long getCount() {
        return (long) em.createQuery("select count (t) FROM Timesheet t").getSingleResult();
    }
    
}
