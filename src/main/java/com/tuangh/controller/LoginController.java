package com.tuangh.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name="login")
@SessionScoped
public class LoginController implements Serializable {
    private String userName;
    private String password;
    private boolean rememberMe=false;

    //@RequiresRoles(value = {"admin","user"},logical = Logical.OR)
    public void loginUser(){

        try{
            final UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            token.setRememberMe(rememberMe);
            Subject subject =SecurityUtils.getSubject();
            if(!subject.isAuthenticated()){
                subject.login(token);
            }

        } catch (UnknownAccountException uae ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", "Your username wrong"));
        } catch (IncorrectCredentialsException ice ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", "Password is incorrect"));
        } catch (LockedAccountException lae ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", "This username is locked"));
        } catch(AuthenticationException aex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", "Thông tin đăng nhập không đúng"));
        }

    }
    public void logout(){
        SecurityUtils.getSubject().logout();
    }
    public void authorizedUserControl(){
        if(null != SecurityUtils.getSubject().getPrincipal()){
            final NavigationHandler nh =  FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            nh.handleNavigation(FacesContext.getCurrentInstance(), null, "/index.xhtml?faces-redirect=true");
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
