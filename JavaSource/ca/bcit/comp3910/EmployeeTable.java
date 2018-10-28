package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;


/**
 * Class representing a database table of Employees.
 * @author Tony Pacheco + Danny DiIorio
 * @version 1.0
 */
@Named("employeeTable")
@SessionScoped
public class EmployeeTable implements Serializable {
    
    /**
     * The Employee data list containing all of the Employees
     * in the system.
     */
    private static List<Employee> employees;
    
    /**
     * The application administrator.
     */
    private static Employee admin;
    
    /**
     * A map containing the usernames and passwords of users
     * to be checked against when users attempt to login.
     */
    private static Map<String, String> credsMap;
    
    /**
     * The default password to set for accounts when the admin 
     * user resets their password.
     */
    private static final String DEFAULT_PASSWORD = "1234";
    
    static {
        employees = new ArrayList<Employee>();
        employees.add(new Employee("Tony Pacheco", 1, "tp1"));
        employees.add(new Employee("Danny Di Iorio", 2, "dd2"));
        
        admin = employees.get(0);
        
        credsMap = new HashMap<>();
        credsMap.put("tp1", "pass");
        credsMap.put("dd2", "pass");
    }
    
    /**
     * Credentials bean which receives username and password from
     * login page user input, and provides this class with access
     * to the values.
     */
    @Inject private Credentials credential;
    
    /**
     * The currently logged in user.
     */
    private Employee currentUser;
    
    /**
     * A user account being edited by the admin.
     */
    private Employee currentEditUser;
    
    /**
     * New password to be given to an edited user.
     */
    private String newPassword;
    
    /**
     * employees getter.
     * @return the ArrayList of users.
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * Returns employee with a given name.
     * @param name the name field of the employee
     * @return the employees.
     */
    public Employee getEmployee(String name) {
        for (Employee emp : employees) {
            if (emp.getName().equals(name)) {
                return emp;
            }
        }
        return null;
    }

    /**
     * Return map of valid passwords for userNames.
     * @return the Map containing the valid (userName, password) combinations.
     */
    public Map<String, String> getLoginCombos() {
        return credsMap;
    }
    
    /**
     * Changes an employees password.
     * @param e the employee
     * @param password the new password
     */
    public void changePassword(Employee e, String password) {
        credsMap.put(e.getUserName(), password);
    }
    
    /**
     * Resets an employees password the the default password.
     * @param e the employee
     */
    public void resetPassword(Employee e) {
        changePassword(e, DEFAULT_PASSWORD);
    }

    /**
     * Assumes single administrator and returns the employee object
     * of that administrator.
     * @return the administrator user object.
     */
    public Employee getAdministrator() {
        return admin;
    }
    
    /**
     * Getter for credential.
     * @return credential
     */
    public Credentials getCredential() {
        return credential;
    }

    /**
     * Setter for credential.
     * @param credential credential
     */
    public void setCredential(Credentials credential) {
        this.credential = credential;
    }

    /**
     * Verifies that the loginID and password is a valid combination.
     *
     * @return true if it is, false if it is not.
     */
    boolean verifyUser() {
        return credsMap.containsKey(credential.getUserName())
            && credsMap.get(credential.getUserName())
            .equals(credential.getPassword());
    }
    
    /**
     * Verifies login credentials, and on success logs user into the system.
     * @return string the page to navigate to after
     * success or fail on login attempt
     */
    public String login() {
        if (verifyUser()) {
            for (int i = 0; i < employees.size(); ++i) {
                if (employees.get(i).getUserName()
                        .equalsIgnoreCase(credential.getUserName())) {
                    currentUser = employees.get(i);
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
    public String logout(Employee employee) {
        currentUser = null;
        return "login.xhtml";
    }

    /**
     * Deletes the specified user from the collection of Users.
     *
     * @param userToDelete the user to delete.
     * @return "viewUsers" the page to navigate to after deleting
     * an employee
     */
    public String deleteEmployee(Employee userToDelete) {
        if (employees.contains(userToDelete)) {
            employees.remove(userToDelete);
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
            credsMap.put(currentEditUser.getUserName(), DEFAULT_PASSWORD);
        }
        currentEditUser = null;
        return "viewUsers";
    }
    
    /**
     * If the currentEditUsers password was changed, applies the
     * change in the credsMap, then sets navigation string back
     * to the viewUsers page.
     * @return "viewUsers" the view users page
     */
    public String saveChanges() {
        if (!newPassword.isEmpty()) {
           credsMap.put(currentEditUser.getUserName(), newPassword);
        }
        newPassword = null;
        currentEditUser = null;
        return "viewUsers";
    }
    
    /**
     * Sets currentEditUser to the user which the admin
     * wants to edit, and sets navigation string to the 
     * editUser page.
     * @param userToEdit the user which the admin wants to edit
     * @return "editUser" the edit user page
     */
    public String editEmployee(Employee userToEdit) {
        setCurrentEditUser(userToEdit);
        return "editUser";
    }

    /**
     * getter for currentUser property.  
     * @return the current user.
     */
    public Employee getCurrentUser() {
        return currentUser;
    }

    /**
     * setter for currentUser property.  
     * @param currentUser the current user.
     */
    public void setCurrentUser(Employee currentUser) {
        this.currentUser = currentUser;
    }
    
    /**
     * getter for currentEditUser.
     * @return currentEditUser
     */
    public Employee getCurrentEditUser() {
        return currentEditUser;
    }
        
    /**
     * Setter for currentEditUser.
     * @param currentEditUser the user to edit
     */
    public void setCurrentEditUser(Employee currentEditUser) {
        this.currentEditUser = currentEditUser;
    }

    /**
     * getter for admin.
     * @return admin
     */
    public static Employee getAdmin() {
        return admin;
    }

    /**
     * setter for admin.
     * @param a admin
     */
    public static void setAdmin(Employee a) {
        admin = a;
    }
    
    /**
     * Checks if the current logged in user is the admin.
     * @return true if the current logged in user is the admin
     */
    public boolean isAdmin() {
        return currentUser == admin;
    }
    
    /**
     * Creates a new empty Employee object and returns the navigation
     * string to navigate to the user edit page so that the page can
     * display a newly created user account.
     * @return "editUser" the page to navigate to in order to edit users 
     */
    public String add() {
        currentEditUser = new Employee();
        return "newUser";
    }

    /**
     * Getter for newPassword.
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Setter for newPassword.
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
}
