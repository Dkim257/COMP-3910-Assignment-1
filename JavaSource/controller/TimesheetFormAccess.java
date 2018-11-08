package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import access.TimesheetManager;
import models.Timesheet;

@Named("timesheets")
@SessionScoped
public class TimesheetFormAccess implements Serializable {

    @Inject private TimesheetManager mgr;
    private List<Timesheet> list;
    
    
    public List<Timesheet> getList() {
        if(list == null)
            refreshList();
        return list;
    }
    
    public void setList(List<Timesheet> suppliers) {
        this.list = suppliers;
    }
    
    private void refreshList() {
        list = mgr.getAll();
    }
    
}
