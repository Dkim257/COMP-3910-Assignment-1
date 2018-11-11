package controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import access.EmployeeManager;
import models.Employees;

@Named("users")
@SessionScoped
public class EmployeeFormAccess implements Serializable {

    private static final long serialVersionUID = 11L;
    @Inject
    private EmployeeManager mgr;
    private List<Employees> list;

    public List<Employees> getList() {
        if (list == null)
            refreshList();
        return list;
    }

    public void setList(List<Employees> employees) {
        this.list = employees;
    }

    private void refreshList() {
        list = mgr.getAll();
    }

}
