package controller;

import java.io.Serializable;

import models.TimesheetRow;

public class EditableRow implements Serializable {

    private boolean editable;
    
    /** Holds supplier to be displayed, edited or deleted.*/
    private TimesheetRow row;
    
    public EditableRow (TimesheetRow model){
        this.setRow(model);
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return the row
     */
    public TimesheetRow getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(TimesheetRow row) {
        this.row = row;
    }
    
    
    public String toggleEditable() {
        editable = !editable;
        return null;
    }
    
}
