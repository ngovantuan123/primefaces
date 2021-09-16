package com.tuangh.Utils;


import org.apache.shiro.subject.Subject;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class SecurityUtils {
    public Subject getUser(){
        return org.apache.shiro.SecurityUtils.getSubject();
    }
}
