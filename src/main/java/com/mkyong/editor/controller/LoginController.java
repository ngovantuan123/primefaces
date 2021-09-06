package com.mkyong.editor.controller;

import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeRequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name="login")
@SessionScoped
public class LoginController implements Serializable {
    private String userName;
    private String password;
    public String loginControl() throws InterruptedException {
        Thread.sleep(3000);
        if (userName.equals("admin") && password.equals("admin")) {

            // get Http Session and store username
            return "index";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid Login!",
                    "Please Try Again!"));

            // invalidate session, and redirect to other pages
            //message = "Invalid Login. Please Try Again!";
            return "";
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
