package access;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.Employees;

/**
 * Handle CRUD actions for Employees class.
 * @author Tony & Danny
 * @version 1
 *
 */
@Dependent
@Stateless
public class EmployeeManager implements Serializable {
    
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
     * Find Employees record from database.
     * 
     * @param id primary key of employee record.
     * @return the Employees record with key = id, null if not found.
     */
    public Employees find(int id) {
        return em.find(Employees.class, id);
    }

    /**
     * Persist Employees record into database.  id must be unique.
     * @param employee the record to be persisted.
     */
    public void persist(Employees employee) {
        em.persist(employee);
    }
    
    /**
     * Merge Employees record fields into existing database record.
     * @param employee the record to be merged.
     */
    public void merge(Employees employee) {
        em.merge(employee);
    }
    
    /**
     * Remove employee from database.
     * @param employee record to be removed from database
     */
    public void remove(Employees employee) {
        employee = find(employee.getEmpNumber());
        em.remove(employee);
    }

    /**
     * Return Employees table as List of Employees.
     * @return List of all records in Employees table
     */
    public List<Employees> getAll() {
        return em.createQuery("select e from Employees e", Employees.class)
                .getResultList();
    }
    
    /**
     * Return all usernames and the matching passwords from Employees table.
     * @return Map of username, password
     */
    public Map<String, String> getLoginCombos() {
        List<Object[]> results = em.createQuery("select e.userName,"
                + " e.password from Employees as e", Object[].class)
                .getResultList();
        Map<String, String> combos = new HashMap<>();
        for (Object[] combo : results) {
            combos.put((String) combo[0], (String) combo[1]);
        }
        return combos;
    }
    
}
