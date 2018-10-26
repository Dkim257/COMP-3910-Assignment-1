package ca.bcit.comp3910;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.security.auth.spi.Users;

@Named("employeeTable")
@SessionScoped
public class EmployeeTable implements Serializable {

    private static List<Employee> employees;
    private static Map<String, String> credsMap;
    static {
        employees = new ArrayList<>();
        employees.add(new Employee("Tony Pacheco", 1, "tp1"));
        employees.add(new Employee("Danny DiOreo", 2, "dd2"));
        credsMap = new HashMap<>();
        credsMap.put("tp1", "pass");
        credsMap.put("dd2", "pass");
    }
    
    @Inject private Credential credential;
    private Employee currentUser, admin;
    
    public EmployeeTable() {
        setAdmin(employees.get(0)); //Tony is admin cuz he's cool
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }

    public Employee getEmployee(String name) {
        return null;
    }

    public Map<String, String> getLoginCombos() {
        return null;
    }

    public Employee getCurrentEmployee() {
        return currentUser;
    }

    public Employee getAdministrator() {
        return null;
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

    public void addEmployee(Employee newEmployee) {
        if(!employees.contains(newEmployee)) {
            employees.add(newEmployee);
        }
    }

    public Employee getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Employee currentUser) {
        this.currentUser = currentUser;
    }

    public Employee getAdmin() {
        return admin;
    }

    public void setAdmin(Employee admin) {
        this.admin = admin;
    }
    
}