package ca.bcit.comp3910;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

@Named("dbEmployee")
@ApplicationScoped
public class EmployeeTableData implements EmployeeList {

    private ArrayList<Employee> employees;
    private ArrayList<Credentials> credentials;
    private HashMap<String, String> credsMap;
    
    private Employee currentEmployee, admin;
    
    
    @Override
    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public Employee getEmployee(String name) {
        for(Employee e : employees) {
            if(e.getName().equals(name))
                return e;
        }
        return null;
    }

    @Override
    public Map<String, String> getLoginCombos() {
        return credsMap;
    }

    @Override
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    @Override
    public Employee getAdministrator() {
        return admin;
    }

    @Override
    public boolean verifyUser(Credentials credential) {
        String user = credential.getUserName();
        String pass = credential.getPassword();
        
        for(Credentials c : credentials) {
            if(c.getUserName().equals(user)) {
                if(c.getPassword().equals(pass))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String logout(Employee employee) {
        currentEmployee = null;
        return "login.xhtml";
    }

    @Override
    public void deleteEmployee(Employee userToDelete) {
        if(currentEmployee == admin) {
            employees.remove(userToDelete);
        }
    }

    @Override
    public void addEmployee(Employee newEmployee) {
        if(currentEmployee == admin) {
            employees.add(newEmployee);
        }
    }

}
