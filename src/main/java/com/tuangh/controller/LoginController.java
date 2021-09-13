package com.tuangh.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeRequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name="login")
@SessionScoped
public class LoginController implements Serializable {
    private String userName;
    private String password;
    private boolean rememberMe=false;
    public String loginControl() throws InterruptedException {
        if (userName.equals("admin") && password.equals("admin")) {

            // get Http Session and store username
            return "index?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid Login!",
                    "Please Try Again!"));

            // invalidate session, and redirect to other pages
            //message = "Invalid Login. Please Try Again!";
            return "";
        }
    }
    public void loginUser(){

        try{

            final UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            token.setRememberMe(rememberMe);
            SecurityUtils.getSubject().login(token);

        } catch (UnknownAccountException uae ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", "Your username wrong"));
        } catch (IncorrectCredentialsException ice ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", "Password is incorrect"));
        } catch (LockedAccountException lae ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", "This username is locked"));
        } catch(AuthenticationException aex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", aex.toString()));
        }

    }
    public void authorizedUserControl(){
        if(null != SecurityUtils.getSubject().getPrincipal()){
            final NavigationHandler nh =  FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            nh.handleNavigation(FacesContext.getCurrentInstance(), null, "/user/user.xhtml?faces-redirect=true");
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
