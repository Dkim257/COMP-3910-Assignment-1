package access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.Employees;

@Dependent
@Stateless
public class EmployeeManager implements Serializable {
    
    @PersistenceContext(unitName="db") EntityManager em;

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
     * merge Employees record fields into existing database record.
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
        employee = find(employee.getEmp_number());
        em.remove(employee);
    }

    /**
     * Return Employeess table as List of Employees.
     * @return List of all records in Employeess table
     */
    public List<Employees> getAll() {
        return em.createQuery("select t from Employees t", Employees.class)
                .getResultList();
    }
    
}
