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
     * Growl message shows when administrator resets a user's password.
     */
    public void growlMessageSuccess(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",
                msg));
    }
    
    /**
     * Growl message shows when administrator resets a user's password.
     */
    public void growlMessageWarning(String msg) {
        FacesMessage message = new FacesMessage(
                "Warning", msg);
        message.setSeverity(FacesMessage.SEVERITY_WARN);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, message);
    }
    
}
