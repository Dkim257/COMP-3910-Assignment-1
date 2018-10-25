package ca.bcit.comp3910;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
 
@Named
@SessionScoped
public class GrowlView implements Serializable {
     
    private String message;
    private String success = "Success";
    private String fail = "Failure";
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
     
    public void changePassSuccess() {
        FacesMessage msg = new FacesMessage("Password changed");
        FacesContext.getCurrentInstance().addMessage(success, msg);
    }
}