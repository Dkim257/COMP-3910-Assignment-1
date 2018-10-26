package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;



@Named("employeeTable")
@SessionScoped
public class EmployeeTable implements Serializable {
    
    private static List<Employee> employees;
    private static Employee admin;
    private static Map<String, String> credsMap;
    
    static {
        employees = new ArrayList<Employee>();
        employees.add(new Employee("Tony Pacheco", 1, "tp1"));
        employees.add(new Employee("Danny DiOreo", 2, "dd2"));
        employees.add(new Employee("Bruce Link", 3, "bl3"));
        
        admin = employees.get(0); //Tony gets to be Admin cuz he's coolio
        
        credsMap = new HashMap<>();
        credsMap.put("tp1", "pass");
        credsMap.put("dd2", "pass");
    }
    
    @Inject private Credential credential;
    private Employee currentUser;
    
    public EmployeeTable() {}
    
    public List<Employee> getEmployees() {
        return employees;
    }

    public Employee getEmployee(String name) {
        for(Employee emp : employees)
            if(emp.getName().equals(name))
                return emp;
        return null;
    }

    public Map<String, String> getLoginCombos() {
        return credsMap;
    }

    public Employee getAdministrator() {
        return admin;
    }
    
    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    boolean verifyUser() {
        return credsMap.containsKey(credential.getUsername())
            && credsMap.get(credential.getUsername()).equals(credential.getPassword());
    }
    
    public String login() {
        if(verifyUser()) {
            for(int i = 0; i < employees.size(); ++i) {
                if(employees.get(i).getUserName().equals(credential.getUsername())) {
                    setCurrentUser(employees.get(i));
                }
            }
            return "timesheet.xhtml";
        }
        return "";
    }

    public String logout(Employee employee) {
        setCurrentUser(null);
        return "login.xhtml";
    }

    public void deleteEmployee(Employee userToDelete) {
        if(employees.contains(userToDelete))
            employees.remove(userToDelete);
    }

    public String addEmployee() {
        if(!employees.contains(currentUser)) {
            employees.add(currentUser);
        }
        return "viewUsers";
    }
    
    public String editEmployee(Employee userToEdit) {
        if (userToEdit != null)
            setCurrentUser(userToEdit);
        
        return "editUser";
    }

    public Employee getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Employee currentUser) {
        this.currentUser = currentUser;
    }

    public static Employee getAdmin() {
        return admin;
    }

    public static void setAdmin(Employee a) {
        admin = a;
    }
    
    public static EmployeeTable getInstance() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return (EmployeeTable) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "EmployeeTable");
    }
    
    public boolean isAdmin() {
        return currentUser == admin;
    }
    
    public String add() {
        currentUser = new Employee();
        return "editUser";
    }
    
}
