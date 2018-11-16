package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import access.EmployeeManager;
import models.Employees;

@Named("employeeTable")
@SessionScoped
public class EmployeeFormAccess implements Serializable {

    private static final long serialVersionUID = 11L;
    @Inject
    private EmployeeManager mgr;
    
    /**
     * The Employee data list containing all of the Employees
     * in the system.
     */
    private List<Employees> employees;

    /**
     * The currently logged in user.
     */
    private Employees currentUser;
    /**
     * The default password to set for accounts when the admin 
     * user resets their password.
     */
    private static final String DEFAULT_PASSWORD = "1234";
    
    /**
     * A user account being edited by the admin.
     */
    private Employees currentEditUser;
    
    private String loginName;
    
    private String loginPass;
    
    public Employees getCurrentEditUser() {
        return currentEditUser;
    }

    public void setCurrentEditUser(Employees currentEditUser) {
        this.currentEditUser = currentEditUser;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * New password to be given to an edited user.
     */
    private String newPassword;
    
    public List<Employees> getEmployees() {
        if (employees == null)
            refreshList();
        return employees;
    }

    public void setEmployees(List<Employees> employees) {
        this.employees = employees;
    }

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
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * @return the loginPass
     */
    public String getLoginPass() {
        return loginPass;
    }

    /**
     * @param loginPass the loginPass to set
     */
    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    /**
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Map<String, String> getLoginCombos() {
        return mgr.getLoginCombos();
    }
    
    /**
     * @return the currentUser
     */
    public Employees getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(Employees currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * TODO: Changes an employees password.
     * @param e the employee
     * @param password the new password
     */
    public void changePassword(Employees e, String password) {
        e.setPassword(password);
    }
    
    /**
     * Resets an employees password the the default password.
     * @param e the employee
     */
    public void resetPassword(Employees e) {
        changePassword(e, DEFAULT_PASSWORD);
    } 
    
    /**
     * Verifies that the loginID and password is a valid combination.
     * @return true if it is, false if it is not.
     */
    boolean verifyUser() {
        Map<String, String> combos = getLoginCombos();
        if(combos.containsKey(loginName)) {
            if(combos.get(loginName).equals(loginPass)) {
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
        if(employees == null) {
            refreshList();
        }
        if (verifyUser()) {
            for (int i = 0; i < employees.size(); ++i) {
                if (employees.get(i).getUserName()
                        .equalsIgnoreCase(loginName)) {
                    currentUser = employees.get(i);
                    //null these so the login credentials aren't just floating around
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
            employees.add(currentEditUser);
            currentEditUser.setPassword(DEFAULT_PASSWORD);
            mgr.merge(currentEditUser);
        }
        currentEditUser = null;
        return "viewUsers";
    }
    
    /**
     * TODO: use mgr
     * If the currentEditUsers password was changed, applies the
     * change in the credsMap, then sets navigation string back
     * to the viewUsers page.
     * @return "viewUsers" the view users page
     */
    public String saveChanges() {
        if (!newPassword.isEmpty()) {
           //credsMap.put(currentEditUser.getUserName(), newPassword);
            //use mgr
        }
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
     * TODO: this
     * Checks if the current logged in user is the admin.
     * @return true if the current logged in user is the admin
     */
    public boolean isAdmin() {
        return true;
        //return currentUser.getIsAdmin();
    }
    
    /**
     * TODO: use mgr
     * Creates a new empty Employee object and returns the navigation
     * string to navigate to the user edit page so that the page can
     * display a newly created user account.
     * @return "editUser" the page to navigate to in order to edit users 
     */
    public String add() {
        currentEditUser = new Employees();
        //currentEditUser.setEmp_number(0);
        return "newUser";
    }
}
