package controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import access.TimesheetManager;
import access.TimesheetRowManager;
import models.TimesheetRow;

@Named("rows")
@SessionScoped
public class TimesheetRowFormAccess implements Serializable {
    
    private static final long serialVersionUID = 11L;
    @Inject private TimesheetRowManager mgr;
    @Inject private TimesheetManager tsMgr;
    private List<TimesheetRow> list;
    
    public TimesheetManager getTsMgr() {
        return tsMgr;
    }

    public List<TimesheetRow> getList() {
        if(list == null)
            refreshList();
        return list;
    }
    
    public void setList(List<TimesheetRow> rows) {
        this.list = rows;
    }
    
    private void refreshList() {
        list = mgr.getAll();
    }

}
