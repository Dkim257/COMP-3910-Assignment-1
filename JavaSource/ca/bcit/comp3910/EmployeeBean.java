package ca.bcit.comp3910;

import ca.bcit.infosys.employee.Employee;
import javax.inject.Named;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;

@Named("employee")
@ApplicationScoped
public class EmployeeBean extends Employee implements Serializable{
    
    public EmployeeBean() {
        super();
    }

}
