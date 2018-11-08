package access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.TimesheetRow;

@Dependent
@Stateless
public class TimesheetRowManager implements Serializable {
    
    @PersistenceContext(unitName="db") EntityManager em;
    
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
     * merge TimesheetRow record fields into existing database record.
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
        return em.createQuery("select t from TimesheetRow t", TimesheetRow.class)
                .getResultList();
    }

}
