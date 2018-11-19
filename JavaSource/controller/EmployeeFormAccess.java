package controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import access.EmployeeManager;
import models.Employees;

/**
 * Timesheet helper class providing functionality between user interface and 
 * persistence tier.
 * @author Danny & Tony
 * @version 2
 *
 */
@Named("employeeTable")
@SessionScoped
public class EmployeeFormAccess implements Serializable {

    private static final long serialVersionUID = 11L;
    
    /**
     * The default password to set for accounts when the admin 
     * user resets their password.
     */
    private static final String DEFAULT_PASSWORD = "1234";
    
    /** Manager from Employee objects. */
    @Inject private EmployeeManager mgr;
    
    /** The Employee data list containing all of the Employees
     * in the system.
     */
    private List<Employees> employees;

    /** The currently logged in user. */
    private Employees currentUser;
    
    /** A user account being edited by the admin. */
    private Employees currentEditUser;
    
    /** Holds login name input by user. */
    private String loginName;
    
    /** Holds login password input by user. */
    private String loginPass;
    
    /** New password to be given to an edited user. */
    private String newPassword;
    
    /**
     * Returns user currently being edited by admin.
     * @return user
     */
    public Employees getCurrentEditUser() {
        return currentEditUser;
    }

    /**
     * Sets user being edited to currentEditUser.
     * @param currentEditUser user selected for edit
     */
    public void setCurrentEditUser(Employees currentEditUser) {
        this.currentEditUser = currentEditUser;
    }

    /**
     * Returns new password input by user.
     * @return new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets new password for an employee.
     * @param newPassword password input by user
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Returns a list of all employees in database.
     * @return list of employees
     */
    public List<Employees> getEmployees() {
        if (employees == null) {
            refreshList();
        }
        return employees;
    }

    /**
     * Sets employee list.
     * @param employees employee list
     */
    public void setEmployees(List<Employees> employees) {
        this.employees = employees;
    }

    /**
     * Refreshes employee list with all records from database.
     */
    private void refreshList() {
        employees = mgr.getAll();
    }
    
    /**
     * Returns employee with a given name.
     * @param name the name field of the employee
     * @return the employees.
     */
    public Employees getEmployee(String name) {
        for (Employees emp : employees) {
            if (emp.getName().equals(name)) {
                return emp;
            }
        }
        return null;
    }
    
    /**
     * Returns login name input.
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * Returns login password input.
     * @return the loginPass
     */
    public String getLoginPass() {
        return loginPass;
    }

    /**
     * Sets login password.
     * @param loginPass the loginPass to set
     */
    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    /**
     * Sets login name.
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * Returns a Map containing all username and password 
     * combinations from the database.
     * @return username/password combos
     */
    public Map<String, String> getLoginCombos() {
        return mgr.getLoginCombos();
    }
    
    /**
     * Returns current user logged into application.
     * @return the currentUser
     */
    public Employees getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets current user that logged into application.
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(Employees currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Changes an employees password.
     * @param e the employee
     * @param password the new password
     */
    public void changePassword(Employees e, String password) {
        e.setPassword(password);
        mgr.merge(e);
    }
    
    /**
     * Resets an employees password the the default password.
     * @param e the employee
     */
    public void resetPassword(Employees e) {
        changePassword(e, DEFAULT_PASSWORD);
        mgr.merge(e);
    } 
    
    /**
     * Verifies that the loginID and password is a valid combination.
     * @return true if it is, false if it is not.
     */
    boolean verifyUser() {
        Map<String, String> combos = getLoginCombos();
        if (combos.containsKey(loginName)) {
            if (combos.get(loginName).equals(loginPass)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifies login credentials, and on success logs user into the system.
     * @return string the page to navigate to after
     * success or fail on login attempt
     */
    public String login() {
        if (employees == null) {
            refreshList();
        }
        if (verifyUser()) {
            for (int i = 0; i < employees.size(); ++i) {
                if (employees.get(i).getUserName()
                        .equalsIgnoreCase(loginName)) {
                    currentUser = employees.get(i);
                    // null these so the login credentials aren't
                    // just floating around
                    loginName = null;
                    loginPass = null;
                    return "timesheetSelect.xhtml";
                }
            }
        }
        FacesMessage msg = new FacesMessage(
                "Warning", "Username and password do not match");
        msg.setSeverity(FacesMessage.SEVERITY_WARN);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        return "";
    }
    
    /**
     * Logs the user out of the system.
     *
     * @param employee the user to logout.
     * @return a String representing the login page.
     */
    public String logout(Employees employee) {
        setCurrentUser(null);
        return "login.xhtml";
    }
    
    /**
     * Deletes the specified user from the collection of Users.
     *
     * @param userToDelete the user to delete.
     * @return "viewUsers" the page to navigate to after deleting
     * an employee
     */
    public String deleteEmployee(Employees userToDelete) {
        if (employees.contains(userToDelete)) {
            mgr.remove(userToDelete);
            employees.remove(userToDelete);
            FacesMessage msg = new FacesMessage(
                    "Info", "User deleted");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, msg);
        }
        return "viewUsers";
    }

    /**
     * Adds a new Employee to the collection of Employees.
     * @return "viewUsers" the page to navigate to after 
     * adding a new employee
     */
    public String addEmployee() {
        if (!employees.contains(currentEditUser)) {
            currentEditUser.setPassword(DEFAULT_PASSWORD);
            employees.add(currentEditUser);
            mgr.merge(currentEditUser);
            refreshList();
        }
        currentEditUser = null;
        return "viewUsers";
    }
    
    /**
     * If the currentEditUsers password was changed, applies the
     * change in the credsMap, then sets navigation string back
     * to the viewUsers page.
     * @return "viewUsers" the view users page
     * @param e employee who is being edited
     */
    public String saveChanges(Employees e) {
        mgr.merge(e);
        newPassword = null;
        currentEditUser = null;
        return "viewUsers";
    }
    
    /**
     * Sets currentEditUser to the user which the admin
     * wants to edit, and sets naviagtion string to the 
     * editUser page.
     * @param userToEdit the user which the admin wants to edit
     * @return "editUser" the edit user page
     */
    public String editEmployee(Employees userToEdit) {
        setCurrentEditUser(userToEdit);
        return "editUser";
    }
    
    /**
     * Checks if the current logged in user is the admin.
     * @return true if the current logged in user is the admin
     */
    public boolean isAdmin() {
        return currentUser.getIsAdmin();
    }
    
    /**
     * Creates a new empty Employee object and returns the navigation
     * string to navigate to the user edit page so that the page can
     * display a newly created user account.
     * @return "editUser" the page to navigate to in order to edit users 
     */
    public String add() {
        currentEditUser = new Employees();
        return "newUser";
    }
}
