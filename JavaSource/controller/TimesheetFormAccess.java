package controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import access.EmployeeManager;
import access.TimesheetManager;
import access.TimesheetRowManager;
import models.Timesheet;

@Named("timesheets")
@SessionScoped
public class TimesheetFormAccess implements Serializable {

    private static final long serialVersionUID = 11L;
    @Inject private TimesheetManager mgr;
    @Inject private EmployeeManager empMgr;
    @Inject private TimesheetRowManager tsRowMgr;
    private List<Timesheet> list;
    
    public EmployeeManager getEmpMgr() {
        return empMgr;
    }
    
    public TimesheetRowManager getTsRowMgr() {
        return tsRowMgr;
    }

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
