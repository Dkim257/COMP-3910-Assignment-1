package access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.Admin;


@Dependent
@Stateless
public class AdminManager implements Serializable {

    @PersistenceContext(unitName="db") EntityManager em;
    
    /**
     * Find Admin record from database.
     * 
     * @param id primary key of admin record.
     * @return the Admin record with key = id, null if not found.
     */
    public Admin find(int id) {
        return em.find(Admin.class, id);
    }

    /**
     * Persist Admin record into database.  id must be unique.
     * @param admin the record to be persisted.
     */
    public void persist(Admin admin) {
        em.persist(admin);
    }
    
    /**
     * merge Admin record fields into existing database record.
     * @param admin the record to be merged.
     */
    public void merge(Admin admin) {
        em.merge(admin);
    }
    
    /**
     * Remove admin from database.
     * @param admin record to be removed from database
     */
    public void remove(Admin admin) {
        admin = find(admin.getEmp_number());
        em.remove(admin);
    }

    /**
     * Return Admins table as List of Admin.
     * @return List of all records in Admins table
     */
    public List<Admin> getAll() {
        return em.createQuery("select t from Admin t", Admin.class)
                .getResultList();
    }
}
