package ca.bcit.comp3910;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("employee")
@SessionScoped
public class Employee implements Serializable {

    private String name, userName;
    private Integer empNumber;
    
    public Employee() {}
    
    public Employee(final String empName, final Integer number, final String id) {
        name = empName;
        empNumber = number;
        userName = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(Integer empNumber) {
        this.empNumber = empNumber;
    }
}
