package ca.bcit.comp3910;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

@Dependent
public class Credential implements Serializable {

    private String username, password;
    
    public Credential(){}
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
