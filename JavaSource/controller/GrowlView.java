package controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Class for creating growl messages when successful events occur.
 * 
 * @author Danny Di Iorio
 * @version 1
 */
@Named
@SessionScoped
public class GrowlView implements Serializable {

    /**
     * Growl message shows when timesheet is saved.
     */
    public void saveTimesheet() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",
                "Timesheet saved"));
    }

    /**
     * Growl message shows when user adds new row to a timesheet.
     */
    public void newRow() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",
                "New row added"));
    }

    /**
     * Growl message shows when administrator resets a user's password.
     */
    public void passwordReset() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",
                "User password reset to default"));
    }
}
